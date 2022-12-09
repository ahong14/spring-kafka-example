package com.example.demo.controllers;

import com.example.demo.services.StudentService;
import com.example.demo.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/student")
public class StudentController {

    private final StudentService studentService;

    // dependency injection
    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // GET /api/student, gets student records from db
    @GetMapping
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    // POST /api/student, create new student record
    @PostMapping
    public ResponseEntity createStudent(@RequestBody Student student) {
        boolean createStudentResult = studentService.createStudent(student);
        if (!createStudentResult) {
            return new ResponseEntity<>("Failed to create new student", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Student enqueued for creation", HttpStatus.OK);
    }
}