import axios from "axios";

const instance = axios.create({
    baseURL: "http://localhost:8080",
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