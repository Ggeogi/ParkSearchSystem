package com.example.parkserchsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ParkingData {
    @JsonProperty("주차장명")
    private String parkingName;
    @JsonProperty("지번주소")
    private String address;
    @JsonProperty("관리기관명")
    private String managedBy;
}
