package com.gainlog.userservice.service.grpc;

import com.gainlog.core.exception.ResourceNotFoundException;
import com.gainlog.userservice.model.entity.Role;
import com.gainlog.userservice.model.entity.User;
import com.gainlog.userservice.repository.RoleRepository;
import com.gainlog.userservice.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.service.GrpcService;
import user.UserProto;
import user.UserServiceGrpc;

import java.util.Optional;
import java.util.Set;

import static com.gainlog.userservice.utils.Constants.USER_NOT_FOUND;
import static com.gainlog.userservice.utils.ERole.ROLE_USER;

@GrpcService
@Log4j2
@RequiredArgsConstructor
public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void createUser(UserProto.CreateUserRequest request,
                           StreamObserver<UserProto.UserResponse> responseObserver) {
        log.info("createUser request received: {}", request.toString());

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        Role role = roleRepository.findByName(ROLE_USER).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        user.setRoles(Set.of(role));

        userRepository.save(user);

        UserProto.UserResponse response = getUserResponse(user);

        log.info("Sending created user response");
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserByEmail(UserProto.UserByEmailRequest request,
                               StreamObserver<UserProto.UserResponse> responseObserver) {
        log.info("getUserByEmail request received: {}", request.toString());
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isPresent()) {
            UserProto.UserResponse response = getUserResponse(user.get());

            log.info("Sending found user response");
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription(String.format(USER_NOT_FOUND, request.getEmail()))
                    .asRuntimeException());
        }
    }

    private static UserProto.UserResponse getUserResponse(User user) {
        return UserProto.UserResponse.newBuilder()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .addAllRoles(user.getRoles().stream().map(role -> role.getName().toString()).toList())
                .build();
    }
}
