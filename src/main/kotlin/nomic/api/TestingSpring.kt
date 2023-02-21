package nomic.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/hello")
class TestingSpring {
    // Path to this is BASEURL/api/hello/springboot
    @GetMapping("springboot")
    fun helloWorld(): String {
        return "Hello Spring!"
    }
}