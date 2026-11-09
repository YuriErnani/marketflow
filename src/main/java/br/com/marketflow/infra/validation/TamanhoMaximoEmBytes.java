package br.com.marketflow.infra.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = TamanhoMaximoEmBytesValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TamanhoMaximoEmBytes {

    int value();

    String message() default "deve ter no máximo {value} bytes (letras acentuadas contam como 2)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
