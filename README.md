# 프로젝트 소개 😀

---


저희 팀원들은 CS전공 스터디로 처음 모였습니다. 스터디를 진행하면서 공부한 지식을 나누고 새로운 것들에 대해 토론하며 많은 성장을 할 수 있었습니다. 이러한 스터디를 통한 함께 성장의 가치를 다른 취업준비생, 현직자들도 누릴 수 있기를 희망했습니다. 그렇게 `Co-meet`을 기획하게 되었습니다.

`Co-meet`은 개발 스터디를 운영하거나 프로젝트 개발 멤버을 모집하기 위한 커뮤니티 플랫폼입니다. 배움과 공유를 통해 멤버간 지속적 성장을 도모할 수 있는 기회와 교류의 장이 될 수 있기를 기대합니다.

---

<br>

## 팀 소개 🤼

>Team `다마네기` <br>
> - 프로젝트 총괄 및 백엔드 개발: 박준우<br>
> - 프론트엔드 개발: 김태이, 박지연, 유제빈, 진형욱<br>
> 

co-meet(코밋)을 기획/개발한 `다마네기`팀은 4명의 프론트엔드 개발자와 1명의 백엔드 개발자로 구성되어있습니다. 저희 팀은 그동안 각자 쌓아온 개발 지식들을 활용하여 실제 유저들에게 도움을 줄 수 있는 서비스를 개발하기 위해 모였습니다.

<br>

### 팀원 👈

