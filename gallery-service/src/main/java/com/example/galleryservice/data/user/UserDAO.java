package com.example.galleryservice.data.user;

import com.example.galleryservice.data.DAO;
import com.example.galleryservice.model.user.User;
import com.example.galleryservice.model.user.UserRole;
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
public class UserDAO implements DAO<User> {

    private final DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public UserDAO(@NotNull final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    private void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    final static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setAuthentication(rs.getBoolean("authentication"));
            user.setRole(UserRole.valueOf(rs.getString("role")));
            return user;
        }
    }

    public boolean authenticate(@NotNull final User user, @NotNull final String password) {
        final String resultPassword = jdbcTemplate.queryForObject("select users.password FROM users WHERE id = ?;",
                new Object[]{user.getId()},
                new BeanPropertyRowMapper<>(String.class));

        return password.equals(resultPassword);
    }

    public Optional<User> findByLogin(@NotNull final String login) {
        return Optional.of(Objects.requireNonNull(
                jdbcTemplate.queryForObject("select * from users where login = ?",
                        new Object[]{login},
                        new BeanPropertyRowMapper<>(User.class))));
    }

    public Optional<User> findByName(@NotNull final String name) {
        return Optional.of(Objects.requireNonNull(
                jdbcTemplate.queryForObject("select * from users where name = ?",
                        new Object[]{name},
                        new BeanPropertyRowMapper<>(User.class))));
    }

    public long insert(@NotNull final User user) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(user);
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public Optional<User> findByID(final long id) {
        return Optional.of(Objects.requireNonNull(
                jdbcTemplate.queryForObject("select * from users where id = ?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(User.class))));
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("select * from users", new UserRowMapper());
    }

    @Override
    public void update(@NotNull final User user) {
        jdbcTemplate.update("update users " + "set login = ?, password = ?, name = ?, email = ? " + " where id = ?",
                user.getLogin(), user.getPassword(), user.getName(), user.getEmail(), user.getId());
    }
}
