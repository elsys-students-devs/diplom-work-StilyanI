"use client";

import {
    AppBar,
    Box, Divider, Drawer,
    IconButton,
    List,
    ListItem,
    ListItemButton,
    ListItemIcon, ListItemText,
    Toolbar,
    Typography
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import Link from "next/link";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import {useState} from "react";
import HomeIcon from "@mui/icons-material/Home";
import LocalMoviesIcon from "@mui/icons-material/LocalMovies";
import TvIcon from "@mui/icons-material/Tv";
import DashboardIcon from "@mui/icons-material/Dashboard";

export default function Header(){
    const [drawerOpen, setDrawerOpen] = useState(false);

    function toggleDrawer() {
        setDrawerOpen(!drawerOpen);
    }

    return (
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