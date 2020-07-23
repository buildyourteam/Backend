package com.eskiiimo.web.user.service;

import com.eskiiimo.repository.user.dto.PeopleDto;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.repository.user.model.UsersStack;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PeopleService {

    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<PeopleDto> getPeople(Long grade, ProjectRole role, String area, Pageable pageable){
        Page<User> page = userRepository.findAll(pageable);

        if(grade != null){
            if(role!=null){
                if(area!=null){//세개 다
                    page = userRepository.findAllByAreaAndRoleAndGrade(area, role, grade, pageable);
                }
                else{//grade,role
                    page = userRepository.findAllByRoleAndGrade(role, grade, pageable);
                }
            }
            else{
                if(area!=null){//grade, area
                    page= userRepository.findAllByGradeAndArea(grade,area,pageable);
                }
                else {//grade
                    page = userRepository.findAllByGrade(grade, pageable);
                }
            }
        }
        else{
            if(role!=null){
                if(area!=null){//role, area
                    page = userRepository.findAllByAreaAndRole(area,role,pageable);
                }
                else {//role
                    page = userRepository.findAllByRole(role,pageable);
                }
            }
            else{
                if(area!=null){//area
                    page = userRepository.findAllByArea(area,pageable);
                }
                else{//null
                    return page.map(this::convertToPeopleList);
                }
            }
        }
        return page.map(this::convertToPeopleList);
    }
    public PeopleDto convertToPeopleList(User profile) {
        List<TechnicalStack> stackList = new ArrayList<TechnicalStack>();
        for(UsersStack stack : profile.getStacks())
            stackList.add(stack.getStack());

        return   PeopleDto.builder()
                .userId(profile.getUserId())
                .userName(profile.getUserName())
                .stacks(stackList)
                .role(profile.getRole())
                .area(profile.getArea())
                .grade(profile.getGrade())
                .build();
    }

}
