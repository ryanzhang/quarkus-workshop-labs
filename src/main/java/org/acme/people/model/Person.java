package org.acme.people.model;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

// import com.fasterxml.jackson.annotation.JsonFormat;

// import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;


@Entity
public class Person extends PanacheEntityBase {
    @Id
    @SequenceGenerator(
        name = "personSequence",
        sequenceName = "person_id_seq",
        allocationSize = 1,
        initialValue = 4
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personSequence")
    Integer id;

    String name;

    // @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birth;

    @Enumerated(EnumType.STRING)
    @Column(length=8)
    EyeColor eyes;

    public static List<Person> findByColor(EyeColor color){
        return list("eyes", color);
    }
    public static List<Person> getBeforeYear(int year){
        return Person.<Person>streamAll()
            .filter(p->p.birth.getYear() <= year )
            .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public EyeColor getEyes() {
        return eyes;
    }

    public void setEyes(EyeColor eyes) {
        this.eyes = eyes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
}