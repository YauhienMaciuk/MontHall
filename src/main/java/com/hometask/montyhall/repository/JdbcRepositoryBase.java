package com.hometask.montyhall.repository;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hometask.montyhall.exception.MontyHallException;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class JdbcRepositoryBase {

    private final LoadingCache<String, String> sqlCache;
    protected final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcRepositoryBase(NamedParameterJdbcTemplate jdbcTemplate) {
        this.sqlCache = CacheBuilder.newBuilder().build(CacheLoader.from(this::readFile));
        this.jdbcTemplate = jdbcTemplate;
    }

    protected String getSql(String sqlFilePath) {
        try {
            return sqlCache.get(sqlFilePath);
        } catch (ExecutionException e) {
            throw new MontyHallException();
        }
    }

    private String readFile(String resourcePath) {
        try {
            return IOUtils.toString(new ClassPathResource(resourcePath).getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MontyHallException();
        }
    }
}
