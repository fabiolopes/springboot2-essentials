package devdojo.springboot2.util;

import devdojo.springboot2.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {

    public static AnimePutRequestBody createAnimePustRequestbody() {
        return AnimePutRequestBody.builder()
                .name(AnimeCreator.createValidUpdatedAnime().getName())
                .id(AnimeCreator.createValidUpdatedAnime().getId())
                .build();
    }
}
