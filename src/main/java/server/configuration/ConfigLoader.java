package server.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;


public class ConfigLoader {

    public static Config loadConfig() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File("src/main/resources/config/world_config.json"), Config.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
