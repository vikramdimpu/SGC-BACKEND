package com.studentgrade.backend.config;

import com.studentgrade.backend.dto.MarksEntryDTO;
import com.studentgrade.backend.model.Role;
import com.studentgrade.backend.model.Student;
import com.studentgrade.backend.model.Subject;
import com.studentgrade.backend.model.User;
import com.studentgrade.backend.repository.StudentRepository;
import com.studentgrade.backend.repository.SubjectRepository;
import com.studentgrade.backend.repository.UserRepository;
import com.studentgrade.backend.service.AcademicService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final PasswordEncoder passwordEncoder;
    private final AcademicService academicService;

    @Value("${app.admin.default-password:Admin@1234}")
    private String adminDefaultPassword;

    public DataInitializer(UserRepository userRepository,
                           StudentRepository studentRepository,
                           SubjectRepository subjectRepository,
                           PasswordEncoder passwordEncoder,
                           AcademicService academicService) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.passwordEncoder = passwordEncoder;
        this.academicService = academicService;
    }

    @Override
    public void run(String... args) {
        initializeAdmin();
        initializeSubjects();
        initializeSampleStudents();
    }

    private void initializeAdmin() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User(
                    "admin",
                    passwordEncoder.encode(adminDefaultPassword),
                    Role.ROLE_ADMIN,
                    true
            );
            userRepository.save(admin);
            System.out.println(">>> Initialized default Administrator account: [admin] with configured password.");
        }
    }

    private void initializeSubjects() {
        if (subjectRepository.count() == 0) {
            List<Subject> subjects = List.of(
                    // Semester 1 Subjects (CSE)
                    new Subject("CS101", "Engineering Mathematics - I", "CSE", 1, "THEORY", 4.0, 30.0, 70.0, 0.0, 25.0, 40.0),
                    new Subject("CS102", "Engineering Physics", "CSE", 1, "THEORY", 3.0, 30.0, 70.0, 0.0, 25.0, 40.0),
                    new Subject("CS103", "Programming for Problem Solving in C", "CSE", 1, "THEORY", 3.0, 30.0, 70.0, 0.0, 25.0, 40.0),
                    new Subject("CS104", "Basic Electrical & Electronics", "CSE", 1, "THEORY", 3.0, 30.0, 70.0, 0.0, 25.0, 40.0),
                    new Subject("CS105P", "C Programming & Problem Solving Lab", "CSE", 1, "LABORATORY", 1.5, 30.0, 70.0, 0.0, 25.0, 40.0),
                    new Subject("CS106P", "Engineering Physics Laboratory", "CSE", 1, "LABORATORY", 1.5, 30.0, 70.0, 0.0, 25.0, 40.0),

                    // Semester 2 Subjects (CSE)
                    new Subject("CS201", "Engineering Mathematics - II", "CSE", 2, "THEORY", 4.0, 30.0, 70.0, 0.0, 25.0, 40.0),
                    new Subject("CS202", "Data Structures & Algorithms", "CSE", 2, "THEORY", 3.5, 30.0, 70.0, 0.0, 25.0, 40.0),
                    new Subject("CS203", "Digital Logic & Computer Design", "CSE", 2, "THEORY", 3.0, 30.0, 70.0, 0.0, 25.0, 40.0),
                    new Subject("CS204", "Object Oriented Programming via Java", "CSE", 2, "THEORY", 3.0, 30.0, 70.0, 0.0, 25.0, 40.0),
                    new Subject("CS205P", "Data Structures Laboratory", "CSE", 2, "LABORATORY", 1.5, 30.0, 70.0, 0.0, 25.0, 40.0),
                    new Subject("CS206P", "Java Programming Laboratory", "CSE", 2, "LABORATORY", 1.5, 30.0, 70.0, 0.0, 25.0, 40.0),

                    // Semester 3 Subjects (CSE)
                    new Subject("CS301", "Database Management Systems", "CSE", 3, "THEORY", 3.5, 30.0, 70.0, 0.0, 25.0, 40.0),
                    new Subject("CS302", "Operating Systems", "CSE", 3, "THEORY", 3.5, 30.0, 70.0, 0.0, 25.0, 40.0),
                    new Subject("CS303", "Design & Analysis of Algorithms", "CSE", 3, "THEORY", 3.5, 30.0, 70.0, 0.0, 25.0, 40.0),
                    new Subject("CS304", "Computer Organization & Architecture", "CSE", 3, "THEORY", 3.0, 30.0, 70.0, 0.0, 25.0, 40.0),
                    new Subject("CS305P", "DBMS & SQL Laboratory", "CSE", 3, "LABORATORY", 1.5, 30.0, 70.0, 0.0, 25.0, 40.0),
                    new Subject("CS306P", "Operating Systems & Linux Lab", "CSE", 3, "LABORATORY", 1.5, 30.0, 70.0, 0.0, 25.0, 40.0)
            );
            subjectRepository.saveAll(subjects);
            System.out.println(">>> Initialized standard B.Tech CSE subjects across semesters 1, 2, and 3.");
        }
    }

    private void initializeSampleStudents() {
        if (!studentRepository.existsByRollNumber("21B91A0501")) {
            // Student 1: 21B91A0501
            User user1 = new User("21B91A0501", passwordEncoder.encode("Student@123"), Role.ROLE_STUDENT, true);
            userRepository.save(user1);

            Student s1 = new Student(
                    "Rahul Sharma",
                    "21B91A0501",
                    "rahul.sharma@college.edu",
                    "Computer Science & Engineering",
                    "CSE",
                    "2023-2027",
                    3,
                    "A"
            );
            studentRepository.save(s1);

            // Seed Marks for Semester 1
            academicService.enterMarks(new MarksEntryDTO("21B91A0501", "CS101", 1, 26.0, 62.0));
            academicService.enterMarks(new MarksEntryDTO("21B91A0501", "CS102", 1, 25.0, 58.0));
            academicService.enterMarks(new MarksEntryDTO("21B91A0501", "CS103", 1, 28.0, 65.0));
            academicService.enterMarks(new MarksEntryDTO("21B91A0501", "CS104", 1, 24.0, 55.0));
            academicService.enterMarks(new MarksEntryDTO("21B91A0501", "CS105P", 1, 29.0, 68.0));
            academicService.enterMarks(new MarksEntryDTO("21B91A0501", "CS106P", 1, 28.0, 66.0));
            academicService.publishStudentSemester(s1.getId(), 1);

            // Seed Marks for Semester 2
            academicService.enterMarks(new MarksEntryDTO("21B91A0501", "CS201", 2, 27.0, 60.0));
            academicService.enterMarks(new MarksEntryDTO("21B91A0501", "CS202", 2, 28.0, 64.0));
            academicService.enterMarks(new MarksEntryDTO("21B91A0501", "CS203", 2, 25.0, 57.0));
            academicService.enterMarks(new MarksEntryDTO("21B91A0501", "CS204", 2, 29.0, 67.0));
            academicService.enterMarks(new MarksEntryDTO("21B91A0501", "CS205P", 2, 29.0, 68.0));
            academicService.enterMarks(new MarksEntryDTO("21B91A0501", "CS206P", 2, 28.0, 65.0));
            academicService.publishStudentSemester(s1.getId(), 2);
        }

        if (!studentRepository.existsByRollNumber("21B91A0502")) {
            // Student 2: 21B91A0502
            User user2 = new User("21B91A0502", passwordEncoder.encode("Student@123"), Role.ROLE_STUDENT, true);
            userRepository.save(user2);

            Student s2 = new Student(
                    "Priya Patel",
                    "21B91A0502",
                    "priya.patel@college.edu",
                    "Computer Science & Engineering",
                    "CSE",
                    "2023-2027",
                    3,
                    "A"
            );
            studentRepository.save(s2);

            academicService.enterMarks(new MarksEntryDTO("21B91A0502", "CS101", 1, 28.0, 66.0));
            academicService.enterMarks(new MarksEntryDTO("21B91A0502", "CS102", 1, 27.0, 63.0));
            academicService.enterMarks(new MarksEntryDTO("21B91A0502", "CS103", 1, 29.0, 68.0));
            academicService.enterMarks(new MarksEntryDTO("21B91A0502", "CS104", 1, 26.0, 60.0));
            academicService.enterMarks(new MarksEntryDTO("21B91A0502", "CS105P", 1, 30.0, 69.0));
            academicService.enterMarks(new MarksEntryDTO("21B91A0502", "CS106P", 1, 29.0, 67.0));
            academicService.publishStudentSemester(s2.getId(), 1);
        }
    }
}
