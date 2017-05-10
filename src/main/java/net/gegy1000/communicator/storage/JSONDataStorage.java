package net.gegy1000.communicator.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@link DataStorage} implementation that stores local data in local JSON files
 */
public class JSONDataStorage implements DataStorage {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private File baseDirectory;

    public JSONDataStorage(File baseDirectory) {
        this.baseDirectory = baseDirectory;
        if (!baseDirectory.exists()) {
            baseDirectory.mkdirs();
        }
    }

    @Override
    public boolean has(String identifier) {
        return this.get(identifier).exists();
    }

    @Override
    public <T> T load(String identifier, Class<T> type) throws IOException {
        return GSON.fromJson(new FileReader(this.get(identifier)), type);
    }

    @Override
    public <T> void save(String identifier, T data) throws IOException {
        File file = this.get(identifier);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (OutputStream out = new FileOutputStream(file)) {
            out.write(GSON.toJson(data).getBytes());
        }
    }

    private File get(String identifier) {
        return new File(this.baseDirectory, identifier + ".json");
    }
}
