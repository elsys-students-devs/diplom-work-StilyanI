import {Movie} from "@/app/services/MovieService";
import {Box, Grid, Typography} from "@mui/material";
import Image from "next/image";

interface MediaGridProps {
    items: Movie[];
}

export default function MediaGrid({
    items
}: MediaGridProps) {
    return (
        <Grid container spacing={3} sx={{ mx: 5 }}>
            {items.map((item, index) => (
                <Grid key={index}>
                    <Box
                        display="flex"
                        flexDirection="column"
                        alignItems="center"
                    >
                        <Image
                            className="media-image"
                            src={item.posterUrl}
                            alt={item.title}
                            width={300}
                            height={450}
                        />
                        <Typography sx={{mt: 2, fontSize: "24px"}}>
                            {item.title}
                        </Typography>
                    </Box>
                </Grid>
            ))}
        </Grid>
    )
}