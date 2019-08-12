package edu.hm.cs.swt2ss18.wmtipp.service.mitspieler;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Entity-Klasse für die Mitspieler.
 * 
 * @author 
 *
 */
@Entity
public class Mitspieler {

  @Id
  @NotNull
  private String login;

  @NotNull
  private String password;

  private String anzeigeName;

  private boolean isAdmin;

  /**
   * Mitspieler.
   * 
   * @param login
   *          loginname des Mitspielers
   * @param password
   *          password des Mitspielers
   * @param anzeigeName
   *          Anzeigename des Mitspielers
   * @param isAdmin
   *          boolean, ob Mitspieler admin ist
   */
  public Mitspieler(@NotNull String login, @NotNull String password, String anzeigeName,
      boolean isAdmin) {
    super();
    this.login = login;
    this.password = password;
    this.anzeigeName = anzeigeName;
    this.isAdmin = isAdmin;
  }

  public Mitspieler() {
    // Do not remove!
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public void setAdmin(boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setAnzeigeName(String anzeigeName) {
    this.anzeigeName = anzeigeName;
  }

  public String getAnzeigeName() {
    return anzeigeName;
  }
}
