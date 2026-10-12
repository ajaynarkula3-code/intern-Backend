package com.smartintern.config;

import com.smartintern.model.*;
import com.smartintern.repository.ApplicationRepository;
import com.smartintern.repository.InternshipRepository;
import com.smartintern.repository.StudentProfileRepository;
import com.smartintern.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudentProfileRepository profileRepository;
    private final InternshipRepository internshipRepository;
    private final ApplicationRepository applicationRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           StudentProfileRepository profileRepository,
                           InternshipRepository internshipRepository,
                           ApplicationRepository applicationRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.internshipRepository = internshipRepository;
        this.applicationRepository = applicationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // Data already initialized
        }

        System.out.println(">>> Initializing Smart Internship seed data...");

        // 1. Create Default Admin
        User admin = new User("System Administrator", "admin@smartintern.com", passwordEncoder.encode("admin123"), Role.ADMIN);
        userRepository.save(admin);

        // 2. Create Default HR Recruiters
        User hrGoogle = new User("Sarah Jenkins", "hr@google.com", passwordEncoder.encode("hr123"), Role.HR);
        userRepository.save(hrGoogle);

        User hrMicrosoft = new User("David Miller", "hr@microsoft.com", passwordEncoder.encode("hr123"), Role.HR);
        userRepository.save(hrMicrosoft);

        // 3. Create Sample Students
        // Student 1: Rahul Sharma (Java / Backend - High CGPA 8.8)
        User uRahul = new User("Rahul Sharma", "rahul@gmail.com", passwordEncoder.encode("student123"), Role.STUDENT);
        userRepository.save(uRahul);

        StudentProfile pRahul = new StudentProfile(uRahul);
        pRahul.setPhone("+91 98765 43210");
        pRahul.setCollege("National Institute of Technology");
        pRahul.setDegree("B.Tech");
        pRahul.setBranch("CSE");
        pRahul.setGraduationYear(2025);
        pRahul.setCgpa(8.8);
        pRahul.setPreferredDomain("Java Development");
        pRahul.setLocationPreference("Bangalore");
        pRahul.setBio("Passionate backend engineer with expertise in Java, Spring Boot microservices, and relational databases.");
        pRahul.setResumeUrl("https://example.com/resumes/rahul-sharma.pdf");
        pRahul.addSkill(new StudentSkill(pRahul, "Java", "Advanced"));
        pRahul.addSkill(new StudentSkill(pRahul, "Spring Boot", "Advanced"));
        pRahul.addSkill(new StudentSkill(pRahul, "MySQL", "Advanced"));
        pRahul.addSkill(new StudentSkill(pRahul, "Docker", "Intermediate"));
        pRahul.addSkill(new StudentSkill(pRahul, "Git", "Advanced"));
        profileRepository.save(pRahul);

        // Student 2: Priya Patel (AI / ML - CGPA 7.5)
        User uPriya = new User("Priya Patel", "priya@gmail.com", passwordEncoder.encode("student123"), Role.STUDENT);
        userRepository.save(uPriya);

        StudentProfile pPriya = new StudentProfile(uPriya);
        pPriya.setPhone("+91 98765 43211");
        pPriya.setCollege("Vellore Institute of Technology");
        pPriya.setDegree("B.Tech");
        pPriya.setBranch("IT");
        pPriya.setGraduationYear(2026);
        pPriya.setCgpa(7.5);
        pPriya.setPreferredDomain("AI/ML");
        pPriya.setLocationPreference("Hyderabad");
        pPriya.setBio("Aspiring AI researcher interested in computer vision, deep learning frameworks, and predictive modeling.");
        pPriya.setResumeUrl("https://example.com/resumes/priya-patel.pdf");
        pPriya.addSkill(new StudentSkill(pPriya, "Python", "Advanced"));
        pPriya.addSkill(new StudentSkill(pPriya, "Machine Learning", "Intermediate"));
        pPriya.addSkill(new StudentSkill(pPriya, "Data Analytics", "Intermediate"));
        pPriya.addSkill(new StudentSkill(pPriya, "Pandas", "Intermediate"));
        pPriya.addSkill(new StudentSkill(pPriya, "SQL", "Intermediate"));
        profileRepository.save(pPriya);

        // Student 3: Amit Kumar (Web Dev - BCA, CGPA 6.9)
        User uAmit = new User("Amit Kumar", "amit@gmail.com", passwordEncoder.encode("student123"), Role.STUDENT);
        userRepository.save(uAmit);

        StudentProfile pAmit = new StudentProfile(uAmit);
        pAmit.setPhone("+91 98765 43212");
        pAmit.setCollege("Delhi University");
        pAmit.setDegree("BCA");
        pAmit.setBranch("Computer Applications");
        pAmit.setGraduationYear(2025);
        pAmit.setCgpa(6.9);
        pAmit.setPreferredDomain("Web Development");
        pAmit.setLocationPreference("Remote");
        pAmit.setBio("Frontend enthusiast creating responsive single-page applications with React and Tailwind CSS.");
        pAmit.setResumeUrl("https://example.com/resumes/amit-kumar.pdf");
        pAmit.addSkill(new StudentSkill(pAmit, "React", "Advanced"));
        pAmit.addSkill(new StudentSkill(pAmit, "JavaScript", "Advanced"));
        pAmit.addSkill(new StudentSkill(pAmit, "HTML", "Advanced"));
        pAmit.addSkill(new StudentSkill(pAmit, "CSS", "Advanced"));
        pAmit.addSkill(new StudentSkill(pAmit, "Git", "Intermediate"));
        profileRepository.save(pAmit);

        // Student 4: Ananya Verma (Cloud / DevOps - CGPA 8.4)
        User uAnanya = new User("Ananya Verma", "ananya@gmail.com", passwordEncoder.encode("student123"), Role.STUDENT);
        userRepository.save(uAnanya);

        StudentProfile pAnanya = new StudentProfile(uAnanya);
        pAnanya.setPhone("+91 98765 43213");
        pAnanya.setCollege("BITS Pilani");
        pAnanya.setDegree("B.Tech");
        pAnanya.setBranch("ECE");
        pAnanya.setGraduationYear(2024);
        pAnanya.setCgpa(8.4);
        pAnanya.setPreferredDomain("Cloud Computing");
        pAnanya.setLocationPreference("Pune");
        pAnanya.setBio("Cloud architecture learner certified in AWS Cloud Practitioner, enthusiastic about CI/CD and containers.");
        pAnanya.setResumeUrl("https://example.com/resumes/ananya-verma.pdf");
        pAnanya.addSkill(new StudentSkill(pAnanya, "AWS", "Intermediate"));
        pAnanya.addSkill(new StudentSkill(pAnanya, "Docker", "Advanced"));
        pAnanya.addSkill(new StudentSkill(pAnanya, "Kubernetes", "Intermediate"));
        pAnanya.addSkill(new StudentSkill(pAnanya, "Linux", "Advanced"));
        profileRepository.save(pAnanya);

        // Student 5: Vikram Singh (Cybersecurity - M.Tech, CGPA 9.2)
        User uVikram = new User("Vikram Singh", "vikram@gmail.com", passwordEncoder.encode("student123"), Role.STUDENT);
        userRepository.save(uVikram);

        StudentProfile pVikram = new StudentProfile(uVikram);
        pVikram.setPhone("+91 98765 43214");
        pVikram.setCollege("IIT Madras");
        pVikram.setDegree("M.Tech");
        pVikram.setBranch("CSE");
        pVikram.setGraduationYear(2025);
        pVikram.setCgpa(9.2);
        pVikram.setPreferredDomain("Cybersecurity");
        pVikram.setLocationPreference("Bangalore");
        pVikram.setBio("Specializing in application security, penetration testing, and secure system architectures.");
        pVikram.setResumeUrl("https://example.com/resumes/vikram-singh.pdf");
        pVikram.addSkill(new StudentSkill(pVikram, "Cybersecurity", "Advanced"));
        pVikram.addSkill(new StudentSkill(pVikram, "Network Security", "Advanced"));
        pVikram.addSkill(new StudentSkill(pVikram, "Python", "Intermediate"));
        pVikram.addSkill(new StudentSkill(pVikram, "Linux", "Advanced"));
        profileRepository.save(pVikram);

        // 4. Create Realistic Internships across 7 Domains
        // Internship 1: Java Enterprise Developer (Google)
        Internship int1 = new Internship();
        int1.setTitle("Java Backend & Cloud Developer Intern");
        int1.setCompanyName("Google");
        int1.setCompanyLogo("https://cdn-icons-png.flaticon.com/512/2991/2991148.png");
        int1.setDomain("Java Development");
        int1.setDescription("Join our Cloud Core engineering group to architect scalable Java microservices, build high-throughput RESTful services, and deploy on GCP container infrastructure.");
        int1.setLocation("Bangalore");
        int1.setWorkMode(WorkMode.HYBRID);
        int1.setDurationMonths(6);
        int1.setStipend(55000.0);
        int1.setOpenings(5);
        int1.setDeadline(LocalDate.now().plusMonths(2));
        int1.setMinCgpa(7.5);
        int1.setEligibleDegrees("B.Tech, B.E., M.Tech, MCA");
        int1.setEligibleBranches("CSE, IT, ECE");
        int1.setEligibleGraduationYears("2025, 2026");
        int1.setPostedBy(hrGoogle);
        int1.addRequiredSkill(new RequiredSkill(int1, "Java", true));
        int1.addRequiredSkill(new RequiredSkill(int1, "Spring Boot", true));
        int1.addRequiredSkill(new RequiredSkill(int1, "MySQL", true));
        int1.addRequiredSkill(new RequiredSkill(int1, "Docker", false));
        internshipRepository.save(int1);

        // Internship 2: Full Stack Web Developer (Amazon)
        Internship int2 = new Internship();
        int2.setTitle("Full Stack Web Developer Intern");
        int2.setCompanyName("Amazon");
        int2.setCompanyLogo("https://cdn-icons-png.flaticon.com/512/732/732200.png");
        int2.setDomain("Web Development");
        int2.setDescription("Work directly with consumer storefront teams building responsive, high-performance web experiences using React.js and modern client-side architectures.");
        int2.setLocation("Remote");
        int2.setWorkMode(WorkMode.REMOTE);
        int2.setDurationMonths(6);
        int2.setStipend(45000.0);
        int2.setOpenings(8);
        int2.setDeadline(LocalDate.now().plusMonths(1));
        int2.setMinCgpa(6.5);
        int2.setEligibleDegrees("All");
        int2.setEligibleBranches("All");
        int2.setEligibleGraduationYears("2024, 2025, 2026");
        int2.setPostedBy(hrGoogle);
        int2.addRequiredSkill(new RequiredSkill(int2, "React", true));
        int2.addRequiredSkill(new RequiredSkill(int2, "JavaScript", true));
        int2.addRequiredSkill(new RequiredSkill(int2, "HTML", true));
        int2.addRequiredSkill(new RequiredSkill(int2, "CSS", true));
        int2.addRequiredSkill(new RequiredSkill(int2, "Git", false));
        internshipRepository.save(int2);

        // Internship 3: AI/ML Research Intern (NVIDIA)
        Internship int3 = new Internship();
        int3.setTitle("Generative AI & Machine Learning Research Intern");
        int3.setCompanyName("NVIDIA");
        int3.setCompanyLogo("https://cdn-icons-png.flaticon.com/512/882/882736.png");
        int3.setDomain("AI/ML");
        int3.setDescription("Collaborate with deep learning researchers on optimizing large transformer architectures, accelerated inference pipelines, and experimental machine learning models.");
        int3.setLocation("Hyderabad");
        int3.setWorkMode(WorkMode.ONSITE);
        int3.setDurationMonths(6);
        int3.setStipend(60000.0);
        int3.setOpenings(3);
        int3.setDeadline(LocalDate.now().plusDays(45));
        int3.setMinCgpa(7.5);
        int3.setEligibleDegrees("B.Tech, M.Tech");
        int3.setEligibleBranches("CSE, IT, Data Science");
        int3.setEligibleGraduationYears("2025, 2026");
        int3.setPostedBy(hrMicrosoft);
        int3.addRequiredSkill(new RequiredSkill(int3, "Python", true));
        int3.addRequiredSkill(new RequiredSkill(int3, "Machine Learning", true));
        int3.addRequiredSkill(new RequiredSkill(int3, "Pandas", false));
        internshipRepository.save(int3);

        // Internship 4: Cloud DevOps Intern (Microsoft)
        Internship int4 = new Internship();
        int4.setTitle("Cloud Systems & DevOps Engineer Intern");
        int4.setCompanyName("Microsoft");
        int4.setCompanyLogo("https://cdn-icons-png.flaticon.com/512/732/732221.png");
        int4.setDomain("Cloud Computing");
        int4.setDescription("Design automated deployment manifests, build infrastructure as code, containerize cloud microservices, and manage distributed telemetry pipelines on Azure.");
        int4.setLocation("Pune");
        int4.setWorkMode(WorkMode.HYBRID);
        int4.setDurationMonths(4);
        int4.setStipend(48000.0);
        int4.setOpenings(4);
        int4.setDeadline(LocalDate.now().plusMonths(3));
        int4.setMinCgpa(7.5);
        int4.setEligibleDegrees("B.Tech, B.E., MCA");
        int4.setEligibleBranches("CSE, IT, ECE");
        int4.setEligibleGraduationYears("2024, 2025, 2026");
        int4.setPostedBy(hrMicrosoft);
        int4.addRequiredSkill(new RequiredSkill(int4, "AWS", true));
        int4.addRequiredSkill(new RequiredSkill(int4, "Docker", true));
        int4.addRequiredSkill(new RequiredSkill(int4, "Kubernetes", true));
        int4.addRequiredSkill(new RequiredSkill(int4, "Linux", true));
        internshipRepository.save(int4);

        // Internship 5: Cybersecurity Analyst (Palo Alto Networks)
        Internship int5 = new Internship();
        int5.setTitle("Security Operations & Threat Defense Intern");
        int5.setCompanyName("Palo Alto Networks");
        int5.setCompanyLogo("https://cdn-icons-png.flaticon.com/512/2092/2092663.png");
        int5.setDomain("Cybersecurity");
        int5.setDescription("Analyze network traffic patterns, assess application attack vectors, implement secure authentication controls, and automate vulnerability scanning scripts.");
        int5.setLocation("Bangalore");
        int5.setWorkMode(WorkMode.HYBRID);
        int5.setDurationMonths(6);
        int5.setStipend(42000.0);
        int5.setOpenings(3);
        int5.setDeadline(LocalDate.now().plusMonths(2));
        int5.setMinCgpa(8.0);
        int5.setEligibleDegrees("B.Tech, M.Tech");
        int5.setEligibleBranches("CSE, IT");
        int5.setEligibleGraduationYears("2024, 2025");
        int5.setPostedBy(hrGoogle);
        int5.addRequiredSkill(new RequiredSkill(int5, "Cybersecurity", true));
        int5.addRequiredSkill(new RequiredSkill(int5, "Network Security", true));
        int5.addRequiredSkill(new RequiredSkill(int5, "Linux", true));
        int5.addRequiredSkill(new RequiredSkill(int5, "Python", false));
        internshipRepository.save(int5);

        // Internship 6: Data Analytics & Business Intelligence (Deloitte)
        Internship int6 = new Internship();
        int6.setTitle("Data Analytics & Business Intelligence Intern");
        int6.setCompanyName("Deloitte");
        int6.setCompanyLogo("https://cdn-icons-png.flaticon.com/512/5968/5968853.png");
        int6.setDomain("Data Analytics");
        int6.setDescription("Transform enterprise datasets into actionable executive dashboards, perform SQL analytical queries, build statistical KPI models, and communicate insights.");
        int6.setLocation("Remote");
        int6.setWorkMode(WorkMode.REMOTE);
        int6.setDurationMonths(3);
        int6.setStipend(35000.0);
        int6.setOpenings(10);
        int6.setDeadline(LocalDate.now().plusMonths(2));
        int6.setMinCgpa(6.5);
        int6.setEligibleDegrees("All");
        int6.setEligibleBranches("All");
        int6.setEligibleGraduationYears("All");
        int6.setPostedBy(hrMicrosoft);
        int6.addRequiredSkill(new RequiredSkill(int6, "SQL", true));
        int6.addRequiredSkill(new RequiredSkill(int6, "Data Analytics", true));
        int6.addRequiredSkill(new RequiredSkill(int6, "Pandas", false));
        internshipRepository.save(int6);

        // Internship 7: Software Engineering Systems Intern (Adobe)
        Internship int7 = new Internship();
        int7.setTitle("Core Software Engineering Systems Intern");
        int7.setCompanyName("Adobe");
        int7.setCompanyLogo("https://cdn-icons-png.flaticon.com/512/5968/5968434.png");
        int7.setDomain("Software Development");
        int7.setDescription("Develop high-performance multimedia micro-engines, optimize memory structures, and craft clean, maintainable object-oriented backend algorithms.");
        int7.setLocation("Noida");
        int7.setWorkMode(WorkMode.HYBRID);
        int7.setDurationMonths(6);
        int7.setStipend(50000.0);
        int7.setOpenings(4);
        int7.setDeadline(LocalDate.now().plusDays(60));
        int7.setMinCgpa(8.0);
        int7.setEligibleDegrees("B.Tech, M.Tech");
        int7.setEligibleBranches("CSE, IT");
        int7.setEligibleGraduationYears("2025, 2026");
        int7.setPostedBy(hrGoogle);
        int7.addRequiredSkill(new RequiredSkill(int7, "Java", true));
        int7.addRequiredSkill(new RequiredSkill(int7, "MySQL", true));
        int7.addRequiredSkill(new RequiredSkill(int7, "Git", true));
        internshipRepository.save(int7);

        // 5. Seed Demonstration Applications (Showcasing all 4 Statuses)
        // Rahul -> Google (Selected)
        Application app1 = new Application(uRahul, int1, "I have built several production-grade Spring Boot microservices and would love to contribute to Google Cloud.");
        app1.setStatus(ApplicationStatus.SELECTED);
        app1.setAdminNotes("Exceptional technical interview and outstanding skill match. Selected for Cloud Core team.");
        applicationRepository.save(app1);

        // Priya -> NVIDIA (Shortlisted)
        Application app2 = new Application(uPriya, int3, "Deeply passionate about optimizing deep learning inference and transformer workflows.");
        app2.setStatus(ApplicationStatus.SHORTLISTED);
        app2.setAdminNotes("Shortlisted for Round 2 technical interview scheduled for next week.");
        applicationRepository.save(app2);

        // Amit -> Amazon (Pending)
        Application app3 = new Application(uAmit, int2, "I enjoy creating fast, accessible web interfaces and have developed 10+ React components.");
        app3.setStatus(ApplicationStatus.PENDING);
        applicationRepository.save(app3);

        // Ananya -> Microsoft (Selected)
        Application app4 = new Application(uAnanya, int4, "Strong foundation in AWS, container orchestration with Kubernetes, and Unix automation.");
        app4.setStatus(ApplicationStatus.SELECTED);
        app4.setAdminNotes("Selected. Excellent hands-on Docker and Kubernetes knowledge.");
        applicationRepository.save(app4);

        // Vikram -> Palo Alto (Shortlisted)
        Application app5 = new Application(uVikram, int5, "Focusing on threat model simulations and cryptographic protocols.");
        app5.setStatus(ApplicationStatus.SHORTLISTED);
        app5.setAdminNotes("Top candidate on academic merit and security experience.");
        applicationRepository.save(app5);

        // Amit -> Adobe (Rejected due to CGPA requirement)
        Application app6 = new Application(uAmit, int7, "Interested in software engineering.");
        app6.setStatus(ApplicationStatus.REJECTED);
        app6.setAdminNotes("Does not meet minimum CGPA threshold (Required: 8.0, Student: 6.9).");
        applicationRepository.save(app6);

        System.out.println(">>> Smart Internship seed data initialized successfully!");
    }
}
