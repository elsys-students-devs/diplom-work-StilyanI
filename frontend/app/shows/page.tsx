"use client";

import { useEffect, useState} from "react";
import {Box, Typography} from "@mui/material";
import MediaGrid from "@/app/components/common/MediaGrid";
import {Media} from "@/app/services/MediaService";
import {getShows} from "@/app/services/ShowService";


export default function MoviesPage(){
    const [showList, setShowList] = useState<Media[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function loadShows() {
            const shows = await getShows();
            setShowList(shows);
            setLoading(false);
        }

        loadShows();
    }, []);

    if (loading) {
        return <Typography variant="h2" className={"list-page-info-text"}>Loading...</Typography>;
    }

    return (
        <Box sx={{ mt: 3 }}>
            {showList.length > 0 ?
                <MediaGrid items={showList}/>
                :
                <Typography variant="h2" className={"list-page-info-text"}>No shows found</Typography>
            }
        </Box>
    )
}