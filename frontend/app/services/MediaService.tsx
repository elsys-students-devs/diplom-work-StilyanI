export type Media = {
    id: string;
    title: string;
    posterUrl: string;
    backdropUrl: string;
    logoUrl: string;
    type: "movie" | "show";
}

const testMedia: Media[] = [
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
    },
    {
        id: "3",
        title: "Friends",
        posterUrl: "https://image.tmdb.org/t/p/original/2koX1xLkpTQM4IZebYvKysFW1Nh.jpg",
        backdropUrl: "https://image.tmdb.org/t/p/original/wGI8MPfv23B80AF5Yrg1Ss2mVCp.jpg",
        logoUrl: "https://image.tmdb.org/t/p/original/blVfE2u4uytU0f8yUO2XvhNSS2Y.png",
        type: "show"
    }
]

export function getAllMedia(): Media[] {
    return testMedia;
}

export function getMediaById(id: string): Media | null {
    const media = testMedia.find((m) => m.id === id);
    return media || null;
}