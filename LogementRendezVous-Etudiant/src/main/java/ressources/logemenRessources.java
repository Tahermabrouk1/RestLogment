package ressources;

import entities.Logement;
import metiers.LogementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/logements")
public class logemenRessources {
    private static LogementBusiness logB = new LogementBusiness();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogements(@QueryParam("delegation") String delegation, @QueryParam("reference") Integer reference) {
        if (reference != null) {
            Logement logement = logB.getLogementsByReference(reference);
            if (logement != null) {
                return Response.ok(logement).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Logement with id " + reference + " not found.")
                        .build();
            }
        } else if (delegation != null && !delegation.isEmpty()) {
            return Response.ok(logB.getLogementsByDeleguation(delegation)).build();
        } else {
            return Response.ok(logB.getLogements()).build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addLog(Logement log) {
        if (logB.addLogement(log)) {
            return Response.status(Response.Status.CREATED).build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Failed to add logement.")
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateLogement(@PathParam("id") int id, Logement log) {
        if (logB.updateLogement(id, log)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Failed to update logement.")
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteLogement(@PathParam("id") int id) {
        if (logB.deleteLogement(id)) {
            return Response.ok().entity("logement deleted").build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Failed to delete logement.")
                .build();
    }
}
