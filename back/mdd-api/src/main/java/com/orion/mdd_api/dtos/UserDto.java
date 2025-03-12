package com.orion.mdd_api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Data Transfer Object for User. */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Data transfer object representing a User.")
public class UserDto {

  private String username;
  private String email;
  private Set<TopicDto> topics;
}
