package me.sathish.runswithshedlock.run_event;

import me.sathish.runswithshedlock.garmin_run.GarminRunDTO;
import me.sathish.runswithshedlock.util.ApplicationProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RunEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties applicationProperties;

    public RunEventPublisher(RabbitTemplate rabbitTemplate, ApplicationProperties applicationProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.applicationProperties = applicationProperties;
    }

    public void publishGarminRun(GarminRunDTO garminRunDTO) {
        rabbitTemplate.convertAndSend(
                applicationProperties.garminExchange(), applicationProperties.garminNewRunQueue(), garminRunDTO);
    }
}
