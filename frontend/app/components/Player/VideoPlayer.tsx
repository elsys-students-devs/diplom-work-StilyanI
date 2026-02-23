"use client";

import {useEffect, useRef} from "react";
import videojs from "video.js";
import Player from "video.js/dist/types/player";
import 'video.js/dist/video-js.css';
import 'videojs-hls-quality-selector';
import {useSearchParams} from "next/navigation";
import {useUser} from "@/app/hooks/UserHook";
import {getProgress, saveProgress} from "@/app/services/ProgressService";

interface VideoJsPlayerWithQualitySelector extends Player {
    hlsQualitySelector?: (options?: any) => void;
}

export default function VideoPlayer() {
    const videoId = useSearchParams().get("video-id");
    const videoSrc = `${process.env.NEXT_PUBLIC_API_URL}/video/${videoId}/master.m3u8`;
    const {user} = useUser();

    const startTimeRef = useRef(0);

    const videoRef = useRef<HTMLVideoElement>(null);
    const playerRef = useRef<VideoJsPlayerWithQualitySelector>(null);

    useEffect(() => {
        if (!playerRef.current && videoRef.current) {

            const player = videojs(videoRef.current, {
                    controls: true,
                    autoplay: true,
                    sources: [{
                        src: videoSrc,
                        type: "application/x-mpegURL"
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
                }
            );

            player.one("loadedmetadata", () => {
                const t = startTimeRef.current || 0;
                if (t > 0) player.currentTime(t);
            });

            if(user) {
                let lastLoggedAt = -Infinity;
                player.on("timeupdate", () => {
                    const t = player.currentTime()!;
                    if (t - lastLoggedAt >= 10) {
                        lastLoggedAt = t;
                        saveProgress(user, Number(videoId), t, (t / player.duration()!) * 100);
                    }
                });
            }

            playerRef.current = player;

            return () => {
                if (playerRef.current) {
                    playerRef.current.dispose();
                    playerRef.current = null;
                }
            }
        }
    }, []);

    useEffect(() => {
        const fetchProgress = async () => {
            const res = await getProgress(user, Number(videoId));
            if (!res) return;

            const t = res.data.progressSeconds ?? 0;
            startTimeRef.current = t;

            const player = playerRef.current;
            if (player && player.readyState() >= 1 && t > 0) {
                player.currentTime(t);
            }
        };

        if (user && videoId) fetchProgress();
    }, [user, videoId]);

    return (
        <div data-vjs-player style={{width: "100vw", height: "100vh"}}>
            <video style={{width: "100vw", height: "100vh"}} ref={videoRef} className="video-js vjs-big-play-centered"/>
        </div>
    );
}