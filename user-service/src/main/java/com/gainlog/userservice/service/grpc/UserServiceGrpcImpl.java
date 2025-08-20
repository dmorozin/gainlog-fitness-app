package com.gainlog.userservice.service.grpc;

import com.gainlog.userservice.model.entity.Role;
import com.gainlog.userservice.model.entity.User;
import com.gainlog.userservice.repository.RoleRepository;
import com.gainlog.userservice.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import user.UserProto;
import user.UserServiceGrpc;

import java.util.Optional;
import java.util.Set;

import static com.gainlog.userservice.utils.ERole.ROLE_USER;

@GrpcService
@RequiredArgsConstructor
public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void createUser(UserProto.CreateUserRequest request,
                           StreamObserver<UserProto.UserResponse> responseObserver) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        Role role = roleRepository.findByName(ROLE_USER ).orElseThrow(() -> new RuntimeException("role not found"));
        user.setRoles(Set.of(role));

        User createdUser = userRepository.save(user);

        UserProto.UserResponse response = getUserResponse(createdUser);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserByEmail(UserProto.UserByEmailRequest request,
                               StreamObserver<UserProto.UserResponse> responseObserver) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isPresent()) {
            UserProto.UserResponse response = getUserResponse(user.get());

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(io.grpc.Status.NOT_FOUND.withDescription("User not found").asRuntimeException());
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
