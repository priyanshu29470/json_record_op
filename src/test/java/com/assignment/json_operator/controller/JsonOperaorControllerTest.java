package com.assignment.json_operator.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.assignment.json_operator.dto.InsertRecordRes;
import com.assignment.json_operator.exceptions.DatasetNotFoundException;
import com.assignment.json_operator.service.JsonOperatorService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class JsonOperaorControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private JsonOperatorService jsonOperatorService;

    @InjectMocks
    private JsonOperaorController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void insertRecord_Success() throws Exception {
        JsonNode sampleJson = objectMapper.readTree("{\"id\": 1, \"name\": \"test\"}");
        InsertRecordRes response = new InsertRecordRes("Record added successfully", "test", 1);

        when(jsonOperatorService.insertRecord(eq("testDataset"), any(JsonNode.class)))
                .thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(post("/api/dataset/testDataset/record")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sampleJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Record added successfully"));
    }

    @Test
    void jsonOperations_Success() throws Exception {
        when(jsonOperatorService.queryDataset("testDataset", null, null, "asc"))
                .thenReturn(ResponseEntity.ok().body("Sample response"));

        mockMvc.perform(get("/api/dataset/testDataset/query"))
                .andExpect(status().isOk());
    }

    @Test
    void jsonOperations_DatasetNotFound_ShouldReturnNotFound() throws Exception {
        when(jsonOperatorService.queryDataset(anyString(), any(), any(), any()))
                .thenThrow(new DatasetNotFoundException("Dataset not found"));

        mockMvc.perform(get("/api/dataset/unknownDataset/query"))
                .andExpect(status().isNotFound());
    }
}
