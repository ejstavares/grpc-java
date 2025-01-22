package cv.ejst.grpc.login;

import cv.ejst.grpc.login.services.HelloServiceImpl;
import cv.ejst.grpc.login.services.StockQuoteProviderImpl;
import cv.ejst.grpc.login.services.UserServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;
import cv.ejst.grpc.login.auth.OAuth2ServerInterceptor;

public class GrpcServer {
    private static final Logger logger = Logger.getLogger(GrpcServer.class.getName());
    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = ServerBuilder.forPort(50051)
                .addService(new UserServiceImpl())
                .addService(new HelloServiceImpl())
                .addService(new StockQuoteProviderImpl())
                .intercept(new OAuth2ServerInterceptor())
                .build();

        server.start();

        logger.info("gRPC Server started on port:: "+server.getPort());

        server.awaitTermination();
    }
}