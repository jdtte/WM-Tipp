package edu.hm.cs.swt2ss18.wmtipp.mvc.ranking;

/**
 * Modellklasse für Einträge in der Rangliste.
 * 
 * @author katz.bastian
 */
public class RanglistenEintrag {

  private String anzeigeName;

  private int punkte;

  public String getAnzeigeName() {
    return anzeigeName;
  }

  public void setAnzeigeName(String displayName) {
    this.anzeigeName = displayName;
  }

  public int getPunkte() {
    return punkte;
  }

  public void setPunkte(int points) {
    this.punkte = points;
  }

}
