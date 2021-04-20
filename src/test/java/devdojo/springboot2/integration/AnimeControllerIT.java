package devdojo.springboot2.integration;

import devdojo.springboot2.domain.Anime;
import devdojo.springboot2.repository.AnimeRepository;
import devdojo.springboot2.requests.AnimePostRequestBody;
import devdojo.springboot2.util.AnimeCreator;
import devdojo.springboot2.util.AnimePostRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AnimeRepository animeRepository;

/*    @LocalServerPort
    private int port;*/

    @Test
    @DisplayName("List returns list of anime inside page when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {

        Anime animeSaved = animeRepository.save(AnimeCreator.animeToBeSaved());

        String expectedName = animeSaved.getName();
        List<Anime> animePage = testRestTemplate.exchange("/animes/all", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animePage).isNotNull().isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("Find by id returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful() {
        Anime animeSaved = animeRepository.save(AnimeCreator.animeToBeSaved());

        Long expectedId = animeSaved.getId();
        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);

        Assertions.assertThat(anime)
                .isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);

    }

    @Test
    @DisplayName("findByName returns an list of anime when anime found")
    void findByName_ReturnsListOfAnimes_WhenAnimeFound() {
        Anime animeSaved = animeRepository.save(AnimeCreator.animeToBeSaved());

        String expectedName = animeSaved.getName();
        String url = String.format("/animes/find?name=%s", expectedName);
        List<Anime> animes = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>(){}).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty();

    }

    @Test
    @DisplayName("findByName returns an empty list of anime when anime not found")
    void findByName_ReturnsEmptyListOfAnimes_WhenAnimeIsNotFound() {
        List<Anime> animes = testRestTemplate.exchange("/animes/find?name=john_doe", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>(){}).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();

    }


    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestbody();

        ResponseEntity<Anime> animeResponseEntity =
                testRestTemplate.postForEntity("/animes", animePostRequestBody, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();

    }


    @Test
    @DisplayName("Replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessful() {

        Anime animeSaved = animeRepository.save(AnimeCreator.animeToBeSaved());

        animeSaved.setName("New name");

        ResponseEntity<Void> animeResponseEntity =
                testRestTemplate.exchange("/animes", HttpMethod.PUT, new HttpEntity<>(animeSaved), Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful() {

        Anime animeSaved = animeRepository.save(AnimeCreator.animeToBeSaved());

        ResponseEntity<Void> animeResponseEntity =
                testRestTemplate.exchange("/animes/{id}", HttpMethod.DELETE, null, Void.class, animeSaved.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
