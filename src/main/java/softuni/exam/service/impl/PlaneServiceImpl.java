package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.PlaneSeedDto;
import softuni.exam.models.dtos.PlaneSeedRootDto;
import softuni.exam.models.entities.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static softuni.exam.constants.GlobalConstants.*;
import static softuni.exam.constants.GlobalConstants.PLANES_PATH;

@Service
public class PlaneServiceImpl implements PlaneService {

    private final PlaneRepository planeRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public PlaneServiceImpl(PlaneRepository planeRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.planeRepository = planeRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(Path.of(PLANES_PATH));
    }

    @Override
    public String importPlanes() throws JAXBException, FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();

        PlaneSeedRootDto planeSeedRootDto =
                this.xmlParser.parseXml(PlaneSeedRootDto.class, PLANES_PATH);

        List<PlaneSeedDto> planeSeedDtos = planeSeedRootDto.getPlanes();

        for (PlaneSeedDto planeSeedDto : planeSeedDtos) {
            if (this.validationUtil.isValid(planeSeedDto)) {

                if (this.planeRepository.findByRegisterNumber(planeSeedDto.getRegisterNumber()) == null) {
                    Plane plane = this.modelMapper.map(planeSeedDto, Plane.class);


                    this.planeRepository.saveAndFlush(plane);

                    stringBuilder.append(String.format("Successfully imported Plane %s", planeSeedDto.getRegisterNumber()));
                } else {
                    stringBuilder.append(EXIST);
                }
            } else {
                stringBuilder.append(String.format(INCORRECT_DATA_MESSAGE, "Plane"));
            }
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }

    @Override
    public Plane findByNumber(String number) {

        return this.planeRepository.findByRegisterNumber(number);
    }
}
