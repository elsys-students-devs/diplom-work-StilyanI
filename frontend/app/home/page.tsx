import { AppBar, Box, IconButton, Toolbar, Typography } from "@mui/material";
import MenuIcon from '@mui/icons-material/Menu';
import { AccountCircle } from "@mui/icons-material";
import Link from "next/link";

export default function HomePage() {
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

            <Typography variant="h4">Continue Watching</Typography>
            <Typography variant="h4">Movies</Typography>
            <Typography variant="h4">Shows</Typography>
        </Box>
    )
}