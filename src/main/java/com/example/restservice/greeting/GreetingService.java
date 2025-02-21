package com.example.restservice.greeting;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GreetingService {

    JdbcTemplate template;

    public GreetingService(JdbcTemplate _template) {
        this.template = _template;
    }

    public Long saveGreeting(String content) {
        KeyHolder holder = new GeneratedKeyHolder();
        template.update(con -> {
            PreparedStatement pstmt = con.prepareStatement("insert into greetings (greeting) values (?) returning greeting_id;", new String[]{"greeting_id"});
            pstmt.setString(1, content);
            return pstmt;
        }, holder);
        return holder.getKey().longValue();
    }

    public Greeting getGreetingById(Long greetingId) {
        return template.queryForObject("select greeting_id, greeting from greeting where greeting_id = ?",
                (rs, rowNum) -> {
                    Greeting greeting = new Greeting(rs.getLong("greeting_id"), rs.getString("greeting"));
                    return greeting;
                }, greetingId);
    }
}
