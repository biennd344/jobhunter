package vn.bin.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.bin.jobhunter.domain.Job;
import vn.bin.jobhunter.domain.response.ResultPaginationDTO;
import vn.bin.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.bin.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.bin.jobhunter.service.JobService;
import vn.bin.jobhunter.util.annotation.ApiMessage;
import vn.bin.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

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
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("create a job")
    public ResponseEntity<ResCreateJobDTO> create(@Valid @RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.create(job));
    }

    @PutMapping("/jobs")
    @ApiMessage("update a job")
    public ResponseEntity<ResUpdateJobDTO> update(@Valid @RequestBody Job job) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchJobById(job.getId());
        if (!currentJob.isPresent()) {
            throw new IdInvalidException("job not found");

        }
        return ResponseEntity.ok().body(this.jobService.update(job, currentJob.get()));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("delete a job")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if (!currentJob.isPresent()) {
            throw new IdInvalidException("job not found");
        }
        this.jobService.deleteJob(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("get a job by id")
    public ResponseEntity<Job> getJob(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if (!currentJob.isPresent()) {
            throw new IdInvalidException("job not found");
        }

        return ResponseEntity.ok().body(currentJob.get());
    }

    @GetMapping("/jobs")
    @ApiMessage("get all jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Job> spec, Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.fetchAll(spec, pageable));
    }

}
