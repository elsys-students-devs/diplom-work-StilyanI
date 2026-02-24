import type { NextConfig } from "next";

const nextConfig: NextConfig = {
    output: "standalone",

    async redirects() {
        return [
            {
                source: "/",
                destination: "/home",
                permanent: true,
            },
        ];
    },

    images: {
        remotePatterns: [
            {
                protocol: "https",
                hostname: "image.tmdb.org",
                port: "",
                pathname: "/t/p/**",
            },
        ],
    },
};

export default nextConfig;