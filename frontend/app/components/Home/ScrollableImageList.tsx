"use client";

import { Media } from "@/app/services/MediaService";
import {Box, ImageList, ImageListItem} from "@mui/material";
import Link from "next/link";
import Image from "next/image";

interface ScrollableImageListProps {
    items: Media[];
    position: "horizontal" | "vertical";
}

export default function ScrollableImageList({
    items, position
}: ScrollableImageListProps) {

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
                                xs: position === "vertical" ? "30vw" : "60vw",
                                sm: position === "vertical" ? "23vw" : "45vw",
                                md: position === "vertical" ? "15vw" : "30vw",
                            },
                            maxWidth: position === "vertical" ? "200px" : "400px"
                        }}
                    >
                        <Link href={(media.type === "movie" ? '/movies/' : "/shows/") + media.id}>
                            <Image
                                alt={media.title}
                                src={position == "vertical" ? media.posterUrl : media.backdropUrl}
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