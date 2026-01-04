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
import {usePathname} from "next/navigation";

export default function Header(){
    const pathname = usePathname();
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
                <Toolbar
                    sx={{display: "flex", justifyContent: "space-between"}}
                >
                    <Box>
                        <IconButton onClick={toggleDrawer}>
                            <MenuIcon/>
                        </IconButton>

                        <IconButton component={Link} href={"/home"} sx={{visibility: pathname === "/home" ? "hidden" : "visible"}}>
                            <HomeIcon/>
                        </IconButton>

                    </Box>

                    <Box sx={{display: "flex"}}>
                        {pathname === "/movies" ?
                            <Typography className="unselectable-header-button">Movies</Typography>
                             :
                            <Typography component={Link} href={"/movies"} className="section-button">Movies</Typography>
                        }

                        <Box sx={{mx: 2}}/>

                        {pathname === "/shows" ?
                            <Typography className="unselectable-header-button">Shows</Typography>
                            :
                            <Typography component={Link} href={"/shows"} className="section-button">Shows</Typography>
                        }

                    </Box>

                    <IconButton>
                        <AccountCircleIcon/>
                    </IconButton>

                </Toolbar>
            </AppBar>

            <Drawer open={drawerOpen} onClose={toggleDrawer} slotProps={{paper: {sx: {backgroundColor:"gray"}} }} onClick={toggleDrawer}>
                <Box sx={{width: 250}}>
                    <List>
                        {['Home', 'Movies', 'Shows'].map((text) => (
                            <ListItem key={text}>
                                <ListItemButton component={Link} href={"/" + text.toLowerCase()}>
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
                            <ListItemButton component={Link} href={"/dashboard"}>
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