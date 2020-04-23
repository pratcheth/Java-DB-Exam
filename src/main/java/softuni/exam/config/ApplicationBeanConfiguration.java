package softuni.exam.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import softuni.exam.util.*;

import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public DateTimeFormatter formatterWithHours() {
        return DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss");
    }


    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtilImpl();
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Bean
    public XmlParser xmlParser() {
        return new XmlParserImpl();
    }

    @Bean
    FileUtil fileIOUtil() {
        return new FileUtilImpl();
    }

}
