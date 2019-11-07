package com.chungke.provider018081.service;

import com.chungke.provider018081.entity.Student;

import java.util.List;

public interface StudentService {
    boolean saveStudent(Student s);
    boolean deleteStudent(String sno);
    boolean updateStudent(Student s);
    Student getStudent(String sno);
    List<Student> findStudent();
}
