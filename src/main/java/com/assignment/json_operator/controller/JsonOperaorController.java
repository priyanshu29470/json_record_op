package com.assignment.json_operator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.json_operator.dto.InsertRecordRes;
import com.assignment.json_operator.service.JsonOperatorService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/dataset/{datasetName}")
public class JsonOperaorController {

    @Autowired
    private JsonOperatorService jsonOperatorService;

    @PostMapping("/record")
    public ResponseEntity<InsertRecordRes> insertRecord(
            @PathVariable String datasetName,
            @RequestBody JsonNode data) {
       return jsonOperatorService.insertRecord(datasetName, data);
    }

    @GetMapping("/query")
    public ResponseEntity<Object> jsonOperations(
            @PathVariable String datasetName,
            @RequestParam(required = false) String groupBy,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        return jsonOperatorService.queryDataset(datasetName, groupBy, sortBy, order);

    }
    
}
