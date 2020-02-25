package com.eskiiimo.api.projects.projectapply;

import org.springframework.hateoas.MediaTypes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/projects/{project_id}/apply", produces = MediaTypes.HAL_JSON_VALUE)
public class ProjectApplyController {
}
