package com.example.restservice.greeting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

@Component
public class GreetingService {
    private final Logger LOG = LoggerFactory.getLogger(GreetingService.class);

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
        return template.queryForObject("select greeting_id, greeting from greetings where greeting_id = ?",
                (rs, rowNum) -> {
                    Greeting greeting = new Greeting(rs.getLong("greeting_id"), rs.getString("greeting"));
                    return greeting;
                }, greetingId);
    }

    public List<Greeting> getGreetings(int offset, int limit) {
        LOG.debug("Getting {} greetings from offset {}", limit, offset);

        return template.query("select greeting_id, greeting from greetings limit ? offset ?",
                new Object[]{limit, offset},
                new int[] {Types.BIGINT, Types.BIGINT},
                (rs, row) -> {
                    Greeting greeting = new Greeting(rs.getLong("greeting_id"), rs.getString("greeting"));
                    return greeting;
                });
    }
}
