package guide_book_4.KTO_public_api_4.contoroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class MainController {

    @GetMapping("/admin")
    public String mainP() {

        return "admin";
    }
}