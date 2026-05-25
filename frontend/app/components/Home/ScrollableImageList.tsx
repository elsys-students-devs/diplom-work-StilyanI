"use client";

import { Media } from "@/app/services/MediaService";
import {Box, ImageList, ImageListItem} from "@mui/material";
import Link from "next/link";
import Image from "next/image";

interface ScrollableImageListProps {
    items: Media[];
    alignment: "horizontal" | "vertical";
    continueWatching?: boolean;
}

function parseInfoLink(media: Media) {
    return (media.type === "movie" ? '/movies/' : "/shows/") + media.tmdbId;
}

export default function ScrollableImageList({
    items, alignment, continueWatching
}: Readonly<ScrollableImageListProps>) {

    return (
        <Box
            sx={{
                width: "100%",
                overflowX: "auto",
                overflowY: "hidden",
                display: "block",
                mb: { xs: 2, md: 3 }
            }}
        >
            <ImageList
                sx={{
                    display: "flex",
                    flexDirection: "row",
                    flexWrap: "nowrap",
                    gap: { xs: 1.5, sm: 2 },
                    p: { xs: 2, sm: 3, md: 4 },
                    pl: 4,
                    alignItems: "stretch",
                    scrollbarWidth: "none"
                }}
            >
                {items?.map((media) => (
                    <ImageListItem
                        key={media.id}
                        sx={{
                            flex: "0 0 auto", mr: {xs: 1.5, sm: 2, md: 3},
                            width: {
                                xs: alignment === "vertical" ? "30vw" : "60vw",
                                sm: alignment === "vertical" ? "23vw" : "45vw",
                                md: alignment === "vertical" ? "15vw" : "30vw",
                            },
                            maxWidth: alignment === "vertical" ? "200px" : "400px"
                        }}
                    >
                        <Link href={continueWatching ? `/player?video-id=${media.id}` : parseInfoLink(media)}>
                            <Image
                                alt={media.name}
                                src={alignment == "vertical" ? media.posterPath : media.backdropPath}
                                height={200}
                                width={400}
                                className={"media-image"}
                            />
                        </Link>
                    </ImageListItem>
                ))}
            </ImageList>
        </Box>
    )
}