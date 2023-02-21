package com.secondproject.monthlycoffee.token;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    @Override
    List<RefreshToken> findAll();
}
