package edu.hm.cs.swt2ss18.wmtipp.service.tipps;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

/**
 * Test für TippService, überprüft die Methode legeTippAb().
 * 
 * @author katz.bastian
 */
@RunWith(MockitoJUnitRunner.class)
public class TippServiceTest {

  // Testdaten bzw Testtipps
  private static final Mitspieler ma = new Mitspieler("ma", "password", "ma", false);
  private static final Spiel spield = new Spiel("heimGermany", "GastBrazilen");
  private static final Tipp tipp = new Tipp();

  @Mock
  MitspielerService service;

  @Mock
  SpielService spielService;

  @Mock
  TippRepository tippRepository;

  // Dem Testobjekt werden Mocks anstelle von echten Komponenten injiziert
  @InjectMocks
  TippService tippService = new TippService();

  @Test
  public void normalTipp()
      throws NegativTippException, InvalidTippExeception, SpielNichtTippbarException {
    // gegeben, gebraucht weil sonst kein spiel und durch abfrage von isTippbar Nullpointer
    Mockito.when(spielService.findeSpiel(spield.getId())).thenReturn(spield);

    tippService.legeTippAb(ma.getLogin(), spield.getId(), 7, 1);

    Mockito.verify(tippRepository).save(tippService.findeTipp(ma.getLogin(), spield.getId()));
  }

  @Test
  public void overwriteTipp()
      throws NegativTippException, InvalidTippExeception, SpielNichtTippbarException {
    // gegeben, gebraucht weil sonst kein spiel und durch abfrage von isTippbar Nullpointer
    Mockito.when(spielService.findeSpiel(spield.getId())).thenReturn(spield);
    // gegeben
    Mockito.when(tippService.findeTipp(ma.getLogin(), spield.getId())).thenReturn(tipp);
    spield.setTippbar(true);
    // aufruf
    tippService.legeTippAb(ma.getLogin(), spield.getId(), 9, 9);
    tippService.legeTippAb(ma.getLogin(), spield.getId(), 7, 1);

    // test
    Assert.assertEquals(tipp.getToreHeimMannschaft(), 7);
    Assert.assertEquals(tipp.getToreGastMannschaft(), 1);
  }

  @Test(expected = NegativTippException.class)
  public void legeTippAbOneNegativExceptionTest()
      throws NegativTippException, InvalidTippExeception, SpielNichtTippbarException {
    // gegeben, gebraucht weil sonst kein spiel und durch abfrage von isTippbar Nullpointer
    Mockito.when(spielService.findeSpiel(spield.getId())).thenReturn(spield);
    tippService.legeTippAb(ma.getLogin(), spield.getId(), -1, 0);
  }

  @Test(expected = NegativTippException.class)
  public void legeTippAbbothNegativExceptionTest()
      throws NegativTippException, InvalidTippExeception, SpielNichtTippbarException {
    // gegeben, gebraucht weil sonst kein spiel und durch abfrage von isTippbar Nullpointer
    Mockito.when(spielService.findeSpiel(spield.getId())).thenReturn(spield);
    tippService.legeTippAb(ma.getLogin(), spield.getId(), -5, -5);
  }

  @Test(expected = NegativTippException.class)
  public void legeTippAbhugeNegativExceptionTest()
      throws NegativTippException, InvalidTippExeception, SpielNichtTippbarException {
    // gegeben, gebraucht weil sonst kein spiel und durch abfrage von isTippbar Nullpointer
    Mockito.when(spielService.findeSpiel(spield.getId())).thenReturn(spield);
    tippService.legeTippAb(ma.getLogin(), spield.getId(), 1, -1000);
  }

  @Test(expected = InvalidTippExeception.class)
  public void legeTippabOneInvalidTippExceptionTest()
      throws NegativTippException, InvalidTippExeception, SpielNichtTippbarException {
    // gegeben, gebraucht weil sonst kein spiel und durch abfrage von isTippbar Nullpointer
    Mockito.when(spielService.findeSpiel(spield.getId())).thenReturn(spield);
    spield.setTippbar(true);
    tippService.legeTippAb(ma.getLogin(), spield.getId(), 1, 1000);
  }

  @Test(expected = InvalidTippExeception.class)
  public void legeTippabbothInvalidTippExceptionTest()
      throws NegativTippException, InvalidTippExeception, SpielNichtTippbarException {
    // gegeben, gebraucht weil sonst kein spiel und durch abfrage von isTippbar Nullpointer
    Mockito.when(spielService.findeSpiel(spield.getId())).thenReturn(spield);
    tippService.legeTippAb(ma.getLogin(), spield.getId(), 1000, 1000);
  }

  @Test(expected = SpielNichtTippbarException.class)
  public void legeTippabNichtTippbarExceptionTest()
      throws NegativTippException, InvalidTippExeception, SpielNichtTippbarException {
    // gegeben, gebraucht weil sonst kein spiel und durch abfrage von isTippbar Nullpointer
    Mockito.when(spielService.findeSpiel(spield.getId())).thenReturn(spield);
    spield.setTippbar(false);
    tippService.legeTippAb(ma.getLogin(), spield.getId(), 1, 1);
  }
}
