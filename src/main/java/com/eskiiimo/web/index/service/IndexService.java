package com.eskiiimo.web.index.service;

import com.eskiiimo.repository.person.repository.PersonRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.web.person.enumtype.PersonStatus;
import com.eskiiimo.web.projects.enumtype.ProjectApplyStatus;
import com.eskiiimo.web.projects.enumtype.RecruitStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexService {
    private final ProjectRepository projectRepository;
    private final PersonRepository personRepository;


    public long getRandomProjectPage(long pageSize) {
        long pages = projectRepository.countAllByRecruitStatus(RecruitStatus.RECRUTING)/pageSize+1;
        long random= (long)Math.random()%pages;
        return random;
    }

    public long getRandomPeoplePage(long peopleSize) {
        long pages = personRepository.countAllByPersonStatus(PersonStatus.FREE)/peopleSize+1;
        long random= (long)Math.random()%pages;
        return random;
    }
}
