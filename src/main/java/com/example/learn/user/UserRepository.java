package com.example.learn.user;

import com.example.learn.user.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

//    @Query("""
//            select new com.example.learn.user.dto.UserResponse (
//                u.id, u.name, u.email, u.userType
//            )
//            from User u
//    """)
//    Page<UserResponse> findAllUsers(Pageable pageable);

    Page<User> findAll(Specification<User> spec, Pageable pageable);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}