"use client";

import { Box, Typography, Select, MenuItem, FormControl, SelectChangeEvent } from "@mui/material";
import Image from "next/image";
import {Media} from "@/app/services/MediaService";
import {useParams} from "next/navigation";
import {getShowById, getShowSeasons} from "@/app/services/ShowService";
import {useEffect, useState} from "react";
import Link from "next/link";
import MediaPageInfo from "@/app/components/MediaPage/MediaPageInfo";

export default function ShowPage(){
    const showId = useParams().id;
    const show = getShowById(showId as string) as Media;
    const seasons = getShowSeasons(showId as string);
    const [selectedSeason, setSelectedSeason] = useState(0);

    useEffect(() => {
        if(seasons.length > 0) setSelectedSeason(seasons[0].number);
    }, []);

    const handleChange = (event: SelectChangeEvent<number>) => {
        setSelectedSeason(Number(event.target.value));
    };

    return (
        <Box sx={{pb: 10}}>
            <MediaPageInfo media={show}/>

            <Box sx={{paddingX: {xs: "10%", md: "20%"}}}>
                <Box sx={{display: "flex", flexDirection: "row", alignItems: "center", pt: 3}}>
                    <Typography sx={{fontSize: 24, fontWeight: 600, mr: 3}}>Episodes</Typography>

                    <FormControl sx={{minWidth: 150, outlineColor: "white"}}>
                        <Select
                            displayEmpty
                            value={selectedSeason}
                            onChange={handleChange}
                            sx={{
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
                                <MenuItem key={season.number} value={season.number}>Season {season.number}</MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </Box>

                <Box sx={{mt: 3}}>
                    {selectedSeason != 0 && seasons[selectedSeason - 1].episodes.map((episode) => (
                        <Link href={"/player"} key={episode.number}>
                            <Box className="show-episode-container">
                                <Typography sx={{fontSize: 32, marginRight: 3}}>{episode.number}</Typography>

                                <Image src={episode.stillUrl} alt={episode.number + " still"} width={200} height={100} style={{borderRadius: "6px", marginRight: 50}}/>

                                <Box>
                                    <Typography sx={{fontSize: 24, fontWeight: 300, marginBottom: 1}}>{episode.title}</Typography>
                                    <Typography sx={{fontSize: 16, fontWeight: 200}}>{episode.description}</Typography>
                                </Box>
                            </Box>
                        </Link>
                    ))}
                </Box>
            </Box>
        </Box>
    )
}