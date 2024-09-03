package main.java.org.tbank.service;

public interface XmlParser<T> {
    void toXML(T objectToParse, String filePath) throws RuntimeException;
}
