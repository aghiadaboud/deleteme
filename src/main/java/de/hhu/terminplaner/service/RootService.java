package de.hhu.terminplaner.service;

import de.hhu.terminplaner.model.Root;
import de.hhu.terminplaner.model.Test;
import de.hhu.terminplaner.repos.RootRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class RootService {

  private RootRepository rootRepository;

  public RootService(RootRepository rootRepository) {
    this.rootRepository = rootRepository;
  }


  public void createRoot(String r) {
    Root test = new Root(r);
    rootRepository.save(test);
  }

  public void addTest(Test test3) {
    Optional<Root> test = rootRepository.findById(1L);
    test.get().addTest(test3);
    rootRepository.save(test.get());
  }

}
