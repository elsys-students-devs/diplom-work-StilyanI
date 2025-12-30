"use client";

import { useEffect, useState} from "react";
import { getMovies, Movie} from "@/app/services/MovieService";
import { Box } from "@mui/material";
import MediaGrid from "@/app/components/common/MediaGrid";


export default function MoviesPage(){
    const [movieList, setMovieList] = useState<Movie[]>([]);

    useEffect(() => {
        const movies = getMovies();
        setMovieList(movies);
    })

    return (
        <Box sx={{ mt: 3 }}>
            <MediaGrid items={movieList}/>
        </Box>
    )
}