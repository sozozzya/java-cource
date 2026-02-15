package ru.senla.hotel.autoconfig.annotation;

import ru.senla.hotel.autoconfig.converter.ConverterType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigProperty {

    String configFileName() default "";

    String propertyName() default "";

    ConverterType type() default ConverterType.AUTO;
}
