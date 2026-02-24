"use client";

import {useEffect, useRef, useCallback} from "react";
import videojs from "video.js";
import Player from "video.js/dist/types/player";
import 'video.js/dist/video-js.css';
import 'videojs-hls-quality-selector';
import {useSearchParams} from "next/navigation";
import {useUser} from "@/app/hooks/UserHook";
import {getProgress, saveProgress} from "@/app/services/ProgressService";
import {Box, Typography} from "@mui/material";

interface VideoJsPlayerWithQualitySelector extends Player {
    hlsQualitySelector?: (options?: Record<string, unknown>) => void;
}

export default function VideoPlayer() {
    const videoId = useSearchParams().get("video-id");
    const {user} = useUser();

    const startTimeRef = useRef(0);
    const videoRef = useRef<HTMLVideoElement>(null);
    const playerRef = useRef<VideoJsPlayerWithQualitySelector>(null);

    const videoSrc = videoId
        ? `${process.env.NEXT_PUBLIC_API_URL}/video/${videoId}/master.m3u8`
        : null;

    const throttledSave = useCallback(() => {
        let lastSavedAt = -Infinity;
        return (currentTime: number, duration: number) => {
            if (currentTime - lastSavedAt >= 10) {
                lastSavedAt = currentTime;
                saveProgress(user, Number(videoId), currentTime, (currentTime / duration) * 100);
            }
        };
    }, [user, videoId]);

    useEffect(() => {
        if (!videoSrc || !videoRef.current || playerRef.current) return;

        const player = videojs(videoRef.current, {
                controls: true,
                autoplay: true,
                sources: [{
                    src: videoSrc,
                    type: "application/x-mpegURL"
                }],
            },
            () => {
                    // @ts-expect-error hlsQualitySelector plugin
                if (player.hlsQualitySelector) {
                    // @ts-expect-error hlsQualitySelector plugin
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
            const save = throttledSave();
            player.on("timeupdate", () => {
                const t = player.currentTime();
                const d = player.duration();
                if (t != null && d != null && d > 0) {
                    save(t, d);
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
    }, [videoSrc, user, throttledSave]);

    useEffect(() => {
        if (!user || !videoId) return;

        const fetchProgress = async () => {
            const res = await getProgress(user, Number(videoId));
            if (!res) return;

            if (res.data.progressPercent > 95) {
                saveProgress(user, Number(videoId), 0, 0);
                startTimeRef.current = 0;
                return;
            }

            const t = res.data.progressSeconds ?? 0;
            startTimeRef.current = t;

            const player = playerRef.current;
            if (player && player.readyState() >= 1 && t > 0) {
                player.currentTime(t);
            }
        };

        fetchProgress();
    }, [user, videoId]);

    if (!videoId) {
        return (
            <Box sx={{display: "flex", justifyContent: "center", alignItems: "center", height: "100vh"}}>
                <Typography variant="h5" color="white">No video selected</Typography>
            </Box>
        );
    }

    return (
        <div data-vjs-player style={{width: "100vw", height: "100vh"}}>
            <video style={{width: "100vw", height: "100vh"}} ref={videoRef} className="video-js vjs-big-play-centered"/>
        </div>
    );
}