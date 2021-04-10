package devdojo.springboot2.repository;

import devdojo.springboot2.domain.Anime;

import java.util.List;

public interface AnimeRepository {
    List<Anime> listAll();
}
