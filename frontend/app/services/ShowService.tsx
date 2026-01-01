import {getAllMedia, Media} from "@/app/services/MediaService";

const testShows: Media[] = getAllMedia().filter((media) => media.type === "show");

export function getShows() {
    return testShows;
}

export function getShowById(id: string) {
    const show = testShows.find((show) => show.id === id);
    return show || null;
}