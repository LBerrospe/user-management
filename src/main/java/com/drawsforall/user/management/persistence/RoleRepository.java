package com.drawsforall.user.management.persistence;

import com.drawsforall.user.management.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "SELECT * FROM role WHERE name IN (:roles)", nativeQuery = true)
    Set<Role> findAllByNameIn(@Param("roles") List<String> roles);
}
