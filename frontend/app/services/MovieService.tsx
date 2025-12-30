export type Movie = {
    id: string,
    title: string,
    posterUrl: string
}

const testMovies: Movie[] = [
    {
        id: "1",
        title: "Inception",
        posterUrl: "https://image.tmdb.org/t/p/original/xlaY2zyzMfkhk0HSC5VUwzoZPU1.jpg",
    },
    {
        id: "2",
        title: "The Matrix",
        posterUrl: "https://image.tmdb.org/t/p/original/p96dm7sCMn4VYAStA6siNz30G1r.jpg",
    }
]

export function getMovies() {
    return testMovies;
}

export function getMovieById(id: string) {
    const movie = testMovies.find((movie) => movie.id === id);
    return movie || null;
}