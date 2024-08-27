package com.y_lab.y_lab;

import com.y_lab.y_lab.config.DatabaseConfig;
import com.y_lab.y_lab.repository.audit.JdbcAuditRepository;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Testcontainers
@SpringJUnitConfig(classes = {DatabaseConfig.class, JdbcAuditRepository.class})
@Transactional
@Rollback
public class IntegrationEnvironment {
    private static final String SCHEMA_CHANGE_LOG = "service";
    @Container
    protected static PostgreSQLContainer<?> postgresContainer;

    protected static Connection connection;

    static {
        postgresContainer = new PostgreSQLContainer<>("postgres:16")
                .withDatabaseName("db")
                .withUsername("caxap")
                .withPassword("1234");
        postgresContainer.start();

        runMigration();
    }

    @BeforeAll
    static void setUp(){
        postgresContainer = new PostgreSQLContainer<>("postgres:16")
                .withDatabaseName("db")
                .withUsername("caxap")
                .withPassword("1234");
        postgresContainer.start();

        runMigration();
    }

    private static void runMigration() {
        String url = IntegrationEnvironment.postgresContainer.getJdbcUrl();
        String password = IntegrationEnvironment.postgresContainer.getPassword();
        String username = IntegrationEnvironment.postgresContainer.getUsername();

        try {
            connection = DriverManager.getConnection(url, username, password);

            // Создание схемы "service"
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE SCHEMA IF NOT EXISTS service;");
            }

            Path path = new File(".")
                    .toPath()
                    .toAbsolutePath()
                    .getParent()
                    .resolve("migration");

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            database.setDefaultSchemaName(SCHEMA_CHANGE_LOG);
            Liquibase liquibase = new Liquibase(
                    "migration/master.xml",
                    new DirectoryResourceAccessor(path.getParent().toFile()),
                    database
            );
            liquibase.update(new Contexts(), new LabelExpression());

        } catch (SQLException | FileNotFoundException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
