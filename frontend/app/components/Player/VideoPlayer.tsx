"use client";

import {useEffect, useRef} from "react";
import videojs from "video.js";
import Player from "video.js/dist/types/player";
import 'video.js/dist/video-js.css';
import 'videojs-hls-quality-selector';

interface VideoJsPlayerWithQualitySelector extends Player {
    hlsQualitySelector?: (options?: any) => void;
}

interface VideoPlayerProps {
    src: string;
    type?: string;
}

export default function VideoPlayer({src, type}: Readonly<VideoPlayerProps>) {
    const videoRef = useRef<HTMLVideoElement>(null);
    const playerRef = useRef<VideoJsPlayerWithQualitySelector>(null);

    useEffect(() => {
        if (!playerRef.current && videoRef.current) {
            const videoElement = videoRef.current;

            const player = videojs(videoElement, {
                controls: true,
                autoplay: true,
                sources: [{
                    src: src,
                    type: type
                }]
            },
            () => {
                    // @ts-ignore
                if (player.hlsQualitySelector) {
                    // @ts-ignore
                    player.hlsQualitySelector({
                        displayCurrentQuality: true,
                    });
                }
            });

            playerRef.current = player;

            return () => {
                if (playerRef.current) {
                    playerRef.current.dispose();
                    playerRef.current = null;
                }
            }
        }
    }, [src, type]);

    return (
        <div data-vjs-player style={{width: "100vw", height: "100vh"}}>
            <video style={{width: "100vw", height: "100vh"}} ref={videoRef} className="video-js vjs-big-play-centered"/>
        </div>
    );
}