package com.example.galleryservice.data.gallery;

import com.example.galleryservice.model.gallery.OwnerArtistPayment;
import lombok.Data;
import lombok.EqualsAndHashCode;
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

@EqualsAndHashCode(callSuper = true)
@Repository
@Data
public class OwnerArtistPaymentDAO extends PaymentDAO {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public OwnerArtistPaymentDAO(@NotNull final DataSource dataSource) {
        super(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withSchemaName("testbase")
                .withTableName("owner_artist_payment");
    }

    final static class OwnerArtistPaymentRowMapper implements RowMapper<OwnerArtistPayment> {
        @Override
        public OwnerArtistPayment mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            OwnerArtistPayment ownerArtistPaymentDAO = new OwnerArtistPayment();
            ownerArtistPaymentDAO.setId(rs.getBigDecimal("id").longValue());
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
                    new OwnerArtistPaymentRowMapper());
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

    public OwnerArtistPayment findByID(final long id) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.owner_artist_payment where id = ?",
                    new Object[]{id},
                    new OwnerArtistPaymentRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<OwnerArtistPayment> findAllOwnerArtistPayments() {
        return jdbcTemplate.query("select * from testbase.owner_artist_payment", new OwnerArtistPaymentRowMapper());
    }

    public int insert(@NotNull final OwnerArtistPayment ownerArtistPayment) {
        final Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("id", ownerArtistPayment.getId());
        parameters.put("expo", ownerArtistPayment.getExpo());
        parameters.put("owner", ownerArtistPayment.getOwner());
        parameters.put("artist", ownerArtistPayment.getArtist());
        return jdbcInsert.execute(parameters);
    }

}
