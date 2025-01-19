package cv.ejst.grpc.login;

import cv.ejst.grpc.login.protos.generated.hello.HelloRequest;
import cv.ejst.grpc.login.protos.generated.hello.HelloResponse;
import cv.ejst.grpc.login.protos.generated.hello.HelloServiceGrpc;
import cv.ejst.grpc.login.services.HelloServiceImpl;
import cv.ejst.grpc.login.services.UserServiceImpl;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.logging.Logger;

public class GrpcClient {
    private static final Logger logger = Logger.getLogger(GrpcClient.class.getName());
    public static void main(String[] args) throws IOException, InterruptedException {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);

        logger.info("gRPC Channel created ");

        var helloRequest = HelloRequest.newBuilder()
                .setFirstName("Ederlino")
                .setLastName("TAVARES")
                .build();

        logger.info("Say hello to server");


        HelloResponse helloResponse = stub.hello(helloRequest);

        logger.info("Response::::::::::::::::::::::::: "+helloResponse.getGreeting());
        logger.info("Shutdown the Channel");
        channel.shutdown();
        logger.info(" Channel is closed ");
    }
}