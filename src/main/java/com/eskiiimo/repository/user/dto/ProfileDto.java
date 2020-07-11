package com.eskiiimo.repository.user.dto;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.model.UsersStack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {
    private String userName;
    private ProjectRole role;
    @Builder.Default
    private List<TechnicalStack> stacks = new ArrayList<TechnicalStack>();
    private String contact;
    private String area;
    private Long grade;
    private String introduction;

    public void updateProfile(User User) {
        List<UsersStack> removeList = new ArrayList<UsersStack>();
        //Compare Stack List and Remove User's Stacks
        for(UsersStack usersStack: User.getStacks()){
            Boolean checkRemove=Boolean.TRUE;
            for(TechnicalStack stack : this.stacks){
                if(usersStack.getStack().equals(stack)){
                    this.stacks.remove(stack);
                    checkRemove=Boolean.FALSE;
                    break;
                }
            }
            if(checkRemove)
                removeList.add(usersStack);
        }
        // Remove User's Stacks
        for(UsersStack stack : removeList)
            User.getStacks().remove(stack);

        // add User's Stacks

        for(TechnicalStack stack : this.stacks){
            UsersStack usersStack = new UsersStack();
            usersStack.setStack(stack);
            User.getStacks().add(usersStack);
        }
            User.setRole(this.role);
            User.setContact(this.contact);
            User.setArea(this.area);
            User.setIntroduction(this.introduction);
    }

}