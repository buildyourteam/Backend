package com.eskiiimo.web.index.service;

import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.web.projects.enumtype.State;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.user.enumtype.UserState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    public long getRandomProjectPage(long pageSize) {
        long pages = projectRepository.countAllByState(State.RECRUTING)/pageSize+1;
        long random= (long)Math.random()%pages;
        return random;
    }

    public long getRandomPeoplePage(long peopleSize) {
        long pages = userRepository.countAllByState(UserState.FREE)/peopleSize+1;
        long random= (long)Math.random()%pages;
        return random;
    }
}
