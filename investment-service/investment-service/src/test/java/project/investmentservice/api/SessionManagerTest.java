package project.investmentservice.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import project.investmentservice.domain.Hello;

public class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    public void sessionTest() {
        //세션 생성 및 Http 응답을 받고, 세션을 쿠키에 담고, response에 쿠키가 담김
        MockHttpServletResponse response = new MockHttpServletResponse();
        Hello hello = new Hello();
        sessionManager.createSession(hello, response);

        //요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        //세션 조회, 클라이언트에서 서버로 다시 요청
        Object result = sessionManager.getSession(request);
        Assertions.assertThat(result).isEqualTo(hello);

        //세션 만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        Assertions.assertThat(expired).isNull();
    }
}
