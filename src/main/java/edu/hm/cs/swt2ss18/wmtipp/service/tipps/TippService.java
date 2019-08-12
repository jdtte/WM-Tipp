package edu.hm.cs.swt2ss18.wmtipp.service.tipps;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;

/**
 * Spring-Service-Klasse für die Tipps.
 * 
 * @author Jannis
 *
 */
@Service
@Transactional
public class TippService {

  @Autowired
  TippRepository repo;

  @Autowired
  SpielService matchService;

  @Autowired
  MitspielerService mitspielerService;

  private static final Logger LOG = LoggerFactory.getLogger(TippService.class);

  /**
   * Findet einen Tipp, eines Users für ein Spiel.
   * 
   * @param login
   *          username für den der Tipp gefunden werden soll.
   * @param spielId
   *          SpielId für den der Tipp gefunden werden soll.
   * @return Tipp, des Users für die SpielId.
   */
  @PreAuthorize("#login == authentication.name")
  public Tipp findeTipp(String login, Long spielId) {
    Spiel match = matchService.findeSpiel(spielId);
    Mitspieler user = mitspielerService.findeMitspieler(login);
    return repo.findOneBySpielAndMitspieler(match, user);
  }

  /**
   * Legt einen Tipp mit den getippten Daten für ein bestimmtes Spiel ab, im
   * {@link edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippRepository} ab.
   * 
   * 
   * @param login
   *          username des Users, welcher den Tipp abgegeben hat.
   * @param spielId
   *          Id auf der, der Tipp abgelegt wird.
   * @param toreHeimMannschaft
   *          getippte Tore der HeimMannschaft.
   * @param toreGastMannschaft
   *          getippte Tore der GastMannschaft.
   * @throws NegativTippException,
   *           wenn der abgegebene Tipp eine Negative Zahl entählt.
   * @throws InvalidTippExeception,
   *           wenn der abgegebene Tipp ungültig ist.
   * @throws SpielNichtTippbarException
   */
  @PreAuthorize("#login == authentication.name")
  public void legeTippAb(String login, Long spielId, int toreHeimMannschaft, int toreGastMannschaft)
      throws NegativTippException, InvalidTippExeception, SpielNichtTippbarException {
    Mitspieler user = mitspielerService.findeMitspieler(login);
    Spiel match = matchService.findeSpiel(spielId);
    Tipp tipp = repo.findOneBySpielAndMitspieler(match, user);
    if (tipp == null) {
      tipp = new Tipp();
      tipp.setSpiel(match);
      tipp.setMitspieler(user);
    }
    if (!match.isTippbar()) {
      throw new SpielNichtTippbarException("Das Spiel ist nicht mehr Tippbar!");
    }
    if (toreHeimMannschaft < 0 || toreGastMannschaft < 0) {
      throw new NegativTippException("Der eingegebene Tipp ist negativ!");
    }
    if (toreHeimMannschaft > 100 || toreGastMannschaft > 100) {
      throw new InvalidTippExeception("Der eingegebene Tipp ist ungültig!");
    }

    tipp.setToreHeimMannschaft(toreHeimMannschaft);
    tipp.setToreGastMannschaft(toreGastMannschaft);
    if (user != null) {
      LOG.info("Tipp für Spiel {} von {}  ({}) lautet  {}:{}", spielId, login,
          user.getAnzeigeName(), toreHeimMannschaft, toreGastMannschaft);
    }
    repo.save(tipp);

  }

  /**
   * Berechnet die gesamten Punkte eines Users.
   * 
   * 
   * @param login
   *          username
   * @return int, anzahl der gesamten Punkte
   */
  public int berechneGesamtPunkte(String login) {
    Mitspieler mitspieler = mitspielerService.findeMitspieler(login);
    List<Tipp> tipps = repo.getTippsByMitspieler(mitspieler);
    int total = 0;
    for (Tipp tipp : tipps) {
      total += berechnePunkte(tipp);
    }
    return total;
  }

  /**
   * Berechnet die Punkte für einen Tipp für ein bestimmtes Spiel.
   * 
   * Aufruf von {@link #berechnePunkte(Tipp)}.
   * 
   * @param login
   *          username des Users, welcher den Tipp abgegeben hat.
   * @param spielId
   *          Id des getippten Spiels
   * @return
   */
  public int berechnePunkte(String login, long spielId) {
    Tipp tipp = findeTipp(login, spielId);
    return berechnePunkte(tipp);
  }

