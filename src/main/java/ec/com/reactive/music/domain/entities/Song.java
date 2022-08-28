package ec.com.reactive.music.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Flux;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Document(collection = "Song")
public class Song {

    @Id
    private String idSong;
    private String name;
    private String idAlbum;
    private String lyricsBy;
    private String producedBy;
    private String arrangedBy;
    private LocalTime duration;

}