import { JavaObject } from '../../../java/lang/JavaObject';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { Sprachbelegung } from '../../../core/data/schueler/Sprachbelegung';
import { ArrayList } from '../../../java/util/ArrayList';
import { Sprachpruefung } from '../../../core/data/schueler/Sprachpruefung';
import { Sprachpruefungniveau } from '../../../core/types/fach/Sprachpruefungniveau';
import type { List } from '../../../java/util/List';
import { Sprachendaten } from '../../../core/data/schueler/Sprachendaten';
import type { Comparator } from '../../../java/util/Comparator';

export class SprachendatenUtils extends JavaObject {


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	/**
	 * Prüft, ob eine unterrichtliche Belegung der übergebenen Sprache existiert.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 * @param sprache   das einstellige Kürzel der Sprache
	 *
	 * @return true, falls eine Belegung existiert und ansonsten false
	 */
	public static hatSprachbelegung(sprachendaten : Sprachendaten | null, sprache : string | null) : boolean {
		if (sprachendaten === null || sprachendaten.belegungen === null || sprache === null || JavaObject.equalsTranspiler("", (sprache))) {
			return false;
		}
		return SprachendatenUtils.getSprachbelegung(sprachendaten, sprache) !== null;
	}

	/**
	 * Prüft, ob eine unterrichtliche Belegung der übergebenen Sprache in der Sekundarstufe I existiert.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 * @param sprache   das einstellige Kürzel der Sprache
	 *
	 * @return true, falls eine Belegung existiert und ansonsten false
	 */
	public static hatSprachbelegungInSekI(sprachendaten : Sprachendaten | null, sprache : string | null) : boolean {
		if (sprachendaten === null || sprachendaten.belegungen === null || sprache === null || JavaObject.equalsTranspiler("", (sprache))) {
			return false;
		}
		const belegung : Sprachbelegung | null = SprachendatenUtils.getSprachbelegung(sprachendaten, sprache);
		if (belegung !== null && SprachendatenUtils.getJahrgangNumerisch(belegung.belegungVonJahrgang) > 0) {
			return SprachendatenUtils.getJahrgangNumerisch(belegung.belegungVonJahrgang) <= 10;
		}
		return false;
	}

	/**
	 * Prüft, ob eine unterrichtliche Belegung der übergebenen Sprache in der Sekundarstufe I mit mind. 2 Jahren existiert.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 * @param sprache das einstellige Kürzel der Sprache
	 *
	 * @return true, falls eine Belegung existiert und ansonsten false
	 */
	public static hatSprachbelegungMitMin2JahrenDauerInSekI(sprachendaten : Sprachendaten | null, sprache : string | null) : boolean {
		return SprachendatenUtils.hatSprachbelegungMitMinNJahrenInSekI(sprachendaten, sprache, 2);
	}

	/**
	 * Prüft, ob eine unterrichtliche Belegung der übergebenen Sprache in der Sekundarstufe I mit mind. 4 Jahren existiert.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 * @param sprache das einstellige Kürzel der Sprache
	 *
	 * @return true, falls eine Belegung existiert und ansonsten false
	 */
	public static hatSprachbelegungMitMin4JahrenDauerInSekI(sprachendaten : Sprachendaten | null, sprache : string | null) : boolean {
		return SprachendatenUtils.hatSprachbelegungMitMinNJahrenInSekI(sprachendaten, sprache, 4);
	}

	/**
	 * Gibt die zu der übergebenen Sprache gehörende Sprachbelegung zurück.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 * @param sprache   das einstellige Kürzel der Sprache
	 *
	 * @return die Sprachbelegung oder null, falls keine existiert
	 */
	public static getSprachbelegung(sprachendaten : Sprachendaten | null, sprache : string | null) : Sprachbelegung | null {
		if (sprachendaten === null || sprachendaten.belegungen === null || sprache === null || JavaObject.equalsTranspiler("", (sprache))) {
			return null;
		}
		const belegungen : List<Sprachbelegung> = sprachendaten.belegungen;
		for (const belegung of belegungen) {
			if (JavaObject.equalsTranspiler(sprache, (belegung.sprache))) {
				return belegung;
			}
		}
		return null;
	}

