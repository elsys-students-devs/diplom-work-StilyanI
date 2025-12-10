"use client";

import { AppBar, Box, IconButton, ImageList, ImageListItem, Toolbar, Typography } from "@mui/material";
import MenuIcon from '@mui/icons-material/Menu';
import { AccountCircle } from "@mui/icons-material";
import Link from "next/link";
import { useEffect, useState } from "react";
import { Media, getAllMedia } from "../services/MediaService";

export default function HomePage() {
    const [continueWatching, setcontinueWatching] = useState<Media[]>([]);

    useEffect(() => {
        const media = getAllMedia();
        setcontinueWatching(media);
    }, [])

    return(
        <Box>
            <AppBar
                sx={{backgroundColor: "gray"}}
                position="sticky"
            >
                <Toolbar>
                    <IconButton>
                        <MenuIcon/>
                    </IconButton>

                    <Box sx={{flexGrow: 1}}/>

                    <Link href={"/movies"}>
                        <Typography sx={{color: "black"}}>Movies</Typography>
                    </Link>

                    <Box sx={{mx: 2}}/>

                    <Link href={"/shows"}>
                        <Typography sx={{color: "black"}}>Shows</Typography>
                    </Link>

                    <Box sx={{flexGrow: 1}}/>

                    <IconButton>
                        <AccountCircle/>
                    </IconButton>
                    
                </Toolbar>
            </AppBar>

            <div>
                {/* TODO: Make scrollable list seperate component */}
                <Typography variant="h4">Continue Watching</Typography>

                <Box
                    sx={{
                        width: "100%",
                        height: 320,
                        overflowX: "auto",
                        overflowY: "hidden",
                        display: "block"
                    }}
                >
                    <ImageList
                        sx={{
                            display: "flex",
                            flexDirection: "row",
                            flexWrap: "nowrap",
                            gap: 2,
                            pl: 1,
                            alignItems: "stretch",
                            scrollbarWidth: "none"
                        }}
                    >
                        {continueWatching?.map((media) => (
                            <ImageListItem
                                key={media.id}
                                sx={{flex: "0 0 auto", height: 300, mr: 3, width: 400}}
                            >
                                <img
                                    src={media.backdropUrl}
                                    style={{borderRadius: 8, width: "100%", height: "100%", objectFit: "cover"}}
                                />
                            </ImageListItem>
                        ))}
                    </ImageList>
                </Box>

                <Typography variant="h4">Recently Added Movies</Typography>

                <Box
                    sx={{
                        width: "100%",
                        height: 320,
                        overflowX: "auto",
                        overflowY: "hidden",
                        display: "block"
                    }}
                >
                    <ImageList
                        sx={{
                            display: "flex",
                            flexDirection: "row",
                            flexWrap: "nowrap",
                            gap: 2,
                            pl: 1,
                            alignItems: "stretch",
                            scrollbarWidth: "none"
                        }}
                    >
                        {continueWatching?.map((media) => (
                            <ImageListItem
                                key={media.id}
                                sx={{flex: "0 0 auto", height: 300, mr: 3, width: 200}}
                            >
                                <img
                                    src={media.posterUrl}
                                    style={{borderRadius: 8, width: "100%", height: "100%", objectFit: "cover"}}
                                />
                            </ImageListItem>
                        ))}
                    </ImageList>
                </Box>

                <Typography variant="h4">Recently Added Shows</Typography>

                <Box
                    sx={{
                        width: "100%",
                        height: 320,
                        overflowX: "auto",
                        overflowY: "hidden",
                        display: "block"
                    }}
                >
                    <ImageList
                        sx={{
                            display: "flex",
                            flexDirection: "row",
                            flexWrap: "nowrap",
                            gap: 2,
                            pl: 1,
                            alignItems: "stretch",
                            scrollbarWidth: "none"
                        }}
                    >
                        {continueWatching?.map((media) => (
                            <ImageListItem
                                key={media.id}
                                sx={{flex: "0 0 auto", height: 300, mr: 3, width: 200}}
                            >
                                <img
                                    src={media.posterUrl}
                                    style={{borderRadius: 8, width: "100%", height: "100%", objectFit: "cover"}}
                                />
                            </ImageListItem>
                        ))}
                    </ImageList>
                </Box>
            </div>
        </Box>
    )
}