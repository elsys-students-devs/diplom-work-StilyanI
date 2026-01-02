import {getAllMedia, Media} from "@/app/services/MediaService";

const testShows: Media[] = getAllMedia().filter((media) => media.type === "show");

export type ShowEpisode = {
    number: number;
    title: string;
    stillUrl: string;
    description: string;
}

export type ShowSeason = {
    showId: string;
    number: number;
    episodes: ShowEpisode[];
}

const testSeasons: ShowSeason[] = [
    {
        showId: "3",
        number: 1,
        episodes: [
            {
                number: 1,
                title: "Pilot",
                stillUrl: "https://image.tmdb.org/t/p/original/uboMk8oH0OcS33TmLnC4lvgk3t8.jpg",
                description: "something happens",
            },
            {
                number: 2,
                title: "Pilot",
                stillUrl: "https://image.tmdb.org/t/p/original/uboMk8oH0OcS33TmLnC4lvgk3t8.jpg",
                description: "something happens",
            }
        ]
    },
    {
        showId: "3",
        number: 2,
        episodes: [
            {
                number: 1,
                title: "Not Pilot",
                stillUrl: "https://image.tmdb.org/t/p/original/uboMk8oH0OcS33TmLnC4lvgk3t8.jpg",
                description: "something happens again",
            }
        ]
    },
]

export function getShows() {
    return testShows;
}

export function getShowById(id: string) {
    const show = testShows.find((show) => show.id === id);
    return show || null;
}

export function getShowSeasons(id: string) {
    return testSeasons.filter((season) => season.showId === id);
}