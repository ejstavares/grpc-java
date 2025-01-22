package cv.ejst.grpc.login;

import cv.ejst.grpc.login.protos.generated.hello.HelloRequest;
import cv.ejst.grpc.login.protos.generated.hello.HelloResponse;
import cv.ejst.grpc.login.protos.generated.hello.HelloServiceGrpc;
import cv.ejst.grpc.login.protos.generated.user.LoginRequest;
import cv.ejst.grpc.login.protos.generated.user.UserServiceGrpc;
import io.grpc.*;
import java.util.concurrent.Executor;

import java.io.IOException;
import java.util.logging.Logger;
import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

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

        var callCredentials = new CallCredentials() {
            @Override
            public void applyRequestMetadata(RequestInfo requestInfo, Executor appExecutor, MetadataApplier applier) {
                Metadata metadata = new Metadata();
                metadata.put(Metadata.Key.of("Authorization", ASCII_STRING_MARSHALLER), "Bearer access-token:fdgfdghgdhdghfdfgdsfgghfgfhg:+1");
                applier.apply(metadata);
            }
        };
        HelloResponse helloResponse = stub.withCallCredentials(callCredentials).hello(helloRequest);

        logger.info("Response::::::::::::::::::::::::: "+helloResponse.getGreeting());

        //User RPC
        UserServiceGrpc.UserServiceBlockingStub userStub = UserServiceGrpc.newBlockingStub(channel);

        var loginRequest = LoginRequest.newBuilder()
                .setUsername("eder")
                .setPassword("123e").build();
        var loginResponse = userStub.withCallCredentials(callCredentials).login(loginRequest);
        logger.info("Response::::::::::::::::::::::::: "+loginResponse.getResponseMessage()+ ", code::::"+loginResponse.getResponseCode());
        logger.info("Shutdown the Channel");
        channel.shutdown();
        logger.info(" Channel is closed ");
    }
}