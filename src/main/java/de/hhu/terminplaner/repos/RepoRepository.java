package de.hhu.terminplaner.repos;

import de.hhu.terminplaner.model.Repo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoRepository extends CrudRepository<Repo, Long> {

    List<Repo> findAll();
}
