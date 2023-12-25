package com.daon.backend.mail.infrastructure;

import com.daon.backend.common.redis.RedisRepository;
import com.daon.backend.mail.service.EmailVerificationTimeExpireException;
import com.daon.backend.mail.service.IncorrectMailCheckCodeException;
import com.daon.backend.mail.service.MailService;
import com.daon.backend.mail.service.UnableToSendEmailException;
import com.daon.backend.member.domain.AlreadyExistsEmailException;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final MemberRepository memberRepository;
    private final RedisRepository redisRepository;
    private final TaskRepository taskRepository;

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    /**
     * 인증 메일 전송
     */
    @Override
    public void sendCodeToEmail(String toEmail) {
        if (memberRepository.existsByEmail(toEmail)) {
            throw new AlreadyExistsEmailException(toEmail);
        }

        String title = "[다온] 이메일 인증 번호입니다.";
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
        MimeMessagePreparator preparatory = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(title);
            helper.setText(text, true);
        };

        try {
            mailSender.send(preparatory);
        } catch (RuntimeException e) {
            throw new UnableToSendEmailException();
        }
    }

    /**
     * 인증 메일 검증
     */
    @Override
    public void verifiedCode(String email, String code) {
        if (!checkCorrectCode(email, code)) {
            throw new IncorrectMailCheckCodeException(email, code);
        }
        redisRepository.remove(AUTH_CODE_PREFIX + email);
    }

    private boolean checkCorrectCode(String email, String code) {
        try {
            return redisRepository.get(AUTH_CODE_PREFIX + email).equals(code);
        } catch (Exception e) {
            throw new EmailVerificationTimeExpireException(email);
        }
    }

    /**
     * 할 일 D-3 메일 전송
     * 전송 시간 : 매일 오전 7시
     */
    @Scheduled(cron = "0 0 7 * * *")
    public void sendEmailForTasksLessThanThreeDaysOld() {
        List<Task> findTasks = taskRepository.findTasksForLessThanThreeDaysOld();
        if (!findTasks.isEmpty()) {
            Map<String, Map<String, Map<String, List<Task>>>> tasksForEmail = findTasks.stream()
                    .collect(Collectors.groupingBy(
                            task -> task.getTaskManager().getWorkspaceParticipant().getProfile().getEmail(),
                            Collectors.groupingBy(
                                    task -> task.getBoard().getProject().getWorkspace().getTitle(),
                                    Collectors.groupingBy(
                                            task -> task.getBoard().getProject().getTitle(),
                                            Collectors.toList()
                                    )
                            )
                    ));

            String subject = "[다온] D-3 이하의 할 일 목록 알림입니다.";
            tasksForEmail.forEach((toEmail, tasks) -> {
                String text = buildEmailText(tasks);
                sendEmail(toEmail, subject, text);
            });
        }
    }

    private String buildEmailText(Map<String, Map<String, List<Task>>> tasksForEmail) {
        StringBuilder text = new StringBuilder();
        text
                .append("<html><body>")
                .append("<p><strong>** 아래 형식을 참고해 주세요. **</strong></p>")
                .append("<li> [ 워크스페이스 제목 ] </li>")
                .append("<ul><li> [ 프로젝트 제목 ] </li>")
                .append("<ul><li> 할 일 제목 (D-day) </li></ul></ul>")
                .append("<br> <hr> <br>");

        tasksForEmail.forEach((workspaceTitle, projects) -> {
            text.append("<li> [ ").append(workspaceTitle).append(" ]</li>");
            text.append("<ul>");
            projects.forEach((projectTitle, tasks) -> {
                text.append("<li>&emsp;[ ").append(projectTitle).append(" ]</li>");
                text.append("<ul>");
                for (Task task : tasks) {
                    text.append(String.format("<li>&emsp;%s (%s)</li>", task.getTitle(), calculateDDay(task.getEndDate())));
                }
                text.append("</ul>");
            });
            text.append("</ul>\n");
        });
        text.append("</body></html>");

        return text.toString();
    }

    private String calculateDDay(LocalDateTime endDate) {
        LocalDate today = LocalDate.now();
        LocalDate endDay = endDate.toLocalDate();

        long daysDifference = ChronoUnit.DAYS.between(today, endDay);

        if (daysDifference == 0) {
            return "D-day";
        } else if (daysDifference == 1) {
            return "D-1";
        } else if (daysDifference == 2) {
            return "D-2";
        } else {
            return "D-3";
        }
    }
}
