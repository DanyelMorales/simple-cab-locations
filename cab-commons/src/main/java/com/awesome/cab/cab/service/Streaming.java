package com.awesome.cab.cab.service;

import com.awesome.cab.cab.model.Cab;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@org.springframework.stereotype.Service
public class Streaming {

    @Value("${docker.kafka.topic}")
    private String cabTopic;


    @Autowired
    private KafkaTemplate<String, Cab> kafkaTemplate;


    // "3b719a3a-bc3f-43a8-b311-94754a3a4d95"
    public String communicateLocation(Cab cabPayload, boolean available) {
        String eventId = UUID.randomUUID().toString();
        byte isAvailable = (byte) (available ? 0 : 1);
        ProducerRecord<String, Cab> producerRecord = new ProducerRecord<>(cabTopic, cabPayload);
        producerRecord.headers().add("eventName", "CAB-LOCATION".getBytes(StandardCharsets.UTF_8));
        producerRecord.headers().add("timestamp", new Date().toString().getBytes(StandardCharsets.UTF_8));
        producerRecord.headers().add("eventID", eventId.getBytes(StandardCharsets.UTF_8));
        producerRecord.headers().add("available", new byte[]{isAvailable});
        kafkaTemplate.send(producerRecord);
        return eventId;
    }



}
