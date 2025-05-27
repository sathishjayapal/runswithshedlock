package me.sathish.runswithshedlock.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "garminrun-event")
public record ApplicationProperties(String garminExchange,String garminNewRunQueue, String garminErrorQueue) {}
