import {getAllMedia, Media} from "@/app/services/MediaService";

const testMovies: Media[] = getAllMedia().filter((media) => media.type === "movie");

export function getMovies() {
    return testMovies;
}

export function getMovieById(id: string) {
    const movie = testMovies.find((movie) => movie.id === id);
    return movie || null;
}