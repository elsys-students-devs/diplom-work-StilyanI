"use client";

import { useEffect, useState } from "react";
import { getMoviesAsync } from "@/app/services/MovieService";
import { Box, CircularProgress } from "@mui/material";
import MediaGrid from "@/app/components/common/MediaGrid";
import { Media } from "@/app/services/MediaService";

export default function MoviesPage() {
    const [movieList, setMovieList] = useState<Media[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getMoviesAsync()
            .then(setMovieList)
            .finally(() => setLoading(false));
    }, []);

    if (loading) {
        return (
            <Box sx={{ display: "flex", justifyContent: "center", mt: 10 }}>
                <CircularProgress />
            </Box>
        );
    }

    return (
        <Box sx={{ mt: 3 }}>
            <MediaGrid items={movieList} />
        </Box>
    );
}