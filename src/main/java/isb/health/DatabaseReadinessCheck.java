package isb.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import isb.services.InscriptionService;

@Readiness
@ApplicationScoped
public class DatabaseReadinessCheck implements HealthCheck {

    @Inject
    private InscriptionService service; // On utilise le service fonctionnel

    @Override
    public HealthCheckResponse call() {
        if (service != null && service.testerConnexionBase()) {
            return HealthCheckResponse.up("PostgreSQL Connection OK");
        } else {
            return HealthCheckResponse.down("PostgreSQL Connection FAILED");
        }
    }
}
