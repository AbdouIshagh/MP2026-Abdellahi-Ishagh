package isb.domain;


import jakarta.persistence.*;

@Entity
@Table(name = "inscriptions")
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // On utilise directement le matricule de l'étudiant pour rester simple et performant
    @Column(nullable = false)
    private String matricule;

    @Enumerated(EnumType.STRING) // Sauvegarde en texte ("VALIDE") plutôt qu'en chiffre (1) dans la base
    @Column(nullable = false)
    private Statut statut;

    @Version
    private Long version; // Anti-concurrence : crucial si 5000 étudiants cliquent en même temps

    // --- Constructeurs ---
    public Inscription() {
    }

    public Inscription(String matricule, Statut statut) {
        this.matricule = matricule;
        this.statut = statut;
    }

    // --- Getters et Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }

    public Long getVersion() { return version; }
}
