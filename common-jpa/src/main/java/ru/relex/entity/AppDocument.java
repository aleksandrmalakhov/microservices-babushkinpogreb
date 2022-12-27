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
@Table(name = "app_document")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String telegramFileId;
    String docName;

    @OneToOne
    BinaryContent binaryContent;
    String mimeType;
    Long fileSize;
}