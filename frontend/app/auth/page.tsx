"use client";

import {Box, Button, Container, Paper, TextField, Typography} from "@mui/material";
import {FormEvent, useState} from "react";
import {styled} from "@mui/system";

const StyledTextField = styled(TextField)({
    '& .MuiInputLabel-root': {
        color: 'white',
    },
    '& .MuiInput-underline:after': {
        borderBottomColor: '#B2BAC2',
    },
    '& .MuiOutlinedInput-root': {
        '& fieldset': {
            borderColor: 'white',
        },
        '&:hover fieldset': {
            borderColor: 'white',
        },
        '&.Mui-focused fieldset': {
            borderColor: 'white',
        },
        '& input': {
            color: 'white',
        }
    },
});

export default function AuthPage() {
    const [login, setLogin] = useState(true);

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        if(!login){
            if(password !== confirmPassword){
                //handle
            }
        }
        //handle auth
    }

    const toggleLogin = () => {
        setLogin(!login);
    };

    return (
        <div>
            <Container maxWidth="sm" sx={{ display: "full" }}>
                <Paper elevation={3} sx={{ padding: 4, mt: 15, backgroundColor: "#5a5a5a" }}>
                    <Box
                        sx={{
                            display: "flex",
                            flexDirection: "column",
                            alignItems: "center"
                        }}
                    >
                        <Typography component="h1" variant="h5" sx={{color: 'white'}}>
                            Please sign in
                        </Typography>
                        <Box component="form" onSubmit={handleSubmit}>
                            <StyledTextField
                                margin="normal"
                                fullWidth
                                required
                                label="Username"
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                            />

                            <StyledTextField
                                margin="normal"
                                fullWidth
                                required
                                label="Password"
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />

                            {login ? null :
                            <StyledTextField
                                margin="normal"
                                fullWidth
                                required
                                label="Confirm Password"
                                type="password"
                                onChange={(e) => setConfirmPassword(e.target.value)}
                            />
                            }

                            <Button
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, backgroundColor: "#333333" }}
                                type="submit"
                            >
                                Sign in
                            </Button>
                            <Button sx={{color: "#b6b6b6", ":hover":{color: "white"} }} onClick={toggleLogin}>
                                {login
                                    ? "Don't have an account? Sign Up"
                                    : "Already have an account? Sign in"}
                            </Button>
                        </Box>
                    </Box>
                </Paper>
            </Container>
        </div>
    )
}