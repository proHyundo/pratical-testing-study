package sample.cafekiosk.spring.api.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

/**
 * 메일 발송 성공 시 발송 내역 테이블 저장
 * */
@RequiredArgsConstructor
@Service
public class MailService {

        private final MailSendClient mailSendClient;
        private final MailSendHistoryRepository mailSendHistoryRepository;

        public boolean sendMail(String from, String to, String subject, String contents) {
                boolean sendResult = mailSendClient.sendEmail(from, to, subject, contents);
                if (sendResult) {
                        mailSendHistoryRepository.save(MailSendHistory.builder()
                                .fromEmail(from)
                                .toEmail(to)
                                .subject(subject)
                                .content(contents)
                                .build());

                        // 만약, mailSendClient 의 특정 메서드만 Mocking 하고, 나머지 메서드는 실제 메서드를 호출하고 싶다면 Spy 객체를 사용한다.
                        mailSendClient.a();
                        mailSendClient.b();
                        mailSendClient.c();

                        return true;


                }
                return false;
        }
}
