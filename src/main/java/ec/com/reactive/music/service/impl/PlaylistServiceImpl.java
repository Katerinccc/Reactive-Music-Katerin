package ec.com.reactive.music.service.impl;

import ec.com.reactive.music.domain.dto.PlaylistDTO;
import ec.com.reactive.music.domain.dto.SongDTO;
import ec.com.reactive.music.domain.entities.Playlist;
import ec.com.reactive.music.repository.IPlaylistRepository;
import ec.com.reactive.music.service.IPlaylistService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlaylistServiceImpl implements IPlaylistService {

    @Autowired
    IPlaylistRepository iPlaylistRepository;

    @Autowired
    SongServiceImpl songService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Mono<ResponseEntity<Flux<PlaylistDTO>>> findAllPlaylists() {
        return this.iPlaylistRepository
                .findAll()
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NO_CONTENT.toString())))
                .map(Playlist -> entityToDTO(Playlist))
                .collectList()
                .map(PlaylistDTOS -> new ResponseEntity<>(Flux.fromIterable(PlaylistDTOS),HttpStatus.FOUND))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(Flux.empty(),HttpStatus.NO_CONTENT)));

    }

    @Override
    public Mono<ResponseEntity<PlaylistDTO>> findPlaylistById(String id) {
        return this.iPlaylistRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString()))) //Capture the error
                .map(this::entityToDTO)
                .map(PlaylistDTO -> new ResponseEntity<>(PlaylistDTO, HttpStatus.FOUND)) //Mono<ResponseEntity<PlaylistDTO>>
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND))); //Handle the error
    }

    @Override
    public Mono<ResponseEntity<PlaylistDTO>> savePlaylist(PlaylistDTO PlaylistDTO) {
        return this.iPlaylistRepository
                .save(DTOToEntity(PlaylistDTO))
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.EXPECTATION_FAILED.toString())))
                .map(Playlist -> entityToDTO(Playlist))
                .map(PlaylistDTO1 -> new ResponseEntity<>(PlaylistDTO1,HttpStatus.CREATED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED)));
    }

    @Override
    public Mono<ResponseEntity<PlaylistDTO>> updatePlaylist(String id, PlaylistDTO aDto) {

        return this.iPlaylistRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .flatMap(Playlist -> this.savePlaylist(aDto))
                .map(PlaylistDTOResponseEntity -> new ResponseEntity<>(PlaylistDTOResponseEntity.getBody(),HttpStatus.ACCEPTED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_MODIFIED)));
    }

    @Override
    public Mono<ResponseEntity<PlaylistDTO>> addSongPlaylist(String idPlaylist, SongDTO songDTO) {
        return this.iPlaylistRepository
                .findById(idPlaylist)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .flatMap(playlist -> {
                    playlist.getSongs().add(songService.dtoToEntity(songDTO));
                    playlist.increasePlaylistDuration(songDTO.getDuration());
                    return this.savePlaylist(entityToDTO(playlist));
                })
                .map(playlistDTOResponseEntity -> new ResponseEntity<>(playlistDTOResponseEntity.getBody(),HttpStatus.OK))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_MODIFIED)));
    }

    @Override
    public Mono<ResponseEntity<PlaylistDTO>> removeSongPlaylist(String idPlaylist, SongDTO songDTO) {
        return this.iPlaylistRepository
                .findById(idPlaylist)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .flatMap(playlist -> {
                    playlist.getSongs().remove(songService.dtoToEntity(songDTO));
                    playlist.decreasePlaylistDuration(songDTO.getDuration());
                    return this.savePlaylist(entityToDTO(playlist));
                })
                .map(playlistDTOResponseEntity -> new ResponseEntity<>(playlistDTOResponseEntity.getBody(),HttpStatus.ACCEPTED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_MODIFIED)));
    }

    @Override
    public Mono<ResponseEntity<String>> deletePlaylist(String idPlaylist) {
        return this.iPlaylistRepository
                .findById(idPlaylist)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .flatMap(Playlist -> this.iPlaylistRepository
                        .deleteById(Playlist.getIdPlaylist())
                        .map(monoVoid -> new ResponseEntity<>(idPlaylist, HttpStatus.ACCEPTED)))
                .thenReturn(new ResponseEntity<>(idPlaylist, HttpStatus.ACCEPTED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));

    }

    @Override
    public Playlist DTOToEntity(PlaylistDTO PlaylistDTO) {
        return this.modelMapper.map(PlaylistDTO, Playlist.class);
    }

    @Override
    public PlaylistDTO entityToDTO(Playlist Playlist) {
        return this.modelMapper.map(Playlist,PlaylistDTO.class);
    }


}
