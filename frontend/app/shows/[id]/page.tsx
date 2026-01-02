"use client";

import { Box, Typography, Select, MenuItem, FormControl, SelectChangeEvent } from "@mui/material";
import Image from "next/image";
import {Media} from "@/app/services/MediaService";
import {useParams} from "next/navigation";
import {getShowById, getShowSeasons} from "@/app/services/ShowService";
import {useState} from "react";
import Link from "next/link";

export default function showPage(){
    const showId = useParams().id;
    const show = getShowById(showId as string) as Media;
    const seasons = getShowSeasons(showId as string);
    const [selectedSeason, setSelectedSeason] = useState(seasons[0].number);

    const handleChange = (event: SelectChangeEvent<number>) => {
        setSelectedSeason(event.target.value);
    };

    return (
        <div>
            <Box
                sx={{
                    backgroundImage: `url(${show.backdropUrl})`,
                    backgroundRepeat: 'no-repeat',
                    backgroundSize: "cover",
                    backgroundAttachment: "fixed",
                    height: "40vh",
                    filter: "blur(4px)",
                    position: "relative",
                    zIndex: -1
                }}
            />

            <Image src={show?.logoUrl} alt={show.title + " logo"} width={500} height={500} style={{position: "absolute", left: "50vw", top: "25vh"}} />
            <Image src={show.posterUrl} alt={show.title + " poster"} width={500} height={500} style={{position: "absolute" , left: "4%", top: "20%"}} />

            <Box sx={{mb: 10}}>
                <Box
                    sx={{backgroundColor: "#202020", paddingY: 5, width: "100%", paddingX: "30%"}}
                >
                    <Box>
                        <Typography fontSize={32}>{show.title}</Typography>
                        <Typography fontSize={16}>{show.releaseYear}</Typography>
                    </Box>
                </Box>

                <Box sx={{paddingX: "30%"}}>
                    <Box sx={{mt: 3}}>
                        <Typography sx={{fontSize: 24, fontWeight: 600}}>Overview</Typography>
                        <Typography sx={{fontSize: 24, fontWeight: 100}}>{show.description}</Typography>
                    </Box>

                    <Box sx={{mt: 3}}>
                        <Box sx={{display: "flex", flexDirection: "row", alignItems: "center"}}>
                            <Typography sx={{fontSize: 24, fontWeight: 600, mr: 3}}>Episodes</Typography>

                            <FormControl sx={{minWidth: 150, outlineColor: "white"}}>
                                <Select
                                    displayEmpty
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
                                        }
                                    }}
                                    value={selectedSeason}
                                    onChange={handleChange}
                                >
                                    {seasons.map(season => (
                                        <MenuItem value={season.number}>Season {season.number}</MenuItem>
                                    ))
                                    }
                                </Select>
                            </FormControl>
                        </Box>
                    </Box>

                    <Box sx={{mt: 3}}>
                        {seasons[selectedSeason - 1].episodes.map((episode) => (
                            <Link href={"/"} key={episode.number}>
                                <Box sx={{display: "flex", alignItems: "center", padding: 3, borderBottomWidth: 1, borderTopWidth: 1, borderColor: "gray", ":hover": {backgroundColor: "#181818", transition: "0.2s"}}}>
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
        </div>
    )
}