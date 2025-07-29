package vn.bin.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.bin.jobhunter.util.error.IdInvalidException;

@RestController
public class HelloController {

    @GetMapping("/")
    // @CrossOrigin
    public String getHelloWorld() throws IdInvalidException {
        return "Hello World (bin;;n)";
    }
}
