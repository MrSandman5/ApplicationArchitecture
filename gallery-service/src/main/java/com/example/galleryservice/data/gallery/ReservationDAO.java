package com.example.galleryservice.data.gallery;

import com.example.galleryservice.data.DAO;
import com.example.galleryservice.model.gallery.Reservation;
import com.example.galleryservice.model.gallery.ReservationStatus;
import lombok.Data;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Data
public class ReservationDAO implements DAO<Reservation> {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ReservationDAO(@NotNull final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withSchemaName("testbase")
                .withTableName("reservations")
                .usingGeneratedKeyColumns("id");
    }

    static class ReservationRowMapper implements RowMapper<Reservation> {
        @Override
        public Reservation mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            Reservation reservation = new Reservation();
            reservation.setId(rs.getBigDecimal("id").longValue());
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
                    new ReservationRowMapper());
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
        final Map<String, Object> parameters = new HashMap<>(6);
        parameters.put("client", reservation.getClient());
        parameters.put("cost", reservation.getCost());
        parameters.put("status", reservation.getStatus());
        parameters.put("dateTime", reservation.getDateTime());
        final Number newId = jdbcInsert.executeAndReturnKey(parameters);
        return (long) newId;
    }
}
