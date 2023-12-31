package com.github.makewheels.springboottemplate.user;

import com.github.makewheels.springboottemplate.user.bean.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class UserRepository {
    @Resource
    private MongoTemplate mongoTemplate;

    public User getById(String id) {
        return mongoTemplate.findById(id, User.class);
    }

    public boolean isUserExist(String id) {
        return mongoTemplate.exists(Query.query(Criteria.where("id").is(id)), User.class);
    }

    public User getByPhone(String phone) {
        Query query = Query.query(Criteria.where("phone").is(phone));
        return mongoTemplate.findOne(query, User.class);
    }

    public User getByToken(String token) {
        Query query = Query.query(Criteria.where("token").is(token));
        return mongoTemplate.findOne(query, User.class);
    }
}
