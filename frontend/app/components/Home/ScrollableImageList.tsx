import { Media } from "@/app/services/MediaService";
import {Box, ImageList, ImageListItem} from "@mui/material";
import Link from "next/link";

interface ScrollableImageListProps {
    items: Media[];
    position: string;
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
                mb: 3
            }}
        >
            <ImageList
                sx={{
                    display: "flex",
                    flexDirection: "row",
                    flexWrap: "nowrap",
                    gap: 2,
                    padding: 2,
                    pl: 4,
                    alignItems: "stretch",
                    scrollbarWidth: "none"
                }}
            >
                {items?.map((media) => (
                    <ImageListItem
                        key={media.id}
                        sx={{flex: "0 0 auto", mr: 3, width: position == "vertical" ? 200 : 400}}
                    >
                        <Link href={'/movies/' + media.id}>
                            <img
                                alt={media.title}
                                src={position == "vertical" ? media.posterUrl : media.backdropUrl}
                                className={"media-image"}
                                style={{width: "100%", height: "100%", objectFit: "cover"}}
                            />
                        </Link>
                    </ImageListItem>
                ))}
            </ImageList>
        </Box>
    )
}