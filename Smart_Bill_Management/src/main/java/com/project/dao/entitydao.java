package com.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.entity.user;

public interface entitydao extends JpaRepository<user, Integer>{
	@Query("select u from user u where u.email = :email")
	public user getuserbyname(@Param("email")String email);
}
