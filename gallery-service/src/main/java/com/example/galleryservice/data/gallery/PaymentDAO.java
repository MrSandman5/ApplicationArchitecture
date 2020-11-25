package com.example.galleryservice.data.gallery;

import com.example.galleryservice.data.DAO;
import com.example.galleryservice.model.gallery.Payment;
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
public class PaymentDAO implements DAO<Payment> {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public PaymentDAO(@NotNull final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withSchemaName("testbase")
                .withTableName("payment")
                .usingGeneratedKeyColumns("id");
    }

    final static class PaymentRowMapper implements RowMapper<Payment> {
        @Override
        public Payment mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            Payment payment = new Payment();
            payment.setId(rs.getBigDecimal("id").longValue());
            payment.setDateTime(rs.getTimestamp("dateTime").toLocalDateTime());
            payment.setPrice(rs.getDouble("price"));
            return payment;
        }
    }

    @Override
    public Payment findByID(long id) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.payment where id = ?",
                    new Object[]{id},
                    new PaymentRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Payment> findAll() {
        return jdbcTemplate.query("select * from testbase.payment", new PaymentRowMapper());
    }

    @Override
    public void update(@NotNull Payment payment) { }

    @Override
    public long insert(@NotNull Payment payment) {
        final Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("dateTime", payment.getDateTime());
        parameters.put("price", payment.getPrice());
        final Number newId = jdbcInsert.executeAndReturnKey(parameters);
        return (long) newId;
    }
}
