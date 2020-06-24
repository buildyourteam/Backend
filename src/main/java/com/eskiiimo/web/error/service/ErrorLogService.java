package com.eskiiimo.web.error.service;

import com.eskiiimo.repository.error.model.ErrorLog;
import com.eskiiimo.repository.error.repository.ErrorLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ErrorLogService {

    @Autowired
    private ErrorLogRepository errorLogRepository;

    @Transactional
    public void save(ErrorLog errorLog) {
        errorLogRepository.save(errorLog);
    }
}
