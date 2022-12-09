package com.example.demo.services;

import com.example.demo.models.Student;
import com.example.demo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

// tells spring this class needs to be instantiated
@Service
public class StudentService {
    private static final Logger logger = LogManager.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private KafkaTemplate<String, Student> studentKafkaTemplate;

    @Autowired
    public StudentService(StudentRepository studentRepository, KafkaTemplate<String, Student> studentKafkaTemplate) {
        this.studentRepository = studentRepository;
        this.studentKafkaTemplate = studentKafkaTemplate;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public boolean createStudent(Student student) {
        try {
            Optional<Student> foundStudent = studentRepository.findStudentByEmail(student.getEmail());
            if (foundStudent.isPresent()) {
                throw new IllegalStateException("Email already taken");
            }
            // save if not found, publish message to kafka
            logger.info("Sending kafka message for: " + student);
            studentKafkaTemplate.send("student", student);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}