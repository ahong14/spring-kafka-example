package com.example.demo;

import com.example.demo.models.Student;
import com.example.demo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


@Component
public class KafkaListeners {
    private static final Logger logger = LogManager.getLogger(KafkaListeners.class);
    private StudentRepository studentRepository;

    @Autowired
    public KafkaListeners(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // listen to messages from student topic, using container factory to deserialize messages
    @KafkaListener(topics = "student", containerFactory = "studentConcurrentKafkaListenerContainerFactory", groupId = "groupId")
    public void listener(Student studentData) {
        try {
            logger.info("Kafka message found for: " + studentData);
            studentRepository.save(studentData);
        } catch (Exception e) {
            logger.error("Failed to insert kafka message *****");
            logger.error(e.getMessage());
        }
    }
}