  /**
   * Berechnet, die Anzahl der geschlossenen Tipps eines Users.
   * 
   * @param login
   *          username
   * @return int, Anzahl der geschlossenen Tipps
   */
  public int berechneAnzahlGeschlossenerTipps(String login) {
    Mitspieler mitspieler = mitspielerService.findeMitspieler(login);
    int tippAnzahl = 0;

    for (Tipp t : repo.getTippsByMitspieler(mitspieler)) {
      Spiel spiel = t.getSpiel();
      if (spiel.isVorbei()) {
        tippAnzahl++;
      }
    }
    return tippAnzahl;
  }

  /*
   * Berechnet die gesamte Anzahl an Tipps eines Users. 
   * 
   * @param login username
   * 
   * @return int, anzahl aller abgegebenen Tipps
   */
  public int berechneAnzahlTipps(String login) {
    Mitspieler mitspieler = mitspielerService.findeMitspieler(login);
    return repo.getTippsByMitspieler(mitspieler).size();
  }

  /**
   * Berechnet, die Anzahl der noch offenen Tipps bzw. der nicht geschlossenen Tipps eines Users.
   * 
   * @param login
   *          username
   * @return int, Anzahl der offenen Tipps
   */
  public int berechneAnzahlOffenerTipps(String login) {
    return berechneAnzahlTipps(login) - berechneAnzahlGeschlossenerTipps(login);
  }

  /**
   * Berechnet, die durchschnittliche Punkte pro Tipp eines Users.
   * 
   * @param login
   *          username
   * @return double durchschnittliche Punkte pro Tipp z.B. 0,3
   */
  public double berechneAveragePunktePerTipp(String login) {
    if (berechneAnzahlGeschlossenerTipps(login) == 0) {
      return 0;
    }
    double number = (double) berechneGesamtPunkte(login) / berechneAnzahlGeschlossenerTipps(login);
    number = Math.floor(number * 100) / 100;
    return number;
  }

  /**
   * Berechnet, die durchschnittliche Gewinnrate von Tipps in Prozent eines Users.
   * 
   * @param login
   *          username
   * @return double, Prozentwert der Gewinnrate
   */
  public double berechneAverageWinrate(String login) {
    if (berechneAnzahlTipps(login) == 0) {
      return 0;
    }
    double number = (double) berechneAnzahlRichtigerTipps(login) / berechneAnzahlTipps(login);
    number = Math.floor((number * 100) * 100) / 100; // rounds % to 2 decimal places
    return number;
  }

  /**
   * Berechnet, die Anzahl richtiger Tipps, von geschlossenen Tipps eines Users.
   * 
   * @param login
   *          username
   * @return int, Anzahl der gesamten richtigen Tipps
   */
  public int berechneAnzahlRichtigerTipps(String login) {
    Mitspieler mitspieler = mitspielerService.findeMitspieler(login);
    List<Tipp> tipps = repo.getTippsByMitspieler(mitspieler);
    int total = 0;
    for (Tipp tipp : tipps) {
      Spiel spiel = tipp.getSpiel();
      if (spiel.isVorbei() && berechnePunkte(tipp) != 0) { // 1 = richtig
        total++;
      }
    }
    return total;
  }

  /**
   * Berechnet, die Anzahl falscher Tipps, von geschlossenen Tipps eines Users.
   * 
   * @param login
   *          username
   * @return int, Anzahl der gesamten falschen Tipps
   */
  public int berechneAnzahlFalscherTipps(String login) {
    Mitspieler mitspieler = mitspielerService.findeMitspieler(login);
    List<Tipp> tipps = repo.getTippsByMitspieler(mitspieler);
    int total = 0;
    for (Tipp tipp : tipps) {
      Spiel spiel = tipp.getSpiel();
      if (spiel.isVorbei() && berechnePunkte(tipp) == 0) { // 0 = falsch
        total++;
      }
    }
    return total;
  }

  /**
   * Vergibt nur einen Punkt, wenn das getippte Ergebnis mit dem Spielergebnis übereinstimmt.
   * 
   * @param tipp
   * @return int 1 wenn Tippergebnis == Spielergebnis, sonst 0
   */
  private int berechnePunkte(Tipp tipp) {
    Spiel spiel = tipp.getSpiel();
    if (!spiel.isVorbei()) {
      return 0;
    }
    if (spiel.getToreHeimMannschaft() == tipp.getToreHeimMannschaft()
        && spiel.getToreGastMannschaft() == tipp.getToreGastMannschaft()) {
      return 1;
    }
    return 0;
  }

}
