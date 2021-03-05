package de.hhu.terminplaner.service;

import de.hhu.terminplaner.repos.RepoRepository;
import org.springframework.stereotype.Service;

@Service
public class RepoService {

    private RepoRepository repoRepository;

    public RepoService(RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }
}
