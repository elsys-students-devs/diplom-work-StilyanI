import { AppBar, Box, Container, IconButton, Toolbar } from "@mui/material";
import MenuIcon from '@mui/icons-material/Menu';
import { AccountCircle } from "@mui/icons-material";

export default function HomePage() {
    return(
        <Box>
            <AppBar>
                <Toolbar>
                    <IconButton>
                        <MenuIcon/>
                    </IconButton>

                    <Box sx={{flexGrow: 1}}/>

                    <IconButton>
                        <AccountCircle/>
                    </IconButton>
                </Toolbar>
            </AppBar>
        </Box>
    )
}