package clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import dtos.MembershipResponse;

@FeignClient(name="user-service")
public interface UserClient {
	
	@GetMapping("/api/users/{id}/membership")
    MembershipResponse getMembership(@PathVariable("id") Long id);
}
