package cv.ejst.grpc.login;

import cv.ejst.grpc.login.services.UserService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

public class ApplicationService {
    private static final Logger logger = Logger.getLogger(ApplicationService.class.getName());
    public static void main(String[] args) throws IOException, InterruptedException {
        logger.info("Hello world!");

//        Server server = ServerBuilder.forPort(50051).addService(new UserService()).build();
//        server.start();
//        logger.info("Server started on port:: "+server.getPort());
//        server.awaitTermination();
    }
}