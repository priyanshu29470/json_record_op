package com.assignment.json_operator.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsertRecordRes {
    private String message;
    private String dataset;
    private long recordId;
}
