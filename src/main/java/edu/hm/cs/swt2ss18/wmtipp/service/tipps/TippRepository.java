package edu.hm.cs.swt2ss18.wmtipp.service.tipps;

import java.util.List;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TippRepository extends JpaRepository<Tipp, Long> {

  Tipp findOneBySpielAndMitspieler(Spiel match, Mitspieler user);

  List<Tipp> getTippsByMitspieler(Mitspieler user);

}
