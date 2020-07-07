package com.eskiiimo.web.person.service;

import com.eskiiimo.repository.person.model.People;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import com.eskiiimo.repository.person.model.Person;
import com.eskiiimo.repository.person.repository.PersonRepository;
import com.eskiiimo.repository.person.model.PersonStack;
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
    PersonRepository personRepository;

    @Transactional
    public Page<People> getPeople(Long level, ProjectRole role, String area, Pageable pageable){
        Page<Person> page = personRepository.findAll(pageable);
        Page<People> dtopage;

        if(level != null){
            if(role!=null){
                if(area!=null){//세개 다
                    page = personRepository.findAllByAreaAndProjectRoleAndPersonLevel(area, role, level, pageable);
                }
                else{//level,role
                    page = personRepository.findAllByProjectRoleAndPersonLevel(role, level, pageable);
                }
            }
            else{
                if(area!=null){//level, area
                    page= personRepository.findAllByPersonLevelAndArea(level,area,pageable);
                }
                else {//level
                    page = personRepository.findAllByPersonLevel(level, pageable);
                }
            }
        }
        else{
            if(role!=null){
                if(area!=null){//role, area
                    page = personRepository.findAllByAreaAndProjectRole(area,role,pageable);
                }
                else {//role
                    page = personRepository.findAllByProjectRole(role,pageable);
                }
            }
            else{
                if(area!=null){//area
                    page = personRepository.findAllByArea(area,pageable);
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
    public People convertToPeopleList(Person profile) {
        List<TechnicalStack> stackList = new ArrayList<TechnicalStack>();
        for(PersonStack stack : profile.getStacks()){
            TechnicalStack technicalStack = stack.getStack();
            stackList.add(technicalStack);
        }
        People dto = new People();
        // Conversion logic
        dto = People.builder()
                .userId(profile.getPersonId())
                .userName(profile.getPersonName())
                .stacks(stackList)
                .area(profile.getArea())
                .level(profile.getPersonLevel())
                .build();
        return dto;
    }

}
