import {getAllMedia, Media} from "@/app/services/MediaService";

const movies: Media[] = getAllMedia().filter((media) => media.type === "movie");

export async function getMovies() {
    return movies;
}

export function getMovieById(id: number) {
    const movie = movies.find((movie) => movie.tmdbId === id);
    return movie || null;
}