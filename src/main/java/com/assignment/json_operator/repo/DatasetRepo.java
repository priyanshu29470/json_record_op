package com.assignment.json_operator.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.json_operator.models.Dataset;

public interface DatasetRepo extends JpaRepository<Dataset, UUID>{
    List<Dataset> findByDatasetName(String datasetName);
}
