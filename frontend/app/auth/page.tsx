"use client";

import {Alert, Box, Button, Container, Paper, TextField, Typography} from "@mui/material";
import {FormEvent, useEffect, useState} from "react";
import {styled} from "@mui/system";
import {useUser} from "@/app/hooks/UserHook";
import {redirect} from "next/navigation";
import * as AuthService from "@/app/services/AuthService";
import {useLocalStorage} from "@/app/hooks/LocalStorageHook";

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
    const { user, setUser } = useUser();
    const [_, setUserId] = useLocalStorage("userId", null);

    const [login, setLogin] = useState(true);

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [errorMessage, setErrorMessage] = useState("");

    const handleSubmit = async (e: FormEvent) => {
        setErrorMessage("");
        e.preventDefault();

        let fetched: () => Promise<void>;
        if(login) {
            fetched = async () => {
                const res = await AuthService.login({username, password});
                if (res.status === 200) {
                    setUser(res.data);
                }
            }
        } else {
            if (password !== confirmPassword) {
                setErrorMessage("Password wasn't correctly repeated");
                return;
            }

            fetched = async () => {
                const res = await AuthService.register({username, password});
                if (res.status === 200) {
                    setUser(res.data);
                }
            }
        }

        await fetched().catch((error: any) => {
            const message =
                error?.response?.data?.message ||
                error?.message ||
                "Something went wrong";

            setErrorMessage(message);
        });
    }

    const toggleLogin = () => {
        setLogin(!login);
    };

    useEffect(() => {
        if (user) {
            setUserId(user.id);
            redirect("/home");
        }
    }, [user]);

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

                            {errorMessage !== "" && <Alert variant="filled" sx={{mt: 2}} severity="error">{errorMessage}</Alert>}

                            <Button
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, backgroundColor: "#333333" }}
                                type="submit"
                            >
                                {login ? "Sign in" : "Sign Up"}
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