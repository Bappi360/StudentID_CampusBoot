# CampusBoot

A tiny Spring-Boot-style framework built with custom annotations and Java Reflection (Java 11+, no external libraries).

- **Name:** <your name>
- **Student ID:** <your ID>

## What it does

| Annotation   | Applies to | Purpose |
|--------------|------------|---------|
| `@Component` | Class      | Container creates and stores an instance (bean). Bean name is `value()`, or the class name with a lowercase first letter. |
| `@Value`     | Field      | Injects a config value (String or int) into the field. |
| `@Range`     | Field      | Validates that an int field is between `min` and `max`. Default message: `Value out of range`. |
| `@Route`     | Method     | Maps a URL path to a method. |

All four annotations use `@Retention(RetentionPolicy.RUNTIME)` so reflection can read them at runtime.

## Files

- `Component.java`, `Value.java`, `Range.java`, `Route.java`: the annotations
- `Container.java`: `scan()`, `injectValues()`, `getBean()`
- `Validator.java`: `validate(Object)` returns a list of violations
- `Router.java`: `dispatch(Container, String)` finds and invokes the `@Route` method, or returns `404 Not Found: <path>`
- `GradeController.java`, `ReportPrinter.java`, `Student.java`, `Main.java`: starter code
- `answers.txt`: answers to the reflection questions (Part E)

## How to compile and run

```
javac *.java
java Main
```

## Expected output

```
[Container] Registered: gradeController (GradeController)
[Container] Skipped: ReportPrinter (no @Component)
[Container] Injected: app.name -> GradeController.appName
[Container] Injected: app.passMark -> GradeController.passMark
--- Validation ---
VIOLATION: score - Score must be between 0 and 100 (actual: 120)
OK
VIOLATION: semester - Value out of range (actual: 15)
--- Routing ---
[Router] /hello -> Welcome to CampusBoot
[Router] /status -> Pass mark is 40
[Router] /unknown -> 404 Not Found: /unknown
```

## Notes

- The `@Route` element is named `value()` (the URL path) instead of `path()`, because the starter code uses the shorthand `@Route("/hello")`, which only compiles with an element named `value`.
- `Main.java` has the `java.util` imports added; the rest of the starter code is unchanged.
