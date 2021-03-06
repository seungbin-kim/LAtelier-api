package com.latelier.api.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    METHOD_NOT_ALLOWED(405, "C001", "Method Not Allowed"),
    INVALID_INPUT_VALUE(400, "C002", "입력값이 잘못되었습니다."),
    INVALID_TOKEN(401, "C003", "권한이 없습니다."),
    //  UNAUTHORIZED(401, "C004", "권한이 없습니다."),
    INSUFFICIENT_SCOPE(403, "C005", "권한이 부족합니다."),
    NOT_FOUND(404, "C006", "찾을 수 없습니다."),
    //  DUPLICATION(409, "C007", "Duplicate"),
    INCORRECT_FORMAT(400, "C008", "잘못된 형식입니다."),
    INTERNAL_SERVER_ERROR(500, "C050", "Server Error"),

    // Member
    MEMBER_NOT_FOUND(404, "M001", "사용자를 찾을 수 없습니다."),
    LOGIN_INPUT_INVALID(400, "M002", "이메일 혹은 비밀번호가 틀립니다."),
    SMS_VERIFICATION_FAILED(400, "M003", "SMS 인증 실패"),
    EMAIL_DUPLICATE(409, "M004", "이메일이 중복됩니다."),
    PHONE_NUMBER_DUPLICATE(409, "M005", "휴대폰 번호가 중복됩니다."),
    EMAIL_AND_PHONE_NUMBER_DUPLICATE(409, "M006", "이메일과 휴대폰 번호가 중복됩니다."),
    EMAIL_NOT_FOUND(404, "M007", "이메일을 찾을 수 없습니다."),
    NOT_ACTIVATED(404, "M010", "비활성화 된 사용자입니다."),
    CART_DUPLICATE(409, "M011", "이미 장바구니에 있습니다."),
    CART_NOT_FOUND(404, "M012", "장바구니에 존재하지 않는 요소입니다."),
    ALREADY_ENROLLED(400, "M013", "이미 구매한 강의입니다."),

    // Course
    COURSE_NOT_FOUND(404, "CS001", "강의를 찾을 수 없거나 승인된 강의가 아닙니다."),
    COURSE_MEETING_NOT_FOUND(404, "CS002", "강의 진행정보가 없습니다."),
    COURSE_STATE_ALREADY_APPROVED(400, "CS003", "이미 승인된 강의입니다."),
    COURSE_MEETING_ALREADY_EXIST(409, "CS004", "이미 현재 진행중인 강의가 있습니다."),
    COURSE_INSTRUCTOR_NOT_MATCH(400, "CS005", "강사님의 강의가 아닙니다."),
    NOT_ENROLLED(400, "CS006", "해당 강의에 등록되지 않았습니다."),
    COURSE_STUDENT_EMPTY(400, "CS007", "수강생이 단 한명도 없습니다."),
    COURSE_MAX_HEAD_COUNT_EXCEEDED(400, "CS008", "최대 수강인원을 초과합니다."),
    COURSE_PERIOD_ENDED(400, "CS009", "종료된 강의입니다."),

    // Payment
    IAMPORT_ERROR(500, "P001", "잘못된 impUid 등으로 인해 결제 검증시 API 호출 에러. 관리자 문의 필요"),
    MEMBER_NOT_MATCH(400, "P002", "로그인 유저와 결제 유저가 달라 결제 취소처리 되었습니다."),
    PAYMENT_FORGERY(400, "P003", "위조된 결제. 결제된 금액과 주문 금액이 달라 결제가 취소되었습니다."),
    PAYMENT_CANCEL(400, "P004", "결제중 수강인원 초과 혹은 강의 종료일이 지난 항목이 있어 결제가 취소되었습니다."),

    // CHAT
    CHAT_ROOM_NOT_FOUND(404, "CH001", "채팅방을 찾을 수 없습니다."),

    // FILE
    FILE_UPLOAD_SIZE_EXCEEDED(413, "F001", "최대 업로드 크기를 초과합니다."),
    FILE_NOT_EXISTED(400, "F004", "파일이 존재하지 않습니다."),
    FILE_IO_FAILED(500, "F003", "파일 입출력 실패."),

    // Zoom
    ACCESS_TOKEN_NOT_OBTAIN(500, "Z001", "ZOOM 액세스 토큰을 얻을 수 없습니다."),
    MEETING_INFORMATION_NOT_OBTAIN(500, "Z002", "회의 정보를 얻을 수 없습니다."),
    ACCESS_TOKEN_REQUEST_FAILED(500, "Z003", "ZOOM 액세스 토큰 요청에 실패하였습니다."),
    ZOOM_API_CALL_FAILED(500, "Z004", "ZOOM API 요청에 실패하였습니다."),

    // Naver
    SMS_API_CALL_FAILED(500, "N001", "SMS API 요청에 실패하였습니다.");

    private final int status;

    private final String code;

    private final String message;


}
