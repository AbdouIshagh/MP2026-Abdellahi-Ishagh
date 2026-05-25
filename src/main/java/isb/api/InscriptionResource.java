package isb.api;

import java.util.List;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import isb.business.InscriptionService;
import isb.domain.Inscription;

@Path("/v1/inscriptions")
@RequestScoped
public class InscriptionResource {

    @Inject
    private InscriptionService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response inscrire(@Valid InscriptionDTO dto) {
        Inscription created = service.creer(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    // 1. GET global : lister toutes les inscriptions
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Inscription> listerToutes() {
        return service.recupererToutes();
    }

    // 2. GET ciblé : obtenir une inscription précise via son ID dans l'URL
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Inscription obtenirParId(@PathParam("id") Long id) {
        return service.chercherParId(id);
    }

    // 3. PUT : Déclencher manuellement la validation d'une inscription
    @PUT
    @Path("/{id}/valider")
    @Produces(MediaType.APPLICATION_JSON)
    public Response valider(@PathParam("id") Long id) {
        Inscription miseAJour = service.validerInscription(id);
        return Response.ok(miseAJour).build();
    }
}
