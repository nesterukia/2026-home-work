package company.vk.edu.distrib.compute.nesterukia;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import company.vk.edu.distrib.compute.KVService;
import company.vk.edu.distrib.compute.nesterukia.utils.HttpMethod;
import company.vk.edu.distrib.compute.nesterukia.utils.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.NoSuchElementException;
import java.util.Objects;


public class FileSystemKVService implements KVService {
    private static final Logger log = LoggerFactory.getLogger(FileSystemKVService.class);

    private static final String BASE_URI = "/v0";
    private static final String STATUS_URI = "/status";
    private static final String ENTITY_URI = "/entity";

    private static final int SERVER_STOP_TIMEOUT_SEC = 1;
    private static final int EMPTY_RESPONSE_LENGTH = 0;
    private final HttpServer server;

    public FileSystemKVService(int port) throws IOException {
        this.server = HttpServer.create(
                new InetSocketAddress(port),
                0
        );
        initService();
        log.debug("Server was initialized on port: {}", port);
    }

    private void initService() {
        server.createContext(BASE_URI.concat(STATUS_URI), new ErrorHttpHandler(http -> {
            final String requestMethod = http.getRequestMethod();
            if (Objects.equals(HttpMethod.GET, requestMethod)) {
                http.sendResponseHeaders(HttpStatus.OK, EMPTY_RESPONSE_LENGTH);
            } else {
                http.sendResponseHeaders(HttpStatus.METHOD_NOT_ALLOWED, EMPTY_RESPONSE_LENGTH);
            }
            http.close();
        }));

        server.createContext(BASE_URI.concat(ENTITY_URI), new ErrorHttpHandler(http -> {
            final String method = http.getRequestMethod();
            final String query = http.getRequestURI().getQuery();

            http.close();
        }));
    }

    @Override
    public void start() {
        server.start();
        log.info("Server has started.");
    }

    @Override
    public void stop() {
        server.stop(SERVER_STOP_TIMEOUT_SEC);
    }

    private static class ErrorHttpHandler implements HttpHandler {
        private final HttpHandler delegate;

        private ErrorHttpHandler(HttpHandler delegate) {
            this.delegate = delegate;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                delegate.handle(exchange);
            } catch (IllegalArgumentException e) {
                exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST, EMPTY_RESPONSE_LENGTH);
            } catch (NoSuchElementException e) {
                exchange.sendResponseHeaders(HttpStatus.NOT_FOUND, EMPTY_RESPONSE_LENGTH);
            } catch (IOException e) {
                exchange.sendResponseHeaders(HttpStatus.INTERNAL_SERVER_ERROR, EMPTY_RESPONSE_LENGTH);
            } finally {
                exchange.close();
            }
        }
    }
}
