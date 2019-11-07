package com.chungke.provider018081.web;

import com.chungke.provider018081.entity.Student;
import com.chungke.provider018081.service.StudentService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Data
@RestController
@RequestMapping("provider/stu")
public class StudentCtroller {
    @Autowired
    private StudentService studentService;

    @GetMapping("save")
    boolean saveStudent(Student s){
        Student student = new Student();
        student.setSsex("1");
        student.setSdept("1");
        student.setSno(UUID.randomUUID().toString());
        studentService.saveStudent(student);
        System.out.println(student.toString());
        return  true;
    }

    @GetMapping("delete")
    boolean deleteStudent(String sno){
        studentService.deleteStudent(sno);
        return  true;
    }
//    boolean updateStudent(Student s);
//    Student getStudent(String sno);
//    List<Student> findStudent();
}
