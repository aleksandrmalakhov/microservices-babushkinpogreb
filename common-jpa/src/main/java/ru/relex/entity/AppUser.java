package ru.relex.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import ru.relex.entity.enums.UserState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@Table(name = "app_user")
@EqualsAndHashCode(exclude = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long telegramUserId;

    @CreationTimestamp
    LocalDateTime firstLoginDate;
    String firstName;
    String lastName;
    String userName;
    String email;
    Boolean isActive;

    @Enumerated(EnumType.STRING)
    UserState userState;

    @OneToMany(mappedBy = "appUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<AppPhoto> photos;

    @OneToMany(mappedBy = "appUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<AppDocument> documents;

    public AppUser() {
        this.photos = new ArrayList<>();
        this.documents = new ArrayList<>();
    }
}