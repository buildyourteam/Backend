package com.eskiiimo.api.index;

import com.eskiiimo.api.projects.ProjectRepository;
import com.eskiiimo.api.projects.Status;
import com.eskiiimo.api.user.UserRepository;
import com.eskiiimo.api.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    public long getRandomProjectPage(long pageSize) {
        long pages = projectRepository.countAllByStatus(Status.RECRUTING)/pageSize+1;
        long random= (long)Math.random()%pages;
        return random;
    }

    public long getRandomPeoplePage(long peopleSize) {
        long pages = userRepository.countAllByStatus(UserStatus.FREE)/peopleSize+1;
        long random= (long)Math.random()%pages;
        return random;
    }
}
