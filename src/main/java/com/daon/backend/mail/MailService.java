package com.daon.backend.mail;

import com.daon.backend.common.redis.RedisRepository;
import com.daon.backend.member.domain.AlreadyExistsEmailException;
import com.daon.backend.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;

@RequiredArgsConstructor
@Transactional
@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final MemberRepository memberRepository;
    private final RedisRepository redisRepository;

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    /**
     * 인증 메일 전송
     */
    public void sendCodeToEmail(String toEmail) {
        if (memberRepository.existsByEmail(toEmail)) {
            throw new AlreadyExistsEmailException(toEmail);
        }

        String title = "Daon 이메일 인증 번호입니다.";
        String authCode = createCode();

        sendEmail(toEmail, title, authCode);
        redisRepository.set(AUTH_CODE_PREFIX + toEmail, authCode, Duration.ofMillis(authCodeExpirationMillis));
    }

    private String createCode() {
        int length = 6;

        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new UnableToSendEmailException();
        }
    }

    private void sendEmail(String toEmail, String title, String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);

        try {
            mailSender.send(emailForm);
        } catch (RuntimeException e) {
            throw new UnableToSendEmailException();
        }
    }

    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }

    /**
     * 인증 메일 검증
     */
    public VerificationEmailResponseDto verifiedCode(String email, String code) {
        if (checkCorrectCode(email, code)) {
            redisRepository.remove(AUTH_CODE_PREFIX + email);
            return new VerificationEmailResponseDto(true);
        }
        return new VerificationEmailResponseDto(false);
    }

    private boolean checkCorrectCode(String email, String code) {
        try {
            return redisRepository.get(AUTH_CODE_PREFIX + email).equals(code);
        } catch (Exception e) {
            throw new EmailVerificationTimeExpireException(email);
        }
    }
}
