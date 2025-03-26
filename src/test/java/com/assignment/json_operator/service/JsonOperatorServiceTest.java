package com.assignment.json_operator.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.assignment.json_operator.dto.InsertRecordRes;
import com.assignment.json_operator.exceptions.DatasetNotFoundException;
import com.assignment.json_operator.exceptions.InvalidRequestException;
import com.assignment.json_operator.models.Dataset;
import com.assignment.json_operator.repo.DatasetRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class JsonOperatorServiceTest {

    @Mock
    private DatasetRepo repo;

    @InjectMocks
    private JsonOperatorService service;

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private JsonNode sampleJson;
    private Dataset sampleDataset;

    @BeforeEach
    void setUp() throws Exception {
        sampleJson = objectMapper.readTree("{\"id\": 1, \"name\": \"test\"}");
        sampleDataset = new Dataset();
        sampleDataset.setDatasetName("testDataset");
        sampleDataset.setJsonData(sampleJson);
    }

    @Test
    void insertRecord_Success() {
        when(repo.save(any(Dataset.class))).thenReturn(sampleDataset);
        ResponseEntity<InsertRecordRes> response = service.insertRecord("testDataset", sampleJson);
        assertEquals(200, response.getStatusCode().value()); 
        assertEquals("Record added successfully", response.getBody().getMessage());
    }

    @Test
    void insertRecord_NullDatasetName_ShouldThrowException() {
        assertThrows(InvalidRequestException.class, () -> service.insertRecord(null, sampleJson));
    }

    @Test
    void insertRecord_EmptyDatasetName_ShouldThrowException() {
        assertThrows(InvalidRequestException.class, () -> service.insertRecord("", sampleJson));
    }

    @Test
    void insertRecord_NullJson_ShouldThrowException() {
        assertThrows(InvalidRequestException.class, () -> service.insertRecord("testDataset", null));
    }

    @Test
    void insertRecord_JsonWithoutId_ShouldThrowException() {
        JsonNode invalidJson = objectMapper.createObjectNode().put("name", "test");
        assertThrows(InvalidRequestException.class, () -> service.insertRecord("testDataset", invalidJson));
    }

    @Test
    void queryDataset_Success() {
        when(repo.findByDatasetName("testDataset")).thenReturn(List.of(sampleDataset));
        ResponseEntity<Object> response = service.queryDataset("testDataset", null, null, null);
        assertEquals(200, response.getStatusCode().value()); 
    }

    @Test
    void queryDataset_NotFound_ShouldThrowException() {
        when(repo.findByDatasetName("testDataset")).thenReturn(List.of());
        assertThrows(DatasetNotFoundException.class, () -> service.queryDataset("testDataset", null, null, null));
    }

    @Test
    void queryDataset_GroupByField() {
        when(repo.findByDatasetName("testDataset")).thenReturn(List.of(sampleDataset));
        ResponseEntity<Object> response = service.queryDataset("testDataset", "name", null, null);
        assertEquals(200, response.getStatusCode().value()); 
    }

    @Test
    void queryDataset_SortByField() {
        when(repo.findByDatasetName("testDataset")).thenReturn(List.of(sampleDataset));
        ResponseEntity<Object> response = service.queryDataset("testDataset", null, "id", "asc");
        assertEquals(200, response.getStatusCode().value()); 
    }

    @Test
    void queryDataset_InvalidSortField_ShouldThrowException() {
        when(repo.findByDatasetName("testDataset")).thenReturn(List.of(sampleDataset));
        assertThrows(InvalidRequestException.class, () -> service.queryDataset("testDataset", null, "unknownField", "asc"));
    }
}
