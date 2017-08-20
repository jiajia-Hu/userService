package com.user.repository;

import com.user.entity.Group;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by jiajia on 2017/8/4
 */
public interface GroupRepository extends PagingAndSortingRepository<Group, Integer> {
    Group findByGroupRole(String role);

}
