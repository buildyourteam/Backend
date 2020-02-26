package com.eskiiimo.api.user;

import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.TechnicalStack;
import com.eskiiimo.api.user.profile.ProfileDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy =  InheritanceType.JOINED)
@EqualsAndHashCode(of="accountId")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long accountId;
    private String userId;
    private String userPassword;
    private String userName;
    private String userEmail;
    private String area;
    private Long level;
    @Enumerated(EnumType.STRING)
    private ProjectRole role;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY,cascade =  CascadeType.ALL)
    @JoinColumn(name ="account_id")
    private List<UsersStack> stacks = new ArrayList<UsersStack>();
    private String contact;
    private String description;

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
                .level(this.level)
                .description(this.description)
                .build();
        return profileDto;
    }

}