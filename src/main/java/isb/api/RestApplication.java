package isb.api;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@DataSourceDefinition(
        name = "java:app/jdbc/uniregis", // C'est ce nom que vous devez écrire dans <jta-data-source> de votre persistence.xml
        className = "org.postgresql.ds.PGSimpleDataSource",
        url = "jdbc:postgresql://uniregis-db:5432/uniregis_db", // Notez l'utilisation du nom du conteneur "uniregis-db"
        user = "uniregis_user",
        password = "uniregis_password"
)@ApplicationPath("/api")
public class RestApplication extends Application {
    // Cette classe reste vide, l'annotation suffit à tout activer !
}
