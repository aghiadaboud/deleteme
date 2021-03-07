package de.hhu.terminplaner.repos;

import de.hhu.terminplaner.model.Test2;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Test2Repository extends CrudRepository<Test2, Long> {

  @Query("select ")
  Test2 findTest2Bytest(Long pk);
}
