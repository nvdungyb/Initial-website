package com.practice.from_scratch.dto.response;

import com.practice.from_scratch.entity.UserDetailsImpl;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseLoginDto {
    private UserDetailsImpl userDetails;
    private String jwt;
}
