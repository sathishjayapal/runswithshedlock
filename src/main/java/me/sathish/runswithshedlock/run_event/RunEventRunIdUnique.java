package me.sathish.runswithshedlock.run_event;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;

/**
 * Validate that the runId value isn't taken yet.
 */
@Target({FIELD, METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RunEventRunIdUnique.RunEventRunIdUniqueValidator.class)
public @interface RunEventRunIdUnique {

    String message() default "{Exists.runEvent.runId}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class RunEventRunIdUniqueValidator implements ConstraintValidator<RunEventRunIdUnique, String> {

        private final RunEventService runEventService;
        private final HttpServletRequest request;

        public RunEventRunIdUniqueValidator(final RunEventService runEventService, final HttpServletRequest request) {
            this.runEventService = runEventService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked")
            final Map<String, String> pathVariables =
                    ((Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null
                    && value.equalsIgnoreCase(
                            runEventService.get(Long.parseLong(currentId)).getRunId())) {
                // value hasn't changed
                return true;
            }
            return !runEventService.runIdExists(value);
        }
    }
}
