package devdojo.springboot2.service;

import devdojo.springboot2.domain.Anime;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeService {

    public List<Anime> listAll() {
        return List.of(new Anime(1l, "DBZ"), new Anime(2l,"Berserk"));
    }

}
