package pl.cyryl.finalproject.app.photo.ProfilePicture;

import lombok.Getter;
import lombok.Setter;
import pl.cyryl.finalproject.app.photo.Photo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Entity
public class ProfilePicture extends Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String path;
    private boolean publicPicture = false;
}
