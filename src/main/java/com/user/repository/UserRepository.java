package com.user.repository;

import com.user.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by jiajia on 2017/8/4
 */
public interface UserRepository extends PagingAndSortingRepository<User,Long> {
    User findByEmail(String email);

    User findById(Long id);

    User findByUsername(String username);

    List<User>  findTop10ByUsernameLike(String username);
}
