/*
 * Copyright (c) 2018年11月12日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package com.neo.mapper.repo;

import com.neo.entity.UserEntity;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/12
 * @Version 1.0.0
 */
@Repository
public class User1Repo {

    @Autowired
    @Qualifier("test1SqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;


    public List<UserEntity> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList("com.neo.mapper.test1.User1Mapper.getAll");
        }
    }

    public UserEntity getOne(Long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne("com.neo.mapper.test1.User1Mapper.getOne", id);
        }
    }

    public void insert(UserEntity user) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.insert("com.neo.mapper.test1.User1Mapper.insert", user);
        }
    }

    public void update(UserEntity user) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update("com.neo.mapper.test1.User1Mapper.update", user);
        }
    }

    public void delete(Long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.delete("com.neo.mapper.test1.User1Mapper.delete", id);
        }
    }
}