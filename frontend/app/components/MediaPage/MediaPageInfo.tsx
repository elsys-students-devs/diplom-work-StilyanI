"use client";

import {Media, parseReleaseDateToYear} from "@/app/services/MediaService";
import {Box, Button, Typography} from "@mui/material";
import Image from "next/image";
import Link from "next/link";

interface MediaPageInfoProps {
    media: Media;
}

export default function MediaPageInfo({media}: Readonly<MediaPageInfoProps>) {
    return (
        <Box>
            <Box display={"grid"} gridTemplateRows={"1fr 1fr 1fr"}>
                <Box className={"media-page-hero-backdrop-image"} sx={{backgroundImage: `url(${media.backdropPath})`}}/>

                <Box
                    sx={{
                        display: {xs: "none", lg: "block"},
                        gridArea: "2 / 2 / 3 / 3",
                        justifySelf: "center",
                        zIndex: 1
                    }}
                >
                    <Image src={media?.logoPath} alt={media.name + " logo"} width={500} height={500} style={{left: "50vw", top: "25vh"}} />
                </Box>

                <Box
                    sx={{
                        display: {xs: "none", sm: "block"},
                        gridArea: "2 / 1 / 4 / 1",
                        zIndex: 1,
                        justifySelf: "center"
                    }}
                >
                    <Image src={media.posterPath} alt={media.name + " poster"} width={500} height={500} style={{width: "15vw"}} />
                </Box>

                <Box
                    className="media-page-info-ribbon"
                    gridArea="3/1/4/4"
                >

                    <Box>
                        <Typography fontSize={32}>{media.name}</Typography>
                        <Box sx={{display: "flex", justifyContent: "space-between"}}>
                            <Typography fontSize={16}>{parseReleaseDateToYear(media.releaseDate)}</Typography>
                        </Box>
                    </Box>

                    {media.type === "movie" &&
                        <Box>
                            <Button component={Link} href={`/player?video-id=${media.id}`} variant="contained" sx={{color: "black", backgroundColor: "white", fontSize: 16}}>Play</Button>
                        </Box>
                    }
                </Box>

            </Box>

            <Box>
                <Box sx={{paddingX: {xs: "10%", md: "20%"}, pt: 3}}>
                    <Typography sx={{fontSize: 24, fontWeight: 600}}>Overview</Typography>
                    <Typography sx={{fontSize: 24, fontWeight: 100}}>{media.overview}</Typography>
                </Box>
            </Box>
        </Box>
    )
}