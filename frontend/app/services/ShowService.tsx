import {Media} from "@/app/services/MediaService";

const testShows: Media[] = [
    {
        id: "3",
        title: "Friends",
        posterUrl: "https://image.tmdb.org/t/p/original/2koX1xLkpTQM4IZebYvKysFW1Nh.jpg",
        backdropUrl: "https://image.tmdb.org/t/p/original/wGI8MPfv23B80AF5Yrg1Ss2mVCp.jpg",
        logoUrl: "https://image.tmdb.org/t/p/original/blVfE2u4uytU0f8yUO2XvhNSS2Y.png",
        type: "show"
    }
]

export function getShows() {
    return testShows;
}

export function getShowById(id: string) {
    const show = testShows.find((show) => show.id === id);
    return show || null;
}