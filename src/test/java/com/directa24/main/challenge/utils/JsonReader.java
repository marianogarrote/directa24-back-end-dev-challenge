package com.directa24.main.challenge.utils;

import com.directa24.main.challenge.application.dto.MoviePageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public final class JsonReader {
    private static final Logger logger = LoggerFactory.getLogger(JsonReader.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String PARSING_ERROR_FORMAT = "Error parsing Json from %s : %s";

    public static <T> T readJson(String path, Class<T> clazz) {
        File file = getFileFromResource(path);
        if(file != null) {
            try {
                return mapper.readValue(file, clazz);
            } catch (IOException e) {
                throw new IllegalArgumentException(String.format(PARSING_ERROR_FORMAT, path, e.getMessage()), e);
            }
        }
        logger.error("File not found at {}", path);
        throw new IllegalArgumentException("File not found at " + path);
    }

    private static File getFileFromResource(String fileName) {

        ClassLoader classLoader = JsonReader.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        }
        try {
            return new File(resource.toURI());
        } catch (URISyntaxException e) {
            return null;
        }
    }



    public static void main(String[] args) {
        MoviePageDto page = readJson("json/page01.json", MoviePageDto.class);
        System.out.println("Page: " + page);
    }
}
