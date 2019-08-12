package edu.hm.cs.swt2ss18.wmtipp.mvc.useradmin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.LoginAlreadyUsedException;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.UserNotAcceptedException;

/**
 * 
 * Spring MVC Controller fuer die Mitspieler-Seite "/mitspieler".
 * 
 * @author 
 *
 */
@Controller
public class MitspielerController {

  private static final Logger LOG = LoggerFactory.getLogger(MitspielerController.class);

  @Autowired
  MitspielerService mitspielerService;

  @GetMapping("/mitspieler")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public String home(Model model, Authentication auth) {
    model.addAttribute("mitspieler", mitspielerService.findeNormaleMitspieler());
    model.addAttribute("administration",
        auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    model.addAttribute("neuerMitspieler", new Mitspieler());
    return "mitspielerliste";
  }

  @PostMapping("/mitspieler")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public String newUser(@ModelAttribute("neuerMitspieler") Mitspieler neuerMitspieler, Model model,
      Authentication auth) {
    neuerMitspieler.setPassword("{noop}" + neuerMitspieler.getPassword());
    try {
      mitspielerService.legeAn(neuerMitspieler);
    } catch (UserNotAcceptedException e) {
      LOG.error("Der neue Nutzername {} ist reserviert oder ungültig", neuerMitspieler.getLogin());
      model.addAttribute("adminfehler", e);
    } catch (LoginAlreadyUsedException e) {
      LOG.error("Der Nutzer {} war bereits vorhanden und wurde überschrieben",
          neuerMitspieler.getLogin());
      model.addAttribute("userfehler", e);
    }

    model.addAttribute("mitspieler", mitspielerService.findeNormaleMitspieler());
    model.addAttribute("administration",
        auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    model.addAttribute("neuerMitspieler", new Mitspieler());
    return "mitspielerliste";
  }

}
