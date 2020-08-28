package com.eskiiimo.web.user.request;

import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileRequest {
    /**
     * 이름
     */
    private String userName;
    /**
     * 역할 ex) LEADER, DEVELOPER... {@link ProjectRole}
     */
    private ProjectRole role;
    /**
     * 기술스택 ex) SPRINGBOOT,GO,DJANGO... {@link TechnicalStack}
     */
    private List<TechnicalStack> stacks;
    /**
     * 연락처
     */
    private String contact;
    /**
     * 활동 지역
     */
    private String area;
    /**
     * 자기소개
     */
    private String introduction;

    /**
     * entity에 사용하기 위해 Dto로 변경
     * @return {@link ProfileDto}
     */
    public ProfileDto toProfileDto() {
        return new ProfileDto(
                this.userName,
                this.role,
                this.contact,
                this.area,
                this.introduction,
                this.stacks
        );
    }
}