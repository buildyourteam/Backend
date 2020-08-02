package com.eskiiimo.web.projects.validator;

import com.eskiiimo.repository.projects.dto.ProjectDetailDto;
import com.eskiiimo.web.projects.exception.WrongDateException;
import com.eskiiimo.web.projects.request.ProjectDetailRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class ProjectValidator {
    public void validateDate(ProjectDetailRequest projectDetailRequest) {
        if(projectDetailRequest.getEndDate().isBefore(LocalDateTime.now()))
            throw new WrongDateException(projectDetailRequest.getEndDate().toString());
    }
}
