package guide_book_4.KTO_public_api_4.dto;

import guide_book_4.KTO_public_api_4.enums.ProviderEnums;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String username;

    private String password;

    private ProviderEnums provider;

    private String lineId;
}