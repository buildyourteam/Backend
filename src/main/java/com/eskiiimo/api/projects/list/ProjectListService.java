package com.eskiiimo.api.projects.list;

import com.eskiiimo.api.error.exception.ProjectNotFoundException;
import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.ProjectField;
import com.eskiiimo.api.projects.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectListService {

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    /*
    1. 직군별, 분야별 둘다 선택되어있을 경우
    2. 직군별, 분야별 둘 중 하나만 선택되어있을 경우
    3.       ...     둘다 선택되어있지 않을 경우
    */
    public Page<Project> getAllByField(String occupation, ProjectField field, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAll(pageable);
        if (page.isEmpty())
            throw new ProjectNotFoundException("프로젝트가 존재하지 않습니다.");
        if (occupation != null) {
            if (occupation.equals("developer")) {

                if (field != null) {
                    page = this.projectRepository.findAllByNeedMemberDeveloperGreaterThanAndProjectField(0, field, pageable);
                } else {
                    page = this.projectRepository.findAllByNeedMemberDeveloperGreaterThan(0, pageable);
                }

            } else if (occupation.equals("designer")) {

                if (field != null) {
                    page = this.projectRepository.findAllByNeedMemberDesignerGreaterThanAndProjectField(0, field, pageable);
                } else {
                    page = this.projectRepository.findAllByNeedMemberDesignerGreaterThan(0, pageable);
                }

            } else if (occupation.equals("planner")) {

                if (field != null) {
                    page = this.projectRepository.findAllByNeedMemberPlannerGreaterThanAndProjectField(0, field, pageable);
                } else {
                    page = this.projectRepository.findAllByNeedMemberPlannerGreaterThan(0, pageable);
                }

            } else if (occupation.equals("etc")) {

                if (field != null) {
                    page = this.projectRepository.findAllByNeedMemberEtcGreaterThanAndProjectField(0, field, pageable);
                } else {
                    page = this.projectRepository.findAllByNeedMemberEtcGreaterThan(0, pageable);
                }

            }
        } else if (field != null) {
            page = this.projectRepository.findAllByProjectField(field, pageable);
        }

        return page;
    }

    public Page<Project> findAllByDdayLessThanOrderByDdayAsc(Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByDdayLessThanOrderByDdayAsc(30, pageable);
        if (page.isEmpty())
            throw new ProjectNotFoundException("프로젝트가 존재하지 않습니다.");
        return page;
    }


    public Project findById(Long project_id) {
        Optional<Project> byId = this.projectRepository.findById(project_id);
        Project existingProjct = byId.get();
        return existingProjct;
    }

    public Project save(Project existingProject) {
        Project project = this.projectRepository.save(existingProject);
        return project;
    }



}
