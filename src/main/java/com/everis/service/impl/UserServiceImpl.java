package com.everis.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.everis.dao.UserGenericDAO;
import com.everis.dto.ApplicationDTO;
import com.everis.dto.ArticleDTO;
import com.everis.dto.UserDTO;
import com.everis.entity.Application;
import com.everis.entity.Article;
import com.everis.entity.User;
import com.everis.service.UserService;
import com.everis.transformers.AbstractTransformer;
import com.everis.transformers.ApplicationTransformer;
import com.everis.transformers.ArticleTransformer;
import com.everis.transformers.UserTransformer;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserGenericDAO userGenericDAO;

	private AbstractTransformer<Application,ApplicationDTO> applicationTransformer = new ApplicationTransformer();
	
	private AbstractTransformer<Article,ArticleDTO> articleTransformer = new ArticleTransformer();
	
	private AbstractTransformer<User,UserDTO> userTransformer = new UserTransformer(applicationTransformer,articleTransformer);
	
	@Override
	public UserDTO create(UserDTO userDTO) {

		User user = userTransformer.toEntity(userDTO);
		User userResult = userGenericDAO.create(user);
		UserDTO userDTOResult = userTransformer.toDTO(userResult);
		
		return userDTOResult;
	}

	@Override
	public UserDTO update(UserDTO userDTO) {
		
		User user = userTransformer.toEntity(userDTO);
		User userResult = userGenericDAO.update(user);
		
		UserDTO userDTOResult = null;
		if(userResult != null)
		   userDTOResult = userTransformer.toDTO(userResult);
		
		return userDTOResult;
	}

	@Override
	public String delete(Integer k) {
		
		User user = userGenericDAO.delete(k);
		
		String result = "Error";
		if(user != null)
			result = "Success";
	
		return result;
	}

	@Override
	public Optional<UserDTO> getById(Integer k) {
		
		Optional<User> userOptional = userGenericDAO.getById(k);
	
		User user = null;
		UserDTO userDTOResult = null;
		if(userOptional.isPresent())
		{
			user = userOptional.get();
			userDTOResult = userTransformer.toDTO(user);
		}

		return Optional.ofNullable(userDTOResult);
	}

	@Override
	public List<UserDTO> getAll() {
		
		List<User> users = userGenericDAO.getAll();
		List<UserDTO> usersDTO = userTransformer.toDTOList(users);
		return usersDTO;
	}
	
}
