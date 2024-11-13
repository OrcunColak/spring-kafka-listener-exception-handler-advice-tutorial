package com.colak.springtutorial.exceptionhandler;

import com.colak.springtutorial.consumer.MyKafkaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(MyKafkaException.class)
    public void handleMyKafkaException(MyKafkaException exception) {
        log.error("Exception thrown in Kafka Listener  :", exception);
    }

}
