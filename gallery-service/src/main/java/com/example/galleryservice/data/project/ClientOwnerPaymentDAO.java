package com.example.galleryservice.data.project;

import com.example.galleryservice.data.DAO;
import com.example.galleryservice.model.project.ClientOwnerPayment;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Data
public class ClientOwnerPaymentDAO implements DAO<ClientOwnerPayment> {

    private final DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ClientOwnerPaymentDAO(@NotNull final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    private void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("client_owner_payments")
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

    public Optional<ClientOwnerPayment> findByReservation(final long reservation) {
        return Optional.of(Objects.requireNonNull(
                jdbcTemplate.queryForObject("select * from client_owner_payments where reservation = ?",
                        new Object[]{reservation},
                        new BeanPropertyRowMapper<>(ClientOwnerPayment.class))));
    }

    public List<ClientOwnerPayment> findByClient(final long client) {
        return jdbcTemplate.query("select * from client_owner_payments where client = ?", new Object[]{client}, new ClientOwnerPaymentRowMapper());
    }

    public List<ClientOwnerPayment> findByOwner(final long owner) {
        return jdbcTemplate.query("select * from client_owner_payments where owner = ?", new Object[]{owner}, new ClientOwnerPaymentRowMapper());
    }

    public Optional<ClientOwnerPayment> findByClientAndOwner(final long client,
                                                             final long owner) {
        return Optional.of(Objects.requireNonNull(
                jdbcTemplate.queryForObject("select * from client_owner_payments where client = ? and owner = ?",
                        new Object[]{client, owner},
                        new BeanPropertyRowMapper<>(ClientOwnerPayment.class))));
    }

    @Override
    public Optional<ClientOwnerPayment> findByID(final long id) {
        return Optional.of(Objects.requireNonNull(
                jdbcTemplate.queryForObject("select * from client_owner_payments where id = ?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(ClientOwnerPayment.class))));
    }

    @Override
    public List<ClientOwnerPayment> findAll() {
        return jdbcTemplate.query("select * from client_owner_payments", new ClientOwnerPaymentRowMapper());
    }

    @Override
    public void update(@NotNull final ClientOwnerPayment clientOwnerPayment) {}

    @Override
    public long insert(@NotNull final ClientOwnerPayment clientOwnerPayment) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(clientOwnerPayment);
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }
}
