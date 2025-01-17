package ec.com.reactive.music.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Document(collection = "Playlist")
@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
public class Playlist {
    @Id
    private String idPlaylist;
    private String name;
    private String username;
    private ArrayList<Song> songs;
    private LocalTime duration;

    public void increasePlaylistDuration(LocalTime songDuration){
        this.duration = this.duration.plusHours(songDuration.getHour())
                .plusMinutes(songDuration.getMinute())
                .plusSeconds(songDuration.getSecond());
    }

    public void decreasePlaylistDuration(LocalTime songDuration){
        this.duration = this.duration.minusHours(songDuration.getHour())
                .minusMinutes(songDuration.getMinute())
                .minusSeconds(songDuration.getSecond());
    }


}
