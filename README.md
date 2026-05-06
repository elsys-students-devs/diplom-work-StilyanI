# Video Streaming Platform

Self-hosted video streaming platform with metadata scraping using user-provided media content.

## Tech Stack

| Layer    | Technology        |
|----------|-------------------|
| Frontend | React, Node.js    |
| Backend  | Java, Spring Boot |
| Database | PostgreSQL        |
| Metadata | TMDB API          |

## Installation

Requirements: [Docker Engine](https://docs.docker.com/engine/)

Clone the repository and create a `.env` file similar to [.env.example](/.env.example). For the `TMDB_API_ACCESS_TOKEN`, create an account and get an API token from [TMDB's website](https://www.themoviedb.org/settings/api).
Open a terminal in the project root and run the `docker-compose.yml` using:

```
docker-compose up --build
```

## Usage

Create a `videos` directory in the project root. Move your video files inside the folder according to the following rules:

### Movies

If the video file represents a movie, move it inside a `Movies` directory within `videos` with a subdirectory titled:

```
Title (Year of release) [ID from IMDb]
```

with the year of release and ID being optional, and useful for disambiguation when there exist multiple entries with the same title. Examples:

```
Title

Title (YYYY)

Title [ttXXXXXXXX]

Title (YYYY) [ttXXXXXXXX]
```


### TV Episodes

If the video file represents an episode from a TV show, move it inside a `Shows` directory, in a subdirectory titled using the same convention as for movies. Inside, create another subfolder titled `S##`, meaning the season number, with `##` being replaced by its number (e.g. 01, 02, 03). Move the video file inside and rename it `E##` representing the episode number, keeping the original file extension.

 ---

An example folder structure:
```
videos/
├── Movies/
│   └── Title (YYYY) [ttXXXXXXXX]/
│       └── movie.mp4
└── Shows/
    └── Title (YYYY) [ttXXXXXXXX]/
        ├── S01/
        │   ├── E01.mp4
        │   └── E02.mp4
        └── S02/
            └── E01.mp4
```