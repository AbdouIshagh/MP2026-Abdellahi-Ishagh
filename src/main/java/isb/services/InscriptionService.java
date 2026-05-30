package isb.services;

import java.util.List;
import isb.dto.InscriptionDTO;
import isb.entite.Etudiant;
import isb.entite.Inscription;
import isb.entite.Statut;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class InscriptionService {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @Inject
    private Event<InscriptionEvent> evt;

    @Transactional
    public Inscription creer(InscriptionDTO dto) {
        Inscription inscription = new Inscription();

        // 1. Génération automatique du matricule au format ISB-XXX
        long totalInscriptions = em.createQuery("SELECT COUNT(i) FROM Inscription i", Long.class)
                .getSingleResult();
        long prochainNumero = totalInscriptions + 1;
        String matriculeAutomatique = String.format("ISB-%03d", prochainNumero);

        // 2. Instancier l'entité Étudiant liée (Correction des accesseurs du Record)
        if (inscription.getEtudiant() == null) {
            isb.entite.Etudiant etudiant = new isb.entite.Etudiant();

            // Utilisation de la syntaxe des records (.nom(), .prenom(), .filiere())
            etudiant.setNom(dto.nom());
            etudiant.setPrenom(dto.prenom());
            etudiant.setFiliere(dto.filiere());
            etudiant.setMatricule(matriculeAutomatique);

            inscription.setEtudiant(etudiant);
        }

        // Correction de la gestion de l'énumération Statut
        inscription.setStatut(Statut.EN_ATTENTE);

        em.persist(inscription);
        return inscription;
    }

    // Récupérer toutes les inscriptions en forçant la jointure pour éviter le lazy-loading résiduel
    public List<Inscription> recupererToutes() {
        return em.createQuery("SELECT i FROM Inscription i JOIN FETCH i.etudiant", Inscription.class).getResultList();
    }

    public Inscription chercherParId(Long id) {
        Inscription ins = em.find(Inscription.class, id);
        if (ins == null) {
            throw new WebApplicationException("Inscription introuvable", Response.Status.NOT_FOUND);
        }
        return ins;
    }

    @Transactional
    public Inscription validerInscription(Long id) {
        Inscription ins = chercherParId(id);
        ins.setStatut(Statut.VALIDE);
        return ins;
    }

    public boolean testerConnexionBase() {
        try {
            em.createNativeQuery("SELECT 1").getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Inscription changerStatutDirect(Long id, String nouveauStatut) {
        // em désigne votre @PersistenceContext EntityManager injecté dans le service
        Inscription inscription = em.find(Inscription.class, id);
        if (inscription != null) {
            inscription.setStatut(Statut.valueOf(nouveauStatut));
            em.merge(inscription);
        }
        return inscription;
    }
}
