package Tamanegiseoul.comeet.dto;

public class ResponseMessage {
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";
    public static final String NOT_ALLOWED = "권한 없음";

    public static final String FOUND_USER = "회원 조회 성공";
    public static final String FOUND_POST = "게시글 조회 성공";
    public static final String FOUND_COMMENT = "덧글 조회 성공";

    public static final String NOT_FOUND_USER = "회원 조회 실패";
    public static final String NOT_FOUND_POST = "게시글 조회 실패";
    public static final String NOT_FOUND_COMMENT = "덧글 조회 실패";

    public static final String CREATED_USER = "회원 가입 성공";
    public static final String CREATED_POST = "게시글 작성 성공";
    public static final String CREATED_COMMNET = "덧글 작성 성공";

    public static final String FAIL_CREATE_USER = "회원 가입 실패";
    public static final String FAIL_CREATE_POST = "게시글 작성 실패";
    public static final String FAIL_CREATE_COMMENT = "덧글 작성 실패";


    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String UPDATE_POST = "게시글 수정 성공";
    public static final String UPDATE_COMMNET = "덧글 수정 성공";

    public static final String DELETE_USER = "회원 탈퇴 성공";
    public static final String DELETE_POST = "게시글 삭제 성공";
    public static final String DELETE_COMMENT = "덧글 삭제 성공";


    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String DB_ERROR = "데이터베이스 에러";
    public static final String ILLEGAL_ARGS = "올바르지 않은 파라미터";

    public static final String RESOURCE_AVAILABLE = "자원 사용 가능";
    public static final String RESOURCE_NOT_FOUND = "존재하지 않는 자원";
    public static final String RESOURCE_UNAVAILABLE = "자원 사용 불가";

    public static final String DUPLICATE_RES = "중복된 리소스";
    public static final String DUPLICATE_NICKNAME = "중복된 닉네임";
    public static final String DUPLICATE_EMAIL = "중복된 이메일";
}
