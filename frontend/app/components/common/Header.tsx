"use client";

import {
    AppBar,
    Box, Drawer,
    IconButton,
    List,
    ListItem,
    ListItemButton,
    ListItemIcon, ListItemText, Menu, MenuItem,
    Toolbar,
    Typography
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import Link from "next/link";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import {useState, MouseEvent} from "react";
import HomeIcon from "@mui/icons-material/Home";
import LocalMoviesIcon from "@mui/icons-material/LocalMovies";
import TvIcon from "@mui/icons-material/Tv";
import {usePathname, useRouter} from "next/navigation";
import {useUser} from "@/app/hooks/UserHook";
import {useLocalStorage} from "@/app/hooks/LocalStorageHook";

export default function Header(){
    const pathname = usePathname();
    const router = useRouter();
    const [drawerOpen, setDrawerOpen] = useState(false);
    const {user, setUser} = useUser();
    const [, setUserId] = useLocalStorage("userId", null);

    const [accountMenuAnchorEl, setAccountMenuAnchorEl] = useState<null | HTMLElement>(null);
    const accountMenuOpen = Boolean(accountMenuAnchorEl);
    const handleAccountClick = (event: MouseEvent<HTMLElement>) => {
        setAccountMenuAnchorEl(event.currentTarget);
    };
    const handleAccountClose = () => {
        setAccountMenuAnchorEl(null);
    };

    const handleLogout = () => {
        setUserId(null);
        setUser(null);
        handleAccountClose();
        router.push("/auth");
    }

    function toggleDrawer() {
        setDrawerOpen(!drawerOpen);
    }

    return (
        (pathname === "/player") ?
            <div></div> : <div>
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

                            <IconButton component={Link} href={"/home"}
                                        sx={{visibility: pathname === "/home" ? "hidden" : "visible"}}>
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

                        <IconButton
                            onClick={handleAccountClick}
                        >
                            <AccountCircleIcon/>
                        </IconButton>

                    </Toolbar>
                </AppBar>

                <Drawer
                    open={drawerOpen}
                    onClose={toggleDrawer}
                    onClick={toggleDrawer}
                    slotProps={{paper: {sx: {backgroundColor: "gray"}}}}
                >
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
                        </List>
                    </Box>
                </Drawer>
            
            <Menu
                open={accountMenuOpen}
                anchorEl={accountMenuAnchorEl}
                onClose={handleAccountClose}
            >
                {user ?
                    <MenuItem onClick={handleLogout}>Log out</MenuItem>
                    :
                    <MenuItem onClick={handleAccountClose} component={Link} href={"/auth"}>Sign In</MenuItem>
                }
            </Menu>
            </div>
    )
}