package com.example.learn.user.specfication;

import com.example.learn.user.User;
import com.example.learn.user.UserType;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecfication {

    public UserSpecfication() {}

    public static Specification<User> hasType(UserType type) {
        return ((root, query, criteriaBuilder) ->
                type == null ? null : criteriaBuilder.equal(root.get("userType"), type)
        );
    }

    public static Specification<User> containsName(String name) {
        return ((root, query, criteriaBuilder) ->
            name == null ? null :
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"
                    )
        );
    }

    public static Specification<User> containsEmail(String email) {
        return ((root, query, criteriaBuilder) ->
            email == null ? null :
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"
                    )
        );
    }
}
