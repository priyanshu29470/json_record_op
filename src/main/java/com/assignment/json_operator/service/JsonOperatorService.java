package com.assignment.json_operator.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.assignment.json_operator.dto.InsertRecordRes;
import com.assignment.json_operator.models.Dataset;
import com.assignment.json_operator.repo.DatasetRepo;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class JsonOperatorService {
    private final DatasetRepo repo;

    public JsonOperatorService(DatasetRepo repo){
        this.repo = repo;
    }
    

    public ResponseEntity<InsertRecordRes> insertRecord(String datasetName, JsonNode data){
        Dataset dataset = new Dataset();
        dataset.setDatasetName(datasetName);
        dataset.setJsonData(data);
        repo.save(dataset);
        long recordId = data.get("id").asInt();
        return ResponseEntity.ok(new InsertRecordRes("Record added successfully", datasetName, recordId));    
    }

    public ResponseEntity<Object> queryDataset(String datasetName, String groupBy, String sortBy, String order) {
        List<Dataset> datasets = repo.findByDatasetName(datasetName);
        List<JsonNode> jsonRecords = datasets.stream()
                .map(Dataset::getJsonData)
                .collect(Collectors.toList());
        if (groupBy != null && !groupBy.isEmpty()) {
            return groupByField(jsonRecords, groupBy);
        } else if (sortBy != null && !sortBy.isEmpty()) {
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
        List<JsonNode> sortedRecords = records.stream()
                .sorted((a, b) -> {
                    if (!a.has(sortBy) || !b.has(sortBy)) return 0;
                    int compare = a.get(sortBy).asInt() - b.get(sortBy).asInt();
                    return "desc".equalsIgnoreCase(order) ? -compare : compare;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("sortedRecords", sortedRecords));
    }

}
