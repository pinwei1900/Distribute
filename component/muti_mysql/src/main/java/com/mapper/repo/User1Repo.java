/*
 * Copyright (c) 2018年11月12日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package com.mapper.repo;

import com.entity.UserEntity;
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

    private String full(String name) {
        return "com.mapper.test1.User1Mapper." + name;
    }

    public List<UserEntity> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList(full("getAll"));
        }
    }

    public UserEntity getOne(Long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne(full("getOne"), id);
        }
    }

    public void insert(UserEntity user) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.insert(full("insert"), user);
        }
    }

    public void update(UserEntity user) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.update(full("update"), user);
        }
    }

    public void delete(Long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.delete(full("delete"), id);
        }
    }
}