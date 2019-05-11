package com.iprogrammerr.time.ruler.model.user;

import com.iprogrammerr.time.ruler.database.DatabaseSession;
import com.iprogrammerr.time.ruler.database.Record;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseUsers implements Users {

    private final DatabaseSession session;

    public DatabaseUsers(DatabaseSession session) {
        this.session = session;
    }

    @Override
    public List<User> all() {
        return session.select(this::users, "SELECT * FROM user");
    }

    private List<User> users(ResultSet result) throws Exception {
        List<User> users = new ArrayList<>();
        while (result.next()) {
            users.add(new User(result));
        }
        return users;
    }

    @Override
    public List<User> allInactive() {
        return session.select(this::users, "SELECT * FROM user WHERE active = 0");
    }

    @Override
    public long create(String name, String email, String password) {
        return session.create(
            new Record(User.TABLE).put(User.NAME, name).put(User.EMAIL, email).put(User.PASSWORD, password)
                .put(User.ACTIVE, false)
        );
    }

    @Override
    public User user(long id) {
        return session.select(r -> mapOrThrow(r, "There is no user with %s id", String.valueOf(id)),
            "SELECT * FROM user WHERE id = ?", id);
    }

    private User mapOrThrow(ResultSet result, String exceptionTemplate, String identifier) throws Exception {
        if (result.next()) {
            return new User(result);
        }
        throw new RuntimeException(String.format(exceptionTemplate, identifier));
    }

    @Override
    public Optional<User> withEmail(String email) {
        return session.select(this::userFromResult, "SELECT * FROM user WHERE email = ?", email);
    }

    private Optional<User> userFromResult(ResultSet result) throws Exception {
        if (result.next()) {
            return Optional.of(new User(result));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> withName(String name) {
        return session.select(this::userFromResult, "SELECT * FROM user WHERE name = ?", name);
    }
}
