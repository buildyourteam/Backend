package com.eskiiimo.web.projects.service;

import com.eskiiimo.repository.projects.dto.ProjectApplicantDto;
import com.eskiiimo.repository.projects.dto.ProjectApplyDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectPerson;
import com.eskiiimo.repository.projects.repository.ProjectPersonRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.web.error.exception.*;
import com.eskiiimo.repository.projects.model.ProjectApply;
import com.eskiiimo.repository.projects.model.ProjectApplyAnswer;
import com.eskiiimo.repository.projects.repository.ProjectApplyRepository;
import com.eskiiimo.web.projects.enumtype.ProjectApplyStatus;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.repository.person.model.Person;
import com.eskiiimo.repository.person.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectApplyService {

    private final PersonRepository personRepository;
    private final ProjectPersonRepository projectPersonRepository;
    private final ProjectRepository projectRepository;
    private final ProjectApplyRepository projectApplyRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void addLeader(Project project, String personId){
        Person person =  personRepository.findByPersonId(personId)
                .orElseThrow(()-> new UserNotFoundException("존재하지 않는 사용자입니다."));
        ProjectPerson projectPerson = ProjectPerson.builder()
                .projectRole(ProjectRole.LEADER)
                .person(person)
                .project(project)
                .hide(Boolean.FALSE)
                .build();
        project.getProjectPersons().add(projectPerson);
        this.projectPersonRepository.save(projectPerson);
        this.projectRepository.save(project);
    }
    @Transactional
    public boolean applyProject(Long projectId, ProjectApplyDto apply, String visitorId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        Person person =  personRepository.findByPersonId(visitorId)
                .orElseThrow(()-> new UserNotFoundException("존재하지 않는 사용자입니다."));
        ProjectApply projectApplyEntity = apply.toEntity(person);
        project.getApplies().add(projectApplyEntity);
        this.projectApplyRepository.save(projectApplyEntity);
        this.projectRepository.save(project);
        return Boolean.TRUE;
    }
    @Transactional
    public boolean updateApply(Long projectId, ProjectApplyDto apply, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        ProjectApply projectApply = projectApplyRepository.findByPerson_PersonId(visitorId)
                .orElseThrow(()->new ApplyNotFoundException("지원정보가 존재하지 않습니다."));
        project.getApplies().remove(projectApply);
        projectApply.setSelfDescription(apply.getSelfDescription());
        projectApply.setProjectRole(apply.getProjectRole());
        List<ProjectApplyAnswer> answers = new ArrayList<ProjectApplyAnswer>();
        for(String answer : apply.getAnswers())
            answers.add(ProjectApplyAnswer.builder().answer(answer).build());
        projectApply.setAnswers(answers);
        project.getApplies().add(projectApply);
        this.projectApplyRepository.save(projectApply);
        this.projectRepository.save(project);
        return Boolean.TRUE;
    }
    @Transactional
    public List<ProjectApplicantDto> getApplicants(Long projectId, String visitorId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        if(!this.isLeader(project,visitorId))
            throw new YouAreNotReaderException("당신은 팀장이 아닙니다.");
            List<ProjectApplicantDto> applicants = new ArrayList<ProjectApplicantDto>();

            for(ProjectApply projectApply : project.getApplies()){
                ProjectApplicantDto projectApplicantDto =ProjectApplicantDto.builder()
                        .projectApplyStatus(projectApply.getProjectApplyStatus())
                        .personId(projectApply.getPerson().getPersonId())
                        .personName(projectApply.getPerson().getPersonName())
                        .projectRole(projectApply.getProjectRole())
                        .build();
                applicants.add(projectApplicantDto);
            }
        if(applicants.isEmpty())
            throw new ApplicantNotFoundException("지원자가 없습니다.");
        return applicants;
    }
    @Transactional
    public ProjectApplyDto getApply(Long projectId, String personId, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        if(this.isLeader(project,visitorId)) {
            for(ProjectApply projectApply : project.getApplies()){
                if(projectApply.getPerson().getPersonId().equals(personId)){
                    if(projectApply.getProjectApplyStatus()== ProjectApplyStatus.UNREAD)
                        projectApply.setProjectApplyStatus(ProjectApplyStatus.READ);
                    projectApplyRepository.save(projectApply);
                    ProjectApplyDto projectApplyDto = ProjectApplyDto.builder()
                            .personName(projectApply.getPerson().getPersonName())
                            .questions(project.getQuestions())
                            .answers(projectApply.getAnswers())
                            .selfDescription(projectApply.getSelfDescription())
                            .projectApplyStatus(projectApply.getProjectApplyStatus())
                            .projectRole(projectApply.getProjectRole())
                            .build();
                    this.projectRepository.save(project);
                    return projectApplyDto;
                }
            }
        }
        return null;
    }
    @Transactional
    public Boolean acceptApply(Long projectId, String personId, String visitorId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        if(isLeader(project,visitorId)){
            ProjectRole personRole=null;
            for(ProjectApply projectApply : project.getApplies()){
                if(projectApply.getPerson().getPersonId().equals(personId)){
                    project.getApplies().remove(projectApply);
                    personRole= projectApply.getProjectRole();
                    projectApply.setProjectApplyStatus(ProjectApplyStatus.ACCEPT);
                    project.getApplies().add(projectApply);
                    this.projectApplyRepository.save(projectApply);
                    break;
                }
            }
            Person person =  personRepository.findByPersonId(personId)
                    .orElseThrow(()-> new UserNotFoundException("존재하지 않는 사용자입니다."));
            ProjectPerson projectPerson = ProjectPerson.builder()
                    .projectRole(personRole)
                    .person(person)
                    .project(project)
                    .hide(Boolean.FALSE)
                    .build();
            project.getProjectPersons().add(projectPerson);
            if(projectPerson.getProjectRole()==ProjectRole.DEVELOPER)
                project.getCurrentPerson().setDeveloper(project.getCurrentPerson().getDeveloper()+1);
            else if(projectPerson.getProjectRole()==ProjectRole.DESIGNER)
                project.getCurrentPerson().setDesigner(project.getCurrentPerson().getDesigner()+1);
            else if(projectPerson.getProjectRole()==ProjectRole.PLANNER)
                project.getCurrentPerson().setPlanner(project.getCurrentPerson().getPlanner()+1);
            else if(projectPerson.getProjectRole()==ProjectRole.ETC)
                project.getCurrentPerson().setEtc(project.getCurrentPerson().getEtc()+1);
            this.projectPersonRepository.save(projectPerson);
            this.projectRepository.save(project);
            return Boolean.TRUE;
        }
        else
            return Boolean.FALSE;
    }
    @Transactional
    public Boolean rejectApply(Long projectId, String personId, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        if(isLeader(project,visitorId)) {
            for (ProjectApply projectApply : project.getApplies()) {
                if (projectApply.getPerson().getPersonId().equals(personId)) {
                    projectApply.setProjectApplyStatus(ProjectApplyStatus.REJECT);
                    this.projectApplyRepository.save(projectApply);
                    this.projectRepository.save(project);
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
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
