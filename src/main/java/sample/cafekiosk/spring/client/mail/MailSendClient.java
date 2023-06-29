package sample.cafekiosk.spring.client.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 실제 메일 전송 처리 담당, 로깅
 * test 마다 실제 메일 전송을 할 수 없다. 따라서 Mocking 이 필요.
 * */
@Slf4j
@Component
public class MailSendClient {
    public boolean sendEmail(String from, String to, String subject, String contents) {
        log.info("메일 전송");
        throw new IllegalArgumentException("메일 전송");
    }

    public void a(){
        log.info("a");
    }

    public void b(){
        log.info("b");
    }

    public void c(){
        log.info("c");
    }
}
