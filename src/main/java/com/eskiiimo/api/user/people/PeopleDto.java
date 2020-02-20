package com.eskiiimo.api.user.people;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PeopleDto {
    private String userName;
    private int level;
    private List<String> tags = new ArrayList<>();

}
