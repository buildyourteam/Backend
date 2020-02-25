package com.eskiiimo.api.user;

import com.eskiiimo.api.projects.ProjectMember;
import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.TechnicalStack;
import com.eskiiimo.api.user.people.People;
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
@SqlResultSetMappings({
        @SqlResultSetMapping(name = "SqlResultSetMapping.count", columns = @ColumnResult(name = "cnt")),
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
        ),
        @SqlResultSetMapping(
                name="peopleMapping",
                classes={
                        @ConstructorResult(
                                targetClass = People.class,
                                columns = {
                                        @ColumnResult(name="user_id", type = String.class),
                                        @ColumnResult(name="user_name", type = String.class),
                                        @ColumnResult(name="stack", type = String.class),
                                        @ColumnResult(name="area", type = String.class),
                                        @ColumnResult(name="level", type = Long.class)
                                }
                        )
                }
        )
})
@NamedNativeQuery(name = "User.findProfileByUserId",
        query = "SELECT u.user_name, u.role, u.stack, u.contact, u.area, u.level, u.description  FROM user  u Where u.user_id = ?1 ",
        resultSetMapping = "proflieMapping")
@NamedNativeQuery(name = "User.findPeopleAll",
        query = "SELECT u.user_id,u.user_name,u.stack,u.area, u.level  FROM user u",
        resultSetMapping = "peopleMapping"
)
@NamedNativeQuery(name = "User.findPeopleAll.count",
        query = "Select count(user_name) as cnt from user",
        resultSetMapping = "SqlResultSetMapping.count"
)
@NamedNativeQuery(name = "User.findPeopleByLevel",
        query = "SELECT u.user_id,u.user_name,u.stack,u.area, u.level  FROM user  u Where u.level = ?1",
        resultSetMapping = "peopleMapping"
)
@NamedNativeQuery(name = "User.findPeopleByLevel.count",
        query = "Select count(user_name) as cnt from user where level = ?1",
        resultSetMapping = "SqlResultSetMapping.count"
)
@NamedNativeQuery(name = "User.findPeopleByRole",
        query = "SELECT u.user_id,u.user_name,u.stack,u.area, u.level  FROM user  u Where u.role = ?1",
        resultSetMapping = "peopleMapping"
)
@NamedNativeQuery(name = "User.findPeopleByRole.count",
        query = "Select count(user_name) as cnt from user Where role =?1",
        resultSetMapping = "SqlResultSetMapping.count"
)
@NamedNativeQuery(name = "User.findPeopleByArea",
        query = "SELECT u.user_id,u.user_name,u.stack,u.area, u.level  FROM user  u Where u.area = ?1",
        resultSetMapping = "peopleMapping"
)
@NamedNativeQuery(name = "User.findPeopleByArea.count",
        query = "Select count(user_name) as cnt from user where area =?1",
        resultSetMapping = "SqlResultSetMapping.count"
)
@NamedNativeQuery(name = "User.findPeopleByLevelAndRole",
        query = "SELECT u.user_id,u.user_name,u.stack,u.area, u.level  FROM user  u Where u.level = ?1 And u.role =?2",
        resultSetMapping = "peopleMapping"
)
@NamedNativeQuery(name = "User.findPeopleByLevelAndRole.count",
        query = "Select count(user_name) as cnt from user where level =?1 And role =?2",
        resultSetMapping = "SqlResultSetMapping.count"
)
@NamedNativeQuery(name = "User.findPeopleByLevelAndArea",
        query = "SELECT u.user_id,u.user_name,u.stack,u.area, u.level  FROM user  u Where u.level = ?1 And u.area = ?2",
        resultSetMapping = "peopleMapping"
)
@NamedNativeQuery(name = "User.findPeopleByLevelAndArea.count",
        query = "Select count(user_name) as cnt from user where level =?1 And area =?2",
        resultSetMapping = "SqlResultSetMapping.count"
)
@NamedNativeQuery(name = "User.findPeopleByRoleAndLevel",
        query = "SELECT u.user_id,u.user_name,u.stack,u.area, u.level  FROM user  u Where u.role = ?1 And u.level = ?2",
        resultSetMapping = "peopleMapping"
)
@NamedNativeQuery(name = "User.findPeopleByRoleAndLevel.count",
        query = "Select count(user_name) as cnt from user where role =?1 And level =?2",
        resultSetMapping = "SqlResultSetMapping.count"
)
@NamedNativeQuery(name = "User.findPeopleByAreaAndRole",
        query = "SELECT u.user_id,u.user_name,u.stack,u.area, u.level  FROM user  u Where u.area = ?1 And u.role =?2",
        resultSetMapping = "peopleMapping"
)
@NamedNativeQuery(name = "User.findPeopleByAreaAndRole.count",
        query = "Select count(user_name) as cnt from user where area =?1 And role =?2",
        resultSetMapping = "SqlResultSetMapping.count"
)
@NamedNativeQuery(name = "User.findPeopleByAreaAndRoleAndLevel",
        query = "SELECT u.user_id,u.user_name,u.stack,u.area, u.level  FROM user  u Where u.area = ?1 And u.role =?2 And u.level = ?3",
        resultSetMapping = "peopleMapping"
)
@NamedNativeQuery(name = "User.findPeopleByAreaAndRoleAndLevel.count",
        query = "Select count(user_name) as cnt from user where area =?1 And role =?2 And level =?3",
        resultSetMapping = "SqlResultSetMapping.count"
)

public class User{
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