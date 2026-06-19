package clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import dtos.BookAvailabilityResponse;

@FeignClient(name="book-service")
public interface BookClient {

	@GetMapping("/api/books/{id}/availability")
	BookAvailabilityResponse checkAvailability(@PathVariable("id") String id);
	
	@PatchMapping("/api/books/{id}/decrease")
    void decreaseAvailableCopies(@PathVariable("id") String id);

    @PatchMapping("/api/books/{id}/increase")
    void increaseAvailableCopies(@PathVariable("id") String id);
}
