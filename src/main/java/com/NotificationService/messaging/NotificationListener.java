package com.NotificationService.messaging;

import com.NotificationService.dto.MailRequest;
import com.NotificationService.service.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final MailService mailService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "notifications_service.queue")
    public void handleNotificationEvent(String message) throws JsonProcessingException, MessagingException {
        try {
            MailRequest request = objectMapper.readValue(message, MailRequest.class);
            mailService.sendHtmlEmail(request.getTo(), request.getSubject(), request.getContent());
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());        }
    }
}
