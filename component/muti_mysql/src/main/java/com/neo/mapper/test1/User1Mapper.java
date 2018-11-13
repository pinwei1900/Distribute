package com.neo.mapper.test1;

import com.neo.entity.UserEntity;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface User1Mapper {
	
	List<UserEntity> getAll();
	
	UserEntity getOne(Long id);

	void insert(UserEntity user);

	void update(UserEntity user);

	void delete(Long id);

}