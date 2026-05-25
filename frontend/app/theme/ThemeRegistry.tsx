"use client";

import * as React from "react";
import { ThemeProvider, createTheme } from "@mui/material/styles";

export default function ThemeRegistry({children}: Readonly<{ children: React.ReactNode; }>)
{
    const theme = React.useMemo(
        () =>
            createTheme({
                typography: {
                    fontFamily: "var(--font-archivo), sans-serif",
                },
            }),
        []
    );

    return (
        <ThemeProvider theme={theme}>
            {children}
        </ThemeProvider>
    );
}
