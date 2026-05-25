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
    const [loading, setLoading] = useState(true);

    const {user} = useUser();

    function renderMovieList() {
        if (loading) return <Typography variant="h5" sx={{ml: 5, mb: 2, color: "gray"}}>Loading...</Typography>;

        if(movies.length > 0) return <ScrollableImageList items={movies} alignment={"vertical"}/>;
        else return <Typography variant="h5" sx={{ml: 5, mb: 2, color: "gray"}}>No movies found</Typography>;
    }

    function renderShowList() {
        if (loading) return <Typography variant="h5" sx={{ml: 5, mb: 2, color: "gray"}}>Loading...</Typography>;

        if(shows.length > 0) return <ScrollableImageList items={shows} alignment={"vertical"}/>;
        else return <Typography variant="h5" sx={{ml: 5, mb: 2, color: "gray"}}>No shows found</Typography>;
    }

    useEffect(() => {
        async function loadMedia() {
            const moviesRes = await getMovies();
            const showsRes = await getShows();

            setMovies(moviesRes);
            setShows(showsRes);
            setLoading(false);
        }

        loadMedia();
    }, []);

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
                {renderMovieList()}

                <Typography variant="h4" sx={{ml: 3, mb: 2}}>Shows</Typography>
                {renderShowList()}
            </Box>
        </div>
    )
}