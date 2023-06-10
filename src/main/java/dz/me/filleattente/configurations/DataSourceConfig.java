package dz.me.filleattente.configurations;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource getDataSource() throws SQLException {
        DataSource datasource = null;
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        try {
            dataSourceBuilder.driverClassName("org.mariadb.jdbc.Driver");
            dataSourceBuilder.url(
                    "jdbc:mariadb://localhost:3306/stat_file_attente?createDatabaseIfNotExist=true&userSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTCe&characterEncoding=UTF-8&useFractionalSeconds=true");
            dataSourceBuilder.username("tarek");
            dataSourceBuilder.password("file1");
            datasource = dataSourceBuilder.build();

            datasource.getConnection();
            return datasource;

        } catch (Exception e) {
            dataSourceBuilder.driverClassName("org.mariadb.jdbc.Driver");
            dataSourceBuilder.url(
                    "jdbc:mariadb://localhost:3306:3306/file_attente?createDatabaseIfNotExist=true&userSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTCe&characterEncoding=UTF-8&useFractionalSeconds=true&authenticationScheme=JavaKerberos");
            dataSourceBuilder.username("tarek");
            dataSourceBuilder.password("file");
            datasource = dataSourceBuilder.build();
            return datasource;
        }

    }
}