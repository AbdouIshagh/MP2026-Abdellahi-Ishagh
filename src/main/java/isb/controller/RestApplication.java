package isb.controller;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig; // Import ajouté
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.Contact;

@DataSourceDefinition(
        name = "java:app/jdbc/uniregis",
        className = "org.postgresql.ds.PGSimpleDataSource",
        url = "jdbc:postgresql://uniregis-db:5432/uniregis_db",
        user = "uniregis_user",
        password = "uniregis_password"
)
@OpenAPIDefinition(
        info = @Info(
                title = "UniRegis API",
                version = "1.0",
                description = "Backend Universitaire Cloud-Native - ISB Mauritanie",
                contact = @Contact(name = "Dr. EL BENANY Mohamed Mahmoud - CS201")
        )
)
@LoginConfig(authMethod = "MP-JWT", realmName = "mp-jwt-realm")
@ApplicationPath("/api")
public class RestApplication extends Application {
}
