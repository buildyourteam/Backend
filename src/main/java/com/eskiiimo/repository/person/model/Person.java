package com.eskiiimo.repository.person.model;

import com.eskiiimo.repository.person.dto.ProfileDto;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import com.eskiiimo.web.person.enumtype.PersonStatus;
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
@Table(name = "T_PERSON")
public class Person implements UserDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false, unique = true, length = 30)
    private String personId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 100)
    @NotBlank
    private String password;

    @Size(min = 0, max = 20)
    @NotNull
    private String personName;

    private String personEmail;

    private String area;

    private Long personLevel;

    @Enumerated(EnumType.STRING)
    private ProjectRole projectRole;

    @Enumerated(EnumType.STRING)
    private PersonStatus personStatus;

    @OneToMany(fetch = FetchType.LAZY,cascade =  CascadeType.ALL,orphanRemoval=true)
    @JoinColumn(name ="account_id")
    @Builder.Default
    private List<PersonStack> stacks = new ArrayList<>();

    private String contact;

    @Size(min = 0, max = 10000)
    private String personDescription;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> personRoles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.personRoles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.personId;
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

    public String getPersonName(){
        return this.personName;
    }

    public ProfileDto toProfileDto(){
        List<TechnicalStack> stackList = new ArrayList<TechnicalStack>();
        for(PersonStack stack : this.stacks){
            TechnicalStack technicalStack = stack.getStack();
            stackList.add(technicalStack);
        }
        ProfileDto profileDto = ProfileDto.builder()
                .personName(this.getPersonName())
                .projectRole(this.projectRole)
                .stacks(stackList)
                .area(this.area)
                .contact(this.contact)
                .personLevel(this.personLevel)
                .personDescription(this.personDescription)
                .build();
        return profileDto;
    }

}