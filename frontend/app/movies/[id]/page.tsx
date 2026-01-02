"use client";

import {getMovieById} from "@/app/services/MovieService";
import {Media} from "@/app/services/MediaService";
import {useParams} from "next/navigation";
import MediaPageInfo from "@/app/components/MediaPage/MediaPageInfo";

export default function MoviePage(){
    const movieId = useParams().id;
    const movie = getMovieById(movieId as string) as Media;

    return (
        <MediaPageInfo media={movie} />
    )
}