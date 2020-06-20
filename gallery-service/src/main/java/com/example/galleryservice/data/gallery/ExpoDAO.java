package com.example.galleryservice.data.gallery;

import com.example.galleryservice.data.DAO;
import com.example.galleryservice.model.gallery.*;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Repository
@Data
public class ExpoDAO implements DAO<Expo> {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ExpoDAO(@NotNull final DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(this.dataSource);
        jdbcInsert = new SimpleJdbcInsert(this.dataSource).withSchemaName("testbase")
                .withTableName("expos")
                .usingGeneratedKeyColumns("id");
    }

    final static class ExpoRowMapper implements RowMapper<Expo> {
        @Override
        public Expo mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            Expo expo = new Expo();
            expo.setId(rs.getBigDecimal("id").longValue());
            expo.setName(rs.getString("name"));
            expo.setInfo(rs.getString("info"));
            expo.setArtist(rs.getLong("artist"));
            expo.setStartTime(rs.getTimestamp("startTime").toLocalDateTime());
            expo.setEndTime(rs.getTimestamp("endTime").toLocalDateTime());
            expo.setTicketPrice(rs.getDouble("ticketPrice"));
            expo.setArtworks(Lists.newArrayList((String[])rs.getArray("artworks").getArray()));
            expo.setStatus(ExpoStatus.valueOf(rs.getString("status")));
            return expo;
        }
    }

    public Expo findByName(@NotNull final String name) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.expos where name = ?",
                    new Object[]{name},
                    new ExpoRowMapper());
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
                    new ExpoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Expo> findAll() {
        return jdbcTemplate.query("select * from testbase.expos", new ExpoRowMapper());
    }

    @SneakyThrows
    @Override
    public void update(@NotNull final Expo expo) {
        jdbcTemplate.update("update testbase.expos " + "set name = ?, info = ?, startTime = ?, endTime = ?, ticketprice = ?, artworks = ?, status = ? " + " where id = ?",
                expo.getName(), expo.getInfo(), Timestamp.valueOf(expo.getStartTime()), Timestamp.valueOf(expo.getEndTime()), expo.getTicketPrice(), dataSource.getConnection().createArrayOf("varchar", expo.getArtworks().toArray(new String[0])), expo.getStatus().toString(), expo.getId());
    }

    @SneakyThrows
    @Override
    public long insert(@NotNull final Expo expo) {
        final Map<String, Object> parameters = new HashMap<>(9);
        parameters.put("name", expo.getName());
        parameters.put("info", expo.getInfo());
        parameters.put("artist", expo.getArtist());
        parameters.put("startTime", expo.getStartTime());
        parameters.put("endTime", expo.getEndTime());
        parameters.put("ticketPrice", expo.getTicketPrice());
        parameters.put("artworks", dataSource.getConnection().createArrayOf("varchar", expo.getArtworks().toArray(new String[0])));
        parameters.put("status", expo.getStatus());
        final Number newId = jdbcInsert.executeAndReturnKey(parameters);
        return (long) newId;
    }
}
