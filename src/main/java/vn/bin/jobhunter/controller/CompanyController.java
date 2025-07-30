package vn.bin.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.bin.jobhunter.domain.Company;
import vn.bin.jobhunter.domain.dto.ResultPaginationDTO;
import vn.bin.jobhunter.service.CompanyService;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;

    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createNewUser(@Valid @RequestBody Company company) {

        Company companyResult = this.companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyResult);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) {

        this.companyService.handleDeleteCompany(id);
        // return ResponseEntity.ok("bin");
        return ResponseEntity.ok(null);
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> fetchCompanyById(@PathVariable("id") long id) {
        Company company = this.companyService.fetchCompanyById(id);
        return ResponseEntity.status(HttpStatus.OK).body(company);
    }

    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> fetchAllCompany(

            @Filter Specification<Company> specification,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.fetchAllCompany(specification, pageable));
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateUser(@Valid @RequestBody Company company) {

        Company companyResult = this.companyService.handleUpdateUser(company);
        return ResponseEntity.status(HttpStatus.OK).body(companyResult);
    }
}
