package com.example.galleryservice.data.user;

import com.example.galleryservice.model.user.Client;
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
public class ClientDAO extends UserDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ClientDAO(@NotNull final DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(this.dataSource);
        jdbcInsert = new SimpleJdbcInsert(this.dataSource).withSchemaName("testbase")
                .withTableName("clients");
    }

    final static class ClientRowMapper implements RowMapper<Client> {
        @Override
        public Client mapRow(@NotNull final ResultSet rs, final int rowNum) throws SQLException {
            Client client = new Client();
            client.setId(rs.getBigDecimal("id").longValue());
            client.setTickets(Lists.newArrayList((Long[])rs.getArray("tickets").getArray()));
            client.setReservations(Lists.newArrayList((Long[])rs.getArray("reservations").getArray()));
            return client;
        }
    }

    @SneakyThrows
    public int insert(@NotNull final Client client) {
        final Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("id", client.getId());
        parameters.put("tickets", dataSource.getConnection().createArrayOf("bigint", client.getTickets().toArray(new Long[0])));
        parameters.put("reservations", dataSource.getConnection().createArrayOf("bigint", client.getReservations().toArray(new Long[0])));
        return jdbcInsert.execute(parameters);
    }

    @Override
    public Client findByID(final long id) {
        try {
            return jdbcTemplate.queryForObject("select * from testbase.clients where id = ?",
                    new Object[]{id},
                    new ClientRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @SneakyThrows
    public void update(@NotNull final Client client) {
        jdbcTemplate.update("update testbase.clients " + "set tickets = ?, reservations = ? " + " where id = ?",
                dataSource.getConnection().createArrayOf("bigint", client.getTickets().toArray(new Long[0])),
                dataSource.getConnection().createArrayOf("bigint", client.getReservations().toArray(new Long[0])),
                client.getId());
    }
}
