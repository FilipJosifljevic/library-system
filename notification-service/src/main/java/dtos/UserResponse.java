package dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public record UserResponse(
		Long id,
        String firstName,
        String lastName,
        String email
){}
