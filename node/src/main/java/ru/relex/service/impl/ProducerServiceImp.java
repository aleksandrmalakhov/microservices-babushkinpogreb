package ru.relex.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.relex.service.ProducerService;

import static ru.relex.model.RabbitQueue.ANSWER_MESSAGE;

@Log4j
@Service
public class ProducerServiceImp implements ProducerService {
    private final RabbitTemplate rabbitTemplate;

    public ProducerServiceImp(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void producerAnswer(SendMessage sendMessage) {
        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
    }

    @Override
    public void producerAnswer(String text, Long chatId) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.enableHtml(true);

        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
    }
}