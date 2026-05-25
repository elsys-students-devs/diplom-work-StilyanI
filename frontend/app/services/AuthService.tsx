import axios from "axios";

const instance = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
        "Content-Type": "application/json",
    },
})

interface AuthDto {
    username: string;
    password: string;
}

export async function getUser(userId: string){
    return await instance.get("/users", {params: {userId} });
}

export async function login(formData: AuthDto){
    return await instance.post(`/users/login`, formData);
}

export async function register(formData: AuthDto){
    return await instance.post(`/users/register`, formData);
}