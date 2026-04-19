package com.example.parkserchsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parking_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ParkingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parking_name", nullable = false)
    private String parkingName;

    private String address;
    private Double latitude;
    private Double longitude;

    @Column(name = "is_rotation_target")
    private boolean isRotationTarget; // 33개소 해당 여부
}