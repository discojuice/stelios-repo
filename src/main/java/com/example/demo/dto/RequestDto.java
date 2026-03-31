package com.example.demo.dto;

import lombok.*;

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

@Setter
@Getter
public class RequestDto {

    private Long id;
    private String question;
    private String answer;
    private Date createdOn;
    private String department;
    private String category;
    private String requestNo;

    public RequestDto() {
    }

//    public RequestDto(Long id, String question, String answer, Date createdOn, String category, String department) {
//        this.id = id;
//        this.question = question;
//        this.answer = answer;
//        this.createdOn = createdOn;
//        this.department = "dasdsa";
//        this.category = "asdsasdaasdasasdas";
//    }

//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setQuestion(String question) {
//        this.question = question;
//    }
//
//    public void setAnswer(String answer) {
//        this.answer = answer;
//    }
//
//    public void setCreatedOn(Date createdOn) {
//        this.createdOn = createdOn;
//    }
//
//
//    public void setDepartment(String department) {
//        this.department = department;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
}
