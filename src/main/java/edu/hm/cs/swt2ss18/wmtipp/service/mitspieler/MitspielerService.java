package edu.hm.cs.swt2ss18.wmtipp.service.mitspieler;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring-Service-Klasse fuer die Mitspieler-Klasse.
 * 
 * @author 
 *
 */
@Service
@Transactional
public class MitspielerService implements UserDetailsService {

  private static final Collection<GrantedAuthority> ADMIN_ROLES = AuthorityUtils
      .createAuthorityList("ROLE_ADMIN");
  private static final Collection<GrantedAuthority> USER_ROLES = AuthorityUtils
      .createAuthorityList("ROLE_USER");
  private static final Logger LOG = LoggerFactory.getLogger(MitspielerService.class);
  @Value("${wmtipp.admin.login}")
  String adminLogin;

  @Value("${wmtipp.admin.password}")
  String adminPassword;

  @Autowired
  private MitspielerRepository mitspielerRepository;

  /**
   * Bootstrapping: Überprüft, dass der in der Konfiguration angegebene Administrator-Account
   * existiert und legt ihn ggf. an.
   */
  @PostConstruct
  public void checkAdminAccount() {
    if (!mitspielerRepository.findById(adminLogin).isPresent()) {
      // Passwörter müssen Hashverfahren benennen. Wir hashen nicht (noop)
      mitspielerRepository
          .save(new Mitspieler(adminLogin, "{noop}" + adminPassword, "adminAnzeige", true));
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    Optional<Mitspieler> findeMitspieler = mitspielerRepository.findById(username);
    if (findeMitspieler.isPresent()) {
      Mitspieler user = findeMitspieler.get();
      return new org.springframework.security.core.userdetails.User(user.getLogin(),
          user.getPassword(), user.isAdmin() ? ADMIN_ROLES : USER_ROLES);

    } else {
      throw new UsernameNotFoundException("");
    }
  }

  public List<Mitspieler> findeNormaleMitspieler() {
    return mitspielerRepository.findByIsAdmin(false);
  }

  /**
   * Legt einen neuen Mitspieler an.
   * 
   * @param createUser
   *          neuer Mitspieler
   * @throws UserNotAcceptedException
   *           , wenn der User nicht akzeptiert wird, weil z.B der Name admin verwendet wird.
   * @throws LoginAlreadyUsedException
   *           , wenn der neu erstellte User bereits verhanden war.
   */
  public void legeAn(Mitspieler createUser)
      throws UserNotAcceptedException, LoginAlreadyUsedException {
    if (createUser.getLogin().equals(adminLogin)) {
      LOG.error("Mitspieler:  {}   ({}) kann nicht angelegt werden, da der Name reserviert ist",
          createUser.getLogin(), createUser.getAnzeigeName());
      throw new UserNotAcceptedException("Nutzer kann nicht angelegt werden.");
    }
    if (isMitspielerBereitsVorhanden(createUser)) {
      LOG.error("Mitspieler: {} ({}) wurde überschrieben, weil er bereits vorhanden war.",
          createUser.getLogin(), createUser.getAnzeigeName());
      mitspielerRepository.save(createUser);
      throw new LoginAlreadyUsedException("Nutzer ist bereits vorhanden");
    }
    mitspielerRepository.save(createUser);
    LOG.info("Mitspieler: {} ({}) wurde angelegt.", createUser.getLogin(),
        createUser.getAnzeigeName());

  }

  @PreAuthorize("#name == authentication.name or hasRole('ROLE_ADMIN')")
  public Mitspieler findeMitspieler(String name) {
    return mitspielerRepository.getOne(name);
  }

  /**
   * Überpüft, ob ein neu erstellter Mitspieler bereits vorhanden war.
   * 
   * @param neuerUser
   *          neu erstellter Mitspieler
   * @return boolean, wenn bereits vorhanden gewesen true, sonst false.
   */
  private boolean isMitspielerBereitsVorhanden(Mitspieler neuerUser) {
    List<Mitspieler> vorhandeneMitspieler = findeNormaleMitspieler();
    for (Mitspieler m : vorhandeneMitspieler) {
      if (m.getLogin() != null && m.getLogin().equals(neuerUser.getLogin())) {
        LOG.trace("mitspieler vorhanden: {} ; neuer user: {}", m.getLogin(), neuerUser.getLogin());
        return true;
      }
    }
    return false;

  }
}
