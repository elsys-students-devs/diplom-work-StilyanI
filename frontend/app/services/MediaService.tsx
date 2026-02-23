import axios from "axios";
console.log("backend url: " + process.env.NEXT_PUBLIC_API_URL);
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
    posterPath: string;
    backdropPath: string;
    logoPath: string;
    runtime?: number;
    releaseDate: string;
    overview: string;
    type: "movie" | "tv";
}

let mediaList: Media[] = [];
try {
    const res = await instance.get("/metadata");
    mediaList = res.data;
} catch (e) {
    console.error(e);
}


export function getAllMedia(): Media[] {
    return mediaList;
}

export function getMediaListByIds(ids: number[]): Media[] {
    return mediaList.filter((m) => m.id != null && ids.includes(m.id));
}

export function parseReleaseDateToYear(date : string){
    return new Date(date).getFullYear();
}