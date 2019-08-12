package edu.hm.cs.swt2ss18.wmtipp.service.spiele;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entity-Klasse fuer ein Spiel.
 * 
 * @author 
 *
 */
@Entity
public class Spiel {

  public static final Logger LOG = LoggerFactory.getLogger(Spiel.class);

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  Long id;

  String heimMannschaft;

  String gastMannschaft;

  boolean tippbar = true;

  boolean vorbei = false;

  int toreHeimMannschaft;

  int toreGastMannschaft;

  LocalDateTime spielDate;

  /**
   * Spiel, mit einer Heimmannschaft und einer Gastmannschaft.
   * 
   * @param heimMannschaft
   *          String, Name der Heimmanschaft
   * @param gastMannschaft
   *          String, Name der Gastmanschaft
   */
  public Spiel(String heimMannschaft, String gastMannschaft) {
    super();
    this.heimMannschaft = heimMannschaft;
    this.gastMannschaft = gastMannschaft;
  }

  /**
   * <p>
   * Spiel, mit einer Heimmannschaft und einer Gastmannschaft mit Angabe des Anstoss Zeitpunkts.
   * </p>
   * <p>
   * Startzeitpunkt im Format "dd.MM.yyyy HH:mm"
   * </p>
   * 
   * @param heimMannschaft
   *          String, Name der Heimmanschaft
   * @param gastMannschaft
   *          String, Name der Gastmanschaft
   * @param zeitString
   *          String, Eingabe des Startzeitpunktes im Format "dd.MM.yyyy HH:mm"
   */
  public Spiel(String heimMannschaft, String gastMannschaft, String zeitString) {
    super();
    this.heimMannschaft = heimMannschaft;
    this.gastMannschaft = gastMannschaft;
    DateTimeFormatter deutscheZeit = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    this.spielDate = LocalDateTime.parse(zeitString, deutscheZeit);
  }

  protected Spiel() {
    // nicht entfernen! Wird für JPA benötigt!
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getHeimMannschaft() {
    return heimMannschaft;
  }

  public void setHeimMannschaft(String heimMannschaft) {
    this.heimMannschaft = heimMannschaft;
  }

  public String getGastMannschaft() {
    return gastMannschaft;
  }

  public void setGastMannschaft(String gastMannschaft) {
    this.gastMannschaft = gastMannschaft;
  }

  public boolean isTippbar() {
    return tippbar;
  }

  /**
   * <p>
   * Setzt ein Spiel Tippbar.
   * </p>
   * <p>
   * Sollte ein Spiel @link {@link #isVorbei()} sein, kann, das Spiel nicht mehr Tippbar gemacht
   * werden.
   * </p>
   * 
   * @param tippbar
   *          boolean, ob Tippbar
   */
  public void setTippbar(boolean tippbar) {
    if (vorbei && tippbar) {
      throw new ValidationException(
          "Abgeschlossene Spiele können nicht zum Tippen freigegeben werden!");
    }
    this.tippbar = tippbar;
  }

  public boolean isVorbei() {
    return vorbei;
  }

  /**
   * Setzt ein Spiel vorbei.
   * 
   * @param vorbei
   *          boolean, ob vorbei
   */
  public void setVorbei(boolean vorbei) {
    this.vorbei = vorbei;
    if (vorbei) {
      setTippbar(false);
    }

  }

  public int getToreHeimMannschaft() {
    return toreHeimMannschaft;
  }

  public void setToreHeimMannschaft(int toreHeimMannschaft) {
    this.toreHeimMannschaft = toreHeimMannschaft;
  }

  public int getToreGastMannschaft() {
    return toreGastMannschaft;
  }

  public void setToreGastMannschaft(int toreGastMannschaft) {
    this.toreGastMannschaft = toreGastMannschaft;
  }

  @Transient
  public String getErgebnis() {
    return vorbei ? (toreHeimMannschaft + ":" + toreGastMannschaft) : "---";
  }

  public LocalDateTime getSpielDate() {
    return spielDate;
  }

  public void setSpielDate(LocalDateTime spielDate) {
    this.spielDate = spielDate;
  }

  /**
   * <p>
   * Gibt den Spielzeitpunkt als deutsch formatierten ZeitString zurück. Sollte kein Date gefunden werden, wird ein cooler Fußball zurück gegeben.
   * </p>
   * <p>
   * Wird in der Ergebnisliste.html verwendet, als Anzeige des Anstosses.
   * </p>
   * 
   * @return String, mit deutsch formatierten Zeitpunkt ("dd.MM.yyyy HH:mm")
   */
  public String getSpielDateGermanString() {
    DateTimeFormatter deutscheZeit = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    if (spielDate != null) {
      return spielDate.format(deutscheZeit);
    }
    return "\u26BD";
  }

}
