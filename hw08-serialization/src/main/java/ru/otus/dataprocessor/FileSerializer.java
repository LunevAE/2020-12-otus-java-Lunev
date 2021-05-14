package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

public class FileSerializer implements Serializer {
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        var map = new TreeMap<>(data);
        String json = new Gson().toJson(map);
        try {
            Files.writeString(Path.of(fileName), json);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}