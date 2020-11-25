package com.example.galleryservice.data.user;

import com.example.galleryservice.model.user.Owner;
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
public class OwnerDAO extends UserDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public OwnerDAO(@NotNull final DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(this.dataSource);
        jdbcInsert = new SimpleJdbcInsert(this.dataSource).withSchemaName("testbase")
                .withTableName("owners");
    }

    final static class OwnerRowMapper implements RowMapper<Owner> {
        @Override
        public Owner mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            Owner owner = new Owner();
            owner.setId(rs.getBigDecimal("id").longValue());
            owner.setExpos(Lists.newArrayList((String[])rs.getArray("expos").getArray()));
            return owner;
        }
    }

    @SneakyThrows
    public int insert(@NotNull final Owner owner) {
        final Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("id", owner.getId());
        parameters.put("expos", dataSource.getConnection().createArrayOf("varchar", owner.getExpos().toArray(new String[0])));
        return jdbcInsert.execute(parameters);
    }

    @Override
    public Owner findByID(final long id) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.owners where id = ?",
                    new Object[]{id},
                    new OwnerRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @SneakyThrows
    public void update(@NotNull final Owner owner) {
        jdbcTemplate.update("update testbase.owners " + "set expos = ? " + " where id = ?",
                dataSource.getConnection().createArrayOf("varchar", owner.getExpos().toArray(new String[0])), owner.getId());
    }
}
