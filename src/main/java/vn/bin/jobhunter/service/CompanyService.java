package vn.bin.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.bin.jobhunter.domain.Company;
import vn.bin.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {

        return this.companyRepository.save(company);
    }

}
