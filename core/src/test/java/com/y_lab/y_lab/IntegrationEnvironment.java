package com.y_lab.y_lab;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import lombok.Getter;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
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
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
public class IntegrationEnvironment {
    private static final String SCHEMA_CHANGE_LOG = "service";
    @Getter
    @Container
    protected static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("db")
            .withUsername("caxap")
            .withPassword("1234");

    protected static Connection connection;

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        runMigration();
    }

    private static void runMigration() {
        postgresContainer.start();
        String url = postgresContainer.getJdbcUrl();
        String password = postgresContainer.getPassword();
        String username = postgresContainer.getUsername();

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
