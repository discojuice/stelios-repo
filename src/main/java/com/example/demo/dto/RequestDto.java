package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private Long id;
    private String question;
    private String answer;
}

//public class RequestDto {
//
//    private Long id;
//    private String question;
//    private String answer;
//
//    public RequestDto() {
//    }
//
//    public RequestDto(Long id, String question, String answer) {
//        this.id = id;
//        this.question = question;
//        this.answer = answer;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getQuestion() {
//        return question;
//    }
//
//    public void setQuestion(String question) {
//        this.question = question;
//    }
//
//    public String getAnswer() {
//        return answer;
//    }
//
//    public void setAnswer(String answer) {
//        this.answer = answer;
//    }
//}
