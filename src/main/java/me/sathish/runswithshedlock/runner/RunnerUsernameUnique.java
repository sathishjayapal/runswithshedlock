package me.sathish.runswithshedlock.runner;

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
 * Validate that the username value isn't taken yet.
 */
@Target({FIELD, METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RunnerUsernameUnique.RunnerUsernameUniqueValidator.class)
public @interface RunnerUsernameUnique {

    String message() default "{Exists.runner.username}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class RunnerUsernameUniqueValidator implements ConstraintValidator<RunnerUsernameUnique, String> {

        private final RunnerService runnerService;
        private final HttpServletRequest request;

        public RunnerUsernameUniqueValidator(final RunnerService runnerService, final HttpServletRequest request) {
            this.runnerService = runnerService;
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
                            runnerService.get(Long.parseLong(currentId)).getUsername())) {
                // value hasn't changed
                return true;
            }
            return !runnerService.usernameExists(value);
        }
    }
}
