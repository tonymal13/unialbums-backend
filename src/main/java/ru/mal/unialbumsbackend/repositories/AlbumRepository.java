package ru.mal.unialbumsbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mal.unialbumsbackend.domain.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album,Long> {
}
