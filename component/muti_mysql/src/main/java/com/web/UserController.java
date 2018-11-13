package com.web;

import com.mapper.test2.User2Mapper;
import com.entity.UserEntity;
import com.mapper.repo.User1Repo;
import com.mapper.test1.User1Mapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private User1Mapper user1Mapper;

	@Autowired
	private User2Mapper user2Mapper;

    @Autowired
    private User1Repo user1Repo;
	
	@RequestMapping("/getUsers")
	public List<UserEntity> getUsers() {
		List<UserEntity> users=user1Repo.getAll();
		return users;
	}
	
    @RequestMapping("/getUser")
    public UserEntity getUser(Long id) {
    	UserEntity user=user1Repo.getOne(id);
        return user;
    }
    
    @RequestMapping("/add")
    public void save(UserEntity user) {
        user1Repo.insert(user);
    }
    
    @RequestMapping(value="update")
    public void update(UserEntity user) {
        user1Repo.update(user);
    }
    
    @RequestMapping(value="/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        user1Repo.delete(id);
    }
    
    
}