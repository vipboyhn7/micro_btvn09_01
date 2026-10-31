package com.example.pharmacy;

import java.sql.Connection;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupReporter implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(StartupReporter.class);
    private final BranchInfo branchInfo;
    private final DataSource dataSource;

    public StartupReporter(BranchInfo branchInfo, DataSource dataSource) {
        this.branchInfo = branchInfo;
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        // Open a real connection so startup fails when database configuration is invalid.
        try (Connection connection = dataSource.getConnection()) {
            if (!connection.isValid(5)) {
                throw new IllegalStateException("Database connection is not valid");
            }
            log.info("Database connected: {}", connection.getMetaData().getDatabaseProductName());
        }
        log.info("Chi nhanh: {} | Hotline: {}", branchInfo.branchName(), branchInfo.hotline());
    }
}
