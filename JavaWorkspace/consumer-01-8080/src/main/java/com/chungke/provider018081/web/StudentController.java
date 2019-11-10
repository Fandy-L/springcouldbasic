package com.chungke.provider018081.web;

import com.chungke.provider018081.entity.Student;
import com.chungke.provider018081.service.StudentService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Data
@RestController
public class StudentController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient client;

//    @PostMapping("save")
//    boolean saveStudent(Student s){
//        Student student = new Student();
//        student.setSsex("1");
//        student.setSdept("1");
//        student.setSno(UUID.randomUUID().toString());
//        return  restTemplate.postForObject();
//    }

    @GetMapping("/discovery")
    Object deleteStudent(String sno){
        List<String> services = client.getServices();
        for(String service:services){
            System.out.println("service: "+service);
            List<ServiceInstance> instances = client.getInstances(service);
            for(ServiceInstance instance:instances){
                URI uri = instance.getUri();
                String host = instance.getHost();
                int port = instance.getPort();
                System.out.println(host+":"+port);
                System.out.println(instance.getServiceId()+":"+uri);
            }
        }
        return  services;
    }
//    boolean updateStudent(Student s);
//    Student getStudent(String sno);
//    List<Student> findStudent();
}
