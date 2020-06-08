package com.example.galleryservice.data.project;

import com.example.galleryservice.data.DAO;
import com.example.galleryservice.model.project.OwnerArtistPayment;
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
public class OwnerArtistPaymentDAO implements DAO<OwnerArtistPayment> {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public OwnerArtistPaymentDAO(@NotNull final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("owner_artist_payment")
                .usingGeneratedKeyColumns("id");
    }

    final static class OwnerArtistPaymentRowMapper implements RowMapper<OwnerArtistPayment> {
        @Override
        public OwnerArtistPayment mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            OwnerArtistPayment ownerArtistPaymentDAO = new OwnerArtistPayment();
            ownerArtistPaymentDAO.setId(rs.getLong("id"));
            ownerArtistPaymentDAO.setDate(rs.getTimestamp("dateTime").toLocalDateTime());
            ownerArtistPaymentDAO.setAmount(rs.getDouble("amount"));
            ownerArtistPaymentDAO.setExpo(rs.getLong("expo"));
            ownerArtistPaymentDAO.setOwner(rs.getLong("owner"));
            ownerArtistPaymentDAO.setArtist(rs.getLong("artist"));
            return ownerArtistPaymentDAO;
        }
    }

    public OwnerArtistPayment findByExpo(final long expo) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.owner_artist_payment where expo = ?",
                    new Object[]{expo},
                    new BeanPropertyRowMapper<>(OwnerArtistPayment.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<OwnerArtistPayment> findByOwner(final long owner) {
        return jdbcTemplate.query("select * from testbase.owner_artist_payment where owner = ?", new Object[]{owner}, new OwnerArtistPaymentRowMapper());
    }

    public List<OwnerArtistPayment> findByArtist(final long artist) {
        return jdbcTemplate.query("select * from testbase.owner_artist_payment where artist = ?", new Object[]{artist}, new OwnerArtistPaymentRowMapper());
    }

    @Override
    public OwnerArtistPayment findByID(final long id) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.owner_artist_payment where id = ?",
                    new Object[]{id},
                    new BeanPropertyRowMapper<>(OwnerArtistPayment.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<OwnerArtistPayment> findAll() {
        return jdbcTemplate.query("select * from testbase.owner_artist_payment", new OwnerArtistPaymentRowMapper());
    }

    @Override
    public void update(@NotNull final OwnerArtistPayment ownerArtistPayment) { }

    @Override
    public long insert(@NotNull final OwnerArtistPayment ownerArtistPayment) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(ownerArtistPayment);
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

}
