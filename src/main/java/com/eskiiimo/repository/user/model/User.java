package com.eskiiimo.repository.user.model;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import com.eskiiimo.web.user.enumtype.UserStatus;
import com.eskiiimo.repository.user.dto.ProfileDto;
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
@EqualsAndHashCode(of="accountId")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
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
    private Long level;
    @Enumerated(EnumType.STRING)
    private ProjectRole role;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY,cascade =  CascadeType.ALL,orphanRemoval=true)
    @JoinColumn(name ="account_id")
    private List<UsersStack> stacks = new ArrayList<UsersStack>();
    private String contact;
    @Size(min = 0, max = 10000)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.userId;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getUserName(){
        return this.userName;
    }

    public ProfileDto toProfileDto(){
        List<TechnicalStack> stackList = new ArrayList<TechnicalStack>();
        for(UsersStack stack : this.stacks){
            TechnicalStack technicalStack = stack.getStack();
            stackList.add(technicalStack);
        }
        ProfileDto profileDto = ProfileDto.builder()
                .userName(this.getUserName())
                .role(this.role)
                .stacks(stackList)
                .area(this.area)
                .contact(this.contact)
                .level(this.level)
                .description(this.description)
                .build();
        return profileDto;
    }

}