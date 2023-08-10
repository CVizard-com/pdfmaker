package com.cvizard.pdfcreator.controller;

import com.cvizard.pdfcreator.model.Resume;
import com.cvizard.pdfcreator.repository.ResumeRepository;
import com.cvizard.pdfcreator.service.ResumeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
import org.springframework.core.io.Resource;

import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TestResumeController {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ResumeRepository resumeRepository;

    @MockBean
    private ResumeService resumeService;

    @BeforeEach
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
    public void canRetrieveNonExistingPdfFile() throws Exception {

        // arrange
        String key = "1234";
        given(resumeRepository.findById(key)).willReturn(Optional.empty());


        // act
        MockHttpServletResponse response = mvc.perform(
                get("/api/maker/download?key=1234")
                        .accept(MediaType.APPLICATION_PDF))
                .andReturn().getResponse();

        // assure
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentAsString()).isEmpty();

    }

    @Test
    public void canRetrieveExistingPdfFile() throws Exception {

        // arrange
        String key = "1234";
        Resume resume = setResume();
        resume.setId(key);

        given(resumeRepository.findById(key)).willReturn(Optional.of(resume));

        // act
        MockHttpServletResponse response = mvc.perform(
                        get("/api/maker/download?key=1234")
                                .accept(MediaType.APPLICATION_PDF))
                .andReturn().getResponse();

        // assure
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isNotEmpty();
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_PDF_VALUE );
        assertThat(response.getContentLength()).isGreaterThan(0);

    }

    @Test
    public void canRetrieveWithoutKey() throws Exception {
        // arrange
        String key = "1234";
        given(resumeRepository.findById(key)).willReturn(null);


        // act
        MockHttpServletResponse response = mvc.perform(
                        get("/api/maker/download")
                                .accept(MediaType.APPLICATION_PDF))
                .andReturn().getResponse();

        // assure
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEmpty();
}
}
