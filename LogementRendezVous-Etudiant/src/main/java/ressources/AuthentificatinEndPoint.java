package ressources;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import entities.UserAuth;
import io.jsonwebtoken.*;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Path("/auth")
public class AuthentificatinEndPoint {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authUser(UserAuth userAuth) {
        try {
            authenticate(userAuth.getUsername(), userAuth.getPassword());

            String token = issueToken(userAuth.getUsername());
            return Response.ok(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private void authenticate(String username, String password) {
        if (!isValidUser(username, password)) {
            throw new RuntimeException("Invalid credentials");
        }
        System.out.println("Authenticating user...");
    }

    private boolean isValidUser(String username, String password) {
        return username != null && password != null ;
    }

    private String issueToken(String username) {
        String keyString = "simplekey";
        Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "HmacSHA256");

        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // Token valide pour 15 minutes
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        return jwtToken;
    }
}

