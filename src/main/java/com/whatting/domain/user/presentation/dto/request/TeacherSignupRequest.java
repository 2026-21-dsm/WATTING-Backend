package com.whatting.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeacherSignupRequest {
    @NotBlank(message = "학교 이름은 필수입니다")
    private String schoolName;

    @NotBlank(message = "이름은 필수입니다")
    @Size(min = 2, max = 20, message = "이름은 2 ~ 20자 범위의 값을 요구합니다")
    private String name;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 20, message = "비밀번호는 8 ~ 20자 범위의 값을 요구합니다")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).+$",
            message = "비밀번호는 영문, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다"
    )
    private String password;

    @NotBlank(message = "교사 인증 코드는 필수입니다")
    private String teacherCode;
}
