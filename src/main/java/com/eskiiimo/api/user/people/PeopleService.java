package com.eskiiimo.api.user.people;

import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.TechnicalStack;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
import com.eskiiimo.api.user.UsersStack;
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
    public Page<People> getPeople(Long level, ProjectRole role, String area, Pageable pageable){
        Page<User> page = userRepository.findAll(pageable);
        Page<People> dtopage;

        if(level != null){
            if(role!=null){
                if(area!=null){//세개 다
                    page = userRepository.findAllByAreaAndRoleAndLevel(area, role, level, pageable);
                }
                else{//level,role
                    page = userRepository.findAllByRoleAndLevel(role, level, pageable);
                }
            }
            else{
                if(area!=null){//level, area
                    page= userRepository.findAllByLevelAndArea(level,area,pageable);
                }
                else {//level
                    page = userRepository.findAllByLevel(level, pageable);
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
                .level(profile.getLevel())
                .build();
        return dto;
    }

}
