package com.eskiiimo.repository.person.dto;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import com.eskiiimo.repository.person.model.Person;
import com.eskiiimo.repository.person.model.PersonStack;
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
    private String personName;
    private ProjectRole projectRole;
    @Builder.Default
    private List<TechnicalStack> stacks = new ArrayList<TechnicalStack>();
    private String contact;
    private String area;
    private Long personLevel;
    private String personDescription;

    public void updateProfile(Person person) {
        List<PersonStack> removeList = new ArrayList<PersonStack>();
        //Compare Stack List and Remove User's Stacks
        for(PersonStack personStack : person.getStacks()){
            Boolean checkRemove=Boolean.TRUE;
            for(TechnicalStack stack : this.stacks){
                if(personStack.getStack().equals(stack)){
                    this.stacks.remove(stack);
                    checkRemove=Boolean.FALSE;
                    break;
                }
            }
            if(checkRemove)
                removeList.add(personStack);
        }
        // Remove User's Stacks
        for(PersonStack stack : removeList)
            person.getStacks().remove(stack);

        // add User's Stacks

        for(TechnicalStack stack : this.stacks){
            PersonStack personStack = new PersonStack();
            personStack.setStack(stack);
            person.getStacks().add(personStack);
        }
        person.setProjectRole(this.projectRole);
        person.setContact(this.contact);
        person.setArea(this.area);
        person.setPersonDescription(this.personDescription);
    }

}