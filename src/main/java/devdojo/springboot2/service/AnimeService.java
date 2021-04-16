package devdojo.springboot2.service;

import devdojo.springboot2.domain.Anime;
import devdojo.springboot2.exception.BadRequestException;
import devdojo.springboot2.mapper.AnimeMapper;
import devdojo.springboot2.repository.AnimeRepository;
import devdojo.springboot2.requests.AnimePostRequestBody;
import devdojo.springboot2.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public Page<Anime> listAll(Pageable pageable) {
        return animeRepository.findAll(pageable);
    }

    public List<Anime> listAllNonPageable() {
        return animeRepository.findAll();
    }

    public List<Anime> findByName(String name) {
        return animeRepository.findByName(name);
    }

    public  Anime findByIdOrThrowBadRequestExdception(long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not found"));
    }

    public Anime save(AnimePostRequestBody animePostRequestBody) {
        Anime save = animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
        return save;
    }

    public void delete(long id) {
        animeRepository.delete(findByIdOrThrowBadRequestExdception(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        Anime savedAnime = findByIdOrThrowBadRequestExdception(animePutRequestBody.getId());
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
        anime.setId(savedAnime.getId());
        animeRepository.save(anime);
    }
}
