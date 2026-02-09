package com.video.api.user.repository;

import com.video.api.user.model.VideoProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VideoProgressRepository extends JpaRepository<VideoProgress, UUID> {
    Optional<VideoProgress> findVideoProgressByUserIdAndVideoId(UUID userId, String videoId);
}
