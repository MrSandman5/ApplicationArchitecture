package com.safonov.galleryservice.ArtGalleryApplication.entity.actor;

import com.safonov.galleryservice.ArtGalleryApplication.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "role")
@EqualsAndHashCode(callSuper = true)
public class Role extends AbstractEntity {

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    private Set<Credentials> users;
}
