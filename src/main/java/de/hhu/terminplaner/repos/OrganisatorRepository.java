package de.hhu.terminplaner.repos;


import de.hhu.terminplaner.model.Organisator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisatorRepository extends CrudRepository<Organisator, Long> {
}
