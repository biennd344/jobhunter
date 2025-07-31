package vn.bin.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;

import jakarta.validation.Valid;
import vn.bin.jobhunter.domain.Company;
import vn.bin.jobhunter.domain.Job;
import vn.bin.jobhunter.domain.Resume;
import vn.bin.jobhunter.domain.User;
import vn.bin.jobhunter.domain.response.ResultPaginationDTO;
import vn.bin.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.bin.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.bin.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.bin.jobhunter.service.ResumeService;
import vn.bin.jobhunter.service.UserService;
import vn.bin.jobhunter.util.SecurityUtil;
import vn.bin.jobhunter.util.annotation.ApiMessage;
import vn.bin.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;
    private final UserService userService;
    private final FilterBuilder filterBuilder;
    private final FilterSpecificationConverter filterSpecificationConverter;

    public ResumeController(ResumeService resumeService, UserService userService, FilterBuilder filterBuilder,
            FilterSpecificationConverter filterSpecificationConverter) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.filterBuilder = filterBuilder;
        this.filterSpecificationConverter = filterSpecificationConverter;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a resume")
    public ResponseEntity<ResCreateResumeDTO> create(@Valid @RequestBody Resume resume) throws IdInvalidException {
        // check id exists
        boolean isIdExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if (!isIdExist) {
            throw new IdInvalidException("User id / Job id khong ton tai");
        }

        // create new resume

        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.create(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResUpdateResumeDTO> update(@RequestBody Resume resume) throws IdInvalidException {
        // check id exists
        Optional<Resume> reqResumeOptional = this.resumeService.fetchByid(resume.getId());
        if (reqResumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume voi id = " + resume.getId() + "khong ton tai");
        }
        Resume reqResume = reqResumeOptional.get();
        reqResume.setStatus(resume.getStatus());

        // create new resume

        return ResponseEntity.ok().body(this.resumeService.update(reqResume));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a resume")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        // check id exists
        Optional<Resume> reqResumeOptional = this.resumeService.fetchByid(id);
        if (reqResumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume voi id = " + id + "khong ton tai");
        }
        this.resumeService.delete(id);

        // create new resume

        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch a resume by id")
    public ResponseEntity<ResFetchResumeDTO> fetchById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Resume> reqResumeOptional = this.resumeService.fetchByid(id);
        if (reqResumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume voi id = " + id + "khong ton tai");
        }

        return ResponseEntity.ok().body(this.resumeService.getResume(reqResumeOptional.get()));
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resume with paginate")
    public ResponseEntity<ResultPaginationDTO> fetchAll(@Filter Specification<Resume> spec, Pageable pageable)
            throws IdInvalidException {
        List<Long> arrJobIds = null;
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = this.userService.handleGetUserByUsername(email);
        if (currentUser != null) {
            Company userCompany = currentUser.getCompany();
            if (userCompany != null) {
                List<Job> companyJobs = userCompany.getJobs();
                if (companyJobs != null && companyJobs.size() > 0) {
                    arrJobIds = companyJobs.stream().map(x -> x.getId()).collect(Collectors.toList());

                }

            }
        }
        Specification<Resume> finalSpec = spec;
        if (arrJobIds != null && !arrJobIds.isEmpty()) {
            String jobFilterStr = "job.in:" + arrJobIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            Specification<Resume> jobInspec = filterSpecificationConverter.convert(jobFilterStr);
            finalSpec = jobInspec.and(spec);
        }
        // Specification<Resume> jobInspec =
        // filterSpecificationConverter.convert(filterBuilder.field("job"))
        // .in(filterBuilder.input(arrJobIds).get());
        // Specification<Resume> finalSpec = jobInspec.and(spec);
        return ResponseEntity.ok().body(this.resumeService.fetchAllresume(finalSpec, pageable));
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("Fetch list resume by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable)
            throws IdInvalidException {

        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
    }

}
