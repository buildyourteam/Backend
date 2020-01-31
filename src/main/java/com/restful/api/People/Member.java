package com.restful.api.People;

import com.restful.api.Projects.ProjectMember;
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
public class Member {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long memberId;

    private String userId;
    private String userPassword;
    private String userEmail;
    private String userName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberId")
    private MemberInfo memberInfo;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private List<ProjectMember> projects = new ArrayList<ProjectMember>();
}
