package com.ksteindl.logolo.repositories;

import com.ksteindl.logolo.domain.Backlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long> {

    Backlog findByProjectKey(String projectKey);

}
