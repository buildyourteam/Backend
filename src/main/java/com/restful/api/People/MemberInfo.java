package com.restful.api.People;

import com.restful.api.Projects.ProjectRole;
import com.restful.api.Projects.TechnicalStack;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
