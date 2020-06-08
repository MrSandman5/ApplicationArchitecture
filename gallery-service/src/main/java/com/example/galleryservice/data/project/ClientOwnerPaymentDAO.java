package com.example.galleryservice.data.project;

import com.example.galleryservice.data.DAO;
import com.example.galleryservice.model.project.ClientOwnerPayment;
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
import java.util.List;

@Repository
@Data
public class ClientOwnerPaymentDAO implements DAO<ClientOwnerPayment> {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ClientOwnerPaymentDAO(@NotNull final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("client_owner_payment")
                .usingGeneratedKeyColumns("id");
    }

    final static class ClientOwnerPaymentRowMapper implements RowMapper<ClientOwnerPayment> {
        @Override
        public ClientOwnerPayment mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            ClientOwnerPayment clientOwnerPayment = new ClientOwnerPayment();
            clientOwnerPayment.setId(rs.getLong("id"));
            clientOwnerPayment.setDate(rs.getTimestamp("dateTime").toLocalDateTime());
            clientOwnerPayment.setAmount(rs.getDouble("amount"));
            clientOwnerPayment.setReservation(rs.getLong("reservation"));
            clientOwnerPayment.setClient(rs.getLong("client"));
            clientOwnerPayment.setOwner(rs.getLong("owner"));
            return clientOwnerPayment;
        }
    }

    public ClientOwnerPayment findByReservation(final long reservation) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.client_owner_payment where reservation = ?",
                    new Object[]{reservation},
                    new BeanPropertyRowMapper<>(ClientOwnerPayment.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ClientOwnerPayment> findByClient(final long client) {
        return jdbcTemplate.query("select * from testbase.client_owner_payment where client = ?", new Object[]{client}, new ClientOwnerPaymentRowMapper());
    }

    public List<ClientOwnerPayment> findByOwner(final long owner) {
        return jdbcTemplate.query("select * from testbase.client_owner_payment where owner = ?", new Object[]{owner}, new ClientOwnerPaymentRowMapper());
    }

    @Override
    public ClientOwnerPayment findByID(final long id) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.client_owner_payment where id = ?",
                    new Object[]{id},
                    new BeanPropertyRowMapper<>(ClientOwnerPayment.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<ClientOwnerPayment> findAll() {
        return jdbcTemplate.query("select * from testbase.client_owner_payment", new ClientOwnerPaymentRowMapper());
    }

    @Override
    public void update(@NotNull final ClientOwnerPayment clientOwnerPayment) {}

    @Override
    public long insert(@NotNull final ClientOwnerPayment clientOwnerPayment) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(clientOwnerPayment);
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }
}
