package vn.bin.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.bin.jobhunter.domain.Job;
import vn.bin.jobhunter.domain.Subscriber;
import vn.bin.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.bin.jobhunter.service.SubscriberService;
import vn.bin.jobhunter.util.annotation.ApiMessage;
import vn.bin.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("create a subscriber")
    public ResponseEntity<Subscriber> create(@Valid @RequestBody Subscriber subscriber) throws IdInvalidException {

        boolean isExist = this.subscriberService.isExistsByEmail(subscriber.getEmail());
        if (isExist = true) {
            throw new IdInvalidException("Email " + subscriber.getEmail() + "da ton tai");

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.create(subscriber));
    }

    @PutMapping("/subscribers")
    @ApiMessage("update a subscriber")
    public ResponseEntity<Subscriber> update(@RequestBody Subscriber subsRequest) throws IdInvalidException {

        Subscriber subsDb = this.subscriberService.findByID(subsRequest.getId());
        if (subsDb == null) {
            throw new IdInvalidException("Id " + subsRequest.getId() + "k ton tai");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.update(subsDb, subsRequest));
    }

}
