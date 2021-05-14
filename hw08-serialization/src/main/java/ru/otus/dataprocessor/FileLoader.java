package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileLoader implements Loader {
    private final List<Measurement> measurements;

    public FileLoader(String fileName) throws FileProcessException {
        try {
            URL resource = getClass().getClassLoader().getResource(fileName);
            String data = Files.readString(Path.of(resource.toURI()));
            Type type = new TypeToken<List<Measurement>>(){}.getType();
            measurements = new Gson().fromJson(data, type);
        }
        catch (IOException | URISyntaxException e) {
            throw new FileProcessException(e);
        }
    }

    @Override
    public List<Measurement> load() {
        return measurements;
    }
}
