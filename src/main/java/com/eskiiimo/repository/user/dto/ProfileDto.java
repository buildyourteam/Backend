package com.eskiiimo.repository.user.dto;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.model.UsersStack;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProfileDto {
    private String userName;
    private ProjectRole role;
    private String contact;
    private String area;
    private Long grade;
    private String introduction;
    private List<TechnicalStack> stacks;

    public ProfileDto(User user) {
        this.userName = user.getUserName();
        this.role = user.getRole();
        this.contact = user.getContact();
        this.area = user.getArea();
        this.grade = user.getGrade();
        this.introduction = user.getIntroduction();
        this.stacks = new ArrayList<TechnicalStack>();
        for (UsersStack stack : user.getStacks())
            this.stacks.add(stack.getStack());
    }

    public ProfileDto(String userName, ProjectRole role, String contact, String area, String introduction, List<TechnicalStack> stacks){
        this.userName = userName;
        this.role = role;
        this.contact = contact;
        this.area = area;
        this.introduction = introduction;
        this.stacks = stacks;
        this.grade = null;
    }
}