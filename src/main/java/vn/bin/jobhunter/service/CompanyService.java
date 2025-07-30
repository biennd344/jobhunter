package vn.bin.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.bin.jobhunter.domain.Company;
import vn.bin.jobhunter.domain.dto.Meta;
import vn.bin.jobhunter.domain.dto.ResultPaginationDTO;
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

    public void handleDeleteCompany(long id) {

        this.companyRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAllCompany(Specification<Company> specification, Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(specification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();
        mt.setPage(pageCompany.getNumber() + 1);
        mt.setPageSize(pageCompany.getSize());

        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());

        return rs;
    }

    public Company fetchCompanyById(long id) {
        Optional<Company> companyOptional = this.companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            return companyOptional.get();
        }
        return null;
    }

    public Company handleUpdateUser(Company reqCompany) {
        Optional<Company> currentOptional = this.companyRepository.findById(reqCompany.getId());
        if (currentOptional.isPresent()) {
            Company currentCompany = currentOptional.get();
            currentCompany.setName(reqCompany.getName());

            currentCompany.setAddress(reqCompany.getAddress());
            currentCompany.setDescription(reqCompany.getDescription());
            currentCompany.setLogo(reqCompany.getLogo());

            // update
            currentCompany = this.companyRepository.save(currentCompany);
        }
        return null;
    }

}
