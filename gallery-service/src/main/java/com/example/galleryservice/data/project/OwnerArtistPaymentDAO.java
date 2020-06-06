package com.example.galleryservice.data.project;

import com.example.galleryservice.data.DAO;
import com.example.galleryservice.model.project.OwnerArtistPayment;
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
public class OwnerArtistPaymentDAO implements DAO<OwnerArtistPayment> {

    private final DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public OwnerArtistPaymentDAO(@NotNull final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    private void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("owner_artist_payments")
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

    public Optional<OwnerArtistPayment> findByExpo(final long expo) {
        return Optional.of(Objects.requireNonNull(
                jdbcTemplate.queryForObject("select * from owner_artist_payments where expo = ?",
                        new Object[]{expo},
                        new BeanPropertyRowMapper<>(OwnerArtistPayment.class))));
    }

    public List<OwnerArtistPayment> findByOwner(final long owner) {
        return jdbcTemplate.query("select * from owner_artist_payments where owner = ?", new Object[]{owner}, new OwnerArtistPaymentRowMapper());
    }

    public List<OwnerArtistPayment> findByArtist(final long artist) {
        return jdbcTemplate.query("select * from owner_artist_payments where artist = ?", new Object[]{artist}, new OwnerArtistPaymentRowMapper());
    }

    @Override
    public Optional<OwnerArtistPayment> findByID(final long id) {
        return Optional.of(Objects.requireNonNull(
                jdbcTemplate.queryForObject("select * from owner_artist_payments where id = ?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(OwnerArtistPayment.class))));
    }

    @Override
    public List<OwnerArtistPayment> findAll() {
        return jdbcTemplate.query("select * from owner_artist_payments", new OwnerArtistPaymentRowMapper());
    }

    @Override
    public void update(@NotNull final OwnerArtistPayment ownerArtistPayment) { }

    @Override
    public long insert(@NotNull final OwnerArtistPayment ownerArtistPayment) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(ownerArtistPayment);
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

}
