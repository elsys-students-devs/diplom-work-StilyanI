"use client";

import { Box, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { Media, getAllMedia } from "../services/MediaService";
import ScrollableImageList from "@/app/components/Home/ScrollableImageList";

export default function HomePage() {
    const [continueWatching, setContinueWatching] = useState<Media[]>([]);

    useEffect(() => {
        const media = getAllMedia();
        setContinueWatching(media);
    }, [])

    return(
        <div>
            <Box sx={{mt: 3}}>
                <Typography variant="h4" sx={{ml: 3, mb: 2}}>Continue Watching</Typography>
                <ScrollableImageList items={continueWatching} position={"horizontal"}/>

                <Typography variant="h4" sx={{ml: 3, mb: 2}}>Recently Added Movies</Typography>
                <ScrollableImageList items={continueWatching} position={"vertical"}/>

                <Typography variant="h4" sx={{ml: 3, mb: 2}}>Recently Added Shows</Typography>
                <ScrollableImageList items={continueWatching} position={"vertical"}/>
            </Box>
        </div>
    )
}