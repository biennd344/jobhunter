package vn.bin.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.bin.jobhunter.service.EmailService;
import vn.bin.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;

    EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email")
    @ApiMessage("Send simple email")
    public String sendSimpleEmail() {
        // this.emailService.sendSimpleEmail();
        // this.emailService.sendEmailSync("biennguyen0344@gmail.com", "test send
        // email", "<h1><b> hello</b></h1>", false,
        // true);
        this.emailService.sendEmailFromTemplateSync("biennguyen0344@gmail.com", "test send email", "job");
        return "ok";
    }

}
