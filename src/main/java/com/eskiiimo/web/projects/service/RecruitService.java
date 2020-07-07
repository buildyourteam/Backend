package com.eskiiimo.web.projects.service;

import com.eskiiimo.repository.projects.dto.RecruitDto;
import com.eskiiimo.repository.projects.model.Recruit;
import com.eskiiimo.repository.projects.repository.RecruitRepository;
import com.eskiiimo.web.error.exception.*;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectPerson;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.SuggestStatus;
import com.eskiiimo.repository.person.model.Person;
import com.eskiiimo.repository.person.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private final PersonRepository personRepository;
    private final ProjectRepository projectRepository;
    private final RecruitRepository recruitRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void recruitProject(String personId, RecruitDto recruit, Long projectId, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        Person person =  personRepository.findByPersonId(personId)
                .orElseThrow(()-> new UserNotFoundException("존재하지 않는 사용자입니다."));
        if(!this.isLeader(project,visitorId))
            throw new YouAreNotReaderException("당신은 팀장이 아닙니다.");
        Recruit projectRecruit = recruit.toEntity(person, project);
        this.recruitRepository.save(projectRecruit);
    }

    @Transactional
    public List<RecruitDto> getRecruitList(String personId, String visitorId) {
        this.personRepository.findByPersonId(personId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        if(!personId.equals(visitorId)){
            throw new RecruitNotAuthException("영입제안 권한이 없습니다.");
        }
        List<RecruitDto> projectRecruits = new ArrayList<RecruitDto>();
        List<Recruit> RecruitList=this.recruitRepository.findAllByPerson_PersonId(visitorId);
        for(Recruit recruit : RecruitList){
            RecruitDto dto = this.modelMapper.map(recruit, RecruitDto.class);
            projectRecruits.add(dto);
        }
        return projectRecruits;
    }

    @Transactional
    public RecruitDto getRecruit(String personId, Long projectId, String visitorId) {
        PersonAndProjectNotFoundCheck(personId, projectId, visitorId);
        Recruit recruit = this.recruitRepository.findProjectRecruitByPerson_PersonIdAndProject_ProjectId(personId, projectId).orElseThrow(()->new RecruitNotFoundException("해당 영입제안이 없습니다."));
        if(recruit.getSuggestStatus().equals(SuggestStatus.UNREAD)){
            recruit.setSuggestStatus(SuggestStatus.READ);
            this.recruitRepository.save(recruit);
        }
        return this.modelMapper.map(recruit, RecruitDto.class);
    }

    @Transactional
    public void acceptRecruit(String personId, Long projectId,String visitorId) {
        PersonAndProjectNotFoundCheck(personId, projectId, visitorId);
        Recruit recruit = this.recruitRepository.findProjectRecruitByPerson_PersonIdAndProject_ProjectId(personId, projectId).orElseThrow(()->new RecruitNotFoundException("해당 영입제안이 없습니다."));
        if(!recruit.getSuggestStatus().equals(SuggestStatus.ACCEPT)){
            recruit.setSuggestStatus(SuggestStatus.ACCEPT);
            this.recruitRepository.save(recruit);
        }
    }

    @Transactional
    public void rejectRecruit(String personId, Long projectId, String visitorId) {
        PersonAndProjectNotFoundCheck(personId, projectId, visitorId);
        Recruit recruit = this.recruitRepository.findProjectRecruitByPerson_PersonIdAndProject_ProjectId(personId, projectId).orElseThrow(()->new RecruitNotFoundException("해당 영입제안이 없습니다."));
        recruit.setSuggestStatus(SuggestStatus.REJECT);
        this.recruitRepository.save(recruit);
    }

    private void PersonAndProjectNotFoundCheck(String personId, Long projectId, String visitorId) {
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        Person person = this.personRepository.findByPersonId(personId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        if(!personId.equals(visitorId)){
            throw new RecruitNotAuthException("확인 권한이 없습니다.");
        }
    }

    public Boolean isLeader(Project project,String visitorId){
        for(ProjectPerson projectPerson : project.getProjectPersons()){
            if(projectPerson.getProjectRole().equals(ProjectRole.LEADER)){
                if(projectPerson.getPerson().getPersonId().equals(visitorId)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }


}
