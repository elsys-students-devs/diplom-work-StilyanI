"use client";

import { useEffect, useState} from "react";
import { Box } from "@mui/material";
import MediaGrid from "@/app/components/common/MediaGrid";
import {Media} from "@/app/services/MediaService";
import {getShows} from "@/app/services/ShowService";


export default function MoviesPage(){
    const shows = getShows();
    const [showList, setShowList] = useState<Media[]>([]);

    useEffect(() => {
        setShowList(shows);
    }, [shows])

    return (
        <Box sx={{ mt: 3 }}>
            <MediaGrid items={showList}/>
        </Box>
    )
}