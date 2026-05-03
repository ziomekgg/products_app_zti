package products.app.zti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

    @GetMapping("/regulamin")
    public String regulamin() { return "info/regulamin"; }

    @GetMapping("/polityka-prywatnosci")
    public String polityka() { return "info/polityka"; }

    @GetMapping("/cookies")
    public String cookies() { return "info/cookies"; }

    @GetMapping("/kontakt")
    public String kontakt() { return "info/kontakt"; }

    @GetMapping("/reklamacje")
    public String reklamacje() { return "info/reklamacje"; }

    @GetMapping("/faq")
    public String faq() { return "info/faq"; }
}