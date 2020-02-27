package com.eskiiimo.api.logback;

import com.eskiiimo.api.logback.util.MDCUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlType;

@ControllerAdvice
public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public String handleException(Throwable throwable) {
        logging(throwable);
        System.out.println("======="+throwable);
        return "error/error";
    }

    protected void logging(Throwable throwable) {
        if (logger.isErrorEnabled()) {
            if (throwable.getMessage() != null) {
                logger.error(throwable.getMessage(), throwable);
            } else {
                logger.error("ERROR", throwable);
            }
        }
    }
}
