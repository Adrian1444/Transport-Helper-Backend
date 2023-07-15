package org.trhelper.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.trhelper.domain.user.Driver;
import org.trhelper.domain.user.UserPost;

import java.util.List;

public interface PostRepository extends JpaRepository<UserPost,Long> {

    List<UserPost> findByPostCreatorIn(List<Driver> drivers, Sort sort);

}
