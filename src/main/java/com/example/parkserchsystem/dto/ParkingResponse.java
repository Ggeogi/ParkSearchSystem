package com.example.parkserchsystem.dto;

import lombok.Data;
import java.util.List;

@Data
public class ParkingResponse {
    private List<ParkingData> data;
}