import {Media} from "@/app/services/MediaService";

const testMovies: Media[] = [
    {
        id: "1",
        title: "Inception",
        posterUrl: "https://image.tmdb.org/t/p/original/xlaY2zyzMfkhk0HSC5VUwzoZPU1.jpg",
        backdropUrl: "https://image.tmdb.org/t/p/original/gqby0RhyehP3uRrzmdyUZ0CgPPe.jpg",
        logoUrl: "https://image.tmdb.org/t/p/original/8ThUfwQKqcNk6fTOVaWOts3kvku.png",
        type: "movie"
    },
    {
        id: "2",
        title: "The Matrix",
        posterUrl: "https://image.tmdb.org/t/p/original/p96dm7sCMn4VYAStA6siNz30G1r.jpg",
        backdropUrl: "https://image.tmdb.org/t/p/original/tlm8UkiQsitc8rSuIAscQDCnP8d.jpg",
        logoUrl: "https://image.tmdb.org/t/p/original/kA8phmxG7h4BIN061fiutckq9Ho.png",
        type: "movie"
    }
]

export function getMovies() {
    return testMovies;
}

export function getMovieById(id: string) {
    const movie = testMovies.find((movie) => movie.id === id);
    return movie || null;
}