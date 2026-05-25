import {getAllMedia, Media} from "@/app/services/MediaService";
import axios from "axios";

const instance = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

export type ShowEpisode = {
    id: number;
    episodeNumber: number;
    name: string;
    stillPath: string;
    overview: string;
}

export type ShowSeason = {
    seasonNumber: number;
    episodes: ShowEpisode[];
}

const shows: Media[] = getAllMedia().filter((media) => media.type === "tv");

export async function getShows() {
    return shows;
}

export function getShowById(id: number) {
    const show = shows.find((show) => show.tmdbId === id);
    return show || null;
}

export async function getShowSeasons(id: number) {
    return await instance.get(`/metadata/shows/${id}/seasons`);
}