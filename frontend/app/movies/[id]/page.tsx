"use client";

import { getMovieById } from "@/app/services/MovieService";
import { useParams } from "next/navigation";
import MediaPageInfo from "@/app/components/MediaPage/MediaPageInfo";
import { Box, Typography } from "@mui/material";

export default function MoviePage() {
    const movieId = useParams().id;
    const movie = getMovieById(Number(movieId));

    if (!movie) {
        return (
            <Box sx={{ display: "flex", justifyContent: "center", mt: 10 }}>
                <Typography variant="h5">Movie not found</Typography>
            </Box>
        );
    }

    return (
        <Box sx={{ pb: 10 }}>
            <MediaPageInfo media={movie} />
        </Box>
    );
}