"use client";

import {getMovieById} from "@/app/services/MovieService";
import {Media} from "@/app/services/MediaService";
import {useParams} from "next/navigation";
import MediaPageInfo from "@/app/components/MediaPage/MediaPageInfo";
import { Box } from "@mui/material";

export default function MoviePage(){
    const movieId = useParams().id;
    const movie = getMovieById(Number(movieId)) as Media;

    console.log(movie);
    return (
        <Box sx={{pb: 10}}>
            <MediaPageInfo media={movie} />
        </Box>
    )
}