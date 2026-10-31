package com.example.pharmacy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BranchController {
    private final BranchInfo branchInfo;

    public BranchController(BranchInfo branchInfo) {
        this.branchInfo = branchInfo;
    }

    @GetMapping("/api/branch")
    public BranchInfo getBranch() {
        return branchInfo;
    }
}
