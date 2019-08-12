package edu.hm.cs.swt2ss18.wmtipp.mvc.statistiken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

/**
 * 
 * Spring Controller fuer die Statistik-Seite "/statistik".
 * 
 * @author Jannis Ditterich
 *
 */
@Controller
public class StatistikController {

  @Autowired
  TippService tippService;

  @GetMapping("/statistik")
  public String home(Model model, Authentication auth) {
    // block 1
    model.addAttribute("gesamtPunkte", tippService.berechneGesamtPunkte(auth.getName()));

    // block 2
    model.addAttribute("anzahlOffenerTipps",
        tippService.berechneAnzahlOffenerTipps(auth.getName()));
    model.addAttribute("anzahlGeschlossenerTipps",
        tippService.berechneAnzahlGeschlossenerTipps(auth.getName()));
    model.addAttribute("anzahlTipps", tippService.berechneAnzahlTipps(auth.getName()));

    //block 3
    model.addAttribute("anzahlRichtigerTipps",
        tippService.berechneAnzahlRichtigerTipps(auth.getName()));
    model.addAttribute("anzahlFalscherTipps",
        tippService.berechneAnzahlFalscherTipps(auth.getName()));
    model.addAttribute("winrate", tippService.berechneAverageWinrate(auth.getName()));
    model.addAttribute("AveragePunktePerTipp",
        tippService.berechneAveragePunktePerTipp(auth.getName()));

    return "statistik";
  }
}
