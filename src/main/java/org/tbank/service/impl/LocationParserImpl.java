package main.java.org.tbank.service.impl;
import main.java.org.tbank.Main;
import main.java.org.tbank.model.Location;
import main.java.org.tbank.service.XmlParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocationParserImpl implements XmlParser<Location> {

    private final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final int MAX_FILE_PATH_LENGTH = 15;

    public void toXML(Location objectToParse, String filePath) throws RuntimeException {
        if (filePath.length() > MAX_FILE_PATH_LENGTH) {
            logger.warn("The file path '{}' is quite long and could potentially cause issues.", filePath);
            throw new IllegalArgumentException("The file path is too long: " + filePath);
        }

        logger.debug("Starting XML conversion...");

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            logger.info("DocumentBuilder created successfully");
        } catch (Exception e) {
            logger.error("Error creating DocumentBuilder", e);
            throw new RuntimeException("Error creating DocumentBuilder", e);
        }

        Document document = documentBuilder.newDocument();
        Element rootElement = document.createElement("Location");
        document.appendChild(rootElement);

        Element slugElement = document.createElement("Slug");
        slugElement.appendChild(document.createTextNode(objectToParse.getSlug()));
        rootElement.appendChild(slugElement);

        Element coordsElement = document.createElement("Coords");
        rootElement.appendChild(coordsElement);

        Element latElement = document.createElement("Lat");
        latElement.appendChild(document.createTextNode(String.valueOf(objectToParse.getCoords().getLat())));
        coordsElement.appendChild(latElement);

        Element lonElement = document.createElement("Lon");
        lonElement.appendChild(document.createTextNode(String.valueOf(objectToParse.getCoords().getLon())));
        coordsElement.appendChild(lonElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
            logger.info("Transformer created successfully");
        } catch (Exception e) {
            logger.error("Error creating Transformer", e);
            throw new RuntimeException("Error creating Transformer", e);
        }
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(filePath));

        logger.debug("Starting XML transformation...");
        try {
            transformer.transform(domSource, streamResult);
            logger.info("XML file written to {}", filePath);
        } catch (TransformerException e) {
            logger.error("Error during XML transformation", e);
            throw new RuntimeException("Error during XML transformation", e);
        }
    }

}

