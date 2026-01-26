package com.example.learn.user;

import com.example.learn.auth.AuthService;
import com.example.learn.auth.dto.CustomUserDetails;
import com.example.learn.common.NotFoundException;
import com.example.learn.common.dto.PageRequestDto;
import com.example.learn.seat.Seat;
import com.example.learn.user.dto.UserFilter;
import com.example.learn.user.dto.UserResponse;
import com.example.learn.user.specfication.UserSpecfication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final AuthService authService;
    public UserService(UserRepository userRepository, AuthService authService) {
        repository = userRepository;
        this.authService = authService;
    }

    public Page<UserResponse> getAllUsers(PageRequestDto pageRequestDto, UserFilter userFilter) {
        if(pageRequestDto.getPage() < 0) {
            throw new IllegalArgumentException("Page number should not be negative");
        }
        if(pageRequestDto.getSize() <= 0 || pageRequestDto.getSize() > 100) {
            throw new IllegalArgumentException("Page size should be in b/w 1 to 100");
        }

        // Specification
        Specification<User> specification = Specification
                .where(UserSpecfication.hasType(userFilter.getType()))
                .and(UserSpecfication.containsName(userFilter.getName()))
                .and(UserSpecfication.containsEmail(userFilter.getEmail()));

        // Pagination
        Sort sort = pageRequestDto.getSortDir().equalsIgnoreCase("desc") ?
                Sort.by(pageRequestDto.getSortBy()).descending() : Sort.by(pageRequestDto.getSortBy()).ascending();
        Pageable pageable = PageRequest.of(pageRequestDto.getPage(), pageRequestDto.getSize(), sort);
        Page<User> users = repository.findAll(specification, pageable);

        if(users.isEmpty()) {
            throw new NotFoundException("No users found");
        }
        return users.map(user -> new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUserType()
        ));
    }

    public User getProfile(Authentication authentication) {
        CustomUserDetails userDetails = authService.getLoggedInUserDetails();
        return new User(
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getEmail(),
                userDetails.getUserType(),
                userDetails.getCeatedAt()
        );
    }

    public User findUser(Integer userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
