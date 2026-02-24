import axios from "axios";
import {User} from "@/app/contexts/UserContext";

const instance = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
        "Content-Type": "application/json",
    },
})

export async function getAllProgress(user: User | null) {
    if (!user) return null;
    try {
        return await instance.get(`/users/video-progress/${user.id}`);
    } catch (e) {
        console.error("Failed to fetch progress:", e);
        return null;
    }
}

export async function getProgress(user: User | null, videoId: number) {
    if (!user) return null;
    try {
        return await instance.get(`/users/video-progress`, {
            params: { userId: user.id, videoId }
        });
    } catch (e) {
        console.error("Failed to fetch video progress:", e);
        return null;
    }
}

export async function saveProgress(user: User | null, videoId: number, progressSeconds: number, progressPercent: number) {
    if (!user) return;

    try {
        return await instance.put("/users/video-progress", {
            userId: user.id,
            videoId,
            progressSeconds,
            progressPercent
        });
    } catch (e) {
        console.error("Failed to save progress:", e);
    }
}