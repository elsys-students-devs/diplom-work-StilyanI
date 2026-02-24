import {getAllMedia, getAllMediaAsync, Media} from "@/app/services/MediaService";
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

export async function getShowsAsync(): Promise<Media[]> {
    const all = await getAllMediaAsync();
    return all.filter((media) => media.type === "tv");
}

export function getShows(): Media[] {
    return getAllMedia().filter((media) => media.type === "tv");
}

export function getShowById(id: number): Media | null {
    const shows = getAllMedia().filter((media) => media.type === "tv");
    return shows.find((show) => show.tmdbId === id) || null;
}

export async function getShowSeasons(id: number) {
    return await instance.get(`/metadata/shows/${id}/seasons`);
}