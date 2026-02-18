import axios from "axios";

const instance = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
        "Content-Type": "application/json",
    },
})

export type Media = {
    id: number | null;
    tmdbId: number;
    name: string;
    posterPath: string;
    backdropPath: string;
    logoPath: string;
    runtime?: number;
    releaseDate: string;
    overview: string;
    type: "movie" | "tv";
}

let mediaList: Media[];
const res = await instance.get("/metadata");
mediaList = res.data;

export function getAllMedia(): Media[] {
    return mediaList;
}

export function getMediaById(id: number): Media | null {
    const media = mediaList.find((m) => m.id === id);
    return media || null;
}

export function parseReleaseDateToYear(date : string){
    return new Date(date).getFullYear();
}