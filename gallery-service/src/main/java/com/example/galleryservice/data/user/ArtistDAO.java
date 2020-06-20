package com.example.galleryservice.data.user;

import com.example.galleryservice.model.user.Artist;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
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
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Repository
@Data
public class ArtistDAO extends UserDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ArtistDAO(@NotNull final DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(this.dataSource);
        jdbcInsert = new SimpleJdbcInsert(this.dataSource).withSchemaName("testbase")
                .withTableName("artists");
    }

    final static class ArtistRowMapper implements RowMapper<Artist> {
        @Override
        public Artist mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            Artist artist = new Artist();
            artist.setId(rs.getBigDecimal("id").longValue());
            artist.setArtworks(Lists.newArrayList((String[])rs.getArray("artworks").getArray()));
            return artist;
        }
    }

    @SneakyThrows
    public int insert(@NotNull final Artist artist) {
        final Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("id", artist.getId());
        parameters.put("expos", dataSource.getConnection().createArrayOf("varchar", artist.getArtworks().toArray(new String[0])));
        return jdbcInsert.execute(parameters);
    }

    @Override
    public Artist findByID(final long id) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.artists where id = ?",
                    new Object[]{id},
                    new ArtistRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @SneakyThrows
    public void update(@NotNull final Artist artist) {
        jdbcTemplate.update("update testbase.artists " + "set artworks = ? " + " where id = ?",
                dataSource.getConnection().createArrayOf("varchar", artist.getArtworks().toArray(new String[0])), artist.getId());
    }
}
