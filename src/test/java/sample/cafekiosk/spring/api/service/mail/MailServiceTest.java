package sample.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

// @Mock 어노테이션을 사용해 Mock 객체를 생성하여 테스트 하기 위해 @ExtendWith(MockitoExtension.class) 을 사용.
@ExtendWith(MockitoExtension.class)
// @SpringBootTest 를 사용하지 않고 @MockBean 을 사용하여 단위테스트를 진행.
class MailServiceTest {

    @Spy
    MailSendClient mailSendClient;
    @Mock
    MailSendHistoryRepository mailSendHistoryRepository;
    // @InjectMocks : MailService 의 생성자를 참고해 Mock 객체를 DI 해준다.
    @InjectMocks
    MailService mailService;

    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
        // given
        // @Mock 어노테이션으로 대체
//        MailSendClient mailSendClient = Mockito.mock(MailSendClient.class);
//        MailSendHistoryRepository mailSendHistoryRepository = Mockito.mock(MailSendHistoryRepository.class);
        // @InjectMocks 어노테이션으로 대체
//        MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

        // @Mock 을 사용한 경우
//        Mockito.when(mailSendClient.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
//                .thenReturn(true);
        // @Spy 를 사용한 경우
        doReturn(true)
                .when(mailSendClient)
                .sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());


        // save 행위에 대해 Mocking 하지 않아도,
        // Mockito 의 public static final Answer<Object> RETURNS_DEFAULTS = Answers.RETURNS_DEFAULTS; 설정에 의해
        // save 메서드의 default 반환 값인 null 이 반환된다.
//        Mockito.when(mailSendHistoryRepository.save(Mockito.any(MailSendHistory.class)))
//                .thenReturn(null);


        // when
        boolean result = mailService.sendMail("from", "to", "subject", "contents");
        // then
        assertThat(result).isTrue();
        Mockito.verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
    }

}