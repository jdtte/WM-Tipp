package edu.hm.cs.swt2ss18.wmtipp.mvc.ranking;

import static edu.hm.cs.swt2ss18.wmtipp.mvc.AuthenticationHelper.isAdmin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

/**
 * Spring MVC Controller für die Übersichtsseite (Mitspieler und Punkte).
 * 
 * @author katz.bastian
 */
@Controller
public class RanglistenController {

  @Autowired
  MitspielerService mitspielerService;

  @Autowired
  TippService tippService;

  /**
   * Erstellt das Modell für die Spieler-Rangliste (Ranking-View).
   * 
   * @param model
   *          MVC-Model
   * @param auth
   *          Authentifizierung
   * @return Ranking-View
   */
  @GetMapping("/")
  public String showRanking(Model model, Authentication auth) {
    model.addAttribute("rangliste", erstelleRangliste());
    model.addAttribute("administration", isAdmin(auth));
    return "rangliste";
  }

  /**
   * Hilfsmethode zum Erstellen der Anzeigeelemente ({@link RanglistenEintrag}s).
   * 
   * @return Liste der Anzeigeelemente.
   */
  protected List<RanglistenEintrag> erstelleRangliste() { // protected for ranglistentest

    List<RanglistenEintrag> ranglistenEintraege = new ArrayList<>();
    for (Mitspieler mitspieler : mitspielerService.findeNormaleMitspieler()) {

      RanglistenEintrag eintrag = new RanglistenEintrag();
      if (mitspieler.getAnzeigeName().length() == 0) {
        eintrag.setAnzeigeName(mitspieler.getLogin());
      } else {
        eintrag.setAnzeigeName(mitspieler.getAnzeigeName());
      }
      eintrag.setPunkte(tippService.berechneGesamtPunkte(mitspieler.getLogin()));
      ranglistenEintraege.add(eintrag);


      Collections.sort(ranglistenEintraege, (RanglistenEintrag one,
          RanglistenEintrag other) -> Integer.compare(other.getPunkte(), one.getPunkte()));
    }
    return ranglistenEintraege;
  }
}
