package services;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.model.MembershipStatus;
import com.library.model.Role;
import com.library.model.User;

import dtos.MembershipResponse;
import dtos.RegisterUserRequest;
import dtos.UpdateUserRequest;
import dtos.UserResponse;
import repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
		super();
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	public UserResponse register(RegisterUserRequest request) {

        if(repository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setMembershipStatus(MembershipStatus.ACTIVE);
        user.setSuspended(false);
        user.setMembershipExpiryDate(LocalDate.now().plusYears(1));

        repository.save(user);

        return map(user);
    }

    public UserResponse getById(Long id) {

        User user = repository.findById(id)
                .orElseThrow();

        return map(user);
    }

    public UserResponse update(Long id, UpdateUserRequest request) {

        User user = repository.findById(id)
                .orElseThrow();

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());

        repository.save(user);

        return map(user);
    }

    public void suspend(Long id) {

        User user = repository.findById(id)
                .orElseThrow();

        user.setSuspended(true);
        user.setMembershipStatus(MembershipStatus.SUSPENDED);

        repository.save(user);
    }

    public MembershipStatus checkMembership(Long id) {

        User user = repository.findById(id)
                .orElseThrow();

        if(user.getMembershipExpiryDate().isBefore(LocalDate.now())) {

            user.setMembershipStatus(MembershipStatus.EXPIRED);

            repository.save(user);
        }

        return user.getMembershipStatus();
    }

    private UserResponse map(User user) {

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getMembershipStatus(),
                user.isSuspended()
        );
    }
    
    public MembershipResponse getMembershipInfo(Long id) {
        User user = repository.findById(id)
                .orElseThrow();

        if (user.getMembershipExpiryDate().isBefore(LocalDate.now())
                && user.getMembershipStatus() != MembershipStatus.SUSPENDED) {
            user.setMembershipStatus(MembershipStatus.EXPIRED);
            repository.save(user);
        }

        return new MembershipResponse(user.getId(), user.getMembershipStatus().name(), user.isSuspended());
    }
}