package edu.hm.cs.swt2ss18.wmtipp.mvc.tipps;

import static edu.hm.cs.swt2ss18.wmtipp.mvc.AuthenticationHelper.isAdmin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.InvalidTippExeception;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.NegativTippException;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.SpielNichtTippbarException;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.Tipp;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

/**
 * Spring MVC Controller für die Tippseiten.
 * 
 * @author katz.bastian
 */
@Controller
public class TippController {

  private static final Logger LOG = LoggerFactory.getLogger(TippController.class);

  @Autowired
  SpielService spielService;

  @Autowired
  TippService tippService;

  @Autowired
  MitspielerService userService;

  @GetMapping("/tipps")
  public String home(Model model, Authentication auth) {
    model.addAttribute("administration", isAdmin(auth));
    model.addAttribute("tipps", getTipps(auth.getName()));

    return "tipplist";
  }

  @GetMapping("/tipps/{spielId}")
  public String tippEdit(Model model, Authentication auth, @PathVariable("spielId") Long spielId) {
    model.addAttribute("administration", isAdmin(auth));

    Tipp tipp = tippService.findeTipp(auth.getName(), spielId);
    if (tipp == null) {
      tipp = new Tipp();
    }
    Spiel spiel = spielService.findeSpiel(spielId);
    model.addAttribute("spiel", spiel);
    model.addAttribute("tipp", tipp);
    return "tippedit";
  }

  @PostMapping("/tipps/{spielId}")
  public String createOrUpdateTipp(Authentication auth, @PathVariable("spielId") Long spielId,
      @ModelAttribute("tipp") Tipp tipp, Model model) {

    try {
      tippService.legeTippAb(auth.getName(), spielId, tipp.getToreHeimMannschaft(),
          tipp.getToreGastMannschaft());

    } catch (NegativTippException e) {
      LOG.error("Eingegebener Tipp ist negativ  {}", tipp.getErgebnis());
      model.addAttribute("negativfehler", e);
      return tippEdit(model, auth, spielId);

    } catch (InvalidTippExeception e) {
      LOG.error("Eingegebener Tipp ist ungültig {}", tipp.getErgebnis());
      model.addAttribute("invalidtippfehler", e);
      return tippEdit(model, auth, spielId);

    } catch (SpielNichtTippbarException e) {
      LOG.error(
          "Der eingegebene Tipp {}  wurde nicht akzeptiert, weil das Spiel nicht tippbar ist.",
          tipp.getErgebnis());
      model.addAttribute("spielNichtTippbarFehler", e);
      return tippEdit(model, auth, spielId);
    }
    return "redirect:/tipps";
  }

  private TippEintrag createViewTipp(Spiel spiel, Tipp tipp) {
    TippEintrag tippEintrag = new TippEintrag();
    tippEintrag.setHeimMannschaft(spiel.getHeimMannschaft());
    tippEintrag.setGastMannschaft(spiel.getGastMannschaft());
    tippEintrag.setErgebnis(spiel.getErgebnis());
    tippEintrag.setTippbar(spiel.isTippbar());
    tippEintrag.setSpielId(spiel.getId());
    tippEintrag.setVorbei(spiel.isVorbei());
    if (spiel.getSpielDate() != null) {
      DateTimeFormatter deutscheZeit = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
      // LocalDateTime.parse(zeitString, deutscheZeit)
      tippEintrag.setAnstossZeit(spiel.getSpielDate().format(deutscheZeit));
    }

    if (tipp != null) {
      tippEintrag.setTipp(tipp.getErgebnis());
      tippEintrag
          .setPunkte(tippService.berechnePunkte(tipp.getMitspieler().getLogin(), spiel.getId()));
    }
    return tippEintrag;
  }

  private List<TippEintrag> getTipps(String name) {
    List<Spiel> matches = spielService.findeAlleSpiele();
    List<TippEintrag> tipps = new ArrayList<>();

    for (Spiel spiel : matches) {
      TippEintrag viewtipp = createViewTipp(spiel, tippService.findeTipp(name, spiel.getId()));
      tipps.add(viewtipp);
    }
    return tipps;
  }
}
