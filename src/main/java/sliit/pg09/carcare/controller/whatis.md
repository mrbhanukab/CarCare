# Whatis `controller`

The `controller` package in a Spring Boot application contains classes that handle incoming HTTP requests and return
responses to the client. These classes are annotated with `@RestController` or `@Controller` and define methods mapped
to specific request paths using annotations like `@GetMapping`, `@PostMapping`, `@PutMapping`, and `@DeleteMapping`.
Controllers act as an intermediary between the client and the service layer, processing input, invoking business logic,
and returning the appropriate output.