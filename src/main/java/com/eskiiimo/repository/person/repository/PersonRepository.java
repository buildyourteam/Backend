package com.eskiiimo.repository.person.repository;

import com.eskiiimo.repository.person.model.Person;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.person.enumtype.PersonStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByPersonId(String personId);
    Page<Person> findAll(Pageable pageable);
    Page<Person> findAllByPersonLevel(Long personLevel, Pageable pageable);
    Page<Person> findAllByProjectRole(ProjectRole projectRole, Pageable pageable);
    Page<Person> findAllByArea(String area, Pageable pageable);
    Page<Person> findAllByPersonLevelAndArea(Long personLevel, String area, Pageable pageable);
    Page<Person> findAllByAreaAndProjectRole(String area, ProjectRole projectRole, Pageable pageable);
    Page<Person> findAllByProjectRoleAndPersonLevel(ProjectRole projectRole, Long personLevel, Pageable pageable);
    Page<Person> findAllByAreaAndProjectRoleAndPersonLevel(String area, ProjectRole projectRole, Long personLevel, Pageable pageable);
    long countAllByPersonStatus(PersonStatus personStatus);
}

