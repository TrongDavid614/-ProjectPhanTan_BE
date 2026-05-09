package com.eightthreads.backend.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("email_cua_ban@gmail.com");
        message.setTo(toEmail);
        message.setSubject("[8Threads Event] Yêu cầu khôi phục mật khẩu");
        message.setText("Chào bạn,\n\nBạn đã yêu cầu đặt lại mật khẩu. Vui lòng click vào đường link dưới đây để đổi mật khẩu mới:\n\n"
                + resetLink + "\n\nNếu bạn không yêu cầu, vui lòng bỏ qua email này.");

        mailSender.send(message);
    }
}
