import axios from "axios";
import {User} from "@/app/contexts/UserContext";

const instance = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
        "Content-Type": "application/json",
    },
})

export async function getAllProgress(user: User | null) {
    if (user) {
        return await instance.get(`/users/video-progress/${user.id}`);
    } else {
        return null;
    }
}

export async function getProgress(user: User | null, videoId: number) {
    if (user) {
        return await instance.get(`/users/video-progress?userId=${user.id}&videoId=${videoId}`);
    } else {
        return null;
    }
}

export async function saveProgress(user: User | null, videoId: number, progressSeconds: number, progressPercent: number) {
    if(!user){
        console.error("User is needed to save progress");
        return;
    }

    const progress = {
        userId: user.id,
        videoId,
        progressSeconds: progressSeconds,
        progressPercent: progressPercent
    }

    return await instance.put("/users/video-progress", progress);
}