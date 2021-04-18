package devdojo.springboot2.util;

import devdojo.springboot2.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {

    public static AnimePostRequestBody createAnimePostRequestbody() {
        return AnimePostRequestBody.builder()
                .name(AnimeCreator.createValidAnime().getName())
                .build();
    }
}
