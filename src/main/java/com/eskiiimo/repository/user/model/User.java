package com.eskiiimo.repository.user.model;

import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import com.eskiiimo.web.user.enumtype.UserActivate;
import com.eskiiimo.web.user.enumtype.UserState;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "accountId")
@Entity
@Table(name = "T_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    @Column(nullable = false, unique = true, length = 30)
    private String userId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 100)
    @NotBlank
    private String password;
    @Size(min = 0, max = 20)
    @NotNull
    private String userName;
    private String userEmail;
    private String area;
    private Long grade;
    @Enumerated(EnumType.STRING)
    private ProjectRole role;
    @Enumerated(EnumType.STRING)
    private UserState state;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private List<UsersStack> stacks;
    private String contact;
    @Size(min = 0, max = 10000)
    private String introduction;
    @Enumerated(EnumType.STRING)
    private UserActivate activate;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    private String refreshToken;

    public User(String userId, String password, String userName, String userEmail, String refreshToken) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.userEmail = userEmail;
        this.activate = UserActivate.REGULAR;
        this.state = UserState.FREE;
        this.grade = (long) 0;
        this.roles = Collections.singletonList("ROLE_USER");
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateProfile(ProfileDto profileDto) {
        List<UsersStack> removeList = new ArrayList<UsersStack>();
        //Compare Stack List and Remove User's Stacks
        if (this.stacks == null)
            this.stacks = new ArrayList<UsersStack>();
        for (UsersStack usersStack : this.stacks) {
            boolean checkRemove = Boolean.TRUE;
            for (TechnicalStack stack : profileDto.getStacks()) {
                if (usersStack.getStack().equals(stack)) {
                    stacks.remove(stack);
                    checkRemove = Boolean.FALSE;
                    break;
                }
            }
            if (checkRemove)
                removeList.add(usersStack);
        }
        // Remove User's Stacks
        for (UsersStack stack : removeList)
            this.stacks.remove(stack);

        // add User's Stacks

        for (TechnicalStack stack : profileDto.getStacks()) {
            UsersStack usersStack = new UsersStack(stack);
            this.stacks.add(usersStack);
        }

        this.userName = profileDto.getUserName();
        this.role = profileDto.getRole();
        this.contact = profileDto.getContact();
        this.area = profileDto.getArea();
        this.introduction = profileDto.getIntroduction();
    }

    public void blockUser() {
        this.activate = UserActivate.BLOCKED;
    }
}