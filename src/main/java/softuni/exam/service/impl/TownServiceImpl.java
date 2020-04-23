package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.TownSeedDto;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

import static softuni.exam.constants.GlobalConstants.*;

@Service
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, FileUtil fileUtil) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return this.fileUtil.readFileContent(TOWNS_PATH);
    }

    @Override
    public String importTowns() throws FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();

        TownSeedDto[] townSeedDtos = this.gson.fromJson(new FileReader(TOWNS_PATH), TownSeedDto[].class);
        for (TownSeedDto townSeedDto : townSeedDtos) {
            if (this.validationUtil.isValid(townSeedDto)) {
                if (this.townRepository.findByName(townSeedDto.getName()) == null) {
                    Town town = this.modelMapper.map(townSeedDto, Town.class);

                    this.townRepository.saveAndFlush(town);
                    stringBuilder.append(String.format("Successfully imported Town %s - %d", townSeedDto.getName(), townSeedDto.getPopulation()));
                } else {
                    stringBuilder.append(EXIST);
                }
            } else {
                stringBuilder.append(String.format(INCORRECT_DATA_MESSAGE, "Town"));
            }
            stringBuilder.append(System.lineSeparator());
        }



        return stringBuilder.toString();
    }

    @Override
    public Town findByName(String name) {
        return this.townRepository.findByName(name);
    }
}
