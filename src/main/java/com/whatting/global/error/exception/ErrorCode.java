package com.whatting.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //teacher
    Not_Excel_File_Exception(400, ".xlsx 형식의 파일만 허용됩니다"),
    Teacher_Not_Found_Exception(404, "존재하지 않는 교직원입니다"),
    Teacher_Not_Exist_Exception(404, "교직원 정보가 비어있습니다"),
    Name_Cell_Is_Empty_Exception(400, "액셀의 이름 셀이 비어있습니다"),
    Name_Already_Exist_Exception(409, "이미 존재하는 교직원 이름입니다"),

    //user
    User_Not_Found_Exception(404, "유저를 찾을 수 없습니다"),
    Student_Already_Exists_Exception(409, "이미 존재하는 학생 계정입니다"),
    Teacher_Already_Exists_Exception(409, "이미 존재하는 교사 계정입니다"),
    Invalid_Teacher_Code_Exception(403, "교사 인증 코드가 일치하지 않습니다"),
    Password_Mis_Match_Exception(400, "비밀번호가 일치하지 않습니다"),
    Refresh_Token_Not_Found_Exception(404, "refreshToken을 찾을 수 없습니다"),
    Refresh_Token_Mis_Match_Exception(403, "refreshToken 값이 저장된 값과 일치하지 않습니다"),

    //alert
    Active_Alert_Exists_Exception(409, "이미 진행 중인 경보가 있습니다"),
    Alert_Not_Found_Exception(404, "경보를 찾을 수 없습니다"),
    Alert_Already_Closed_Exception(409, "이미 종료된 경보입니다"),
    Teacher_Permission_Required_Exception(403, "교사 권한이 필요합니다"),
    Student_Permission_Required_Exception(403, "학생 권한이 필요합니다"),
    Alert_Participant_Not_Found_Exception(404, "해당 경보 대상 학생이 아닙니다"),
    Invalid_Student_Status_Exception(400, "허용되지 않은 학생 상태입니다"),

    //apply
    Invalid_Meal_Type_Exception(400, "유효하지 않은 급식 유형입니다."),
    Apply_Not_Found_Exception(404, "해당 신청 내역을 찾을 수 없습니다."),
    Already_Applied_Exception(409, "이미 해당 날짜에 동일한 급식 신청이 존재합니다.");

    private final int status;
    private final String message;
}
