"use client";

import { AppBar, Box, IconButton, Toolbar, Typography, Drawer, List, ListItem, ListItemIcon, ListItemText, ListItemButton, Divider } from "@mui/material";
import Link from "next/link";
import { useEffect, useState } from "react";
import { Media, getAllMedia } from "../services/MediaService";
import ScrollableImageList from "@/app/components/Home/ScrollableImageList";

import MenuIcon from '@mui/icons-material/Menu';
import HomeIcon from '@mui/icons-material/Home';
import LocalMoviesIcon from '@mui/icons-material/LocalMovies';
import TvIcon from '@mui/icons-material/Tv';
import DashboardIcon from '@mui/icons-material/Dashboard';
import AccountCircleIcon from "@mui/icons-material/AccountCircle";

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
                    <IconButton onClick={toggleDrawer}>
                        <MenuIcon/>
                    </IconButton>

                    <Box sx={{flexGrow: 1}}/>

                    <Link href={"/movies"}>
                        <Typography className="section-button">Movies</Typography>
                    </Link>

                    <Box sx={{mx: 2}}/>

                    <Link href={"/shows"}>
                        <Typography className="section-button">Shows</Typography>
                    </Link>

                    <Box sx={{flexGrow: 1}}/>

                    <IconButton>
                        <AccountCircleIcon/>
                    </IconButton>
                    
                </Toolbar>
            </AppBar>

            <Box sx={{mt: 3}}>
                <Typography variant="h4" sx={{ml: 3, mb: 2}}>Continue Watching</Typography>
                <ScrollableImageList items={continueWatching} position={"horizontal"}/>

                <Typography variant="h4" sx={{ml: 3, mb: 2}}>Recently Added Movies</Typography>
                <ScrollableImageList items={continueWatching} position={"vertical"}/>

                <Typography variant="h4" sx={{ml: 3, mb: 2}}>Recently Added Shows</Typography>
                <ScrollableImageList items={continueWatching} position={"vertical"}/>
            </Box>

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