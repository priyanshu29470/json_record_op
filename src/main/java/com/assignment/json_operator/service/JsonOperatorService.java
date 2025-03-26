package com.assignment.json_operator.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.assignment.json_operator.dto.InsertRecordRes;
import com.assignment.json_operator.exceptions.DatasetNotFoundException;
import com.assignment.json_operator.exceptions.InvalidRequestException;
import com.assignment.json_operator.models.Dataset;
import com.assignment.json_operator.repo.DatasetRepo;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class JsonOperatorService {
    private final DatasetRepo repo;

    public JsonOperatorService(DatasetRepo repo) {
        this.repo = repo;
    }

    public ResponseEntity<InsertRecordRes> insertRecord(String datasetName, JsonNode data) {
        if (datasetName == null || datasetName.isBlank()) {
            throw new InvalidRequestException("Dataset name cannot be null or empty.");
        }
        if (data == null || !data.has("id")) {
            throw new InvalidRequestException("JSON data must contain an 'id' field.");
        }

        try {
            Dataset dataset = new Dataset();
            dataset.setDatasetName(datasetName);
            dataset.setJsonData(data);
            repo.save(dataset);
            long recordId = data.get("id").asInt();
            return ResponseEntity.ok(new InsertRecordRes("Record added successfully", datasetName, recordId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InsertRecordRes("Error inserting record: " + e.getMessage(), datasetName, -1));
        }
    }

    public ResponseEntity<Object> queryDataset(String datasetName, String groupBy, String sortBy, String order) {
        if (datasetName == null || datasetName.isBlank()) {
            throw new InvalidRequestException("Dataset name cannot be null or empty.");
        }

        List<Dataset> datasets = repo.findByDatasetName(datasetName);
        if (datasets.isEmpty()) {
            throw new DatasetNotFoundException("No dataset found with name: " + datasetName);
        }

        List<JsonNode> jsonRecords = datasets.stream()
                .map(Dataset::getJsonData)
                .collect(Collectors.toList());

        if (groupBy != null && !groupBy.isBlank()) {
            return groupByField(jsonRecords, groupBy);
        } else if (sortBy != null && !sortBy.isBlank()) {
            return sortByField(jsonRecords, sortBy, order);
        }

        return ResponseEntity.ok(Map.of("records", jsonRecords));
    }

    private ResponseEntity<Object> groupByField(List<JsonNode> records, String groupBy) {
        Map<String, List<JsonNode>> groupedRecords = records.stream()
                .collect(Collectors.groupingBy(record -> record.has(groupBy) ? record.get(groupBy).asText() : "Unknown"));

        return ResponseEntity.ok(Map.of("groupedRecords", groupedRecords));
    }

    private ResponseEntity<Object> sortByField(List<JsonNode> records, String sortBy, String order) {
        if (records.stream().noneMatch(record -> record.has(sortBy))) {
            throw new InvalidRequestException("Sort field does not exist");
        }
        List<JsonNode> sortedRecords = records.stream()
                .sorted((a, b) -> {
                    if (!a.has(sortBy) || !b.has(sortBy)) return 0;
                    try {
                        int compare = a.get(sortBy).asInt() - b.get(sortBy).asInt();
                        return "desc".equalsIgnoreCase(order) ? -compare : compare;
                    } catch (Exception e) {
                        throw new InvalidRequestException("Sorting error: Invalid field type for sorting.");
                    }
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("sortedRecords", sortedRecords));
    }
}
