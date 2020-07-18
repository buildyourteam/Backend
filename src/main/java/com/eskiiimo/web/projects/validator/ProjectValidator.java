package com.eskiiimo.web.projects.validator;

import com.eskiiimo.repository.projects.dto.ProjectDetailDto;
import com.eskiiimo.web.projects.exception.WrongDateException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class ProjectValidator {
    public void validateDate(ProjectDetailDto projectDetailDto) {
        if(projectDetailDto.getEndDate().isBefore(LocalDateTime.now()))
            throw new WrongDateException(projectDetailDto.getEndDate().toString());
    }
}
