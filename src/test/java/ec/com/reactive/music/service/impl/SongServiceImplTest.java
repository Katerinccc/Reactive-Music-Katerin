package ec.com.reactive.music.service.impl;

import ec.com.reactive.music.domain.dto.SongDTO;
import ec.com.reactive.music.domain.entities.Song;
import ec.com.reactive.music.repository.ISongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class SongServiceImplTest {

    @Mock
    ISongRepository repositoryMock;

    ModelMapper modelMapper;
    SongServiceImpl songService;

    private final Song songExpected = new Song(
            "630935a2937f2b44a23863b6",
            "Blue oceans",
            "6308bc3181fed979ea74e52c",
            "Kurt Cobain",
            "Kurt Cobain",
            "Kurt Cobain",
            LocalTime.now());

    @BeforeEach
    void init(){
        modelMapper = new ModelMapper();
        songService = new SongServiceImpl(repositoryMock,modelMapper);
    }

    @Test
    void findAllSongs() {

        ArrayList<Song> listSongs = new ArrayList<>();
        listSongs.add(new Song());
        listSongs.add(new Song());

        ArrayList<SongDTO> listSongDTOS = listSongs.stream()
                .map(song -> modelMapper.map(song,SongDTO.class))
                .collect(Collectors.toCollection(ArrayList::new));

        var fluxResult = Flux.fromIterable(listSongs);
        var fluxResultDTO = Flux.fromIterable(listSongDTOS);

        ResponseEntity<Flux<SongDTO>> result = new ResponseEntity<>(fluxResultDTO, HttpStatus.FOUND);

        Mockito.when(repositoryMock.findAll()).thenReturn(fluxResult);

        var service = songService.findAllSongs();

        StepVerifier.create(service)
                .expectNextMatches(fluxResponseEntity -> fluxResponseEntity.getStatusCode().is3xxRedirection())
                .expectComplete().verify();
    }

    @Test
    void findAllSongsError(){

        ResponseEntity<Flux<SongDTO>> responseExpected = new ResponseEntity<>(Flux.empty(),HttpStatus.NO_CONTENT);

        Mockito.when(repositoryMock.findAll()).thenReturn(Flux.empty());

        var service = songService.findAllSongs();

        StepVerifier.create(service)
                .expectNext(responseExpected)
                .expectComplete()
                .verify();

    }

    @Test
    void findSongByID(){

        var songExpectedDTO = modelMapper.map(songExpected, SongDTO.class);

        ResponseEntity<SongDTO> songResponseDTO = new ResponseEntity<>(songExpectedDTO,HttpStatus.FOUND);

        Mockito.when(repositoryMock.findById(Mockito.any(String.class))).thenReturn(Mono.just(songExpected));

        var service = songService.findSongById("630935a2937f2b44a23863b6");

        StepVerifier.create(service)
                .expectNext(songResponseDTO)
                .expectComplete()
                .verify();

        Mockito.verify(repositoryMock).findById("630935a2937f2b44a23863b6");
    }

    @Test
    void findSongByIdError() {

        ResponseEntity<SongDTO> responseExpected = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Mockito.when(repositoryMock.findById(Mockito.any(String.class))).thenReturn(Mono.empty());

        var service = songService.findSongById("630935a2937f2b44a23863b6");

        StepVerifier.create(service)
                .expectNext(responseExpected)
                .expectComplete().verify();

        Mockito.verify(repositoryMock).findById("630935a2937f2b44a23863b6");

    }

    @Test
    void saveSong() {

        var songExpectedDTO = modelMapper.map(songExpected, SongDTO.class);

        ResponseEntity<SongDTO> responseExpected = new ResponseEntity<>(songExpectedDTO,HttpStatus.CREATED);

        Mockito.when(repositoryMock.save(Mockito.any(Song.class))).thenReturn(Mono.just(songExpected));

        var service = songService.saveSong(songExpectedDTO);

        StepVerifier.create(service)
                .expectNext(responseExpected)
                .expectComplete()
                .verify();

        Mockito.verify(repositoryMock).save(songExpected);
    }

    @Test
    void saveSongError(){

        var songExpectedDTO = modelMapper.map(songExpected, SongDTO.class);

        ResponseEntity<SongDTO> responseExpected = new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);

        Mockito.when(repositoryMock.save(Mockito.any(Song.class))).thenReturn(Mono.empty());

        var service = songService.saveSong(songExpectedDTO);

        StepVerifier.create(service)
                .expectNext(responseExpected)
                .expectComplete()
                .verify();
    }

    @Test
    void updateSong() {

        var songUpdated = songExpected.toBuilder().arrangedBy("Mozart").build();

        var songUpdatedDTO = modelMapper.map(songUpdated, SongDTO.class);

        ResponseEntity<SongDTO> responseExpected = new ResponseEntity<>(songUpdatedDTO,HttpStatus.ACCEPTED);

        Mockito.when(repositoryMock.findById(Mockito.any(String.class))).thenReturn(Mono.just(songExpected));
        Mockito.when(repositoryMock.save(Mockito.any(Song.class))).thenReturn(Mono.just(songUpdated));

        var service =
                songService.updateSong("630935a2937f2b44a23863b6", songUpdatedDTO);

        StepVerifier.create(service)
                .expectNext(responseExpected)
                .expectComplete()
                .verify();

        Mockito.verify(repositoryMock).save(songUpdated);

    }

    @Test
    void updateSongError() {

        ResponseEntity<SongDTO> responseExpected = new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        Mockito.when(repositoryMock.findById(Mockito.any(String.class))).thenReturn(Mono.empty());

        var service = songService.updateSong("Ky71113MpdL", null);

        StepVerifier.create(service)
                .expectNext(responseExpected)
                .expectComplete()
                .verify();
    }

    @Test
    void deleteSong() {

        ResponseEntity<String> responseExpected = new ResponseEntity<>(songExpected.getIdSong(),HttpStatus.ACCEPTED);

        Mockito.when(repositoryMock.findById(Mockito.any(String.class)))
                .thenReturn(Mono.just(songExpected));

        Mockito.when(repositoryMock.deleteById(Mockito.any(String.class)))
                .thenReturn(Mono.empty());


        var service = songService.deleteSong("630935a2937f2b44a23863b6");


        StepVerifier.create(service)
                .expectNext(responseExpected)
                .expectComplete()
                .verify();

        Mockito.verify(repositoryMock).findById(Mockito.any(String.class));
        Mockito.verify(repositoryMock).deleteById(Mockito.any(String.class));

    }

    @Test
    void deleteSongError() {

        ResponseEntity<String> responseExpected = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Mockito.when(repositoryMock.findById(Mockito.any(String.class))).thenReturn(Mono.empty());

        var service = songService.deleteSong("Ky71113MpdL");

        StepVerifier.create(service)
                .expectNext(responseExpected)
                .expectComplete()
                .verify();
    }

}