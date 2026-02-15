package dev.n0fckgway.lab4.api;

import dev.n0fckgway.lab4.api.record.point.PointCheckRequest;
import dev.n0fckgway.lab4.api.record.point.PointResultResponse;
import dev.n0fckgway.lab4.model.PointResult;
import dev.n0fckgway.lab4.model.User;
import dev.n0fckgway.lab4.persistence.PointResultRepository;
import dev.n0fckgway.lab4.persistence.UserRepository;
import dev.n0fckgway.lab4.services.AreaCheckService;
import dev.n0fckgway.lab4.services.AuthService;
import jakarta.ejb.EJB;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.List;

@Path("/points")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PointController {

    private static final List<Double> ALLOWED_VALUES = List.of(-5d, -4d, -3d, -2d, -1d, 0d, 1d, 2d, 3d);

    @EJB
    private AuthService authService;

    @EJB
    private UserRepository userRepository;

    @EJB
    private PointResultRepository pointResultRepository;

    @EJB
    private AreaCheckService areaCheckService;

    @GET
    @Path("/history")
    public Response getHistory(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        Long userId = requireUserId(authHeader);
        if (userId == null) {
            return unauthorized();
        }

        List<PointResultResponse> history = pointResultRepository.findAllByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();

        return Response.ok(history).build();
    }

    @DELETE
    @Path("/history")
    public Response clearHistory(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        Long userId = requireUserId(authHeader);
        if (userId == null) {
            return unauthorized();
        }

        pointResultRepository.deleteAllByUserId(userId);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/check")
    public Response checkPoint(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @Valid PointCheckRequest request
    ) {
        Long userId = requireUserId(authHeader);
        if (userId == null) {
            return unauthorized();
        }
        if (request == null || request.x() == null || request.y() == null || request.r() == null || request.fromGraph() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Point payload is required")
                    .build();
        }
        if (!isValidX(request.x(), request.fromGraph())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("X must be in range [-5, 3]")
                    .build();
        }
        if (!isValidY(request.y())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Y must be in range [-3, 3]")
                    .build();
        }
        if (!isValidR(request.r())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("R must be one of -5..3 and greater than 0")
                    .build();
        }

        User user = userRepository.findById(userId);
        if (user == null) {
            return unauthorized();
        }

        boolean hit = areaCheckService.isHit(request.x(), request.y(), request.r());

        PointResult result = new PointResult();
        result.setUser(user);
        result.setX(request.x());
        result.setY(request.y());
        result.setR(request.r());
        result.setHit(hit);
        result.setCheckedAt(Instant.now());

        pointResultRepository.save(result);

        return Response.ok(toResponse(result)).build();
    }

    private boolean isValidX(Double x, Boolean fromGraph) {
        if (Boolean.TRUE.equals(fromGraph)) {
            return x >= -5.0 && x <= 3.0;
        }
        return ALLOWED_VALUES.contains(x);
    }

    private boolean isValidY(Double y) {
        return y >= -3.0 && y <= 3.0;
    }

    private boolean isValidR(Double r) {
        return ALLOWED_VALUES.contains(r) && r > 0.0;
    }

    private Long requireUserId(String authHeader) {
        try {
            return authService.getUserIdByToken(authHeader);
        } catch (AuthenticationException e) {
            return null;
        }
    }

    private Response unauthorized() {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Unauthorized")
                .build();
    }

    private PointResultResponse toResponse(PointResult p) {
        return new PointResultResponse(
                p.getId(),
                p.getX(),
                p.getY(),
                p.getR(),
                p.isHit(),
                p.getCheckedAt().toString()
        );
    }
}
