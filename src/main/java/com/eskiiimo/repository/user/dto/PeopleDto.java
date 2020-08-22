package com.eskiiimo.repository.user.dto;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.model.UsersStack;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Relation(collectionRelation = "peopleList")
public class PeopleDto {
    private String userId;
    private String userName;
    private String area;
    private Long grade;
    private ProjectRole role;
    private List<TechnicalStack> stacks;

    public PeopleDto(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.area = user.getArea();
        this.grade = user.getGrade();
        this.role = user.getRole();
        this.stacks = new ArrayList<TechnicalStack>();
        for (UsersStack stack : user.getStacks())
            this.stacks.add(stack.getStack());
    }
}
