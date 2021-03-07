package de.hhu.terminplaner.repos;

import de.hhu.terminplaner.model.Root;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RootRepository extends CrudRepository<Root, Long> {
}
