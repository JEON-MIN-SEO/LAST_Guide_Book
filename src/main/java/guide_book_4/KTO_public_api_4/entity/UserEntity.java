package guide_book_4.KTO_public_api_4.entity;

import guide_book_4.KTO_public_api_4.enums.ProviderEnums;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "provider")
    private ProviderEnums provider;
}