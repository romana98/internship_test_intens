package com.project.internship.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "skills")
    private Set<Candidate> candidates = new HashSet<>();

    public Skill() {
    }

    public Skill(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Skill(String name) {
        this.name = name;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(HashSet<Candidate> candidates) {
        this.candidates = candidates;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Skill)) {
            return false;
        }

        Skill s = (Skill) o;

        return id == s.id && name.equals(s.name);
    }
}
