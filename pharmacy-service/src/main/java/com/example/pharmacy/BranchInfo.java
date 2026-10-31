package com.example.pharmacy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record BranchInfo(String branchName, String hotline) {
    public BranchInfo(@Value("${app.branch-name}") String branchName,
                      @Value("${app.hotline}") String hotline) {
        this.branchName = branchName;
        this.hotline = hotline;
    }
}
