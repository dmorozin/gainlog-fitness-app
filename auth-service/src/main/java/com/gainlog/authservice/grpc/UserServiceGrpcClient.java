package com.gainlog.authservice.grpc;

import com.gainlog.authservice.model.dto.RegisterUserDTO;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import user.UserProto;
import user.UserServiceGrpc;

@Log4j2
@Service
public class UserServiceGrpcClient {

    private final UserServiceGrpc.UserServiceBlockingStub blockingStub;

    public UserServiceGrpcClient(@Value("${user.service.address:localhost}") String serverAddress,
                                 @Value("${user.service.grpc.port:9002}") int serverPort) {
        log.info("Connecting to User Service GRPC server at {}:{}", serverAddress, serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();
        blockingStub = UserServiceGrpc.newBlockingStub(channel);
    }

    public UserProto.UserResponse getUserByEmail(String email) {
        UserProto.UserByEmailRequest request = UserProto.UserByEmailRequest.newBuilder()
                .setEmail(email)
                .build();

        return blockingStub.getUserByEmail(request);
    }

    public UserProto.UserResponse createUser(RegisterUserDTO registerUserDTO) {
        UserProto.CreateUserRequest request = UserProto.CreateUserRequest.newBuilder()
                .setEmail(registerUserDTO.getEmail())
                .setPassword(registerUserDTO.getPassword())
                .setUsername(registerUserDTO.getUsername())
                .setFirstName(registerUserDTO.getFirstName())
                .setLastName(registerUserDTO.getLastName())
                .build();

        return blockingStub.createUser(request);
    }
}
