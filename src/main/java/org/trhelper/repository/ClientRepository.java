package org.trhelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.trhelper.domain.user.Client;
import org.trhelper.domain.user.UserAvatar;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client,Long> {
    Client findClientByUsername(String username);

    @Modifying
    @Query("update Client p set p.userAvatar=?1 where p.username = ?2")
    void updateUserAvatar(UserAvatar userAvatar, String username);
}
