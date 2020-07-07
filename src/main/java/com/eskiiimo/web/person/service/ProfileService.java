package com.eskiiimo.web.person.service;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectPerson;
import com.eskiiimo.repository.projects.repository.ProjectPersonRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.repository.person.dto.ProfileDto;
import com.eskiiimo.web.error.exception.ProjectMemberNotFoundException;
import com.eskiiimo.web.error.exception.ProjectNotFoundException;
import com.eskiiimo.web.error.exception.UserNotFoundException;
import com.eskiiimo.web.projects.enumtype.RecruitStatus;
import com.eskiiimo.repository.person.model.Person;
import com.eskiiimo.repository.person.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectPersonRepository projectPersonRepository;

    @Autowired
    ModelMapper modelMapper;

    @Transactional
    public ProfileDto getProfile(String person_id) {
        Person profile =  personRepository.findByPersonId(person_id)
                .orElseThrow(()-> new UserNotFoundException("존재하지 않는 사용자입니다."));
        ProfileDto profileDto = profile.toProfileDto();
        return profileDto;
    }

    @Transactional
    public ProfileDto updateProfile(String person_id, ProfileDto updateData) {
        Person profile =  personRepository.findByPersonId(person_id)
                .orElseThrow(()-> new UserNotFoundException("존재하지 않는 사용자입니다."));
        updateData.updateProfile(profile);
        return  this.personRepository.save(profile).toProfileDto();
    }

    public Page<Project> getRunning(String person_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByProjectPersons_Person_PersonIdAndProjectPersons_HideAndRecruitStatus(person_id,Boolean.FALSE, RecruitStatus.RUNNING, pageable);
        return page;

    }

    public Page<Project> getEnded(String person_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByProjectPersons_Person_PersonIdAndProjectPersons_HideAndRecruitStatus(person_id, Boolean.FALSE, RecruitStatus.ENDED, pageable);
        return page;
    }

    public Page<Project> getPlanner(String person_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByLeaderIdAndProjectPersons_Hide(person_id, Boolean.FALSE,pageable);
        return page;
    }
    public Page<Project> getHiddenRunning(String person_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByProjectPersons_Person_PersonIdAndProjectPersons_HideAndRecruitStatus(person_id,Boolean.TRUE, RecruitStatus.RUNNING, pageable);
        return page;
    }

    public Page<Project> getHiddenEnded(String person_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByProjectPersons_Person_PersonIdAndProjectPersons_HideAndRecruitStatus(person_id, Boolean.TRUE, RecruitStatus.ENDED, pageable);
        return page;
    }

    public Page<Project> getHiddenPlanner(String person_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByLeaderIdAndProjectPersons_Hide(person_id, Boolean.TRUE,pageable);
        return page;
    }
    public void reShowProject(String person_id, Long projectId) {
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        ProjectPerson projectPerson = this.projectPersonRepository.findByProject_ProjectIdAndPerson_PersonId(projectId,person_id)
                .orElseThrow(()->new ProjectMemberNotFoundException("프로젝트에 소속되어있지 않습니다."));
        project.getProjectPersons().remove(projectPerson);
        projectPerson.setHide(Boolean.FALSE);
        project.getProjectPersons().add(projectPerson);
        this.projectPersonRepository.save(projectPerson);
        this.projectRepository.save(project);
    }

    public void hideProject(String person_id, Long projectId) {
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        ProjectPerson projectPerson = this.projectPersonRepository.findByProject_ProjectIdAndPerson_PersonId(projectId,person_id)
                .orElseThrow(()->new ProjectMemberNotFoundException("프로젝트에 소속되어있지 않습니다."));
        project.getProjectPersons().remove(projectPerson);
        projectPerson.setHide(Boolean.TRUE);
        project.getProjectPersons().add(projectPerson);
        this.projectPersonRepository.save(projectPerson);
        this.projectRepository.save(project);
    }
}
