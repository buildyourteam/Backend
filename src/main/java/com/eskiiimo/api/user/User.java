package com.eskiiimo.api.user;

import com.eskiiimo.api.projects.ProjectMember;
import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.TechnicalStack;
import com.eskiiimo.api.user.profile.ProfileDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="accountId")
@Entity

@NamedNativeQuery(name = "User.findProfileByUserId",
        query = "SELECT u.user_name, u.role, u.stack, u.contact, u.area, u.level, u.description  FROM user  u Where u.user_id = ?1 ",
        resultSetMapping = "proflieMapping")
@SqlResultSetMapping(
        name="proflieMapping",
        classes={
                @ConstructorResult(
                        targetClass = ProfileDto.class,
                        columns = {
                                @ColumnResult(name="user_name", type = String.class),
                                @ColumnResult(name="role", type = String.class),
                                @ColumnResult(name="stack", type = String.class),
                                @ColumnResult(name="contact", type = String.class),
                                @ColumnResult(name="area", type = String.class),
                                @ColumnResult(name="level", type = Long.class),
                                @ColumnResult(name="description", type = String.class)
                        }
                )
        }
)
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long accountId;

    private String userId;
    private String userPassword;
    private String userEmail;
    private String userName;
    @Enumerated(EnumType.STRING)
    private ProjectRole role;
    @Enumerated(EnumType.STRING)
    private TechnicalStack stack;
    private String contact;
    private String area;
    private Long level;
    private String description;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<ProjectMember> projects = new ArrayList<ProjectMember>();

    public void addProjects(ProjectMember projectMember){
        this.projects.add(projectMember);
        if(projectMember.getUser() != this)
            projectMember.setUser(this);
    }

}