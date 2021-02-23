package com.project.internship.dto;

public class SearchDTO {
    private String byName;
    private String bySkillName;

    public SearchDTO() {
    }

    public String getByName() {
        return byName;
    }

    public void setByName(String byName) {
        this.byName = byName;
    }

    public String getBySkillName() {
        return bySkillName;
    }

    public void setBySkillName(String bySkillName) {
        this.bySkillName = bySkillName;
    }
}
