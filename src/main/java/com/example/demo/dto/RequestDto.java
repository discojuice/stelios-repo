package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class RequestDto {
//    private Long id;
//    private String question;
//    private String answer;
//    private Date createdOn;
//}

public class RequestDto {

    private Long id;
    private String question;
    private String answer;
    private Date createdOn;

    public RequestDto() {
    }

    public RequestDto(Long id, String question, String answer, Date createdOn) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.createdOn = createdOn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
