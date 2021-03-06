package de.hhu.terminplaner.repos;

import de.hhu.terminplaner.model.Test;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends CrudRepository<Test, Long> {

}
