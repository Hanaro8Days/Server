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
public class ReportedPostDto {
    private int reportedPostId;
    private int userId; // 신고자 정보
    private UserDto writerDto; // Post 작성자 정보
    private PostDto postDto;
    private int total;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private ReportCategoryDto reportCategoryDto;
    private String status; // 활성 : Active , 완료 : Completed

    // 신고 받은 각 category 개수
    private int cntCategory1;
    private int cntCategory2;
    private int cntCategory3;
    private int cntCategory4;
    private int cntCategory5;
    private int cntCategory6;
    private int cntCategory7;

    public String getCreateDate() {
        if (createDate == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH시 mm분");
        return createDate.plusHours(9).format(formatter);
    }
}
