package com.restful.api.Projects;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
@Entity
public class ProjectAnswer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long projectAnswerId;
    private String answer;

}
