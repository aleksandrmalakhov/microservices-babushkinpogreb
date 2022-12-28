package ru.relex.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_photo")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String telegramFileId;

    @OneToOne
    BinaryContent binaryContent;
    Long fileSize;

    @ManyToOne
    AppUser appUser;
}