	/**
	 * Prüft, ob die übergebene Sprache als eine fortgeführte Fremdsprache in der gymnasialen Oberstufe
	 * gemäß APO-GOSt ab EF belegt werden kann. Dazu zählen alle belegten Sprachen mit mind. 2 Jahren Belegung in Sek-I
	 * sowie Sprachen aus bestimmten Sprachprüfungen.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 * @param sprache das einstellige Kürzel der Sprache
	 *
	 * @return true, falls die Sprache als fortgeführte Fremdsprache ab EF belegt werden kann, andernfalls false
	 */
	public static istFortfuehrbareSpracheInGOSt(sprachendaten : Sprachendaten | null, sprache : string | null) : boolean {
		if (sprachendaten === null || sprache === null || JavaObject.equalsTranspiler("", (sprache))) {
			return false;
		}
		if (SprachendatenUtils.hatSprachbelegungMitMin2JahrenDauerInSekI(sprachendaten, sprache)) {
			return true;
		}
		const pruefungen : List<Sprachpruefung> = sprachendaten.pruefungen;
		if (pruefungen !== null) {
			for (const pruefung of pruefungen) {
				if (!JavaObject.equalsTranspiler(sprache, (pruefung.sprache)) && !JavaObject.equalsTranspiler(sprache, (pruefung.ersetzteSprache))) {
					continue;
				}
				if (pruefung.istHSUPruefung && (pruefung.note !== null) && (pruefung.note <= 4) && (pruefung.anspruchsniveauId === Sprachpruefungniveau.EESA.daten.id || pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id)) {
					return true;
				}
				if (SprachendatenUtils.istFeststellungspruefungEESAMSABestanden(pruefung))
					return true;
			}
		}
		return false;
	}

	/**
	 * Prüft, ob die übergebene Sprache als eine neueinsetzende Fremdsprache in der gymnasialen Oberstufe
	 * gemäß APO-GOSt ab EF belegt werden kann. Dazu zählen alle bisher nicht belegten Sprachen oder Sprachen,
	 * die nur im Rahmen des HSU belegt oder geprüft wurden.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 * @param sprache das einstellige Kürzel der Sprache
	 *
	 * @return true, falls die Sprache als neu einsetzende Fremdsprache ab EF belegt werden kann, andernfalls false
	 */
	public static istNeueinsetzbareSpracheInGOSt(sprachendaten : Sprachendaten | null, sprache : string | null) : boolean {
		if (sprachendaten === null || sprache === null || JavaObject.equalsTranspiler("", (sprache))) {
			return false;
		}
		if (SprachendatenUtils.hatSprachbelegungMitMin2JahrenDauerInSekI(sprachendaten, sprache)) {
			return false;
		}
		const pruefungen : List<Sprachpruefung> = sprachendaten.pruefungen;
		if (pruefungen !== null) {
			for (const pruefung of pruefungen) {
				if (!JavaObject.equalsTranspiler(sprache, (pruefung.sprache)) && !JavaObject.equalsTranspiler(sprache, (pruefung.ersetzteSprache))) {
					continue;
				}
				if (SprachendatenUtils.istFeststellungspruefungEESAMSABestanden(pruefung))
					return false;
			}
		}
		return true;
	}

	/**
	 * Sammelt alle Sprachen, die in der GOSt als fortgeführte Sprachen belegt werden können, sei
	 * es aufgrund einer Belegung von mindestens zwei Jahren oder aufgrund einer Sprachprüfung.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 *
	 * @return Liste alle Sprachen, die in der GOSt fortgeführt werden können.
	 */
	public static getFortfuehrbareSprachenInGOSt(sprachendaten : Sprachendaten | null) : List<string> {
		const sprachen : List<string> = new ArrayList();
		if (sprachendaten !== null) {
			const belegungen : List<Sprachbelegung> = sprachendaten.belegungen;
			if (belegungen !== null && !belegungen.isEmpty()) {
				for (const belegung of belegungen) {
					if (SprachendatenUtils.istFortfuehrbareSpracheInGOSt(sprachendaten, belegung.sprache))
						sprachen.add(belegung.sprache);
				}
			}
			const pruefungen : List<Sprachpruefung> = sprachendaten.pruefungen;
			if (pruefungen !== null && !pruefungen.isEmpty()) {
				for (const pruefung of pruefungen) {
					if (SprachendatenUtils.istFortfuehrbareSpracheInGOSt(sprachendaten, pruefung.sprache))
						sprachen.add(pruefung.sprache);
				}
			}
		}
		return sprachen;
	}

