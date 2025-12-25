package com.pocketsiem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThreatReportRequest {
    
    @NotBlank(message = "App name cannot be blank")
    @Size(min = 1, max = 255)
    private String appName;
    
    @NotBlank(message = "Target IP cannot be blank")
    @Pattern(regexp = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$", 
             message = "Invalid IP address format")
    private String targetIp;
    
    @NotNull(message = "Timestamp cannot be null")
    private Long timestamp;
}
