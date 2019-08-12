package edu.hm.cs.swt2ss18.wmtipp.service.spiele;

import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring-Service-Klasse fuer die Spiel-Klasse.
 * 
 * @author 
 *
 */
@Service // komponenten automatisch instanziert spring
@Transactional
public class SpielService { 
  public static final Logger LOG = LoggerFactory.getLogger(SpielService.class);

  @Autowired // sinnvoll initasiert von spring
  private SpielRepository spielRepository;

  /**
   * Fügt TestSpiele den spielRepository hinzu (Testdaten).
   */
  @PostConstruct
  public void insertGames() {
    if (spielRepository.count() == 0) {
      // erster Spieltag
      // eingabe der Zeit mit dd.MM.yyyy hh:MM
      spielRepository.save(new Spiel("Ägypten", "Uruguay", "20.06.2018 13:54"));
      spielRepository.save(new Spiel("Russland", "Saudi-Arabien", "20.09.2018 13:54"));
      spielRepository.save(new Spiel("Marokko", "Iran", "24.01.2018 19:12"));
      spielRepository.save(new Spiel("Portugal", "Spanien"));
      spielRepository.save(new Spiel("Frankreich", "Australien", "25.05.2018 20:15"));
      spielRepository.save(new Spiel("Argentinien", "Island"));
      spielRepository.save(new Spiel("Peru", "Dänemark"));
      spielRepository.save(new Spiel("Kroatien", "Nigeria"));
      spielRepository.save(new Spiel("Costa Rica", "Serbien"));
      spielRepository.save(new Spiel("Deutschland", "Mexico"));
      spielRepository.save(new Spiel("Brasilien", "Schweiz"));
      spielRepository.save(new Spiel("Schweden", "Südkorea"));
      spielRepository.save(new Spiel("Belgien", "Panama"));
      spielRepository.save(new Spiel("Tunesien", "England"));
      spielRepository.save(new Spiel("Kolumbien", "Japan"));
      spielRepository.save(new Spiel("Polen", "Senegal"));

      // zweiter Spieltag
      spielRepository.save(new Spiel("Russland", "Ägypten"));
      spielRepository.save(new Spiel("Portugal", "Marokko"));
      spielRepository.save(new Spiel("Uruguay", "Saudi-Arabien"));
      spielRepository.save(new Spiel("Iran", "Spanien"));
      spielRepository.save(new Spiel("Dänemark", "Australien"));
      spielRepository.save(new Spiel("Frankreich", "Peru"));
      spielRepository.save(new Spiel("Argentinien", "Kroatien"));
      spielRepository.save(new Spiel("Brasilien", "Costa Rica"));
      spielRepository.save(new Spiel("Nigeria", "Island"));
      spielRepository.save(new Spiel("Serbien", "Schweiz"));
      spielRepository.save(new Spiel("Belgien", "Tunesien"));
      spielRepository.save(new Spiel("Südkorea", "Mexiko"));
      spielRepository.save(new Spiel("Deutschland", "Schweden"));
      spielRepository.save(new Spiel("England", "Panama"));
      spielRepository.save(new Spiel("Japan", "Senegal"));
      spielRepository.save(new Spiel("Polen", "Kolumbien"));

    }
  }

  public Spiel findeSpiel(Long id) {
    return spielRepository.getOne(id);
  }

  public List<Spiel> findeAlleSpiele() {
    return spielRepository.findAll();
  }

  /**
   * Eingabe eines Spieles, das Spiel wird dann erneut geprüft und mit den neuen Werten im
   * spielRepository gespeichert.
   * 
   * @param spiel
   *          aktualiserendes Spiel
   */
  public void aktualisiere(Spiel spiel) {
    LOG.info("Das Endergebnis von {} : {} (Spiel {}) lautet  {} : {}", spiel.getHeimMannschaft(),
        spiel.getGastMannschaft(), spiel.getId(), spiel.getToreHeimMannschaft(),
        spiel.getToreGastMannschaft());
    spielRepository.save(spiel);

    LOG.info("Kann Spiel {} getippt werden: {}", spiel.getId(), spiel.isTippbar());

  }

  /**
   * Überpüft alle Spiele, ob deren Anstoßzeitpunkt nach dem jetzigen Zeitpunkt liegt und schließt
   * diese, wenn dies der Fall sein sollte.
   * 
   * <p>
   * Vergleicht bei allen Spiele, ob das
   * {@link edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel#spielDate} nach dem jetztigen
   * {@link LocalDateTime} liegt.
   * </p>
   * 
   * <p>
   * Methode wird von einem scheduler in {@link #BackgroundSpielService} in einem Thread geöffnet,
   * welcher jede Minute einmal aufgerufen wird.
   * </p>
   */
  public void closesGamesIfStartAfterNow() {
    List<Spiel> spiele = findeAlleSpiele();
    LocalDateTime jetzt = LocalDateTime.now();
    for (Spiel s : spiele) {
      if (s.getSpielDate() != null) {
        if (jetzt.isAfter(s.getSpielDate()) && s.isTippbar()) {

          LOG.info("Spiel " + s.getId() + " " + s.getHeimMannschaft() + " vs "
              + s.getGastMannschaft() + "ist auf vorbei gesetzt worden");
          s.setTippbar(false);
        } else {
          LOG.trace("Spiel " + s.getId() + " " + s.getHeimMannschaft() + " vs "
              + s.getGastMannschaft() + "ist noch nicht vorbei");

        }
        LOG.trace("Anstoß des Spiels {}, start:  {}, Zeit jetzt  {}  ist Nach? {} \n\n", s.getId(),
            s.getSpielDate(), jetzt, jetzt.isAfter(s.getSpielDate()));
      }
    }
  }

}
