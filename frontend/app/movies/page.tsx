"use client";

import { useEffect, useState} from "react";
import { getMovies } from "@/app/services/MovieService";
import { Box } from "@mui/material";
import MediaGrid from "@/app/components/common/MediaGrid";
import {Media} from "@/app/services/MediaService";


export default function MoviesPage(){
    const movies = getMovies();
    const [movieList, setMovieList] = useState<Media[]>([]);

    useEffect(() => {
        setMovieList(movies);
    }, [movies])

    return (
        <Box sx={{ mt: 3 }}>
            <MediaGrid items={movieList}/>
        </Box>
    )
}