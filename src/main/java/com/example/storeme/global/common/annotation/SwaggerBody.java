package com.example.storeme.global.common.annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.core.annotation.AliasFor;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.lang.annotation.*;

/**
 * swagger에서 multipart/form-data 형식의 각 파트 데이터의 content-type을 명시하는 애노테이션
 */
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RequestBody
@Inherited
public @interface SwaggerBody {

	@AliasFor(annotation = RequestBody.class)
	String description() default "";

	@AliasFor(annotation = RequestBody.class)
	Content[] content() default {};

	@AliasFor(annotation = RequestBody.class)
	boolean required() default false;

	@AliasFor(annotation = RequestBody.class)
	Extension[] extensions() default {};

	@AliasFor(annotation = RequestBody.class)
	String ref() default "";

	@AliasFor(annotation = RequestBody.class)
	boolean useParameterTypeSchema() default false;

}