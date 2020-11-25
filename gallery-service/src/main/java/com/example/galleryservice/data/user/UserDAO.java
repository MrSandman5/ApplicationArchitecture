package com.example.galleryservice.data.user;

import com.example.galleryservice.data.DAO;
import com.example.galleryservice.model.user.User;
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
import java.util.*;

@Repository
@Data
public class UserDAO implements DAO<User> {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public UserDAO(@NotNull final DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(this.dataSource);
        jdbcInsert = new SimpleJdbcInsert(this.dataSource).withSchemaName("testbase")
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    final static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getBigDecimal("id").longValue());
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setAuthentication(rs.getBoolean("authentication"));
            return user;
        }
    }

    public User findByLogin(@NotNull final String login) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.users where login = ?",
                    new Object[]{login},
                    new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    public User findByName(@NotNull final String name) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.users where name = ?",
                    new Object[]{name},
                    new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public long insert(@NotNull final User user) {
        final Map<String, Object> parameters = new HashMap<>(6);
        parameters.put("login", user.getLogin());
        parameters.put("password", user.getPassword());
        parameters.put("name", user.getName());
        parameters.put("email", user.getEmail());
        parameters.put("authentication", user.getAuthentication());
        final Number newId = jdbcInsert.executeAndReturnKey(parameters);
        return (long) newId;
    }

    @Override
    public User findByID(final long id) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.users where id = ?",
                    new Object[]{id},
                    new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("select * from testbase.users", new UserRowMapper());
    }

    @Override
    public void update(@NotNull final User user) {
        jdbcTemplate.update("update testbase.users " + "set login = ?, password = ?, name = ?, email = ?, authentication = ? " + " where id = ?",
                user.getLogin(), user.getPassword(), user.getName(), user.getEmail(), user.getAuthentication(), user.getId());
    }
}
