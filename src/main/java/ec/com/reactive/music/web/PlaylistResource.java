package ec.com.reactive.music.web;

import ec.com.reactive.music.domain.dto.PlaylistDTO;
import ec.com.reactive.music.service.impl.PlaylistServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PlaylistResource {

    @Autowired
    PlaylistServiceImpl playlistService;

    @GetMapping("/findAllPlaylist")
    private Mono<ResponseEntity<Flux<PlaylistDTO>>> getPlaylists(){
        return playlistService.findAllPlaylists();
    }

    @GetMapping("/findPlaylist/{id}")
    private Mono<ResponseEntity<PlaylistDTO>> getPlaylistById(@PathVariable String id){
        return playlistService.findPlaylistById(id);
    }

    @PostMapping("/savePlaylist")
    private Mono<ResponseEntity<PlaylistDTO>> postPlaylist(@RequestBody PlaylistDTO playlistDTO){
        return playlistService.savePlaylist(playlistDTO);
    }

    @PutMapping("/updatePlaylist/{id}")
    private Mono<ResponseEntity<PlaylistDTO>> putPlaylist(@PathVariable String id ,
                                                          @RequestBody PlaylistDTO playlistDTO)
    {
        return playlistService.updatePlaylist(id, playlistDTO);
    }

    @DeleteMapping("/deletePlaylist/{id}")
    private Mono<ResponseEntity<String>> deletePlaylist(@PathVariable String id){
        return playlistService.deletePlaylist(id);
    }

}
