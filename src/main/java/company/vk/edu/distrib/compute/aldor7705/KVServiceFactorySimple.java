package company.vk.edu.distrib.compute.aldor7705;

import company.vk.edu.distrib.compute.KVService;
import company.vk.edu.distrib.compute.KVServiceFactory;

import java.io.IOException;

public class KVServiceFactorySimple extends KVServiceFactory {
    @Override
    protected KVService doCreate(int port) throws IOException {
        return new KVServiceSimple(port, new EntityDao());
    }
}
