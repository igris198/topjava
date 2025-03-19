package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.JdbcUtil;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {
    private static final ResultSetExtractor<List<User>> RESULT_SET_EXTRACTOR = rs -> {
        Map<Integer, User> userMap = new LinkedHashMap<>();

        while (rs.next()) {
            int userId = rs.getInt("id");
            User user = userMap.get(userId);

            if (user == null) {
                user = new User();
                user.setId(userId);
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRegistered(rs.getTimestamp("registered"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                user.setRoles(new HashSet<>());
                userMap.put(userId, user);
            }
            String roleColumn = rs.getString("role");
            if (roleColumn != null) {
                user.getRoles().add(Role.valueOf(roleColumn));
            }
        }
        return new ArrayList<>(userMap.values());
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        JdbcUtil.validate(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            batchRolesUpdate(newKey.intValue(), user.getRoles(), user.getRoles().size());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            batchRolesUpdate(user.getId(), user.getRoles(), user.getRoles().size());
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT u.*, r.role FROM users u LEFT JOIN user_role r ON r.user_id = u.id WHERE id=?", RESULT_SET_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT u.*, r.role FROM users u LEFT JOIN user_role r ON r.user_id = u.id WHERE email=?", RESULT_SET_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT u.*, r.role FROM users u LEFT JOIN user_role r ON r.user_id = u.id ORDER BY name, email", RESULT_SET_EXTRACTOR);
    }

    private void batchRolesUpdate(int id, Set<Role> roles, int size) {
        jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", id);
        jdbcTemplate.batchUpdate("INSERT INTO user_role(user_id, role) values(?,?)",
                roles,
                size,
                (ps, argument) -> {
                    ps.setInt(1, id);
                    ps.setString(2, argument.name());
                });
    }
}
