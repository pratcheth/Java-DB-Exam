package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.PassengerSeedDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.TownService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static softuni.exam.constants.GlobalConstants.*;

@Service
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final TownService townService;

    @Autowired
    public PassengerServiceImpl(PassengerRepository passengerRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, FileUtil fileUtil, TownService townService) {
        this.passengerRepository = passengerRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return this.passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return this.fileUtil.readFileContent(PASSENGER_PATH);
    }

    @Override
    public String importPassengers() throws FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();

        PassengerSeedDto[] passengerSeedDtos = this.gson.fromJson(new FileReader(PASSENGER_PATH), PassengerSeedDto[].class);
        for (PassengerSeedDto passengerSeedDto : passengerSeedDtos) {
            Town town = this.townService.findByName(passengerSeedDto.getTown());
            if (this.validationUtil.isValid(passengerSeedDto) && town != null) {
                if (this.passengerRepository.findByEmail(passengerSeedDto.getEmail()) == null) {
                    Passenger passenger = this.modelMapper.map(passengerSeedDto, Passenger.class);
                    passenger.setTown(town);

                    this.passengerRepository.saveAndFlush(passenger);
                    stringBuilder.append(String.format("Successfully imported Passenger %s - %s", passengerSeedDto.getLastName(), passengerSeedDto.getEmail()));
                } else {
                    stringBuilder.append(EXIST);
                }
            } else {
                stringBuilder.append(String.format(INCORRECT_DATA_MESSAGE, "Passenger"));
            }
            stringBuilder.append(System.lineSeparator());
        }


        return stringBuilder.toString();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() throws FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();

        List<Passenger> passengers = this.passengerRepository.findAllOrdered();

        for (Passenger passenger : passengers) {
            stringBuilder.append(String.format("Passenger %s  %s%n      Email - %s%n        Phone - %s%n        Number of tickets - %d%n",
                    passenger.getFirstName(),
                    passenger.getLastName(),
                    passenger.getEmail(),
                    passenger.getPhoneNumber(),
                    passenger.getTickets().size())).append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    @Override
    public Passenger findByEmail(String email) {
        return this.passengerRepository.findByEmail(email);
    }

}
