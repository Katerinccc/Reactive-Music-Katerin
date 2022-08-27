package ec.com.reactive.music.service.impl;

import ec.com.reactive.music.domain.dto.PlaylistDTO;
import ec.com.reactive.music.repository.IPlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceImplTest {

    @Mock
    IPlaylistRepository iPlaylistRepositoryMock;
    ModelMapper modelMapper;
    PlaylistServiceImpl playlistService;
    SongServiceImpl songService;

    @BeforeEach
    void init(){
        modelMapper = new ModelMapper();
        playlistService = new PlaylistServiceImpl(iPlaylistRepositoryMock, songService, modelMapper);
    }

    @Test
    void updatePlaylist() {

        ResponseEntity<PlaylistDTO> playlistDTOResponse = new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        Mockito.when(iPlaylistRepositoryMock.findById(Mockito.any(String.class))).thenReturn(Mono.empty());

        var service = playlistService.updatePlaylist("fdsfsd", null);

        StepVerifier.create(service)
                .expectNext(playlistDTOResponse)
                .expectComplete()
                .verify();
    }
}