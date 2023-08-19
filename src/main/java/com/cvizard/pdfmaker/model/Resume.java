package com.cvizard.pdfmaker.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@NoArgsConstructor
@Data
@Document("cvizard")
@Builder
@AllArgsConstructor
public class Resume {
    @MongoId
    public String id;
    public ResumeStatus status;
    public List<Work> work;
    public List<Education> education;
    public List<Certificate> certificates;
    public List<Skill> skills;
    public List<Language> languages;
    public List<Project> projects;
    public List<Interest> interests;

    @NoArgsConstructor
    @Data
    public static class Work{
        public String name;
        public String position;
        public String startDate;
        public String endDate;
        public String summary;
    }

    @NoArgsConstructor
    @Data
    public static class Education{
        public String institution;
        public String area;
        public String studyType;
        public String startDate;
        public String endDate;
    }

    @NoArgsConstructor
    @Data
    public static class Certificate{
        public String name;
        public String date;
        public String issuer;
    }

    @NoArgsConstructor
    @Data
    public static class Skill{
        public String name;
        public String level;
        public List<String> keywords;
    }

    @NoArgsConstructor
    @Data
    public static class Language{
        public String language;
        public String level;
    }

    @NoArgsConstructor
    @Data
    public static class Project{
        public String name;
        public String startDate;
        public String endDate;
        public String summary;
    }

    @NoArgsConstructor
    @Data
    public static class Interest{
        public String name;
        public List<String> keywords;
    }


}
