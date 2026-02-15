package dev.n0fckgway.lab4.api;

import dev.n0fckgway.lab4.api.record.auth.LoginRequest;
import dev.n0fckgway.lab4.api.record.auth.LoginResponse;
import dev.n0fckgway.lab4.api.record.auth.RegisterRequest;
import dev.n0fckgway.lab4.api.record.auth.RegisterResponse;
import dev.n0fckgway.lab4.model.User;
import dev.n0fckgway.lab4.persistence.UserRepository;
import dev.n0fckgway.lab4.services.JwtService;
import dev.n0fckgway.lab4.services.hash.Argon2EncoderPassword;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;



@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    @EJB
    private UserRepository userRepository;

    @EJB
    private JwtService jwtService;

    private final Argon2EncoderPassword argon2EncoderPassword = new Argon2EncoderPassword();

    @POST
    @Transactional
    @Path("/register")
    public Response register(@Valid RegisterRequest request) {
        if (request == null || request.email() == null || request.password() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String email = normalizeEmail(request.email());
        String pass = request.password().trim();

        if (userRepository.userExists(email)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new RegisterResponse("Такая почта уже используется! Введите другую!", false))
                    .build();
        }

        if (email.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (pass.length() < 6) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new RegisterResponse("Пароль должен иметь не менее 6 символов", false)).build();
        }


        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(argon2EncoderPassword.hashPassword(pass));
        userRepository.save(user);

        String token = jwtService.createToken(user.getId());
        return Response.status(Response.Status.CREATED)
            .entity(new RegisterResponse("Вы успешно зарегистрировались!", true, token))
            .build();

    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.email() == null || loginRequest.password() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        String email = normalizeEmail(loginRequest.email());
        String password = loginRequest.password().trim();

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new LoginResponse("Неверная почта или пароль!", null))
                .build();
        }

        User user = userOpt.get();
        String passwordHash = user.getPasswordHash();

        if (!argon2EncoderPassword.checkPass(passwordHash, password)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new LoginResponse("Неверная почта или пароль!", null))
                .build();
        }

        String token = jwtService.createToken(user.getId());
        return Response.ok(new LoginResponse("login successful", token)).build();

    }

    @GET
    @Path("/logout")
    public Response logout(@Context HttpServletRequest request) {
        if (request != null) {
            var session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
        return Response.ok(new LoginResponse("logout successful, remove token on client", null)).build();
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }




}
