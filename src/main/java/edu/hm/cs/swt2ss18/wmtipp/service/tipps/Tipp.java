package edu.hm.cs.swt2ss18.wmtipp.service.tipps;

import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * Entity-Klasse fuer ein Tipp.
 * 
 * @author 
 *
 */
@Entity
public class Tipp {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  Long id;

  @ManyToOne
  Mitspieler mitspieler;

  @ManyToOne
  Spiel spiel;

  int toreHeimMannschaft;

  int toreGastMannschaft;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Mitspieler getMitspieler() {
    return mitspieler;
  }

  public void setMitspieler(Mitspieler mitspieler) {
    this.mitspieler = mitspieler;
  }

  public Spiel getSpiel() {
    return spiel;
  }

  public void setSpiel(Spiel match) {
    this.spiel = match;
  }

  public int getToreHeimMannschaft() {
    return toreHeimMannschaft;
  }

  public void setToreHeimMannschaft(Integer toreHeimMannschaft) {
    this.toreHeimMannschaft = toreHeimMannschaft;
  }

  public int getToreGastMannschaft() {
    return toreGastMannschaft;
  }

  public void setToreGastMannschaft(Integer toreGastMannschaft) {
    this.toreGastMannschaft = toreGastMannschaft;
  }

  @Transient
  public String getErgebnis() {
    return toreHeimMannschaft + ":" + toreGastMannschaft;
  }

}
