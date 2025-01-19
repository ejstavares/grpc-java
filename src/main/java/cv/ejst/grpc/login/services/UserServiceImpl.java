package cv.ejst.grpc.login.services;


import cv.ejst.grpc.login.protos.generated.user.Empty;
import cv.ejst.grpc.login.protos.generated.user.LoginRequest;
import cv.ejst.grpc.login.protos.generated.user.LoginResponse;
import cv.ejst.grpc.login.protos.generated.user.UserServiceGrpc.UserServiceImplBase;
import io.grpc.stub.StreamObserver;

public class UserServiceImpl extends UserServiceImplBase {
    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        super.login(request, responseObserver);
    }

    @Override
    public void logout(Empty request, StreamObserver<LoginResponse> responseObserver) {
        super.logout(request, responseObserver);
    }
}
