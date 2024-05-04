package com.hana.app.data.dto;


import com.hana.app.common.type.UType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String email;
    private int userId; //DB에서 자동 생성
    private String nickname;
    private String gender;
    private boolean isAdmin;
    private UType status;
    private LocalDateTime suspendedDate;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

}