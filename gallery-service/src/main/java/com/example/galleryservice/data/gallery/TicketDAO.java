package com.example.galleryservice.data.gallery;

import com.example.galleryservice.data.DAO;
import com.example.galleryservice.model.gallery.Ticket;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Data
public class TicketDAO implements DAO<Ticket> {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public TicketDAO(@NotNull final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withSchemaName("testbase")
                .withTableName("tickets")
                .usingGeneratedKeyColumns("id");
    }

    final static class TicketRowMapper implements RowMapper<Ticket> {
        @Override
        public Ticket mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            Ticket ticket = new Ticket();
            ticket.setId(rs.getBigDecimal("id").longValue());
            ticket.setClient(rs.getLong("client"));
            ticket.setExpo(rs.getLong("expo"));
            ticket.setCost(rs.getDouble("cost"));
            return ticket;
        }
    }

    public List<Ticket> findByClient(final long client) {
        return jdbcTemplate.query("select * from testbase.tickets where client = ?", new Object[]{client}, new TicketRowMapper());
    }

    public List<Ticket> findByExpo(final long expo) {
        return jdbcTemplate.query("select * from testbase.tickets where expo = ?", new Object[]{expo}, new TicketRowMapper());
    }

    @Override
    public Ticket findByID(final long id) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.tickets where id = ?",
                    new Object[]{id},
                    new TicketRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public List<Ticket> findAll() {
        return jdbcTemplate.query("select * from testbase.tickets", new TicketRowMapper());
    }

    @Override
    public void update(@NotNull final Ticket ticket) { }

    @Override
    public long insert(@NotNull final Ticket ticket) {
        final Map<String, Object> parameters = new HashMap<>(6);
        parameters.put("client", ticket.getClient());
        parameters.put("expo", ticket.getExpo());
        parameters.put("cost", ticket.getCost());
        final Number newId = jdbcInsert.executeAndReturnKey(parameters);
        return (long) newId;
    }

}
