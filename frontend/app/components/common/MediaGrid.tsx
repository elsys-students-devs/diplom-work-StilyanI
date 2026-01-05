import {Box, Grid, Typography} from "@mui/material";
import Image from "next/image";
import Link from "next/link";
import {Media} from "@/app/services/MediaService";

interface MediaGridProps {
    items: Media[];
}

export default function MediaGrid({
    items
}: Readonly<MediaGridProps>) {
    return (
        <Grid container spacing={3} sx={{ mx: 5 }}>
            {items.map((item) => (
                <Grid key={item.id}>
                    <Box
                        display="flex"
                        flexDirection="column"
                        alignItems="center"
                    >
                        <Link replace href={(item.type === "movie" ? '/movies/' : "/shows/") + item.id}>
                            <Image
                                className="media-image"
                                src={item.posterUrl}
                                alt={item.title}
                                width={300}
                                height={450}
                            />
                        </Link>
                        <Typography sx={{mt: 2, fontSize: "24px"}}>
                            {item.title}
                        </Typography>
                    </Box>
                </Grid>
            ))}
        </Grid>
    )
}