---
| 팀원 | 역할 |
|:--:|:--|
| <img src="https://avatars.githubusercontent.com/u/10703437?s=120&v=4" width="70" height="70"> <br>[박준우](https://github.com/93jpark) |  - 프로젝트 총괄 및 리드 <br> - Hibernate JPA를 통한 엔티티 설계 및 구현 <br> - API 설계 및 명세화 <br> - Spring 기반 RestAPI 개발 <br> - Spring Security, JWT를 활용한 인증/인가 구현<br> - JUnit, Postman을 통한 테스트 실시 <br>  - AWS를 활용한 배포 및 운영 (EC2,RDS)    |
| <img src="https://avatars.githubusercontent.com/u/100752008?s=96&v=4" width="70" height="70"> <br>[진형욱](https://github.com/orgs/Tamanegi-seoul/people/huunguk) | - 메인페이지 필터링 기능, 슬라이드 개발<br> - 모달창 구현<br>  |
| <img src="https://avatars.githubusercontent.com/u/22023762?s=96&v=4" width="70" height="70"> <br>[박지연](https://github.com/orgs/Tamanegi-seoul/people/jiyeon22) |  - 글 작성 기능 구현<br> - 상세보기 페이지, 덧글 렌더링 구현<br>  |
| <img src="https://avatars.githubusercontent.com/u/106040138?s=96&v=4" width="70" height="70"> <br>[김태이](https://github.com/orgs/Tamanegi-seoul/people/taeyeess) | - 사용자 정보 수정 페이지 개발<br> - 하단메뉴 개발<br> - 이미지 업로드 기능 구현<br> - 내 작성글 페이지 구현<br>  |
| <img src="https://avatars.githubusercontent.com/u/80400157?s=96&v=4" width="70" height="70"> <br>[유제빈](https://github.com/orgs/Tamanegi-seoul/people/Yujaebin) |  - 메인페이지 주도 개발<br> - React Router 설정<br> - 회원가입, 로그인 페이지 개발<br> - 메인페이지 필터링 기능 개발<br>  |

<br>

## 프로젝트 아키텍처 🧩

<img src="/assets/images/project-architecture.jpeg" width="820" height="500">


<br>

## 프로젝트 구조 및 기술 스택 ⚒️

- Spring Boot, Spring JPA(Hibernate), Spring Security
- JWT
- MariaDB, AWS EC2 & RDS

<br>

## ERD 📊

<img src="/assets/images/comeet-db.png" width="600" height="500">


<br>

## Documents 📝

- [요구사항 명세서](https://temporal-tie-650.notion.site/26ecc9e13b114ba5908cdf308a24c7fc)

- [RestAPI 명세서](https://docs.google.com/spreadsheets/d/14jZsVFbIVOiChAX0vDx1bMsGLhW0lYa-efDRx9FVq6Y/edit#gid=0)

- [개발일지](https://docs.google.com/spreadsheets/d/1JbBsHJf1QMLOI4wpm6DCsqqa9aH1xJd8F5Pvpjw9WPM/edit#gid=0)


## 트러블 슈팅 🚀

<br>
<details><summary>Entity의 생성 - 생성자, 생성메소드, 빌더</summary>

<p>

    개발 초기, Entity클래스에서 생성자를 정의하고 new 키워드를 통해 객체를 생성하면서 해당 엔티티의 파라미터를 통해 초기화를 했었다. 하지만 이 작업은 객체를 생성하는 작업에 대해 직관적이지 않았다.

    이를 보완하기 위해 생성 메소드를 따로 만들었다. ie createMember(). 생성 메소드를 통해 해당 엔티티를 생성하는 과정을 명시적으로 행할 수 있었지만, 선택적인 필드에 대해 생성과정에서 관리하기 힘들었다. 예를 들어, Member클래스에서 프로필 이미지는 없을 수 있는 선택 항목이다. 이를 위해서 오버라이딩된 생성메소드를 정의해야만 했다.

    위의 문제를 해결하기 위해 `Builder`에 대해 알게 되었다. 생성자에 `@Builder`처리하여 동적으로 파라미터를 받을 수 있도록 하였다.
    
</p>
</details>

<details>
    <summary>Setter사용을 권하지 않는 이유</summary>
    <div markdown="1">       
    
    클래스에 @Data를 통해서 toString, Getter, Setter등을 구현할 수 있다. 엔티티를 작성함에 있어서 Setter사용을 방지하기 위해서 엔티티에 대해서 @Data를 사용하지 않고 @Getter만 사용했다.

    Setter를 사용하지 않는 이유는 엔티티에 대한 변경을 추적하기 위함이다. Setter를 사용하게 되면 변경에 대한 가능성을 열어둠으로써 객체에 대한 불변성을 보장할 수 없게 된다. 또한, 객체의 정보에 대해 수정했을 때, 해당 작업이 어떤 목적으로 이루어졌는지 직관적으로 알기 어렵다. 

    예를 들어, setAmount()를 통해 잔액을 변경하는 로직이 있다고 하자. 이 때, setAmount()가 초기 잔액을 설정하기 위함인지, 출금으로 인해 값을 변경하는 것인지 해당 메소드만을 통해서 알기 어렵다. 이러한 점을 보완하기 위해 목적에 따른 setter역할 메소드를 정의하였다. ie. initAmount(), increaseAmount(), decreaseAmount() etc.

</details>



<details>
    <summary>게시글(post) 수정 방법에 대한 고민</summary>
    <div markdown="1">       

    게시글의 필드마다 update메소드를 생성하고, 작성된 포스트를 로드해서 변경부분에 대한 update 작업을 수행하도록 했다. 하지만, 변경된 필드에 대한 개별적인 수정 메소드가 필요했다. 어떤 필드가 변경되었는지 탐지하는데에 리소스가 낭비되었다.

    따라서 변경하고자 하는 게시글의 id를 기반으로 DB에서 게시글 정보를 로드하도록 했다. 그 후, 변경된 내용을 반영한 게시글을 로드한 게시글에 덮어 씌우도록 게시글 업데이트 로직을 구성했다. 즉, post id만 동일하며 내용은 수정된 것으로 반영된다.

</details>

<details>
    <summary>Java Response 객체와 JavaScript JSON 네이밍 규칙</summary>
    <div markdown="1">       

    Java에서 Response를 반환할 때, 각 필드는 자바의 Camel Case를 따른다. 하지만 클라이언트 사이드에서는 JavaScript를 사용하며 Snake Case를 따른다. 협업에 있어서 클라이언트 개발자에게 변수 네이밍에 대한 혼동을 방지하기 위해서 Response를 반환할 때, Camel case의 각 필드를 Snake case로 변환해야 한다고 판단했다.

    이에 대해 @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)애노테이션을 통해 해결하였다.
</details>


<details>
    <summary>Service 간에 대한 의존성 발생</summary>
    <div markdown="1">       

    개발 초기, MemberService 내에서 Post/CommentService 등 다른 서비스의 메소드를 사용하도록 코드를 작성하였다. 하지만 여기서 다른 서비스를 의존하게 되면서 Circular reference issue가 발생했다.

    이후의 유사 issue를 방지하기 위해서 controller, service, repository레이어의 구분을 명확히 하였다.   service레이어에서는 필요한 repository를 참조하여 메소드를 사용하도록 코드를 작성하였다.
</details>


<details>
    <summary>이미지 파일 저장 방법에 대한 고민</summary>
    <div markdown="1">       

    회원의 프로필 이미지를 저장하는 방법에 대해 고민하게 되었다. 

    1. 원격 저장소에 이미지 파일을 저장하고 DB에는 해당 URL을 저장한다.
    2. DB에 이미지파일을 바이트 형태로 저장한다.

    a의 경우, URL형태로 저장하고 요청에 대한 응답도 URL형식으로 전송되기때문에 프론트단에서 이미지를 출력하기 편리할것으로 보였다. 또한, DB 내부에는 파일이 저장되는 것이 아니기에 용량 부하가 적다. 하지만 원격 저장소에 대한 비용이 발생한다.

    b의 경우, 원격 저장소를 사용하지 않음으로써 비용절감이 가능하다. 반면, 이미지 파일을 DB에 저장한다는 것 자체가 DB 리소스를 사용하게 된다.

    서버를 1개 더 들이는 것이 프로젝트 예산에 어려움이 있었기에 b안을 채용하기로 했다. 단, 이미지 업로드 시 파일의 크기에 제약을 걸기로 결정했다.
</details>


<details>
    <summary>발급한 토큰을 어디에 저장할 것인가</summary>
    <div markdown="1">       

    JWT토큰을 발행했을 때, 해당 토큰을 어디에 저장되어야할지 고민이 되었다.

    JWT는 브라우저 내의 안전한 저장소에 저장되어야한다. LocalStorage에 저장 될 경우, 페이지 내부의 모든 스크립트에서 액세스할 수 있기때문에 XSS 공격대상이 되거나 외부 공격자가 토큰에 접근할 수 있게 된다. 따라서 LocalStorage/SeesionStorage에 저장하는것은 좋은 방법이 아니었다. JWT는 HTTP 요청으로만 서버에 전송되는 HttpOnly 쿠키 내에 저장되어야 한다.
</details>


<details>
    <summary>언제 토큰 갱신 메소드를 호출하도록 설계할것인가</summary>
    <div markdown="1">       

    토큰 갱신을 언제 하도록 할지 설계에 있어 고민이 되었다.

    a. API콜에 대해 토큰 인증 및 유효여부만 확인하고, 만료된 경우엔 만료에 대해 response를 보내고 갱신 api를 재요청하도록 한다.
    b. 토큰 유효성 검증 후 만료된 경우엔 refresh토큰을 서버에서 자동으로 발급하여 응답에 토큰을 추가로 함께 발급한다.

    전체적인 통신의 횟수를 보았을 땐 a 방법이 좋다고 느껴졌지만, 토큰 재발급을 자동으로 하게 될 경우 보안 이슈가 있을 수 있다고 판단했다. 프론트팀과 협의하여 토큰 만료 응답을 통해 프론트에서 재요청 api를 요청하도록 설계했다.
</details>


<details>
    <summary>Axios와 GET Request body</summary>
    <div markdown="1">       

    처음 API를 설계할 때, 조회와 같은 api는 GET메소드를 기반으로 request body에 찾고자 하는 리소스 정보를 담도록 했다. 하지만 실제 프론트팀에서 개발해보니 Axios에서 GET요청에 대해서는 request body에 정보를 포함할 수 없었다. 이를 해결하기 위해 request param을 사용하는 방식으로 API를 재설계했다.
</details>


<details>
    <summary>CORS 이슈</summary>
    <div markdown="1">       

    postman에서는 정상작동하던 api들이 프론트팀의 리액트 프로그램에서 작동하지 않았고 CORS에러를 발생시켰다. CORS는 추가적인 HTTP header를 통해 어플리케이션이 다른 Origin의 리소스에 접근할 수 있도록 하는 메커니즘을 뜻한다. 다른 Origin에서 서버의 리소스에 함부로 접근하지 못하도록 설정되어 있었다.

    WebConfig에서 CORS설정을 통해 allowed origin으로 localhost와 프론트의 배포된 주소를 등록하였고, maxAge를 설정하여 preflight 결과를 캐시에 저장하도록 변경하였다.
</details>


<details>
    <summary>API 문서 수기 작성과 Swagger</summary>
    <div markdown="1">       

    프로젝트 초기단계에서 프론트팀과의 협업을 위해서 수기로 API문서를 작성하였다. 작성된 API문서를 구글 스프레드시트로 공유하였고, 개발과정에서 발생하는 이슈 및 수정/보완사항은 해당 문서를 통해 소통할 수 있었다. API가 서버 배포를 통해 동작하는 상태에서 Swagger에 대해 알게 되었으며, Swagger/OpenAPI를 통해 문서 자동화와 테스트를 도모했다.
</details>
<br>



## 추후 개선사항 ✔️

- [ ] 토큰 발행 시 Cookie를 이용한 전송(보안)
- [ ] N+1이슈 발생 여부 확인 및 보완
- [ ] https통신을 위한 SSL 인증서 발급 및 등록
- [ ] 통합 테스트 진행
- [ ] Interceptor를 활용한 ExceptionHandelr 구현



