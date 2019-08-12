package edu.hm.cs.swt2ss18.wmtipp.mvc.tipps;

/**
 * MVC-Modell-Klasse für die Spiele aus Sicht eines Mittippers, d.h. ggf. mit einem hinterlegten
 * Tipp.
 * 
 * @author katz.bastian
 *
 */
public class TippEintrag {

  long spielId;

  String heimMannschaft;

  String gastMannschaft;

  String ergebnis;

  String tipp;

  String anstossZeit;

  int punkte;

  boolean tippbar;

  boolean vorbei;

  public String getAnstossZeit() {
    return anstossZeit;
  }

  public void setAnstossZeit(String zeit) {
    this.anstossZeit = zeit;
  }

  public long getSpielId() {
    return spielId;
  }

  public void setSpielId(long spielId) {
    this.spielId = spielId;
  }

  public boolean isTippbar() {
    return tippbar;
  }

  public void setTippbar(boolean tippbar) {
    this.tippbar = tippbar;
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

  public String getErgebnis() {
    return ergebnis;
  }

  public void setErgebnis(String ergebnis) {
    this.ergebnis = ergebnis;
  }

  public String getTipp() {
    return tipp;
  }

  public void setTipp(String tipp) {
    this.tipp = tipp;
  }

  public int getPunkte() {
    return punkte;
  }

  public void setPunkte(int punkte) {
    this.punkte = punkte;
  }

  public boolean isVorbei() {
    return vorbei;
  }

  public void setVorbei(boolean vorbei) {
    this.vorbei = vorbei;
  }

}
