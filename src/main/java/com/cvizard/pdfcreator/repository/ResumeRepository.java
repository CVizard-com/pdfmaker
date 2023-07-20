package com.cvizard.pdfcreator.repository;

import com.cvizard.pdfcreator.model.Resume;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends MongoRepository<Resume, String> {

}
