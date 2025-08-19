package org.example;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Viktor Shvidkiy
 */

@Data
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private LocalDateTime createdAt;
}
