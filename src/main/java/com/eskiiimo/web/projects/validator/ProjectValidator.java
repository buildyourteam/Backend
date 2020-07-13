package com.eskiiimo.web.projects.validator;

import com.eskiiimo.repository.projects.dto.ProjectDetailDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class ProjectValidator {
    public void validate(ProjectDetailDto projectDetailDto, Errors errors) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = projectDetailDto.getEndDate();
        if(endDate.isBefore(now)) {
            errors.rejectValue("endDate", "wrongValue", "endDate is wrong");
        }
    }
}