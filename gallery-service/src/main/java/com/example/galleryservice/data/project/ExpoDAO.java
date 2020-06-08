package com.example.galleryservice.data.project;

import com.example.galleryservice.data.DAO;
import com.example.galleryservice.model.project.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
@Data
public class ExpoDAO implements DAO<Expo> {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ExpoDAO(@NotNull final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("expos")
                .usingGeneratedKeyColumns("id");
    }

    final static class ExpoRowMapper implements RowMapper<Expo> {
        @Override
        public Expo mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            Expo expo = new Expo();
            expo.setId(rs.getLong("id"));
            expo.setName(rs.getString("name"));
            expo.setInfo(rs.getString("info"));
            expo.setArtist(rs.getLong("artist"));
            expo.setStartTime(rs.getTimestamp("startTime").toLocalDateTime());
            expo.setEndTime(rs.getTimestamp("endTime").toLocalDateTime());
            expo.setStatus(ExpoStatus.valueOf(rs.getString("status")));
            return expo;
        }
    }

    public Expo findByName(@NotNull final String name) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.expos where name = ?",
                    new Object[]{name},
                    new BeanPropertyRowMapper<>(Expo.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Expo> findByArtist(final long artist) {
        return jdbcTemplate.query("select * from testbase.expos where artist = ?", new Object[]{artist}, new ExpoRowMapper());
    }

    public List<Expo> findByStatus(@NotNull final String status) {
        return jdbcTemplate.query("select * from testbase.expos where status = ?", new Object[]{status}, new ExpoRowMapper());
    }

    @Override
    public Expo findByID(final long id) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.expos where id = ?",
                    new Object[]{id},
                    new BeanPropertyRowMapper<>(Expo.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Expo> findAll() {
        return jdbcTemplate.query("select * from testbase.expos", new ExpoRowMapper());
    }

    @Override
    public void update(@NotNull final Expo expo) {
        jdbcTemplate.update("update testbase.expos " + "set name = ?, info = ?, startTime = ?, endTime = ?, status = ? " + " where id = ?",
                expo.getName(), expo.getInfo(), Timestamp.valueOf(expo.getStartTime()), Timestamp.valueOf(expo.getEndTime()), expo.getStatus().toString(), expo.getId());
    }

    @Override
    public long insert(@NotNull final Expo expo) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(expo);
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }
}
