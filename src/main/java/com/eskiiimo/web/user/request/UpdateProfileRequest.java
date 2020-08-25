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
    private String userName;
    private ProjectRole role;
    private List<TechnicalStack> stacks;
    private String contact;
    private String area;
    private String introduction;

    public ProfileDto toProfileDto(){
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