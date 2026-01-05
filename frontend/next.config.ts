import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  async redirects(){
    return [
      {
        source:'/',
        destination:'/home',
        permanent: true
      }
    ]
  }
};

module.exports = {
    images: {
        remotePatterns: [
            {
                protocol: 'https',
                hostname: 'image.tmdb.org',
                port: '',
                pathname: '/t/p/**',
            },
        ],
    },
}

export default nextConfig;
