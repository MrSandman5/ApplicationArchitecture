package com.example.galleryservice.data.project;

import com.example.galleryservice.data.DAO;
import com.example.galleryservice.model.project.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Transactional
public class TicketDAO extends JdbcDaoSupport implements DAO<Ticket> {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    public TicketDAO() {
        final DataSource dataSource = getDataSource();
        jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(dataSource));
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("tickets")
                .usingGeneratedKeyColumns("id");
    }

    final static class TicketRowMapper implements RowMapper<Ticket> {
        @Override
        public Ticket mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            Ticket ticket = new Ticket();
            ticket.setId(rs.getLong("id"));
            ticket.setClient(rs.getLong("client"));
            ticket.setExpo(rs.getLong("expo"));
            ticket.setCost(rs.getDouble("cost"));
            return ticket;
        }
    }

    public List<Ticket> findByClient(final long client) {
        return jdbcTemplate.query("select * from tickets where client = ?", new Object[]{client}, new TicketRowMapper());
    }

    public List<Ticket> findByExpo(final long expo) {
        return jdbcTemplate.query("select * from tickets where expo = ?", new Object[]{expo}, new TicketRowMapper());
    }

    @Override
    public Optional<Ticket> findByID(final long id) {
        return Optional.of(Objects.requireNonNull(
                jdbcTemplate.queryForObject("select * from tickets where id = ?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Ticket.class))));
    }

    @Override
    public List<Ticket> findAll() {
        return jdbcTemplate.query("select * from tickets", new TicketRowMapper());
    }

    @Override
    public void update(@NotNull final Ticket ticket) { }

    @Override
    public long insert(@NotNull final Ticket ticket) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(ticket);
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

}
