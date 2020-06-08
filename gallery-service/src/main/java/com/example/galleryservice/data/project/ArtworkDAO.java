package com.example.galleryservice.data.project;

import com.example.galleryservice.data.DAO;
import com.example.galleryservice.model.project.Artwork;
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
import java.util.Objects;
import java.util.Optional;

@Repository
@Data
public class ArtworkDAO implements DAO<Artwork> {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ArtworkDAO(@NotNull final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("artworks")
                .usingGeneratedKeyColumns("id");
    }

    static class ArtworkRowMapper implements RowMapper<Artwork> {
        @Override
        public Artwork mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            Artwork artwork = new Artwork();
            artwork.setId(rs.getLong("id"));
            artwork.setName(rs.getString("name"));
            artwork.setInfo(rs.getString("info"));
            artwork.setArtist(rs.getLong("artist"));
            return artwork;
        }
    }

    public Artwork findByName(@NotNull final String name) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.artworks where name = ?",
                    new Object[]{name},
                    new BeanPropertyRowMapper<>(Artwork.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Artwork> findByArtist(final long artist) {
        return jdbcTemplate.query("select * from testbase.artworks where artist = ?", new Object[]{artist}, new ArtworkRowMapper());
    }

    @Override
    public Artwork findByID(final long id) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.artworks where id = ?",
                    new Object[]{id},
                    new BeanPropertyRowMapper<>(Artwork.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Artwork> findAll() {
        return jdbcTemplate.query("select * from testbase.artworks", new ArtworkRowMapper());
    }

    @Override
    public void update(@NotNull final Artwork artwork) {
        jdbcTemplate.update("update testbase.artworks " + "set name = ?, info = ? " + " where id = ?",
                artwork.getName(), artwork.getInfo(), artwork.getId());
    }

    @Override
    public long insert(@NotNull final Artwork artwork) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(artwork);
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }
}
