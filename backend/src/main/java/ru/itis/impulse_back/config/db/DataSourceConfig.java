package ru.itis.impulse_back.config.db;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    private static final String PRIMARY = "spring.primary.datasource";
    private static final String REPLICA = "spring.replica.datasource";

    @Bean
    @ConfigurationProperties(PRIMARY)
    public DataSourceProperties primaryProps() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(PRIMARY + ".hikari")
    public DataSource primaryDataSource(DataSourceProperties primaryProps) {
        return primaryProps.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @ConfigurationProperties(REPLICA)
    public DataSourceProperties replicaProps() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(REPLICA + ".hikari")
    public DataSource replicaDataSource(DataSourceProperties replicaProps) {
        return replicaProps.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @Primary
    public DataSource routingDataSource(
            DataSource primaryDataSource,
            DataSource replicaDataSource
    ) {
        RoutingDataSource routing = new RoutingDataSource();
        Map<Object,Object> targets = new HashMap<>();
        targets.put(RoutingDataSource.Route.PRIMARY, primaryDataSource);
        targets.put(RoutingDataSource.Route.REPLICA, replicaDataSource);

        routing.setTargetDataSources(targets);
        routing.setDefaultTargetDataSource(primaryDataSource);
        return routing;
    }
}
