package com.chungke.provider018081.service.impl;

import com.chungke.provider018081.entity.Student;
import com.chungke.provider018081.entity.StudentRepository;
import com.chungke.provider018081.service.StudentService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public boolean saveStudent(Student s) {
        studentRepository.save(s);
        return true;
    }

    @Override
    public boolean deleteStudent(String sno) {
        studentRepository.deleteById(sno);
        return false;
    }

    @Override
    public boolean updateStudent(Student s) {
        Student save = studentRepository.save(s);
        if(save != null){
            return  true;
        }
        return false;
    }

    @Override
    public Student getStudent(String sno) {

        return studentRepository.getOne(sno);
    }

    @Override
    public List<Student> findStudent() {
        return studentRepository.findAll();
    }
}
