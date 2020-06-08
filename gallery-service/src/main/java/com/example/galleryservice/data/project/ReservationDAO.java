package com.example.galleryservice.data.project;

import com.example.galleryservice.data.DAO;
import com.example.galleryservice.model.project.Reservation;
import com.example.galleryservice.model.project.ReservationStatus;
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
public class ReservationDAO implements DAO<Reservation> {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ReservationDAO(@NotNull final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("reservations")
                .usingGeneratedKeyColumns("id");
    }

    static class ReservationRowMapper implements RowMapper<Reservation> {
        @Override
        public Reservation mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            Reservation reservation = new Reservation();
            reservation.setId(rs.getLong("id"));
            reservation.setClient(rs.getLong("client"));
            reservation.setCost(rs.getDouble("cost"));
            reservation.setStatus(ReservationStatus.valueOf(rs.getString("status")));
            reservation.setDateTime(rs.getTimestamp("dateTime").toLocalDateTime());
            return reservation;
        }
    }

    public List<Reservation> findByClient(final long client) {
        return jdbcTemplate.query("select * from testbase.reservations where client = ?", new Object[]{client}, new ReservationRowMapper());
    }

    public List<Reservation> findByStatus(@NotNull final String status) {
        return jdbcTemplate.query("select * from testbase.reservations where status = ?", new Object[]{status}, new ReservationRowMapper());
    }

    @Override
    public Reservation findByID(final long id) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.reservations where id = ?",
                    new Object[]{id},
                    new BeanPropertyRowMapper<>(Reservation.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query("select * from testbase.reservations", new ReservationRowMapper());
    }

    @Override
    public void update(@NotNull final Reservation reservation) {
        jdbcTemplate.update("update testbase.reservations " + "set status = ?, dateTime = ? " + " where id = ?",
                reservation.getStatus().toString(), Timestamp.valueOf(reservation.getDateTime()), reservation.getId());
    }

    @Override
    public long insert(@NotNull final Reservation reservation) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(reservation);
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }
}
