package com.study.springboot.dto;

import java.util.Date;
import lombok.Data;

@Data
public class AnswerLatestDTO {
  private String q_no;
  private String a_no;      
  private String a_content;
  private Date a_date;
}

