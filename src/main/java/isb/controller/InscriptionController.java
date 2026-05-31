package isb.controller;

import java.util.List;
import java.util.Map;

import isb.dto.InscriptionDTO;
import isb.entite.Inscription;
import isb.services.InscriptionService;
import jakarta.annotation.security.DeclareRoles;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.annotation.security.RolesAllowed;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/v1/inscriptions")
@RequestScoped
@DeclareRoles({"ADMIN", "PROFESSOR"})
public class InscriptionController {

    @Inject
    private InscriptionService service;

    @Inject
    private JsonWebToken callerPrincipal;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Créer une inscription")
    @APIResponse(responseCode = "201", description = "Inscription créée avec succès")
    public Response inscrire(@Valid InscriptionDTO dto) {
        Inscription created = service.creer(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Lister toutes les inscriptions")
    @APIResponse(responseCode = "200", description = "Liste récupérée avec succès")
    public Response listerToutes() {
        List<Inscription> inscriptions = service.recupererToutes();
        return Response.ok(inscriptions).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtenir une inscription par ID")
    @APIResponse(responseCode = "200", description = "Inscription trouvée")
    public Response obtenirParId(@PathParam("id") Long id) {
        Inscription ins = service.chercherParId(id);
        return Response.ok(ins).build();
    }

    @PUT
    @Path("/{id}/valider")
    @RolesAllowed({"ADMIN", "PROFESSOR"})
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Valider une inscription")
    @APIResponse(responseCode = "200", description = "Inscription validée avec succès")
    public Response valider(@PathParam("id") Long id) {
        Inscription miseAJour = service.validerInscription(id);
        return Response.ok(miseAJour).build();
    }

    @PUT
    @Path("/{id}/changer-statut")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Changer le statut directement (Contournement Sécurité pour Démo)")
    public Response changerStatutDirect(@PathParam("id") Long id, Map<String, String> body) {
        String nouveauStatut = body.get("statut");
        if (nouveauStatut == null || nouveauStatut.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erreur\":\"Le statut est obligatoire\"}").build();
        }

        try {
            Inscription inscription = service.changerStatutDirect(id, nouveauStatut);
            if (inscription == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"erreur\":\"Inscription introuvable\"}").build();
            }
            return Response.ok(inscription).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"erreur\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
