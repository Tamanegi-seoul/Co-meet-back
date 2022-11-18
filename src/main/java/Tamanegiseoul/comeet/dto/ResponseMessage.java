package Tamanegiseoul.comeet.dto;

public class ResponseMessage {
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";

    public static final String FOUND_USER = "회원 정보 조회 성공";
    public static final String FOUND_CHAR = "캐릭 정보 조회 성공";
    public static final String FOUND_GUILD = "길드 정보 조회 성공";
    public static final String FOUND_COMMUNITY = "연합 정보 조회 성공";

    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String NOT_FOUND_CHAR = "캐릭을 찾을 수 없습니다.";
    public static final String NOT_FOUND_GUILD = "길드를 찾을 수 없습니다.";
    public static final String NOT_FOUND_COMMUNITY = "연합을 찾을 수 없습니다.";

    public static final String CREATED_CHAR = "캐릭 생성 성공";
    public static final String CREATED_USER = "회원 가입 성공";
    public static final String CREATED_GUILD = "길드 생성 성공";
    public static final String CREATED_COMMUNITY = "연합 생성 성공";

    public static final String JOINED_NEW_USER = "신규 회원 가입 성공";
    public static final String CREATED_NEW_POST = "신규 게시글 작성 성공";
    public static final String CREATED_NEW_COMMENT = "신규 덧글 작성 성공";


    public static final String FAIL_CREATE_USER = "회원 가입 실패";
    public static final String FAIL_CREATE_CHAR = "캐릭터 등록 실패";
    public static final String CREATE_GUILD_FAIL = "길드 등록 실패";
    public static final String CREATE_COMMUNITY_FAIL = "연합 등록 실패";


    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String UPDATE_CHAR = "캐릭 정보 수정 성공";
    public static final String UPDATE_GUILD = "길드 정보 수정 성공";
    public static final String UPDATE_COMMUNITY = "연합 정보 수정 성공";

    public static final String DELETE_USER = "회원 탈퇴 성공";
    public static final String DELETE_CHAR = "캐릭 삭제 성공";
    public static final String DELETE_GUILD = "길드 삭제 성공";
    public static final String DELETE_COMMUNITY = "연합 삭제 성공";


    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String DB_ERROR = "데이터베이스 에러";
    public static final String ILLEGAL_ARGS = "올바르지 않은 파라미터";

    public static final String RESOURCE_AVAILABLE = "자원 사용 가능";
    public static final String RESOURCE_NOT_FOUND = "존재하지 않는 자원";
    public static final String RESOURCE_UNAVAILABLE = "자원 사용 불가";

    public static final String DUPLICATE_RES = "중복된 리소스";
    public static final String DUPLICATE_NICKNAME = "중복된 닉네임";
    public static final String DUPLICATE_EMAIL = "중복된 이메일";
    public static final String DUPLICATE_CHAR_NAME = "중복된 캐릭명";
    public static final String DUPLICATE_GUILD_NAME = "중복된 길드명";
    public static final String DUPLICATE_COM_NAME = "중복된 연합명";
}
