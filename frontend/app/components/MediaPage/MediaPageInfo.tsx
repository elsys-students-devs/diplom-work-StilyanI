"use client";

import {Media} from "@/app/services/MediaService";
import {Box, Button, Typography} from "@mui/material";
import Image from "next/image";
import {runtimeFormated} from "@/app/util/MediaUtil";

interface MediaPageInfoProps {
    media: Media;
}

export default function MediaPageInfo({media}: MediaPageInfoProps) {
    return (
        <Box>
            <Box
                className="media-page-hero"
                sx={{
                    backgroundImage: `url(${media.backdropUrl})`,
                }}
            />
    
            <Image src={media?.logoUrl} alt={media.title + " logo"} width={500} height={500} style={{position: "absolute", left: "50vw", top: "25vh"}} />
            <Image src={media.posterUrl} alt={media.title + " poster"} width={500} height={500} style={{position: "absolute", width: "20vw" , left: "4%", top: "20%"}} />
        
            <Box>
                <Box
                    className="media-page-info-ribbon"
                >
                    <Box>
                        <Typography fontSize={32}>{media.title}</Typography>
                        <Box sx={{display: "flex", justifyContent: "space-between"}}>
                            <Typography fontSize={16}>{media.releaseYear}</Typography>
                            {media.type === "movie" &&
                                <Typography fontSize={16}>{runtimeFormated(media.runtime!)}</Typography>
                            }
                        </Box>
                    </Box>

                    {media.type === "movie" &&
                        <Box>
                            <Button variant="contained" sx={{color: "black", backgroundColor: "white", fontSize: 16}}>Play</Button>
                        </Box>
                    }
                </Box>
        
                <Box sx={{paddingX: "30%", mt: 3}}>
                    <Typography sx={{fontSize: 24, fontWeight: 600}}>Overview</Typography>
                    <Typography sx={{fontSize: 24, fontWeight: 100}}>{media.description}</Typography>
                </Box>
            </Box>
        </Box>
    )
}