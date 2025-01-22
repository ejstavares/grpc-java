package cv.ejst.grpc.login.services;

import cv.ejst.grpc.login.protos.generated.hello.HelloRequest;
import cv.ejst.grpc.login.protos.generated.hello.HelloResponse;
import cv.ejst.grpc.login.protos.generated.hello.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver)  {
        String greeting = new StringBuilder()
                .append("Hello ")
                .append(request.getFirstName())
                .append(" ")
                .append(request.getLastName())
                .toString();
        Logger logger = Logger.getLogger(HelloServiceImpl.class.getName());
        for (int i = 0; i < 5; i++) {
            logger.info("minutes:: "+i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting(greeting)
                .build();


        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
