"use client";

import { AppBar, Box, IconButton, Toolbar, Typography, Drawer, List, ListItem, ListItemIcon, ListItemText, ListItemButton, Divider } from "@mui/material";
import MenuIcon from '@mui/icons-material/Menu';
import HomeIcon from '@mui/icons-material/Home';
import LocalMoviesIcon from '@mui/icons-material/LocalMovies';
import TvIcon from '@mui/icons-material/Tv';
import DashboardIcon from '@mui/icons-material/Dashboard';
import { AccountCircle } from "@mui/icons-material";
import Link from "next/link";
import { useEffect, useState } from "react";
import { Media, getAllMedia } from "../services/MediaService";
import ScrollableImageList from "@/app/components/Home/ScrollableImageList";

export default function HomePage() {
    const [continueWatching, setContinueWatching] = useState<Media[]>([]);
    const [drawerOpen, setDrawerOpen] = useState(false);

    function toggleDrawer() {
        setDrawerOpen(!drawerOpen);
    }

    useEffect(() => {
        const media = getAllMedia();
        setContinueWatching(media);
    }, [])

    return(
        <div>
            <AppBar
                sx={{backgroundColor: "gray"}}
                position="sticky"
            >
                <Toolbar>
                    <IconButton onClick={toggleDrawer} aria-label="open drawer">
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
                <Typography variant="h4">Continue Watching</Typography>
                <ScrollableImageList items={continueWatching} position={"horizontal"}/>

                <Typography variant="h4">Recently Added Movies</Typography>
                <ScrollableImageList items={continueWatching} position={"vertical"}/>

                <Typography variant="h4">Recently Added Shows</Typography>
                <ScrollableImageList items={continueWatching} position={"vertical"}/>
            </div>

            <Drawer open={drawerOpen} onClose={toggleDrawer} slotProps={{paper: {sx: {backgroundColor:"gray"}} }}>
                <Box sx={{width: 250}}>
                    <List>
                        {['Home', 'Movies', 'Shows'].map((text) => (
                        <ListItem key={text}>
                            <ListItemButton href={"/" + text.toLowerCase()}>
                                <ListItemIcon>
                                    {
                                        {
                                            'Home': <HomeIcon/>,
                                            'Movies': <LocalMoviesIcon/>,
                                            'Shows': <TvIcon/>
                                        }[text]
                                    }
                                </ListItemIcon>
                                <ListItemText primary={text}/>
                            </ListItemButton>
                        </ListItem>
                        ))}

                        <Divider/>

                        <ListItem key={'dashboard'}>
                            <ListItemButton href={"/dashboard"}>
                                <ListItemIcon>
                                    <DashboardIcon/>
                                </ListItemIcon>
                                <ListItemText primary={'Dashboard'}/>
                            </ListItemButton>
                        </ListItem>

                    </List>
                </Box>
            </Drawer>
        </div>
    )
}