package org.trhelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trhelper.domain.user.UserAvatar;

@Repository
public interface UserAvatarRepository extends JpaRepository<UserAvatar,Long> {
    UserAvatar findUserAvatarByName(String name);
}
