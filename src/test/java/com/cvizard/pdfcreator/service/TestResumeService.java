package com.cvizard.pdfcreator.service;

import com.cvizard.pdfcreator.model.Resume;
import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TestResumeService {

    @Autowired
    private ResumeService resumeService;


    public Resume setResume() throws Exception {
        Resume resume = new Resume();

        // Populate the 'work' list
        List<Resume.Work> workList = new ArrayList<>();
        Resume.Work work = new Resume.Work();
        work.setName("ABC Company");
        work.setPosition("Software Engineer");
        work.setStartDate("2019-01-01");
        work.setEndDate("2022-03-15");
        work.setSummary("Developed and maintained web applications.");
        workList.add(work);
        resume.setWork(workList);

        // Populate the 'education' list
        List<Resume.Education> educationList = new ArrayList<>();
        Resume.Education education = new Resume.Education();
        education.setInstitution("University of XYZ");
        education.setArea("Computer Science");
        education.setStudyType("Bachelor's Degree");
        education.setStartDate("2015-09-01");
        education.setEndDate("2019-06-30");
        educationList.add(education);
        resume.setEducation(educationList);

        // Populate the 'certificates' list
        List<Resume.Certificate> certificateList = new ArrayList<>();
        Resume.Certificate certificate = new Resume.Certificate();
        certificate.setName("Java Certification");
        certificate.setDate("2020-05-20");
        certificate.setIssuer("Oracle");
        certificateList.add(certificate);
        resume.setCertificates(certificateList);

        // Populate the 'skills' list
        List<Resume.Skill> skillList = new ArrayList<>();
        Resume.Skill skill = new Resume.Skill();
        skill.setName("Java");
        skill.setLevel("Advanced");
        skill.setKeywords(Arrays.asList("Programming", "Software Development"));
        skillList.add(skill);
        resume.setSkills(skillList);

        // Populate the 'languages' list
        List<Resume.Language> languageList = new ArrayList<>();
        Resume.Language language = new Resume.Language();
        language.setLanguage("English");
        language.setLevel("Fluent");
        languageList.add(language);
        resume.setLanguages(languageList);

        // Populate the 'projects' list
        List<Resume.Project> projectList = new ArrayList<>();
        Resume.Project project = new Resume.Project();
        project.setName("E-commerce Website");
        project.setStartDate("2021-02-01");
        project.setEndDate("2021-08-15");
        project.setSummary("Designed and implemented an online store platform.");
        projectList.add(project);
        resume.setProjects(projectList);

        // Populate the 'interests' list
        List<Resume.Interest> interestList = new ArrayList<>();
        Resume.Interest interest = new Resume.Interest();
        interest.setName("Photography");
        interest.setKeywords(Arrays.asList("Landscape", "Portrait"));
        interestList.add(interest);
        resume.setInterests(interestList);

        return resume;

    }

    @Test
    public void testResumeService() throws Exception {
        // arrange
        Resume resume = setResume();
        String key = "1234";
        // act
        resumeService.createPdf(key, resume);
        File file = new File("resources/1234.pdf");
        // assert
        assertThat(file.exists()).isTrue();
        assertThat(file.getName()).isEqualTo(key + ".pdf");
        assertThat(file.length()).isGreaterThan(0);
    }




}
