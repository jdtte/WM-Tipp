package edu.hm.cs.swt2ss18.wmtipp;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.LoginAlreadyUsedException;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.UserNotAcceptedException;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielRepository;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.InvalidTippExeception;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.NegativTippException;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.SpielNichtTippbarException;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

/**
 * Diese Klasse enthält Testdaten für manuelle Tests, um das Testen von Änderungen zu beschleunigen.
 * 
 * @author katz.bastian
 */
@Component
@Profile("test") // start nur wenn man profile "test" startet, sonst wird ignoriert
public class WmtippTestData {

  @Autowired
  SpielRepository spielRepo;

  @Autowired
  MitspielerService mitspielerService;

  @Autowired
  SpielService spielService;

  @Autowired
  TippService tippService;

  @PostConstruct
  public void createTestData() throws UserNotAcceptedException, LoginAlreadyUsedException,
      NegativTippException, InvalidTippExeception, SpielNichtTippbarException {
    loescheMatches();
    erzeugeTestSpieler();
    erzeugeSpieleUndTipps();
  }

  private void loescheMatches() {
    spielRepo.deleteAll();
  }

  private void erzeugeTestSpieler() throws UserNotAcceptedException, LoginAlreadyUsedException {
    // Das {noop} steht für "keinen Hash" -> Praktikum
    String noHash = "{noop}";
    mitspielerService.legeAn(new Mitspieler("a", noHash, "a1", false));
    mitspielerService.legeAn(new Mitspieler("b", noHash, "b1", false));
    mitspielerService.legeAn(new Mitspieler("c", noHash, "c1", false));
    mitspielerService.legeAn(new Mitspieler("d", noHash, "d1", false));
  }

  private void erzeugeSpieleUndTipps() throws NegativTippException, InvalidTippExeception, SpielNichtTippbarException {
    Spiel spiel = new Spiel("Friesland", "UNO");
    spiel.setTippbar(false);
    spiel.setVorbei(true);
    spiel.setToreHeimMannschaft(1);
    spiel.setToreGastMannschaft(1);
    spielRepo.save(spiel);
    tippService.legeTippAb("a", spiel.getId(), 1, 3);
    tippService.legeTippAb("b", spiel.getId(), 2, 3);
    tippService.legeTippAb("c", spiel.getId(), 3, 3);

    spiel = new Spiel("Schweiz", "Kamerun");
    spielRepo.save(spiel);
    spiel.setTippbar(false);
    spiel.setVorbei(true);
    spiel.setToreHeimMannschaft(1);
    spiel.setToreGastMannschaft(3);
    tippService.legeTippAb("a", spiel.getId(), 3, 3);
    tippService.legeTippAb("b", spiel.getId(), 1, 3);
    tippService.legeTippAb("c", spiel.getId(), 2, 3);

  }
}
