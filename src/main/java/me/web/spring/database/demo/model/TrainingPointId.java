package me.web.spring.database.demo.model;

import java.io.Serializable;

public class TrainingPointId implements Serializable {
    private int student_id;

    private String semester;

    private int year;

    public TrainingPointId() {
    }

    public TrainingPointId(int student_id, String semester, int year) {
        this.student_id = student_id;
        this.semester = semester;
        this.year = year;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        Integer si = student_id;
        result = prime * result + si.hashCode();
        result = prime * result + ((semester == null) ? 0 : semester.hashCode());
        Integer y = year;
        result = prime * result + y.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TrainingPointId other = (TrainingPointId) obj;
        return (student_id == other.student_id
                && semester.equals(other.semester)
                && year == other.year);
    }
}
