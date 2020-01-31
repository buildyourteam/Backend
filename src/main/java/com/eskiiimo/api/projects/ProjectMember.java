package com.eskiiimo.api.projects;

import com.eskiiimo.api.people.Member;
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

public class ProjectMember {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private  Long projectMemberId;
    private ProjectRole role;
    private TechnicalStack stack;
    @Lob
    private String selfDescription;

    @ManyToOne
    @JoinColumn(name="memberId")
    private Member member;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectMemberId")
    private List<ProjectAnswer> answers = new ArrayList<ProjectAnswer>();

}
