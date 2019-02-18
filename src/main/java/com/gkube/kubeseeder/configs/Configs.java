package com.gkube.kubeseeder.configs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.synchronoss.cloud.nio.multipart.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@ComponentScan("com.gkube.*")
public class Configs {

    @Bean("names-list")
    public List<String>namesList() throws IOException {
        InputStream is = TypeReference.class.getResourceAsStream("/json/names.json");

        ObjectMapper objeMapper=new ObjectMapper();
        List<Object> objects = objeMapper.readValue(IOUtils.inputStreamAsString(is, "UTF-8"),
                new TypeReference<List<String>>() {
                });

        return objects.stream()
                .map(e->e.toString())
                .collect(Collectors.toList());
    }
}
