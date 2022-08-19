package com.alexandros.teleram.bot.repositories;

import com.alexandros.teleram.bot.model.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepository extends MongoRepository<UserInfo,String> {

    @Query("{name: '?0'}")
    List<UserInfo> findByName(String name);

    @Query("{mobilePhone: '?0'}")
    UserInfo findByMobilePhone (String mobilePhone);

    @Query(value="{}",fields="{'id' : 1, 'name' : 1, 'afm':1, 'amka':1, 'mobilePhone':1,'email': 1}")
    List<UserInfo> findAll();

}
