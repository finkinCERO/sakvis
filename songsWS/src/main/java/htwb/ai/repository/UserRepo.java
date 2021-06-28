package htwb.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import htwb.ai.model.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {

	//	Optional<DAOUser> findByUsername(String username);
	Users findByUsername(String username);
}