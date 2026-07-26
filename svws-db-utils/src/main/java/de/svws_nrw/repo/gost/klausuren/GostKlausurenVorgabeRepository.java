package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenVorgaben;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die GOSt-Klausurvorgaben.
 */
public interface GostKlausurenVorgabeRepository extends Repository<DTOGostKlausurenVorgaben> {

	/**
	 * Ermittelt alle Klausurvorgaben eines Abiturjahrgangs.
	 *
	 * @param abiturjahr das Abiturjahr
	 *
	 * @return die Liste der Klausurvorgaben
	 */
	List<DTOGostKlausurenVorgaben> getListByAbiturjahr(int abiturjahr);

	/**
	 * Ermittelt alle Klausurvorgaben eines Abiturjahrgangs für die angegebenen Halbjahre.
	 *
	 * @param abiturjahr das Abiturjahr
	 * @param halbjahre die GOSt-Halbjahre
	 *
	 * @return die Liste der Klausurvorgaben
	 */
	List<DTOGostKlausurenVorgaben> getListByAbiturjahrAndHalbjahre(int abiturjahr, Collection<GostHalbjahr> halbjahre);


	/**
	 * Gibt eine Liste mit den Klausurvorgaben der übergebenen Abiturjahrgänge zurück.
	 *
	 * @param abiturjahrgaenge   die Abiturjahrgänge
	 *
	 * @return die Liste
	 */
	List<DTOGostKlausurenVorgaben> getListByAbiturjahrgaenge(Collection<Integer> abiturjahrgaenge);


	/**
	 * Gibt eine Map mit den Klausurvorgaben der übergebenen Abiturjahrgänge zugeordnet zu den Abiturjahrgängen zurück.
	 *
	 * @param abiturjahrgaenge   die Abiturjahrgänge
	 *
	 * @return die Map
	 */
	Map<Integer, List<DTOGostKlausurenVorgaben>> getMapByAbiturjahrgaenge(Collection<Integer> abiturjahrgaenge);

}
