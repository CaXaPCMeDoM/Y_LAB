package com.y_lab.y_lab;

import com.y_lab.y_lab.repository.car.JdbcCarRepository;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class IntegrationEnvironment {

    @Container
    protected static final PostgreSQLContainer<?> postgresContainer;

    protected static Connection connection;

    static {
        postgresContainer = new PostgreSQLContainer<>("postgres:16")
                .withDatabaseName("db")
                .withUsername("caxap")
                .withPassword("1234");
        postgresContainer.start();

        runMigration(postgresContainer);
    }

    private static void runMigration(PostgreSQLContainer<?> postgres) {
        String url = postgres.getJdbcUrl();
        String password = postgres.getPassword();
        String username = postgres.getUsername();

        try {
            connection = DriverManager.getConnection(
                    url,
                    username,
                    password
            );

            Path path = new File(".")
                    .toPath()
                    .toAbsolutePath()
                    .getParent()
                    .resolve("migration");

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
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
