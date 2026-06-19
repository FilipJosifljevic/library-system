package clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import dtos.UserResponse;

@FeignClient(name="user-service")
public interface UserClient {

	@GetMapping("/api/users/{id}")
    UserResponse getUser(@PathVariable("id") Long id);
}
