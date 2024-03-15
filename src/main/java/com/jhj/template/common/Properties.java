package com.jhj.template.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {

  @Value("${spring.profiles.active}")
  public String profilesActive;

  @Value("${server.port}")
  public String serverPort;

  @Value("${server.tomcat.basedir}")
  public String basedir;

}
