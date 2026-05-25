package com.video.api.video;

import com.video.api.video.config.VideoProperties;
import com.video.api.video.exception.InvalidStreamQualityException;
import com.video.api.video.exception.TranscodingFailedException;
import com.video.api.video.exception.VideoFileNotFoundException;
import com.video.api.video.model.Video;
import com.video.api.video.repository.VideoRepository;
import com.video.api.video.service.TranscodingService;
import com.video.api.video.service.VideoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoServiceImplTest {

    @TempDir
    Path tempDir;

    @Mock
    private TranscodingService transcodingService;

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private VideoServiceImpl videoService;

    @Test
    void getVideoMasterPlaylistCreatesMasterManifestWhenMissing() throws Exception {
        configureVideoProperties();

        Resource resource = videoService.getVideoMasterPlaylist(0L);

        assertThat(resource.exists()).isTrue();
        String content = Files.readString(resource.getFile().toPath());
        assertThat(content).contains("#EXTM3U")
            .contains("1080p/playlist.m3u8")
            .contains("720p/playlist.m3u8")
            .contains("480p/playlist.m3u8");
    }

    @Test
    void getVideoPlaylistReturnsExistingPlaylistWithoutTranscoding() throws Exception {
        configureVideoProperties();
        Path sourceVideo = Files.createFile(tempDir.resolve("movie.mp4"));
        mockVideo(1L, sourceVideo);

        Path playlistPath = tempDir.resolve(Path.of("hls", "1", "720p", "playlist-temp.m3u8"));
        Files.createDirectories(playlistPath.getParent());
        Files.writeString(playlistPath, "#EXTM3U\n");

        Resource resource = videoService.getVideoPlaylist(1L, "720p").join();

        assertThat(resource.getFile().toPath()).isEqualTo(playlistPath);
        verifyNoInteractions(transcodingService);
    }

    @Test
    void getVideoSegmentReturnsExistingSegmentWithoutTranscoding() throws Exception {
        configureVideoProperties();
        Path sourceVideo = Files.createFile(tempDir.resolve("movie.mp4"));
        mockVideo(2L, sourceVideo);

        Path segmentPath = tempDir.resolve(Path.of("hls", "2", "720p", "segment003.ts"));
        Files.createDirectories(segmentPath.getParent());
        Files.writeString(segmentPath, "segment");

        Resource resource = videoService.getVideoSegment(2L, "720p", 3).join();

        assertThat(resource.getFile().toPath()).isEqualTo(segmentPath);
        verifyNoInteractions(transcodingService);
    }

    @Test
    void getVideoPlaylistThrowsForInvalidQuality() throws Exception {
        configureVideoProperties();
        Path sourceVideo = Files.createFile(tempDir.resolve("movie.mp4"));
        mockVideo(3L, sourceVideo);

        assertThatThrownBy(() -> videoService.getVideoPlaylist(3L, "144p"))
                .isInstanceOf(InvalidStreamQualityException.class)
                .hasMessageContaining("Given stream quality(144p) is not valid");
    }

    @Test
    void getVideoSegmentThrowsWhenVideoDoesNotExist() {
        configureVideoProperties();
        when(videoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> videoService.getVideoSegment(99L, "720p", 0))
                .isInstanceOf(VideoFileNotFoundException.class)
                .hasMessage("Source video not found for videoId=99");
    }

    @Test
    void getVideoSegmentCompletesWithExceptionWhenTranscodingFails() throws Exception {
        configureVideoProperties();
        Path sourceVideo = Files.createFile(tempDir.resolve("movie.mp4"));
        mockVideo(4L, sourceVideo);
        when(transcodingService.hasJobFailed(sourceVideo, "720p")).thenReturn(true);

        assertThatThrownBy(() -> videoService.getVideoSegment(4L, "720p", 4).join())
                .isInstanceOf(CompletionException.class)
                .hasCauseInstanceOf(TranscodingFailedException.class)
                .rootCause()
                .hasMessage("Transcoding failed for videoId=4");

        verify(transcodingService).ensureTranscoding(
                sourceVideo,
                tempDir.resolve(Path.of("hls", "4", "720p")),
                com.video.api.video.model.StreamQuality.byName("720p"),
                4
        );
    }

    private void configureVideoProperties() {
        VideoProperties properties = new VideoProperties();
        properties.setHlsOutputPath(tempDir.resolve("hls").toString());
        properties.setSegmentDuration(10);
        properties.setSeekThresholdSegments(2);
        properties.setStoragePath(tempDir.toString());
        org.springframework.test.util.ReflectionTestUtils.setField(videoService, "videoProperties", properties);
    }

    private void mockVideo(Long id, Path sourceVideo) {
        Video video = new Video();
        video.setId(id);
        video.setPathToVideo(sourceVideo.toString());
        when(videoRepository.findById(id)).thenReturn(Optional.of(video));
    }
}