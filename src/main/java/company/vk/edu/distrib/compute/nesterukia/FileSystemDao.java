package company.vk.edu.distrib.compute.nesterukia;

import company.vk.edu.distrib.compute.Dao;

import java.io.IOException;
import java.util.NoSuchElementException;

public class FileSystemDao implements Dao<byte[]> {
    @Override
    public byte[] get(String key) throws NoSuchElementException, IllegalArgumentException, IOException {
        return new byte[0];
    }

    @Override
    public void upsert(String key, byte[] value) throws IllegalArgumentException, IOException {

    }

    @Override
    public void delete(String key) throws IllegalArgumentException, IOException {

    }

    @Override
    public void close() throws IOException {
    }
}
