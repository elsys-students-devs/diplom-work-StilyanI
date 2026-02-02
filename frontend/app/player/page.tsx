"use client";

import VideoPlayer from "@/app/components/Player/VideoPlayer";
import { useSearchParams } from 'next/navigation';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {IconButton} from "@mui/material";
import Link from "next/link";

export default function PlayerPage(){
    const videoId = useSearchParams().get("video-id");
    const videoSrc = `http://localhost:8080/video/${videoId}/master.m3u8`;

    return (
        <div style={{overflow: 'hidden'}}>
            <IconButton component={Link} href={"/home"} sx={{position: "absolute", zIndex: 1, top: "2%", left: "2%", scale: 2}}>
                <ArrowBackIcon style={{color:'white'}}/>
            </IconButton>
            <VideoPlayer src={videoSrc} type="application/x-mpegURL"/>
        </div>
    )
}