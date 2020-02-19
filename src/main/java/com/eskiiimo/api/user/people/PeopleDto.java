package com.eskiiimo.api.user.people;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class PeopleDto {
    private String userId;
    private String userName;
    private String stack;
    private String area;
    private Long level;

    public PeopleDto(String userId ,String userName, String stack, String area, Long level){
        super();
        this.userId = userId;
        this.area =area;
        this.userName = userName;
        this.level = level;
        this.stack =stack;
    }
}
