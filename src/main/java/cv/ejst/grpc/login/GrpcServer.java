package cv.ejst.grpc.login;

import cv.ejst.grpc.login.services.HelloServiceImpl;
import cv.ejst.grpc.login.services.UserServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

public class GrpcServer {
    private static final Logger logger = Logger.getLogger(GrpcServer.class.getName());
    public static void main(String[] args) throws IOException, InterruptedException {
        logger.info("Hello world!");

        Server server = ServerBuilder.forPort(50051)
                .addService(new UserServiceImpl())
                .addService(new HelloServiceImpl())
                .build();
        server.start();
        logger.info("gRPC Server started on port:: "+server.getPort());
        server.awaitTermination();
    }
}