"use client";

import { Box, CircularProgress, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { Media, getMediaListByIds } from "../services/MediaService";
import ScrollableImageList from "@/app/components/Home/ScrollableImageList";
import { getMoviesAsync } from "@/app/services/MovieService";
import { getShowsAsync } from "@/app/services/ShowService";
import { useUser } from "@/app/hooks/UserHook";
import { getAllProgress } from "@/app/services/ProgressService";

export default function HomePage() {
    const [movies, setMovies] = useState<Media[]>([]);
    const [shows, setShows] = useState<Media[]>([]);
    const [continueWatching, setContinueWatching] = useState<Media[]>([]);
    const [loading, setLoading] = useState(true);

    const { user } = useUser();

    useEffect(() => {
        const loadData = async () => {
            setLoading(true);
            try {
                const [fetchedMovies, fetchedShows] = await Promise.all([
                    getMoviesAsync(),
                    getShowsAsync(),
                ]);
                setMovies(fetchedMovies);
                setShows(fetchedShows);

                if (user) {
                    const res = await getAllProgress(user);
                    if (res != null) {
                        const ids = res.data as number[];
                        setContinueWatching(getMediaListByIds(ids));
                    }
                }
            } catch (e) {
                console.error("Failed to load home page data:", e);
            } finally {
                setLoading(false);
            }
        };
        loadData();
    }, [user]);

    if (loading) {
        return (
            <Box sx={{ display: "flex", justifyContent: "center", mt: 10 }}>
                <CircularProgress />
            </Box>
        );
    }

    return (
        <div>
            <Box sx={{ mt: 3 }}>
                {user && continueWatching.length > 0 && (
                    <div>
                        <Typography variant="h4" sx={{ ml: 3, mb: 2 }}>Continue Watching</Typography>
                        <ScrollableImageList continueWatching items={continueWatching} alignment={"horizontal"} />
                    </div>
                )}

                <Typography variant="h4" sx={{ ml: 3, mb: 2 }}>Movies</Typography>
                <ScrollableImageList items={movies} alignment={"vertical"} />

                <Typography variant="h4" sx={{ ml: 3, mb: 2 }}>Shows</Typography>
                <ScrollableImageList items={shows} alignment={"vertical"} />
            </Box>
        </div>
    );
}