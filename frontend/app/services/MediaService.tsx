import axios from "axios";

const instance = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

export type Media = {
    id: number | null;
    tmdbId: number;
    name: string;
    posterPath?: string;
    backdropPath?: string;
    logoPath?: string;
    runtime?: number;
    releaseDate: string;
    overview: string;
    type: "movie" | "tv";
}

let mediaList: Media[] = [];
let fetched = false;

async function fetchAllMedia(): Promise<Media[]> {
    if (fetched) return mediaList;
    try {
        const res = await instance.get("/metadata");
        mediaList = res.data;
    } catch (e) {
        console.error("Failed to fetch media list:", e);
        mediaList = [];
    }
    fetched = true;
    return mediaList;
}

export async function getAllMediaAsync(): Promise<Media[]> {
    return fetchAllMedia();
}

export function getAllMedia(): Media[] {
    return mediaList;
}

export function getMediaListByIds(ids: number[]): Media[] {
    return mediaList.filter((m) => m.id != null && ids.includes(m.id));
}

export function parseReleaseDateToYear(date: string): number {
    return new Date(date).getFullYear();
}

export const FALLBACK_IMAGE = "/globe.svg";
