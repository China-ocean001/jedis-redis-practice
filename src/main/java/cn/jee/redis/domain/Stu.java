package cn.jee.redis.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Stu {
  private long id;
  private String username;
  private double java,math;
}
