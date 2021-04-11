package devdojo.springboot2.service;

import devdojo.springboot2.domain.Anime;
import devdojo.springboot2.repository.AnimeRepository;
import devdojo.springboot2.requests.AnimePostRequestBody;
import devdojo.springboot2.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public List<Anime> listAll() {
        return animeRepository.findAll();
    }

    public  Anime findByIdOrThrowBadRequestExdception(long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found"));
    }

    public Anime save(AnimePostRequestBody animePostRequestBody) {
        return animeRepository.save(Anime.builder().name(animePostRequestBody.getName()).build());
    }

    public void delete(long id) {
        animeRepository.delete(findByIdOrThrowBadRequestExdception(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        Anime savedAnime = findByIdOrThrowBadRequestExdception(animePutRequestBody.getId());
        Anime anime = Anime.builder().name(animePutRequestBody.getName()).id(savedAnime.getId()).build();
        animeRepository.save(anime);
    }
}
