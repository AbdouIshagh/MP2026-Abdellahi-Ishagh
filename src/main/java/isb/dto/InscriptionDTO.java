package isb.dto;

import isb.entite.Statut;
import jakarta.validation.constraints.NotBlank;

public record InscriptionDTO(
        Long id,
        String matricule, // Optionnel désormais puisque créé par le serveur
        @NotBlank(message = "Le nom est obligatoire") String nom,
        @NotBlank(message = "Le prénom est obligatoire") String prenom,
        @NotBlank(message = "La filière est obligatoire") String filiere,
        Statut statut
) {
}
