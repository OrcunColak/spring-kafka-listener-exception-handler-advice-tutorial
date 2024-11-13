package com.colak.springtutorial.aspect;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Aspect
@Component

@RequiredArgsConstructor
@Slf4j
public class KafkaExceptionHandlerAspect {

    private final ApplicationContext applicationContext;

    private final Map<Object, Method> exceptionMethodMapping = new HashMap<>();

    // @AfterThrowing is used to tell Spring Boot to monitor methods only for exceptions.
    // throwing attribute value and method parameter must have the same name
    @AfterThrowing(pointcut = "@annotation(org.springframework.kafka.annotation.KafkaListener)", throwing = "exception")
    public void afterThrowingExceptionAdvice(Exception exception) {
        try {
            Method excMethod = exceptionMethodMapping.get(exception.getClass().getName());
            if (excMethod != null) {
                excMethod.invoke(applicationContext.getBean(excMethod.getDeclaringClass()), exception);
            }
        } catch (Exception e) {
            log.error("Exception thrown in ControllerAdvice :", e);
        }
    }

    @PostConstruct
    private void init() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ControllerAdvice.class));
        Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents("com.colak.*");
        try {
            for (BeanDefinition beanDefinition : beanDefinitions) {
                final Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                final Method[] methods = clazz.getMethods();
                loadExceptionMethodMapping(methods);
            }
        } catch (ClassNotFoundException classNotFoundException) {
            log.error("Kafka bean :{}", beanDefinitions, classNotFoundException);
        }
    }

    private void loadExceptionMethodMapping(Method[] methods) {
        for (final Method method : methods) {
            ExceptionHandler exceptionHandlerAnnotation;
            try {
                if (method.isAnnotationPresent(ExceptionHandler.class)) {
                    exceptionHandlerAnnotation = method.getAnnotation(ExceptionHandler.class);
                    if (exceptionHandlerAnnotation != null) {
                        for (Class<? extends Throwable> exceptionClazz : exceptionHandlerAnnotation.value()) {
                            exceptionMethodMapping.put(exceptionClazz.getName(), method);
                        }
                    }
                }
            } catch (Exception exception) {
                log.error("Exception while loading @ExceptionHandler", exception);
            }
        }
    }
}
