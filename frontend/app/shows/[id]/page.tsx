"use client";

import { Box, Typography, Button } from "@mui/material";
import Image from "next/image";
import {Media} from "@/app/services/MediaService";
import {useParams} from "next/navigation";
import {getShowById} from "@/app/services/ShowService";

export default function showPage(){
    const showId = useParams().id;
    const show = getShowById(showId as string) as Media;

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
            <Image src={show.posterUrl} alt={show.title + " poster"} width={500} height={500} style={{position: "absolute", width: "20vw" , left: "4%", top: "20%"}} />

            <Box>
                <Box
                    sx={{backgroundColor: "#202020", paddingLeft: "30%", paddingRight: "10%", display: "flex", flexDirection: "row", justifyContent: "space-between", alignItems: "center", paddingY: 5}}
                >
                    <Box>
                        <Typography fontSize={32}>{show.title}</Typography>
                        <Box sx={{display: "flex", justifyContent: "space-between"}}>
                            <Typography fontSize={16}>{show.releaseYear}</Typography>
                        </Box>
                    </Box>

                    <Box>
                        <Button variant="contained" sx={{color: "black", backgroundColor: "white", fontSize: 16}}>Play</Button>
                    </Box>
                </Box>

                <Box sx={{paddingX: "30%", mt: 3}}>
                    <Typography sx={{fontSize: 24, fontWeight: 600}}>Overview</Typography>
                    <Typography sx={{fontSize: 24, fontWeight: 100}}>{show.description}</Typography>
                </Box>
            </Box>
        </div>
    )
}