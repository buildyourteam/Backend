package com.eskiiimo.web.user.service;

import com.eskiiimo.repository.user.model.People;
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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PeopleService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    public Page<People> getPeople(Long grade, ProjectRole role, String area, Pageable pageable){
        Page<User> page = userRepository.findAll(pageable);
        Page<People> dtopage;

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
                    dtopage = page.map(this::convertToPeopleList);
                    return dtopage;
                }
            }
        }
        dtopage = page.map(this::convertToPeopleList);
        return dtopage;
    }
    public People convertToPeopleList(User profile) {
        List<TechnicalStack> stackList = new ArrayList<TechnicalStack>();
        for(UsersStack stack : profile.getStacks()){
            TechnicalStack technicalStack = stack.getStack();
            stackList.add(technicalStack);
        }
        People dto = new People();
        // Conversion logic
        dto = People.builder()
                .userId(profile.getUserId())
                .userName(profile.getUserName())
                .stacks(stackList)
                .area(profile.getArea())
                .grade(profile.getGrade())
                .build();
        return dto;
    }

}
