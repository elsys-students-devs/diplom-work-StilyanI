"use client";

import { useEffect, useState } from "react";
import { Box, CircularProgress } from "@mui/material";
import MediaGrid from "@/app/components/common/MediaGrid";
import { Media } from "@/app/services/MediaService";
import { getShowsAsync } from "@/app/services/ShowService";

export default function ShowsPage() {
    const [showList, setShowList] = useState<Media[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getShowsAsync()
            .then(setShowList)
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
            <MediaGrid items={showList} />
        </Box>
    );
}