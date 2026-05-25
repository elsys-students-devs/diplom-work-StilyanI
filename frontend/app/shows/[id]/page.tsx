"use client";

import { Box, Typography, Select, MenuItem, SelectChangeEvent, CircularProgress } from "@mui/material";
import Image from "next/image";
import {Media} from "@/app/services/MediaService";
import {useParams} from "next/navigation";
import {getShowById, getShowSeasons, ShowSeason} from "@/app/services/ShowService";
import {useEffect, useState} from "react";
import Link from "next/link";
import MediaPageInfo from "@/app/components/MediaPage/MediaPageInfo";

export default function ShowPage(){
    const showId = useParams().id;
    const show = getShowById(Number(showId)) as Media;
    const [seasons, setSeasons] = useState<ShowSeason[]>([]);
    const [selectedSeason, setSelectedSeason] = useState(0);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchShowSeasons = async () => {
            try {
                const res = await getShowSeasons(Number(showId));
                setSeasons(res.data);
            } finally {
                setLoading(false);
            }
        };

        if (showId) fetchShowSeasons();
    }, [showId]);

    useEffect(() => {
        if(seasons.length > 0) setSelectedSeason(seasons[0].seasonNumber);
    }, [seasons]);

    const handleChange = (event: SelectChangeEvent<number>) => {
        setSelectedSeason(Number(event.target.value));
    };

    const currentSeason = seasons.find(s => s.seasonNumber === selectedSeason);

    return (
        <Box sx={{pb: 10}}>
            <MediaPageInfo media={show}/>

            <Box sx={{paddingX: {xs: "10%", md: "20%"}}}>
                <Box sx={{display: "flex", flexDirection: "row", alignItems: "center", pt: 3}}>
                    <Typography sx={{fontSize: 24, fontWeight: 600, mr: 3}}>Episodes</Typography>
                    {loading ? (
                        <CircularProgress size={24} />
                    ) : (
                        <Select
                            displayEmpty
                            value={selectedSeason}
                            onChange={handleChange}
                            sx={{
                                minWidth: 150,
                                color:"white",
                                backgroundColor: "#242424",
                                '& .MuiOutlinedInput-notchedOutline': {
                                    borderColor: 'white'
                                },
                                '&:hover .MuiOutlinedInput-notchedOutline': {
                                    borderColor: 'white'
                                },
                                '& .MuiSvgIcon-root': {
                                    color: 'white'
                                },
                                '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
                                    borderColor: 'white',
                                    borderWidth: 1
                                }
                            }}
                        >
                            {selectedSeason != 0 && seasons.map(season => (
                                <MenuItem key={season.seasonNumber} value={season.seasonNumber}>Season {season.seasonNumber}</MenuItem>
                            ))}
                        </Select>
                    )}
                </Box>

                <Box sx={{mt: 3}}>
                    {currentSeason?.episodes.map((episode) => (
                        <Link href={`/player?video-id=${episode.id}`} key={episode.id}>
                            <Box className="show-episode-container">
                                <Typography sx={{fontSize: 32, marginRight: 3}}>{episode.episodeNumber}</Typography>

                                <Image src={episode.stillPath} alt={episode.episodeNumber + " still"} width={200} height={100} style={{borderRadius: "6px", marginRight: 50}}/>

                                <Box>
                                    <Typography sx={{fontSize: 24, fontWeight: 300, marginBottom: 1}}>{episode.name}</Typography>
                                    <Typography sx={{fontSize: 16, fontWeight: 200}}>{episode.overview}</Typography>
                                </Box>
                            </Box>
                        </Link>
                    ))}
                </Box>
            </Box>
        </Box>
    )
}