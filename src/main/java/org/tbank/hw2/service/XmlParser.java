package org.tbank.hw2.service;

public interface XmlParser<T> {
    void toXML(T objectToParse, String filePath) throws RuntimeException;
}
