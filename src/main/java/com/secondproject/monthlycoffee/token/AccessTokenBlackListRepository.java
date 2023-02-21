package com.secondproject.monthlycoffee.token;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AccessTokenBlackListRepository extends CrudRepository<AccessTokenBlackList, String> {

    @Override
    List<AccessTokenBlackList> findAll();
}
