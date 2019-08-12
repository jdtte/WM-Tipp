package edu.hm.cs.swt2ss18.wmtipp.mvc.ergebnisse;

import static edu.hm.cs.swt2ss18.wmtipp.mvc.AuthenticationHelper.isAdmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;

/**
 * 
 * Spring Controller fuer die Ergebnis-Seite. "/ergebnisse" 
 * 
 * @author Jannis Ditterich
 *
 */
@Controller
public class ErgebnisController {

  @Autowired
  SpielService spielService;

  @GetMapping("/ergebnisse")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public String zeigeSpielliste(Model model, Authentication auth) {
    model.addAttribute("administration", isAdmin(auth));
    model.addAttribute("spiele", spielService.findeAlleSpiele());
    return "ergebnisliste";
  }

  @GetMapping("/ergebnisse/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public String showMatch(Model model, Authentication auth, @PathVariable("id") Long id) {
    model.addAttribute("administration", isAdmin(auth));
    model.addAttribute("spiel", spielService.findeSpiel(id));
    return "ergebnisedit";
  }

  @PostMapping("/ergebnisse")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public String aktualisiereSpiel(@ModelAttribute("spiel") Spiel spiel) {
    spiel.setVorbei(true);
    spielService.aktualisiere(spiel);
    return "redirect:/ergebnisse";
  }

  @PostMapping("/ergebnisse/{id}/tippbar")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public String schalteTippbar(@PathVariable("id") Long id) {
    Spiel spiel = spielService.findeSpiel(id);
    spiel.setTippbar(!spiel.isTippbar());
    spielService.aktualisiere(spiel);
    return "redirect:/ergebnisse";
  }

}
