# Read Me

The original idea is from  
https://shiksha-com.medium.com/kafka-exception-handler-in-spring-boot-dc575fcbc074

@ControllerAdvice and @ExceptionHandler annotations to handle exceptions approach only works within the context of the
dispatcher servlet and therefore does not apply to Kafka consumers, which are outside of this context

Use AOP to handle exceptions thrown from KafkaListeners