package com.eskiiimo.web.projects.service;

import com.eskiiimo.repository.projects.dto.ProjectListDto;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectListService {

    private final ProjectRepository projectRepository;

    /*
    1. 직군별, 분야별 둘다 선택되어있을 경우
    2. 직군별, 분야별 둘 중 하나만 선택되어있을 경우
    3.       ...     둘다 선택되어있지 않을 경우
    */
    @Transactional(readOnly = true)
    public Page<ProjectListDto> getAllByField(String occupation, ProjectField field, Pageable pageable) {
        Page<ProjectListDto> page = this.projectRepository.findAllProjectedBy(pageable);
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

    @Transactional(readOnly = true)
    public Page<ProjectListDto> findAllByDdayLessThanOrderByDdayAsc(Pageable pageable) {
        Page<ProjectListDto> page = this.projectRepository.findAllByDdayLessThanOrderByDdayAsc(30, pageable);
        return page;
    }

}
