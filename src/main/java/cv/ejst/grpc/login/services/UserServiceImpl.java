package cv.ejst.grpc.login.services;


import cv.ejst.grpc.login.protos.generated.user.Empty;
import cv.ejst.grpc.login.protos.generated.user.LoginRequest;
import cv.ejst.grpc.login.protos.generated.user.LoginResponse;
import cv.ejst.grpc.login.protos.generated.user.UserServiceGrpc.UserServiceImplBase;
import io.grpc.stub.StreamObserver;

public class UserServiceImpl extends UserServiceImplBase {
    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        String username = request.getUsername();
        String password = request.getPassword();

        LoginResponse loginResponse = LoginResponse.newBuilder().build();

        if (username.equals("eder") && password.equals("123")){
            loginResponse = loginResponse.toBuilder()
                    .setResponseMessage("Login Success")
                    .setResponseCode(200)
                    .build();
        } else {
            loginResponse = loginResponse.newBuilderForType()
                    .setResponseMessage("Login Failed")
                    .setResponseCode(401)
                    .build();
        }
        responseObserver.onNext(loginResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void logout(Empty request, StreamObserver<LoginResponse> responseObserver) {
    }
}
