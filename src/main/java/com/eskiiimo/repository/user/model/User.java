package com.eskiiimo.repository.user.model;

import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import com.eskiiimo.web.user.enumtype.UserActivate;
import com.eskiiimo.web.user.enumtype.UserState;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@EqualsAndHashCode(of = "accountId")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "T_USER")
public class User{
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

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public ProfileDto toProfileDto() {
        List<TechnicalStack> stackList = new ArrayList<TechnicalStack>();
        for (UsersStack stack : this.stacks) {
            TechnicalStack technicalStack = stack.getStack();
            stackList.add(technicalStack);
        }
        ProfileDto profileDto = ProfileDto.builder()
                .userName(this.getUserName())
                .role(this.role)
                .stacks(stackList)
                .area(this.area)
                .contact(this.contact)
                .grade(this.grade)
                .introduction(this.introduction)
                .build();
        return profileDto;
    }

    public void updateProfile(String userName, ProjectRole role, List<TechnicalStack> stacks, String contact, String area, String introduction) {
        List<UsersStack> removeList = new ArrayList<UsersStack>();
        //Compare Stack List and Remove User's Stacks
        if(this.stacks== null)
            this.stacks = new ArrayList<UsersStack>();
        for (UsersStack usersStack : this.stacks) {
            boolean checkRemove = Boolean.TRUE;
            for (TechnicalStack stack : stacks) {
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

        for (TechnicalStack stack : stacks) {
            UsersStack usersStack = new UsersStack(stack);
            this.stacks.add(usersStack);
        }

        this.userName = userName;
        this.role = role;
        this.contact = contact;
        this.area = area;
        this.introduction = introduction;
    }

    public void blockUser(){
        this.activate = UserActivate.BLOCKED;
    }
}