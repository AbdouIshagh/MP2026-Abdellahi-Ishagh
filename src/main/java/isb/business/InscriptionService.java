package isb.business;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import isb.api.InscriptionDTO;
import isb.domain.Inscription;
import isb.domain.Statut;

@ApplicationScoped
public class InscriptionService {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @Inject
    private Event<InscriptionEvent> evt;

    @Transactional
    public Inscription creer(InscriptionDTO dto) {
        Inscription ins = new Inscription(dto.matricule(), Statut.EN_ATTENTE);
        em.persist(ins);
        evt.fire(new InscriptionEvent(ins.getMatricule()));
        return ins;
    }

    // 1. Récupérer toutes les inscriptions de la base
    public List<Inscription> recupererToutes() {
        return em.createQuery("SELECT i FROM Inscription i", Inscription.class).getResultList();
    }

    // 2. Trouver une inscription par son ID unique
    public Inscription chercherParId(Long id) {
        Inscription ins = em.find(Inscription.class, id);
        if (ins == null) {
            // Renvoie directement une erreur 404 HTTP si l'ID n'existe pas
            throw new WebApplicationException("Inscription introuvable", Response.Status.NOT_FOUND);
        }
        return ins;
    }

    // 3. Valider une inscription (Passage de EN_ATTENTE à VALIDE)
    @Transactional
    public Inscription validerInscription(Long id) {
        Inscription ins = chercherParId(id);
        ins.setStatut(Statut.VALIDE);
        // Pas besoin de faire em.merge(), @Transactional synchronise la modification automatiquement à la fin de la méthode
        return ins;
    }
}
