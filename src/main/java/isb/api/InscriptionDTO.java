package isb.api;

import jakarta.validation.constraints.NotBlank;
import isb.domain.Statut;

public record InscriptionDTO(
        Long id,

        @NotBlank(message = "Le matricule est obligatoire") // Évite qu'on envoie un texte vide
        String matricule,

        Statut statut
) {}
