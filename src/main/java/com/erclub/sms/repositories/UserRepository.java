package com.erclub.sms.repositories;

import com.erclub.sms.models.ErcUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserRepository extends JpaRepository<ErcUser, String> {
  Optional<ErcUser> findByLoginId(String loginId);

  @Transactional
  @Modifying
  @Query("UPDATE ErcUser SET NAME = ?2, SUSPEND_YN = ?3 WHERE ID = ?1")
  void updateSuspendYnByLoginId(String id, String name, String suspendYn);
}
