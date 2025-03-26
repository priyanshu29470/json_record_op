package com.assignment.json_operator.models;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dataset_records")
public class Dataset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String datasetName;
    
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode jsonData;
}
