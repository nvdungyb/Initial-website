package com.practice.from_scratch.dto.response.v2;

import com.practice.from_scratch.dto.response.TokenDto;
import com.practice.from_scratch.entity.UserDetailsImpl;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseLoginDtoV2 {
    private UserDetailsImpl userDetails;
    private TokenDto tokens;
}
