package com.eskiiimo.api.people;

import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.TechnicalStack;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
@Entity
public class MemberInfo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long memberId;

    private ProjectRole role;
    private TechnicalStack stack;
    private String contact;
    private String area;
    private Long level;
    private String description;


}
