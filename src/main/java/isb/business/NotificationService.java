package isb.business;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class NotificationService {

    // Grâce à @Observes, cette méthode se déclenche automatiquement dès qu'un InscriptionEvent est publié
    public void onNouvelleInscription(@Observes InscriptionEvent event) {
        System.out.println("\n====================================================");
        System.out.println("[NOTIF] Événement CDI intercepté avec succès !");
        System.out.println("[NOTIF] Traitement de l'inscription pour le matricule : " + event.matricule());
        System.out.println("====================================================\n");
    }
}
