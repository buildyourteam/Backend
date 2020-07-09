package com.eskiiimo.repository.user.model;

import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@Table(name = "T_PEOPLE")
public class People {
    private String userId;
    private String userName;
    @Builder.Default
    private List<TechnicalStack> stacks = new ArrayList<TechnicalStack>();
    private String area;
    private Long grade;

    public People(String userId , String userName, List<TechnicalStack> stacks, String area, Long grade){
        super();
        this.userId = userId;
        this.area =area;
        this.userName = userName;
        this.grade = grade;
        this.stacks =stacks;
    }
}
