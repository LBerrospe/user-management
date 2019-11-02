package com.drawsforall.user.management.persistence;

import com.drawsforall.user.management.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT mail FROM USER mail where LOWER(mail.email) = LOWER(?1) ", nativeQuery = true)
    List<User> find(@Param("email") String email);

    Boolean findByEmailLike(String email);



}
