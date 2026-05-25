"use client";

import VideoPlayer from "@/app/components/Player/VideoPlayer";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {IconButton} from "@mui/material";
import Link from "next/link";
import {Suspense} from "react";

export default function PlayerPage(){
    return (
        <div style={{overflow: 'hidden'}}>
            <IconButton component={Link} href={"/home"} sx={{position: "absolute", zIndex: 1, top: "2%", left: "2%", scale: 2}}>
                <ArrowBackIcon style={{color:'white'}}/>
            </IconButton>
            <Suspense>
                <VideoPlayer/>
            </Suspense>
        </div>
    )
}