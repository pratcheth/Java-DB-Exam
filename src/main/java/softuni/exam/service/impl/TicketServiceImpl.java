package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.TicketSeedDto;
import softuni.exam.models.dtos.TicketSeedRootDto;
import softuni.exam.models.entities.Ticket;
import softuni.exam.repository.TicketRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.PlaneService;
import softuni.exam.service.TicketService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static softuni.exam.constants.GlobalConstants.*;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final DateTimeFormatter formatterWithHours;
    private final PassengerService passengerService;
    private final TownService townService;
    private final PlaneService planeService;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, DateTimeFormatter formatterWithHours, PassengerService passengerService, TownService townService, PlaneService planeService) {
        this.ticketRepository = ticketRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.formatterWithHours = formatterWithHours;
        this.passengerService = passengerService;
        this.townService = townService;
        this.planeService = planeService;
    }

    @Override
    public boolean areImported() {
        return this.ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(Path.of(TICKETS_PATH));
    }

    @Override
    public String importTickets() throws JAXBException, FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();

        TicketSeedRootDto ticketSeedRootDto =
                this.xmlParser.parseXml(TicketSeedRootDto.class, TICKETS_PATH);

        List<TicketSeedDto> ticketSeedDtos = ticketSeedRootDto.getTickets();

        for (TicketSeedDto ticketSeedDto : ticketSeedDtos) {
            if (this.validationUtil.isValid(ticketSeedDto)) {

                if (this.ticketRepository.findBySerialNumber(ticketSeedDto.getSerialNumber()) == null) {
                    Ticket ticket = this.modelMapper.map(ticketSeedDto, Ticket.class);

                    ticket.setTakeOff(LocalDateTime.parse(ticketSeedDto.getTakeOff(), formatterWithHours));
                    ticket.setFromTown(this.townService.findByName(ticketSeedDto.getFromTown().getName()));
                    ticket.setToTown(this.townService.findByName(ticketSeedDto.getToTown().getName()));
                    ticket.setPassenger(this.passengerService.findByEmail(ticketSeedDto.getPassengerDto().getEmail()));
                    ticket.setPlane(this.planeService.findByNumber(ticketSeedDto.getPlaneDto().getRegisterNumber()));


                    this.ticketRepository.saveAndFlush(ticket);

                    stringBuilder.append(String.format("Successfully imported Ticket %s - %s", ticketSeedDto.getFromTown().getName(), ticketSeedDto.getToTown().getName()));
                } else {
                    stringBuilder.append(EXIST);
                }
            } else {
                stringBuilder.append(String.format(INCORRECT_DATA_MESSAGE, "Ticket"));
            }
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }
}
