package me.web.spring.database.demo.model;

import java.io.Serializable;

public class SectionId implements Serializable {
    private String course_id;
    private String sec_id;
    private String semester;
    private int year;

    public SectionId() {
    }

    public SectionId(String course_id, String sec_id, String semester, int year) {
        this.course_id = course_id;
        this.sec_id = sec_id;
        this.semester = semester;
        this.year = year;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((course_id == null) ? 0 : course_id.hashCode());
        result = prime * result + ((sec_id == null) ? 0 : sec_id.hashCode());
        result = prime * result + ((semester == null) ? 0 : semester.hashCode());
        Integer y = year;
        result = prime * result + y.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SectionId other = (SectionId) obj;
        return (course_id.equals(other.course_id)
                && sec_id.equals(other.sec_id)
                && semester.equals(other.semester)
                && year == other.year);
    }
}
