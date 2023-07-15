package org.trhelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.trhelper.domain.order.OrderStatus;
import org.trhelper.domain.user.Client;
import org.trhelper.domain.user.Driver;
import org.trhelper.domain.user.UserAvatar;

import java.util.List;

@Repository
@Transactional
public interface DriverRepository extends JpaRepository<Driver,Long> {
    Driver findDriverByUsername(String username);

    @Modifying
    @Query("update Driver p set p.userAvatar=?1 where p.username = ?2")
    void updateUserAvatar(UserAvatar userAvatar,String username);

    List<Driver> findByLastNameContainingOrFirstNameContaining(String part, String part1);
}