	/**
	 * Prüft für den Zeitpunkt Ende Sek-I, ob eine Fremdsprache im Umfang von mindestens 4 Jahren belegt wurde.
	 * Dabei wird davon ausgegangen, dass Sprachen ohne Ende der Belegung am Ende der Sekundarstufe I belegt wurden.
	 * Ist dies in der Sprachenfolge nicht der Fall, werden zusätzlich evtl. Sprachprüfungen herangezogen.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 *
	 * @return true, falls der Nachweis gemäß der aktuellen Sprachdaten erfüllt ist, andernfalls false.
	 */
	public static hatEineSpracheAb5bis7MitMin4JahrenDauerEndeSekI(sprachendaten : Sprachendaten | null) : boolean {
		if (sprachendaten === null)
			return false;
		const anzahlSprachen : number = SprachendatenUtils.getSprachlegungenNachBeginnUndDauerEndeSekI(sprachendaten, "05", "07", 4).size();
		if (anzahlSprachen >= 1)
			return true;
		const pruefungen : List<Sprachpruefung> = sprachendaten.pruefungen;
		if (pruefungen !== null) {
			for (const pruefung of pruefungen) {
				if (SprachendatenUtils.kannFeststellungspruefungErsteSpracheErsetzen(pruefung) || SprachendatenUtils.kannFeststellungspruefungZweiteSpracheErsetzen(pruefung))
					return true;
			}
		}
		return false;
	}

