package com.project.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.entity.contact;

public interface entitycontact extends JpaRepository<contact, Integer>{

	@Query("from contact as c where c.user.id=:id")
	public List<contact> findcontactByuser(@Param("id")int id);
}
