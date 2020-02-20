package com.eskiiimo.api.user.profile;

import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.TechnicalStack;
import com.eskiiimo.api.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProfileDto {
    private String userName;
    private String role;
    private String stack;
    private String contact;
    private String area;
    private Long level;
    private String description;

    public void updateEntity(User user){
        user.setUserName(this.getUserName());
        user.setRole(ProjectRole.valueOf(this.getRole()));
        user.setStack(TechnicalStack.valueOf(this.getStack()));
        user.setContact(this.getContact());
        user.setArea(this.getArea());
        user.setDescription(this.getDescription());
    }
}