package com.eskiiimo.repository.user.dto;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Relation(collectionRelation = "peopleList")
public class PeopleDto {
    private String userId;
    private String userName;
    @Builder.Default
    private List<TechnicalStack> stacks = new ArrayList<TechnicalStack>();
    private String area;
    private Long grade;
    private ProjectRole role;
}
