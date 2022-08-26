package ec.com.reactive.music.service;

import ec.com.reactive.music.domain.dto.PlaylistDTO;
import ec.com.reactive.music.domain.dto.SongDTO;
import ec.com.reactive.music.domain.entities.Playlist;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPlaylistService {

    Mono<ResponseEntity<Flux<PlaylistDTO>>> findAllPlaylists();
    Mono<ResponseEntity<PlaylistDTO>> findPlaylistById(String id);
    Mono<ResponseEntity<PlaylistDTO>> savePlaylist(PlaylistDTO playlistDTO);
    Mono<ResponseEntity<PlaylistDTO>> updatePlaylist (String id, PlaylistDTO playlistDTO);

    Mono<ResponseEntity<PlaylistDTO>> addSongPlaylist (String idPlaylist, SongDTO songDTO);

    Mono<ResponseEntity<PlaylistDTO>> removeSongPlaylist (String idPlaylist, SongDTO songDTO);

    Mono<ResponseEntity<String>> deletePlaylist (String idPlaylist);

    Playlist DTOToEntity (PlaylistDTO playlistDTO);
    PlaylistDTO entityToDTO(Playlist playlist);

}
