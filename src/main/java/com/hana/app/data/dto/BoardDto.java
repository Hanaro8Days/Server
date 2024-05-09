package com.hana.app.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {
    private int boardId;
    private String name;
    private String content;
    private String img;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public String getCreateDate() {
        if (createDate == null) {
            return ""; // createDate가 null인 경우 빈 문자열 반환
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH시 mm분");
        return createDate.plusHours(9).format(formatter);
    }
}
