import {getAllMedia, getAllMediaAsync, Media} from "@/app/services/MediaService";

export async function getMoviesAsync(): Promise<Media[]> {
    const all = await getAllMediaAsync();
    return all.filter((media) => media.type === "movie");
}

export function getMovies(): Media[] {
    return getAllMedia().filter((media) => media.type === "movie");
}

export function getMovieById(id: number): Media | null {
    const movies = getAllMedia().filter((media) => media.type === "movie");
    return movies.find((movie) => movie.tmdbId === id) || null;
}