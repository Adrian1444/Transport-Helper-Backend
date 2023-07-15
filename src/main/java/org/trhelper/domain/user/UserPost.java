package org.trhelper.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
@AllArgsConstructor
public class UserPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    Driver postCreator;

    String content;

    String timestamp;

    public UserPost(Driver postCreator, String content, String timestamp) {
        this.postCreator = postCreator;
        this.content = content;
        this.timestamp=timestamp;
    }
}
