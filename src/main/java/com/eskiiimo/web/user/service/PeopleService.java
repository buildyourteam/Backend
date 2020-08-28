package com.eskiiimo.web.user.service;

import com.eskiiimo.repository.user.dto.PeopleDto;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.user.enumtype.UserActivate;
import com.eskiiimo.web.user.enumtype.UserState;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 팀을 구하는 사람들 서비스
 *
 * @author always0ne
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class PeopleService {

    private final UserRepository userRepository;

    /**
     * 프로필 조회하기
     *
     * @param grade    점수
     * @param role     역할
     * @param area     활동지역
     * @param pageable 페이지 정보
     * @return 페이징된 {@link PeopleDto} 리스트
     * <p>
     * 필터에 맞게 쿼리를 다르게 하다보니 if문이 복잡하게 되어있음. 수정이 필요함.
     */
    @Transactional(readOnly = true)
    public Page<PeopleDto> getPeople(Long grade, ProjectRole role, String area, Pageable pageable) {
        Page<User> page;

        if (grade != null) {
            if (role != null) {
                if (area != null) {//세개 다
                    page = userRepository.findAllByAreaAndRoleAndGradeAndActivateAndState(area, role, grade, UserActivate.REGULAR, UserState.FREE, pageable);
                } else {//grade,role
                    page = userRepository.findAllByRoleAndGradeAndActivateAndState(role, grade, UserActivate.REGULAR, UserState.FREE, pageable);
                }
            } else {
                if (area != null) {//grade, area
                    page = userRepository.findAllByGradeAndAreaAndActivateAndState(grade, area, UserActivate.REGULAR, UserState.FREE, pageable);
                } else {//grade
                    page = userRepository.findAllByGradeAndActivateAndState(grade, UserActivate.REGULAR, UserState.FREE, pageable);
                }
            }
        } else {
            if (role != null) {
                if (area != null) {//role, area
                    page = userRepository.findAllByAreaAndRoleAndActivateAndState(area, role, UserActivate.REGULAR, UserState.FREE, pageable);
                } else {//role
                    page = userRepository.findAllByRoleAndActivateAndState(role, UserActivate.REGULAR, UserState.FREE, pageable);
                }
            } else {
                if (area != null) {//area
                    page = userRepository.findAllByAreaAndActivateAndState(area, UserActivate.REGULAR, UserState.FREE, pageable);
                } else {//null
                    page = userRepository.findAllByActivateAndState(UserActivate.REGULAR, UserState.FREE, pageable);
                }
            }
        }
        return page.map(PeopleDto::new);
    }
}
