package com.example.galleryservice.data.project;

import com.example.galleryservice.data.DAO;
import com.example.galleryservice.model.project.Artwork;
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
public class ArtworkDAO extends JdbcDaoSupport implements DAO<Artwork> {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    public ArtworkDAO() {
        final DataSource dataSource = getDataSource();
        jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(dataSource));
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("artworks")
                .usingGeneratedKeyColumns("id");
    }

    @PostConstruct
    private void postConstruct() {

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

    public Optional<Artwork> findByName(@NotNull final String name) {
        return Optional.of(Objects.requireNonNull(
                jdbcTemplate.queryForObject("select * from artworks where name = ?",
                        new Object[]{name},
                        new BeanPropertyRowMapper<>(Artwork.class))));
    }

    public List<Artwork> findByArtist(final long artist) {
        return jdbcTemplate.query("select * from artworks where artist = ?", new Object[]{artist}, new ArtworkRowMapper());
    }

    @Override
    public Optional<Artwork> findByID(final long id) {
        return Optional.of(Objects.requireNonNull(
                jdbcTemplate.queryForObject("select * from artworks where id = ?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Artwork.class))));
    }

    @Override
    public List<Artwork> findAll() {
        return jdbcTemplate.query("select * from artworks", new ArtworkRowMapper());
    }

    @Override
    public void update(@NotNull final Artwork artwork) {
        jdbcTemplate.update("update artworks " + "set name = ?, info = ? " + " where id = ?",
                artwork.getName(), artwork.getInfo(), artwork.getId());
    }

    @Override
    public long insert(@NotNull final Artwork artwork) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(artwork);
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }
}
