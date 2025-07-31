package vn.bin.jobhunter.domain.response.resume;

import java.time.Instant;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.bin.jobhunter.util.constant.ResumeStateEnum;

@Getter
@Setter
public class ResFetchResumeDTO {
    private Long id;
    private String email;

    private String url;
    @Enumerated(EnumType.STRING)
    private ResumeStateEnum status;

    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;

    private String updatedBy;
    private String companyName;
    private UserResume user;
    private JobResume job;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserResume {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class JobResume {
        private long id;
        private String name;
    }
}
