"use client";

import { Box, Typography, Button } from "@mui/material";
import {getMovieById} from "@/app/services/MovieService";
import Image from "next/image";
import {Media} from "@/app/services/MediaService";
import {useParams} from "next/navigation";
import {runtimeFormated} from "@/app/util/MediaUtil";

export default function MoviePage(){
    const movieId = useParams().id;
    const movie = getMovieById(movieId as string) as Media;

    return (
        <div>
            <Box
                sx={{
                    backgroundImage: `url(${movie.backdropUrl})`,
                    backgroundRepeat: 'no-repeat',
                    backgroundSize: "cover",
                    backgroundAttachment: "fixed",
                    height: "40vh",
                    filter: "blur(4px)",
                    position: "relative",
                    zIndex: -1
                }}
            />

            <Image src={movie?.logoUrl} alt={movie.title + " logo"} width={500} height={500} style={{position: "absolute", left: "50vw", top: "25vh"}} />
            <Image src={movie.posterUrl} alt={movie.title + " poster"} width={500} height={500} style={{position: "absolute", width: "20vw" , left: "4%", top: "20%"}} />

            <Box>
                <Box
                    sx={{backgroundColor: "#202020", paddingLeft: "30%", paddingRight: "10%", display: "flex", flexDirection: "row", justifyContent: "space-between", alignItems: "center", paddingY: 5}}
                >
                    <Box>
                        <Typography fontSize={32}>{movie.title}</Typography>
                        <Box sx={{display: "flex", justifyContent: "space-between"}}>
                            <Typography fontSize={16}>{movie.releaseYear}</Typography>
                            <Typography fontSize={16}>{runtimeFormated(movie.runtime!)}</Typography>
                        </Box>
                    </Box>

                    <Box>
                        <Button variant="contained" sx={{color: "black", backgroundColor: "white", fontSize: 16}}>Play</Button>
                    </Box>
                </Box>

                <Box sx={{paddingX: "30%", mt: 3}}>
                    <Typography sx={{fontSize: 24, fontWeight: 600}}>Overview</Typography>
                    <Typography sx={{fontSize: 24, fontWeight: 100}}>{movie.description}</Typography>
                </Box>
            </Box>
        </div>
    )
}