package com.whatting.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TeacherSignupRequest(
        @NotBlank(message = "학교 이름은 필수입니다")
        @Size(max = 100, message = "학교 이름은 최대 100자까지 입력할 수 있습니다")
        String schoolName,

        @NotBlank(message = "이름은 필수입니다")
        @Size(min = 2, max = 50, message = "이름은 2 ~ 50자 범위의 값을 요구합니다")
        String name,

        @NotBlank(message = "비밀번호는 필수입니다")
        @Size(min = 8, max = 20, message = "비밀번호는 8 ~ 20자 범위의 값을 요구합니다")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).+$",
                message = "비밀번호는 영문, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다"
        )
        String password,

        @NotBlank(message = "교사 인증 코드는 필수입니다")
        String teacherCode
) {
}
