package main.java.org.tbank.hw2;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.org.tbank.hw2.model.Location;
import main.java.org.tbank.hw2.service.XmlParser;
import main.java.org.tbank.hw2.service.impl.LocationParserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Main {
    private static final XmlParser<Location> locationParser = new LocationParserImpl();
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        processFile("city-error");
        processFile("city");
    }

    public static void processFile(String fileName) {
        try {
            Location location = parseJsonFile(fileName);
            writeXmlFile(location, fileName);
        } catch (Exception e) {
            logger.error("An error occurred", e);
        }
    }

    private static Location parseJsonFile(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonFilePath = String.format("json/%s.json", fileName);
        logger.debug("Parsing JSON file: {}", jsonFilePath);
        Location location = objectMapper.readValue(new File(jsonFilePath), Location.class);
        logger.debug("Successfully parsed JSON file: {}", location);
        return location;
    }

    private static void writeXmlFile(Location location, String fileName) throws RuntimeException {
        createXmlDirectory();
        String xmlFilePath = String.format("xml/%s.xml", fileName);
        locationParser.toXML(location, xmlFilePath);
        logger.info("XML file created successfully: {}", xmlFilePath);
    }

    public static void createXmlDirectory() {
        File xmlDirectory = new File("xml");
        if (!xmlDirectory.exists()) {
            if (xmlDirectory.mkdirs()) {
                logger.info("Created 'xml' directory");
            } else {
                logger.warn("Failed to create 'xml' directory");
            }
        }
    }
}