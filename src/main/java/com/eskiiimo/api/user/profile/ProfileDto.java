package com.eskiiimo.api.user.profile;

import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.TechnicalStack;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UsersStack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ProfileDto {
    private String userName;
    private ProjectRole role;
    private List<TechnicalStack> stacks = new ArrayList<TechnicalStack>();
    private String contact;
    private String area;
    private Long level;
    private String description;

    public void updateProfile(User User) {
        List<UsersStack> stackList = new ArrayList<UsersStack>();
        for (TechnicalStack stack : this.stacks) {
            User.setRole(this.role);
            User.setStacks(stackList);
            User.setContact(this.contact);
            User.setArea(this.area);
            User.setLevel(this.level);
            User.setDescription(this.description);
        }
    }

}