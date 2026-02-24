"use client";

import { Box, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import {Media, getMediaListByIds} from "../services/MediaService";
import ScrollableImageList from "@/app/components/Home/ScrollableImageList";
import {getMovies} from "@/app/services/MovieService";
import {getShows} from "@/app/services/ShowService";
import {useUser} from "@/app/hooks/UserHook";
import {getAllProgress} from "@/app/services/ProgressService";

export default function HomePage() {
    const [movies, setMovies] = useState<Media[]>([]);
    const [shows, setShows] = useState<Media[]>([]);
    const [continueWatching, setContinueWatching] = useState<Media[]>([]);
    const [progress, setProgress] = useState<number[]>([]);

    const {user} = useUser();

    useEffect(() => {
        const fetched = async () => {
            const res = await getAllProgress(user);
            if(res != null) {
                setProgress(res.data as number[]);
            }
        }
        fetched();
    }, [user]);

    useEffect(() => {
        const watchedMedia = getMediaListByIds(progress);
        setContinueWatching(watchedMedia);

        setMovies(getMovies());
        setShows(getShows());
    }, [progress])

    return(
        <div>
            <Box sx={{mt: 3}}>
                {user && continueWatching.length > 0 &&
                    <div>
                        <Typography variant="h4" sx={{ml: 3, mb: 2}}>Continue Watching</Typography>
                        <ScrollableImageList continueWatching items={continueWatching} alignment={"horizontal"}/>
                    </div>
                }

                <Typography variant="h4" sx={{ml: 3, mb: 2}}> Movies</Typography>
                <ScrollableImageList items={movies} alignment={"vertical"}/>

                <Typography variant="h4" sx={{ml: 3, mb: 2}}>Shows</Typography>
                <ScrollableImageList items={shows} alignment={"vertical"}/>
            </Box>
        </div>
    )
}