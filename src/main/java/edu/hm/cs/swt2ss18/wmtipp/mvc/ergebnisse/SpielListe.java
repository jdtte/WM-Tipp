package edu.hm.cs.swt2ss18.wmtipp.mvc.ergebnisse;

import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;

import java.util.ArrayList;
import java.util.List;

public class SpielListe {

  private List<Spiel> spiele;

  public SpielListe() {

  }

  public SpielListe(List<Spiel> spiele) {
    super();
    this.spiele = new ArrayList<>(spiele);
  }

  public List<Spiel> getMatches() {
    return spiele;
  }

  public void setMatches(List<Spiel> spiele) {
    this.spiele = spiele;
  }

}
