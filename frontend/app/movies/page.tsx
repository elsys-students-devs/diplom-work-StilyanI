"use client";

import { useEffect, useState} from "react";
import { getMovies } from "@/app/services/MovieService";
import {Box, Typography} from "@mui/material";
import MediaGrid from "@/app/components/common/MediaGrid";
import {Media} from "@/app/services/MediaService";


export default function MoviesPage(){
    const [movieList, setMovieList] = useState<Media[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function loadMovies() {
            const movies = await getMovies();
            setMovieList(movies);
            setLoading(false);
        }

        loadMovies();
    }, []);

    if (loading) {
        return <Typography variant="h2" className={"list-page-info-text"}>Loading...</Typography>;
    }

    return (
        <Box sx={{ mt: 3 }}>
            {movieList.length > 0 ?
                <MediaGrid items={movieList}/>
                :
                <Typography variant="h2" sx={{color: "gray", textAlign: "center", mt: 5}}>No movies found</Typography>
            }
        </Box>
    )
}