	/**
	 * Prüft für den Zeitpunkt Ende Sek-I, ob eine zweite Fremdsprache im Umfang von mindestens 4 Jahren belegt wurde.
	 * Dabei wird davon ausgegangen, dass Sprachen ohne Ende der Belegung am Ende der Sekundarstufe I belegt wurden.
	 * Ist dies in der Sprachenfolge nicht der Fall, werden zusätzlich evtl. Sprachprüfungen herangezogen.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 *
	 * @return true, falls der Nachweis gemäß der aktuellen Sprachdaten erfüllt ist, andernfalls false.
	 */
	public static hatZweiSprachenAb5Bis7MitMin4JahrenDauerEndeSekI(sprachendaten : Sprachendaten | null) : boolean {
		if (sprachendaten === null)
			return false;
		const belegungen : List<Sprachbelegung> = SprachendatenUtils.getSprachlegungenNachBeginnUndDauerEndeSekI(sprachendaten, "05", "07", 4);
		const anzahlSprachen : number = belegungen.size();
		if (anzahlSprachen >= 2)
			return true;
		if (anzahlSprachen === 1) {
			const pruefungen : List<Sprachpruefung> = sprachendaten.pruefungen;
			if (pruefungen !== null) {
				for (const pruefung of pruefungen) {
					if ((SprachendatenUtils.kannFeststellungspruefungErsteSpracheErsetzen(pruefung) || SprachendatenUtils.kannFeststellungspruefungZweiteSpracheErsetzen(pruefung)) && (!JavaObject.equalsTranspiler(belegungen.get(0).sprache, (pruefung.sprache))))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * Prüft für den Zeitpunkt Ende Sek-I, ob eine Fremdsprache ab Kasse 8/9 im Umfang von mindestens 2 Jahren belegt wurde.
	 * Dabei wird davon ausgegangen, dass Sprachen ohne Ende der Belegung am Ende der Sekundarstufe I belegt wurden.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 *
	 * @return true, falls der Nachweis gemäß der aktuellen Sprachdaten erfüllt ist, andernfalls false.
	 */
	public static hatEineSpracheAb8MitMin2JahrenDauerEndeSekI(sprachendaten : Sprachendaten | null) : boolean {
		if (sprachendaten === null)
			return false;
		const anzahlSprachen : number = SprachendatenUtils.getSprachlegungenNachBeginnUndDauerEndeSekI(sprachendaten, "08", "10", 2).size();
		return (anzahlSprachen >= 1);
	}

	/**
	 * Ermittelt, ob eine Fremdsprache ab Kasse 8/9 im Umfang von mindestens 2 Jahren belegt wurde und gibt sie zurück
	 * Dabei wird davon ausgegangen, dass Sprachen ohne Ende der Belegung am Ende der Sekundarstufe I belegt wurden.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 *
	 * @return Sprache, falls eine Belegung vorhanden ist, sonst null
	 */
	public static getEineSpracheAb8MitMin2JahrenDauerEndeSekI(sprachendaten : Sprachendaten | null) : string | null {
		if (sprachendaten === null)
			return null;
		const belegungen : List<Sprachbelegung> = sprachendaten.belegungen;
		if (belegungen !== null) {
			const sprachbelegungen : List<Sprachbelegung> = SprachendatenUtils.getSprachlegungenNachBeginnUndDauerEndeSekI(sprachendaten, "08", "10", 2);
			if (!sprachbelegungen.isEmpty())
				return sprachbelegungen.get(0).sprache;
		}
		return null;
	}

	/**
	 * Prüft, ob eine Sprachfeststellungsprüfung auf dem Niveau der Einführungsphase (EF) der GOSt vorliegt.
	 * Nach §11 (2) APO-GOSt setzt das eine Prüfung in der gleichen Sprache am Ende der Sek-I voraus
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 *
	 * @return true, falls entsprechende Sprachprüfungen vorhanden sind, andernfalls false.
	 */
	public static hatSprachfeststellungspruefungAufEFNiveau(sprachendaten : Sprachendaten | null) : boolean {
		if (sprachendaten === null)
			return false;
		const pruefungen : List<Sprachpruefung> = sprachendaten.pruefungen;
		if (pruefungen !== null) {
			for (const pruefungS1 of pruefungen) {
				if (SprachendatenUtils.kannFeststellungspruefungErsteSpracheErsetzen(pruefungS1) || SprachendatenUtils.kannFeststellungspruefungZweiteSpracheErsetzen(pruefungS1)) {
					for (const pruefungEF of pruefungen) {
						if (pruefungEF.istFeststellungspruefung && JavaObject.equalsTranspiler(pruefungEF.sprache, (pruefungS1.sprache)) && (pruefungEF.kannErstePflichtfremdspracheErsetzen || pruefungEF.kannZweitePflichtfremdspracheErsetzen || pruefungEF.kannWahlpflichtfremdspracheErsetzen) && pruefungEF.anspruchsniveauId === Sprachpruefungniveau.EF.daten.id && (pruefungEF.note !== null) && (pruefungEF.note <= 4))
							return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Gibt die Fremdsprache zurück, die als erste Fremdsprache der Sekundarstufe I gewertet werden kann.
	 * Im Falle einer Sprachprüfung als erste Pflichtfremdsprache wird diese zurückgegeben, da der Prüfungseintrag diese als erste Sprache explizit festlegt.
	 * Ist keine Sprachprüfung als erste Pflichtfremdsprache vorhanden, so wird die als erste Sprache in der Sekundarstufe I belegt
	 * Sprache zurückgegeben, unabhängig von deren Belegdauer.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 *
	 * @return Die erste belegte Sprache (gemäß Belegung oder Prüfung) oder null, falls keine existiert
	 */
	public static getErsteSpracheInSekI(sprachendaten : Sprachendaten | null) : string | null {
		if (sprachendaten === null)
			return null;
		const pruefungen : List<Sprachpruefung> = sprachendaten.pruefungen;
		if (pruefungen !== null) {
			for (const pruefung of pruefungen) {
				if (SprachendatenUtils.kannFeststellungspruefungErsteSpracheErsetzen(pruefung))
					return pruefung.sprache;
			}
		}
		const belegungen : List<Sprachbelegung> = sprachendaten.belegungen;
		if (belegungen !== null) {
			const sprachbelegungen : List<Sprachbelegung> = SprachendatenUtils.getSprachlegungenNachBeginnUndDauerEndeSekI(sprachendaten, "05", "10", 0);
			if (!sprachbelegungen.isEmpty())
				return sprachbelegungen.get(0).sprache;
		}
		return null;
	}

	/**
	 * Gibt die Fremdsprache zurück, die als zweite Fremdsprache der Sekundarstufe I gewertet werden kann.
	 * Im Falle einer Sprachprüfung als zweite Pflichtfremdsprache bzw. WP-Sprache wird diese zurückgegeben, da der Prüfungseintrag diese als zweite Sprache explizit festlegt.
	 * Ist keine Sprachprüfung als zweite Pflichtfremdsprache bzw. WP-Sprache vorhanden, so wird die als zweite Sprache in der Sekundarstufe I belegt
	 * Sprache zurückgegeben, unabhängig von deren Belegdauer.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 *
	 * @return Die zweite belegte Sprache (gemäß Belegung oder Prüfung) oder null, falls keine existiert
	 */
	public static getZweiteSpracheInSekI(sprachendaten : Sprachendaten | null) : string | null {
		if (sprachendaten === null)
			return null;
		let pruefungErsteSprache : string | null = "";
		let pruefungZweiteSprache : string | null = "";
		const pruefungen : List<Sprachpruefung> = sprachendaten.pruefungen;
		if (pruefungen !== null) {
			for (const pruefung of pruefungen) {
				if (SprachendatenUtils.kannFeststellungspruefungErsteSpracheErsetzen(pruefung)) {
					pruefungErsteSprache = pruefung.sprache;
				}
				if (SprachendatenUtils.kannFeststellungspruefungZweiteSpracheErsetzen(pruefung)) {
					pruefungZweiteSprache = pruefung.sprache;
				}
			}
		}
		if (!(JavaObject.equalsTranspiler("", (pruefungZweiteSprache))))
			return pruefungZweiteSprache;
		const belegungen : List<Sprachbelegung> = sprachendaten.belegungen;
		if (belegungen !== null) {
			const sprachbelegungen : List<Sprachbelegung> = SprachendatenUtils.getSprachlegungenNachBeginnUndDauerEndeSekI(sprachendaten, "05", "10", 0);
			if (!(JavaObject.equalsTranspiler("", (pruefungErsteSprache)))) {
				for (const sprachbelegung of sprachbelegungen) {
					if (!JavaObject.equalsTranspiler(sprachbelegung.sprache, (pruefungErsteSprache)))
						return sprachbelegung.sprache;
				}
			} else {
				if (sprachbelegungen.size() > 1)
					return sprachbelegungen.get(1).sprache;
			}
		}
		return null;
	}

	/**
	 * Hilfsfunktion, die prüft, ob eine unterrichtliche Belegung der übergebenen Sprache in der Sekundarstufe I mit mind. 2 Jahren existiert.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 * @param sprache das einstellige Kürzel der Sprache
	 * @param n Anzahl der Sprachbelegung, die mindestens erreicht worden sein müssen.
	 *
	 * @return true, falls eine Belegung existiert und ansonsten false
	 */
	private static hatSprachbelegungMitMinNJahrenInSekI(sprachendaten : Sprachendaten | null, sprache : string | null, n : number) : boolean {
		if (sprachendaten === null || sprachendaten.belegungen === null || sprache === null || JavaObject.equalsTranspiler("", (sprache))) {
			return false;
		}
		const belegung : Sprachbelegung | null = SprachendatenUtils.getSprachbelegung(sprachendaten, sprache);
		if (belegung === null) {
			return false;
		}
		let belegtVonJahrgangNumerisch : number;
		let belegtBisJahrgangNumerisch : number;
		let letzterJahrgangSekI : number;
		if (belegung.belegungVonJahrgang !== null) {
			belegtVonJahrgangNumerisch = SprachendatenUtils.getJahrgangNumerisch(belegung.belegungVonJahrgang);
			belegtBisJahrgangNumerisch = SprachendatenUtils.getJahrgangNumerisch(belegung.belegungBisJahrgang);
			letzterJahrgangSekI = 10;
			if (0 < belegtVonJahrgangNumerisch && belegtVonJahrgangNumerisch <= 10) {
				if (belegtBisJahrgangNumerisch === 0 || belegtBisJahrgangNumerisch > letzterJahrgangSekI) {
					belegtBisJahrgangNumerisch = letzterJahrgangSekI;
				}
				return ((belegtBisJahrgangNumerisch - belegtVonJahrgangNumerisch + 1) >= n);
			}
		}
		return false;
	}

	/**
	 * Hilfsfunktion, die Sprachen aus der Sprachenfolge zurückliefert, deren Beginn im angegebenen Zeitraum liegt und die angegebene Dauer besitzt.
	 * Dabei wird davon ausgegangen, dass Sprachen ohne Ende der Belegung am Ende der Sekundarstufe I belegt wurden.
	 * Bei einem Schüler der Sek-II wird auch nur die Dauer der Belegung in der Sek-I betrachtet.
	 * Sprachprüfungen werden nicht berücksichtigt.
	 * Anmerkung: Das Ende Sekundarstufe I wird stets in der Stufe 10 angesetzt. Dadurch ergibt sich (nur) an einem G8-Gymnasium eine Abweichung der
	 * Belegungsdauer von einem Jahr zugunsten des Schülers. Da in der APO-GOSt aber nur Sprachen mit mindestens vier Jahren mit Beginn bis
	 * einschließlich Klasse 7 und Sprachen ab Klasse 8 unterschieden werden, ergeben sich bei der späteren Anwendung dieser Hilfsfunktion keine
	 * Auswirkungen bei der Zuordnung zu einer dieser Gruppen (es ergeben sich max. 3 Jahre bei Beginn in Klasse 8 und ein Sprachbeginn in Klasse 9
	 * am Gymnasium G8 gibt es nicht).
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 * @param belegungsbeginnStart Es werden nur Sprachen berücksichtigt, deren Belegungsbeginn größer oder gleich dem angegebenen ASDJahrgang ist.
	 * @param belegungsbeginnEnde Es werden nur Sprachen berücksichtigt, deren Belegungsbeginn kleiner oder gleich dem angegebenen ASDJahrgang ist.
	 * @param mindestBelegdauer Zulässig sind Werte 1 bis 5 für die minimale Dauer der Sprachbelegung, damit die Sprache berücksichtigt wird.
	 *
	 * @return List mit Sprachbelegungen, die die Kriterien erfüllen. Die Liste ist nach Belegungsbeginn aufsteigend sortiert
	 */
	private static getSprachlegungenNachBeginnUndDauerEndeSekI(sprachendaten : Sprachendaten | null, belegungsbeginnStart : string | null, belegungsbeginnEnde : string | null, mindestBelegdauer : number | null) : List<Sprachbelegung> {
		const resultBelegungen : List<Sprachbelegung> = new ArrayList();
		if (sprachendaten === null || sprachendaten.belegungen === null || belegungsbeginnStart === null || JavaObject.equalsTranspiler("", (belegungsbeginnStart)) || belegungsbeginnEnde === null || JavaObject.equalsTranspiler("", (belegungsbeginnEnde)) || mindestBelegdauer === null || mindestBelegdauer < 0)
			return resultBelegungen;
		let belegtVonJahrgangNumerisch : number;
		let belegtBisJahrgangNumerisch : number;
		let letzterJahrgangSekI : number;
		const alleBelegungen : List<Sprachbelegung> = sprachendaten.belegungen;
		for (const belegung of alleBelegungen) {
			if (belegung.sprache !== null && belegung.belegungVonJahrgang !== null) {
				belegtVonJahrgangNumerisch = SprachendatenUtils.getJahrgangNumerisch(belegung.belegungVonJahrgang);
				belegtBisJahrgangNumerisch = SprachendatenUtils.getJahrgangNumerisch(belegung.belegungBisJahrgang);
				letzterJahrgangSekI = 10;
				if (belegtBisJahrgangNumerisch === 0 || belegtBisJahrgangNumerisch > letzterJahrgangSekI) {
					belegtBisJahrgangNumerisch = letzterJahrgangSekI;
				}
				if (((belegtBisJahrgangNumerisch - belegtVonJahrgangNumerisch + 1) >= mindestBelegdauer) && (belegtVonJahrgangNumerisch > 0) && SprachendatenUtils.getJahrgangNumerisch(belegungsbeginnStart) <= belegtVonJahrgangNumerisch && belegtVonJahrgangNumerisch <= SprachendatenUtils.getJahrgangNumerisch(belegungsbeginnEnde)) {
					resultBelegungen.add(belegung);
				}
			}
		}
		if (!resultBelegungen.isEmpty()) {
			const comparator : Comparator<Sprachbelegung> | null = { compare : (a: Sprachbelegung, b: Sprachbelegung) => JavaInteger.compare(SprachendatenUtils.getJahrgangNumerisch(a.belegungVonJahrgang), SprachendatenUtils.getJahrgangNumerisch(b.belegungVonJahrgang)) };
			resultBelegungen.sort(comparator);
		}
		return resultBelegungen;
	}

	/**
	 * Hilfsfunktion, die prüft, ob die Sprache der übergebenen Feststellungsprüfung an die Stelle der ersten Pflichtfremdsprache treten kann.
	 *
	 * @param pruefung	Feststellungsprüfung, die geprüft werden soll.
	 *
	 * @return True, wenn die Sprache der Prüfung die erste Pflichtfremdsprache ersetzen kann, sonst false
	 */
	private static kannFeststellungspruefungErsteSpracheErsetzen(pruefung : Sprachpruefung | null) : boolean {
		return (pruefung !== null && pruefung.istFeststellungspruefung && (pruefung.note !== null) && (pruefung.note <= 4) && pruefung.kannErstePflichtfremdspracheErsetzen && (pruefung.anspruchsniveauId === Sprachpruefungniveau.EESA.daten.id || pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id));
	}

	/**
	 * Hilfsfunktion, die prüft, ob die Sprache der übergebenen Feststellungsprüfung an die Stelle der zweiten Pflichtfremdsprache bzw. einer Wahlpflichtsprache treten kann.
	 *
	 * @param pruefung	Feststellungsprüfung, die geprüft werden soll.
	 *
	 * @return True, wenn die Sprache der Prüfung die zweite Pflichtfremdsprache bzw. eine Wahlpflichtsprache ersetzen kann, sonst false
	 */
	private static kannFeststellungspruefungZweiteSpracheErsetzen(pruefung : Sprachpruefung | null) : boolean {
		return (pruefung !== null && pruefung.istFeststellungspruefung && (pruefung.note !== null) && (pruefung.note <= 4) && (pruefung.kannZweitePflichtfremdspracheErsetzen || pruefung.kannWahlpflichtfremdspracheErsetzen) && (pruefung.anspruchsniveauId === Sprachpruefungniveau.EESA.daten.id || pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id));
	}

	/**
	 * Hilfsfunktion, die prüft, ob die Sprache der übergebenen Feststellungsprüfung mit einer erfolgreichen Feststellungsprüfung auf EESA/MSA Niveau abgeschlossen wurde.
	 *
	 * @param pruefung	Feststellungsprüfung, die geprüft werden soll.
	 *
	 * @return True, wenn die Sprache erfolgreich auf Niveau EESA/MSA geprüft wurde, sonst false
	 */
	private static istFeststellungspruefungEESAMSABestanden(pruefung : Sprachpruefung | null) : boolean {
		return (pruefung !== null) && pruefung.istFeststellungspruefung && (pruefung.note !== null) && (pruefung.note <= 4) && ((pruefung.kannBelegungAlsFortgefuehrteSpracheErlauben && pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id) || ((pruefung.kannErstePflichtfremdspracheErsetzen || pruefung.kannZweitePflichtfremdspracheErsetzen || pruefung.kannWahlpflichtfremdspracheErsetzen) && (pruefung.anspruchsniveauId === Sprachpruefungniveau.EESA.daten.id || pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id)));
	}

	/**
	 * Hilfsfunktion, die einen ASDJahrgang nach APO-SI und APO-GOSt und in einen numerischen Wert für Vergleiche umwandelt.
	 * Dabei wird EF zu 11, Q1 zu 12 und Q2 zu 13. Die übrigen Stufen werden gemäß ihrer numerischen Stufenangaben umgewandelt.
	 *
	 * @param kuerzelJg   der in den nummerischen Wert umzuwandelnde ASDJahrgang.
	 *
	 * @return Wert des ASDJahrgangs zwischen 5 und 13, wenn dieser nicht bestimmt werden kann, wird der Wert 0 zurückgegeben.
	 */
	private static getJahrgangNumerisch(kuerzelJg : string | null) : number {
		if (kuerzelJg === null || JavaObject.equalsTranspiler("", (kuerzelJg)))
			return 0;
		switch (kuerzelJg) {
			case "EF": {
				return 11;
			}
			case "Q1": {
				return 12;
			}
			case "Q2": {
				return 13;
			}
			default: {
				try {
					return JavaInteger.parseInt(kuerzelJg);
				} catch(e) {
					return 0;
				}
			}
		}
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.schueler.SprachendatenUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_schueler_SprachendatenUtils(obj : unknown) : SprachendatenUtils {
	return obj as SprachendatenUtils;
}
