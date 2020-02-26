package com.eskiiimo.api.user.people;

import com.eskiiimo.api.projects.TechnicalStack;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class People {
    private String userId;
    private String userName;
    private List<TechnicalStack> stacks = new ArrayList<TechnicalStack>();
    private String area;
    private Long level;

    public People(String userId , String userName, List<TechnicalStack> stacks, String area, Long level){
        super();
        this.userId = userId;
        this.area =area;
        this.userName = userName;
        this.level = level;
        this.stacks =stacks;
    }
}
