package com.eskiiimo.api.user.people;

import com.eskiiimo.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PeopleService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    public Page<PeopleDto> getPeople(Long level, String role, String area, Pageable pageable){
        Page<PeopleDto> page = userRepository.findPeopleAll(pageable);
        if(level != null){
            if(role!=null){
                if(area!=null){//세개 다
                    page = userRepository.findPeopleByAreaAndRoleAndLevel(area, role, level, pageable);
                }
                else{//level,role
                    page = userRepository.findPeopleByRoleAndLevel(role, level, pageable);
                }
            }
            else{
                if(area!=null){//level, area
                    page= userRepository.findPeopleByLevelAndArea(level,area,pageable);
                }
                else {//level
                    page = userRepository.findPeopleByLevel(level, pageable);
                }
            }
        }
        else{
            if(role!=null){
                if(area!=null){//role, area
                    page = userRepository.findPeopleByAreaAndRole(area,role,pageable);
                }
                else {//role
                    page = userRepository.findPeopleByRole(role,pageable);
                }
            }
            else{
                if(area!=null){//area
                    page = userRepository.findPeopleByArea(area,pageable);
                }
                else{//null
                    return page;
                }
            }
        }
        return page;
    }


}
