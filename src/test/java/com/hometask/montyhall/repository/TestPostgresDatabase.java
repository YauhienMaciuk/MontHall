package com.hometask.montyhall.repository;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

@Profile("dbtest")
public class TestPostgresDatabase implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestPostgresDatabase.class);
    private static final JdbcDatabaseContainer<?> POSTGRES_CONTAINER = initiatePostgresContainer();
    public static NamedParameterJdbcTemplate jdbcTemplate;

    static {
        try {
            initiateJdbc();
        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        }
    }

    public static void resetDb() {
        try {
            LOGGER.info("CLEANING UP DATABASE");
            runLiquibase(Objects.requireNonNull(jdbcTemplate.getJdbcTemplate().getDataSource()), "liquibase/changelog-test.xml");
        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        }
    }

    private static void initiateJdbc() throws SQLException, LiquibaseException {
        POSTGRES_CONTAINER.start();
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource(
                POSTGRES_CONTAINER.getJdbcDriverInstance(),
                POSTGRES_CONTAINER.getJdbcUrl(),
                POSTGRES_CONTAINER.getUsername(),
                POSTGRES_CONTAINER.getPassword());

        runLiquibase(dataSource, "db/changelog-master.xml");
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private static void runLiquibase(DataSource dataSource, String changeLogFile) throws SQLException, LiquibaseException {
        Connection dbConnection = dataSource.getConnection();
        try (Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), new JdbcConnection(dbConnection))) {
            liquibase.rollback(1, "test");
            liquibase.update("test");
        }
    }

    private static JdbcDatabaseContainer<?> initiatePostgresContainer() {
        try (JdbcDatabaseContainer<?> jdbcDatabaseContainer = new PostgreSQLContainer("postgres:11.9")
                .withDatabaseName("monty_hall_db")
                .withUsername("postgres")
                .withPassword("postgres")
        ) {
            return jdbcDatabaseContainer;
        }
    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url=" + POSTGRES_CONTAINER.getJdbcUrl(),
                "spring.datasource.username=" + POSTGRES_CONTAINER.getUsername(),
                "spring.datasource.password=" + POSTGRES_CONTAINER.getPassword()
        ).applyTo(configurableApplicationContext);
    }
}
