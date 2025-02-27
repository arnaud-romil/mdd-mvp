package com.orion.mdd_api.dtos;

import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Data Transfer Object for User. */
@Setter
@Getter
@NoArgsConstructor
public class UserDto {

  private String username;
  private String email;
  private Set<TopicDto> topics;
}
