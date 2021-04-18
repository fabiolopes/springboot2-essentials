package devdojo.springboot2.repository;

import devdojo.springboot2.domain.Anime;
import devdojo.springboot2.util.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@DataJpaTest
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save creates anime when successful")
    void save_PersistAnime_WhenSuccessful() {

        Anime animeToSaved = AnimeCreator.animeToBeSaved();

        Anime animeSaved = this.animeRepository.save(animeToSaved);

        Assertions.assertThat(animeSaved).isNotNull();

        Assertions.assertThat(animeSaved.getId()).isNotNull();

        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToSaved.getName());

    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_Removes_WhenSuccessful() {
        Anime animeToSaved = AnimeCreator.animeToBeSaved();

        Anime animeSaved = this.animeRepository.save(animeToSaved);

        animeRepository.delete(animeSaved);

        Optional<Anime> animeOptional = animeRepository.findById(animeSaved.getId());
        Assertions.assertThat(animeOptional).isEmpty();

    }

    @Test
    @DisplayName("Find by name returns list of anime when successful")
    void findByName_ReturnListOfAnime_WhenSuccessful() {
        Anime animeToSaved = AnimeCreator.animeToBeSaved();

        Anime animeSaved = this.animeRepository.save(animeToSaved);

        String name = animeSaved.getName();
        List<Anime> animes = animeRepository.findByName(name);

        Assertions.assertThat(animes).isNotEmpty().contains(animeSaved);

    }

    @Test
    @DisplayName("Find by name returns empty list when successful")
    void findByName_ReturnEmptyList_WhenSuccessful() {
        List<Anime> animes = animeRepository.findByName("Whatever");

        Assertions.assertThat(animes).isEmpty();

    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {
        Anime animeToSaved = new Anime();

/*        Assertions.assertThatThrownBy(()-> this.animeRepository.save(animeToSaved))
                .isInstanceOf(ConstraintViolationException.class);*/

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.animeRepository.save(animeToSaved))
                .withMessageContaining("The name cannot be empty");

    }

    @Test
    @DisplayName("Save updates anime when Successful")
    void save_UpdatesAnime_WhenSuccessful() {
        Anime animeToBeSaved = AnimeCreator.animeToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        animeSaved.setName("Overlord");

        Anime animeUpdated = this.animeRepository.save(animeSaved);

        Assertions.assertThat(animeUpdated).isNotNull();

        Assertions.assertThat(animeUpdated.getId()).isNotNull();

        Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeSaved.getName());

    }

}