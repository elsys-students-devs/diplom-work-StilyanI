import type { Metadata } from "next";
import { Archivo } from "next/font/google";
import ThemeRegistry from "@/app/theme/ThemeRegistry";
import "./globals.css";
import Header from "@/app/components/common/Header";
import {UserProvider} from "@/app/contexts/UserContext";

const archivo = Archivo({
    subsets: ["latin"],
    variable: "--font-archivo",
});

export const metadata: Metadata = {
  title: "Video Streaming",
  description: "Video streaming platform for movies and shows",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={archivo.variable}
      >
          <UserProvider>
              <ThemeRegistry>
                  <Header/>
                  {children}
              </ThemeRegistry>
          </UserProvider>
      </body>
    </html>
  );
}
