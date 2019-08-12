package edu.hm.cs.swt2ss18.wmtipp.mvc.ranking;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

/**
 * Test für den RanglistenController, überprüft die Sortierung.
 * 
 * @author katz.bastian
 */
@RunWith(MockitoJUnitRunner.class)
public class RanglistenTest {

  // Testdaten zur Verwendung in den Tests
  private static final Mitspieler ma = new Mitspieler("ma", "password", "ma", false);
  private static final Mitspieler mb = new Mitspieler("mb", "password", "mb", false);

  // Mocks sind Test-Stümpfe, deren Verhalten man in den Tests definieren
  // kann, d.h. hier werden nicht wirklich Objekte von den entsprechenden
  // Typen abgelegt, sondern programmierbare Dummys.
  @Mock
  MitspielerService service;

  @Mock
  TippService tippService;

  // Dem Testobjekt werden Mocks anstelle von echten Komponenten injiziert
  @InjectMocks
  RanglistenController ranglistenController = new RanglistenController();

  @Test
  public void sortedRangliste() {

    // Mit "when" / "thenReturn" wird partielles Verhalten für Stümpfe
    // festgelegt
    Mockito.when(service.findeNormaleMitspieler()).thenReturn(Arrays.asList(ma, mb));
    Mockito.when(tippService.berechneGesamtPunkte(ma.getLogin())).thenReturn(12);
    Mockito.when(tippService.berechneGesamtPunkte(mb.getLogin())).thenReturn(24);

    // Hier wird das Modul aufgerufen
    List<RanglistenEintrag> rangliste = ranglistenController.erstelleRangliste();

    // Hier werden die Ergebnisse getestet
    Assert.assertEquals(mb.getLogin(), rangliste.get(0).getAnzeigeName());
    Assert.assertEquals(ma.getLogin(), rangliste.get(1).getAnzeigeName());
  }

  @Test
  public void singleUserRangliste() {
    // gegeben
    Mockito.when(service.findeNormaleMitspieler()).thenReturn(Arrays.asList(ma));

    // aufruf
    List<RanglistenEintrag> rangliste = ranglistenController.erstelleRangliste();

    // test
    Assert.assertEquals(ma.getLogin(), rangliste.get(0).getAnzeigeName());
  }

  @Test // wenn gleiche punkte, dann wird sotiert, welcher account zu erst vorhanden war
  public void samePointsRangliste() {
    // gegeben
    Mockito.when(service.findeNormaleMitspieler()).thenReturn(Arrays.asList(ma, mb));
    // aufruf
    List<RanglistenEintrag> rangliste = ranglistenController.erstelleRangliste();

    // test
    Assert.assertEquals(ma.getLogin(), rangliste.get(0).getAnzeigeName());
    Assert.assertEquals(mb.getLogin(), rangliste.get(1).getAnzeigeName());
  }

  @Test(expected = NullPointerException.class)
  public void emptyRangliste() {
    // gegeben
    Mockito.when(service.findeNormaleMitspieler()).thenReturn(null);
    // aufruf, hier wurf der nullpointerexception
    List<RanglistenEintrag> rangliste = ranglistenController.erstelleRangliste();

    // test, nur zur uebersichtlichkeit
    Assert.assertEquals(null, rangliste);
  }

}