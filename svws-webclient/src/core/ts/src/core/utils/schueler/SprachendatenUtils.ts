import { JavaObject } from '../../../java/lang/JavaObject';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { Sprachbelegung } from '../../../core/data/schueler/Sprachbelegung';
import { ArrayList } from '../../../java/util/ArrayList';
import { Sprachpruefung } from '../../../core/data/schueler/Sprachpruefung';
import { Sprachpruefungniveau } from '../../../core/types/fach/Sprachpruefungniveau';
import { Sprachendaten } from '../../../core/data/schueler/Sprachendaten';
import { HashSet } from '../../../java/util/HashSet';
import { Comparator } from '../../../java/util/Comparator';

export class SprachendatenUtils extends JavaObject {


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation of " + SprachendatenUtils.class.getName()! + " not allowed")
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
	 * Prüft, ob eine unterrichtliche Belegung der übergebenen Sprache in der Sekundarstufe I mit der angegebenen Belegungsdauer existiert.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 * @param sprache das einstellige Kürzel der Sprache
	 * @param mindestBelegdauer Zulässig sind Werte 1 bis 5 für die minimale Dauer der Sprachbelegung, damit die Sprache berücksichtigt wird.
	 *
	 * @return true, falls eine Belegung existiert und ansonsten false
	 */
	public static hatSprachbelegungInSekIMitDauer(sprachendaten : Sprachendaten | null, sprache : string | null, mindestBelegdauer : number | null) : boolean {
		if (sprachendaten === null || sprachendaten.belegungen === null || sprache === null || JavaObject.equalsTranspiler("", (sprache)) || mindestBelegdauer === null || mindestBelegdauer <= 0) {
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
			if (belegtVonJahrgangNumerisch === 6 || belegtVonJahrgangNumerisch === 8) {
				letzterJahrgangSekI = 9;
			}
			if (0 < belegtVonJahrgangNumerisch && belegtVonJahrgangNumerisch <= 10) {
				if (belegtBisJahrgangNumerisch === 0 || belegtBisJahrgangNumerisch > letzterJahrgangSekI) {
					belegtBisJahrgangNumerisch = letzterJahrgangSekI;
				}
				return ((belegtBisJahrgangNumerisch - belegtVonJahrgangNumerisch + 1) >= mindestBelegdauer);
			}
		}
		return false;
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
		const belegungen : ArrayList<Sprachbelegung> = sprachendaten.belegungen;
		for (const belegung of belegungen) {
			if (JavaObject.equalsTranspiler(sprache, (belegung.sprache))) {
				return belegung;
			}
		}
		return null;
	}

	/**
	 * Liefert die Sprachen aus der Sprachenfolge zurück, deren Beginn im angegebenen Zeitraum liegt und die angegebene Dauer besitzt.
	 * Dabei wird davon ausgegangen, dass Sprachen ohne Ende der Belegung am Ende der Sekundarstufe I belegt wurden.
	 * Bei einem Schüler der Sek-II wird auch nur die Dauer der Belegung in der Sek-I betrachtet.
	 * Sprachprüfungen werden nicht berücksichtigt.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 * @param belegungbeginnStart Es werden nur Sprachen berücksichtigt, deren Belegungsbeginn größer oder gleich dem angegebenen ASDJahrgang ist.
	 * @param belegungbeginnEnde Es werden nur Sprachen berücksichtigt, deren Belegungsbeginn kleiner oder gleich dem angegebenen ASDJahrgang ist.
	 * @param mindestBelegdauer Zulässig sind Werte 1 bis 5 für die minimale Dauer der Sprachbelegung, damit die Sprache berücksichtigt wird.
	 *
	 * @return ArrayList mit Sprachbelegungen, die die Kriterien erfüllen. Die Liste ist nach Belegungsbeginn aufsteigend sortiert
	 */
	public static getSprachlegungenNachBeginnUndDauerEndeSekI(sprachendaten : Sprachendaten | null, belegungbeginnStart : string | null, belegungbeginnEnde : string | null, mindestBelegdauer : number | null) : ArrayList<Sprachbelegung> {
		const belegungen : ArrayList<Sprachbelegung> = new ArrayList();
		if (sprachendaten !== null && sprachendaten.belegungen !== null && belegungbeginnStart !== null && !JavaObject.equalsTranspiler(belegungbeginnStart, ("")) && belegungbeginnEnde !== null && !JavaObject.equalsTranspiler(belegungbeginnEnde, ("")) && mindestBelegdauer !== null && mindestBelegdauer >= 0) {
			let belegtVonJahrgangNumerisch : number;
			let belegtBisJahrgangNumerisch : number;
			let letzterJahrgangSekI : number;
			const gefundeneSprachen : HashSet<string | null> | null = new HashSet();
			const alleBelegungen : ArrayList<Sprachbelegung> = sprachendaten.belegungen;
			for (const belegung of alleBelegungen) {
				if (belegung.belegungVonJahrgang !== null) {
					belegtVonJahrgangNumerisch = SprachendatenUtils.getJahrgangNumerisch(belegung.belegungVonJahrgang);
					belegtBisJahrgangNumerisch = SprachendatenUtils.getJahrgangNumerisch(belegung.belegungBisJahrgang);
					letzterJahrgangSekI = 10;
					if (belegtVonJahrgangNumerisch === 6 || belegtVonJahrgangNumerisch === 8) {
						letzterJahrgangSekI = 9;
					}
					if (belegtVonJahrgangNumerisch > 0 && SprachendatenUtils.getJahrgangNumerisch(belegungbeginnStart) <= belegtVonJahrgangNumerisch && belegtVonJahrgangNumerisch <= SprachendatenUtils.getJahrgangNumerisch(belegungbeginnEnde)) {
						if (belegtBisJahrgangNumerisch === 0 || belegtBisJahrgangNumerisch > letzterJahrgangSekI) {
							belegtBisJahrgangNumerisch = letzterJahrgangSekI;
						}
						if ((belegtBisJahrgangNumerisch - belegtVonJahrgangNumerisch + 1) >= mindestBelegdauer) {
							if (!gefundeneSprachen.contains(belegung.sprache)) {
								belegungen.add(belegung);
							}
							gefundeneSprachen.add(belegung.sprache);
						}
					}
				}
			}
		}
		if (!belegungen.isEmpty()) {
			const comparator : Comparator<Sprachbelegung> | null = { compare : (a: Sprachbelegung, b: Sprachbelegung) => JavaInteger.compare(SprachendatenUtils.getJahrgangNumerisch(a.belegungVonJahrgang), SprachendatenUtils.getJahrgangNumerisch(b.belegungVonJahrgang)) };
			belegungen.sort(comparator);
		}
		return belegungen;
	}

	/**
	 * Prüft, ob die übergebene Sprache als eine fortgeführte Fremdsprache in der gymnasialen Oberstufe
	 * gemäß APO-GOSt ab EF belegt werden kann. Dazu zählen alle belegten Sprachen mit mind. 2 Jahren Belegung in Sek-I
	 * sowie Sprachen aus bestimmten Sprachprüfungen.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 * @param sprache das einstellige Kürzel der Sprache
	 *
	 * @return true, falls die Sprache als fortgeführte Fremdsprache als EF belegt werden kann, andernfalls false
	 */
	public static istFortfuehrbareSpracheInGOSt(sprachendaten : Sprachendaten | null, sprache : string | null) : boolean {
		if (sprachendaten === null || sprache === null || JavaObject.equalsTranspiler("", (sprache))) {
			return false;
		}
		if (SprachendatenUtils.hatSprachbelegungInSekIMitDauer(sprachendaten, sprache, 2)) {
			return true;
		}
		const pruefungen : ArrayList<Sprachpruefung> = sprachendaten.pruefungen;
		if (pruefungen !== null) {
			for (const pruefung of pruefungen) {
				if (!JavaObject.equalsTranspiler(sprache, (pruefung.sprache)) && !JavaObject.equalsTranspiler(sprache, (pruefung.ersetzteSprache))) {
					continue;
				}
				if (pruefung.istHSUPruefung && (pruefung.anspruchsniveauId === Sprachpruefungniveau.HA10.daten.id || pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id) && (pruefung.note !== null) && (pruefung.note <= 4)) {
					return true;
				}
				if (pruefung.istFeststellungspruefung && (pruefung.note !== null) && (pruefung.note <= 4) && ((pruefung.kannBelegungAlsFortgefuehrteSpracheErlauben && pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id) || ((pruefung.kannErstePflichtfremdspracheErsetzen || pruefung.kannZweitePflichtfremdspracheErsetzen || pruefung.kannWahlpflichtfremdspracheErsetzen) && (pruefung.anspruchsniveauId === Sprachpruefungniveau.HA10.daten.id || pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id)))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Sammelt alle Sprachen, die in der GOSt als fortgeführte Sprachen belegt werden können, sei
	 * es aufgrund einer Belegung von mindestens zwei Jahren oder aufgrund einer Sprachprüfung.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 *
	 * @return Liste alle Sprachen, die in der GOSt fortgeführt werden können.
	 */
	public static getFortfuehrbareSprachenInGOSt(sprachendaten : Sprachendaten | null) : ArrayList<string> {
		const sprachen : ArrayList<string> = new ArrayList();
		if (sprachendaten !== null) {
			const belegungen : ArrayList<Sprachbelegung> = SprachendatenUtils.getSprachlegungenNachBeginnUndDauerEndeSekI(sprachendaten, "05", "10", 2);
			for (const belegung of belegungen) {
				sprachen.add(belegung.sprache);
			}
			const pruefungen : ArrayList<Sprachpruefung> = sprachendaten.pruefungen;
			if (pruefungen !== null) {
				for (const pruefung of pruefungen) {
					if (pruefung.istHSUPruefung && (pruefung.anspruchsniveauId === Sprachpruefungniveau.HA10.daten.id || pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id) && (pruefung.note !== null) && (pruefung.note <= 4)) {
						if (!(pruefung.ersetzteSprache === null || JavaObject.equalsTranspiler(pruefung.ersetzteSprache, ("")))) {
							sprachen.add(pruefung.ersetzteSprache);
						} else
							if (!(pruefung.sprache === null || JavaObject.equalsTranspiler(pruefung.sprache, ("")))) {
								sprachen.add(pruefung.sprache);
							}
					}
					if (pruefung.istFeststellungspruefung && (pruefung.note !== null) && (pruefung.note <= 4) && ((pruefung.kannBelegungAlsFortgefuehrteSpracheErlauben && pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id) || ((pruefung.kannErstePflichtfremdspracheErsetzen || pruefung.kannZweitePflichtfremdspracheErsetzen || pruefung.kannWahlpflichtfremdspracheErsetzen) && (pruefung.anspruchsniveauId === Sprachpruefungniveau.HA10.daten.id || pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id)))) {
						if (!(pruefung.ersetzteSprache === null || JavaObject.equalsTranspiler(pruefung.ersetzteSprache, ("")))) {
							sprachen.add(pruefung.ersetzteSprache);
						} else
							if (!(pruefung.sprache === null || JavaObject.equalsTranspiler(pruefung.sprache, ("")))) {
								sprachen.add(pruefung.sprache);
							}
					}
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
	public static hatEineSpracheMitMin4JahrenDauerEndeSekI(sprachendaten : Sprachendaten | null) : boolean {
		if (sprachendaten === null)
			return false;
		const anzahlSprachen : number = SprachendatenUtils.getSprachlegungenNachBeginnUndDauerEndeSekI(sprachendaten, "05", "07", 4).size();
		if (anzahlSprachen >= 1)
			return true;
		const pruefungen : ArrayList<Sprachpruefung> = sprachendaten.pruefungen;
		if (pruefungen !== null) {
			for (const pruefung of pruefungen) {
				if (pruefung.istFeststellungspruefung && (pruefung.kannErstePflichtfremdspracheErsetzen || pruefung.kannZweitePflichtfremdspracheErsetzen || pruefung.kannWahlpflichtfremdspracheErsetzen) && (pruefung.anspruchsniveauId === Sprachpruefungniveau.HA10.daten.id || pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id) && (pruefung.note !== null) && (pruefung.note <= 4))
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
	public static hatZweiSprachenMitMin4JahrenDauerEndeSekI(sprachendaten : Sprachendaten | null) : boolean {
		if (sprachendaten === null)
			return false;
		const belegungen : ArrayList<Sprachbelegung> = SprachendatenUtils.getSprachlegungenNachBeginnUndDauerEndeSekI(sprachendaten, "05", "07", 4);
		const anzahlSprachen : number = belegungen.size();
		if (anzahlSprachen >= 2)
			return true;
		if (anzahlSprachen === 1) {
			const pruefungen : ArrayList<Sprachpruefung> = sprachendaten.pruefungen;
			if (pruefungen !== null) {
				for (const pruefung of pruefungen) {
					if ((pruefung.istFeststellungspruefung && (pruefung.kannErstePflichtfremdspracheErsetzen || pruefung.kannZweitePflichtfremdspracheErsetzen || pruefung.kannWahlpflichtfremdspracheErsetzen) && (pruefung.anspruchsniveauId === Sprachpruefungniveau.HA10.daten.id || pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id) && (pruefung.note !== null) && (pruefung.note <= 4)) && (!JavaObject.equalsTranspiler(belegungen.get(0).sprache, (pruefung.sprache))))
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
	public static hatSpracheMit2JahrenDauerEndeSekI(sprachendaten : Sprachendaten | null) : boolean {
		if (sprachendaten === null)
			return false;
		const anzahlSprachen : number = SprachendatenUtils.getSprachlegungenNachBeginnUndDauerEndeSekI(sprachendaten, "08", "10", 2).size();
		return (anzahlSprachen >= 1);
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
		const pruefungen : ArrayList<Sprachpruefung> = sprachendaten.pruefungen;
		if (pruefungen !== null) {
			for (const pruefungS1 of pruefungen) {
				if (pruefungS1.istFeststellungspruefung && (pruefungS1.kannErstePflichtfremdspracheErsetzen || pruefungS1.kannZweitePflichtfremdspracheErsetzen || pruefungS1.kannWahlpflichtfremdspracheErsetzen) && (pruefungS1.anspruchsniveauId === Sprachpruefungniveau.HA10.daten.id || pruefungS1.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id) && (pruefungS1.note !== null) && (pruefungS1.note <= 4)) {
					for (const pruefungEF of pruefungen) {
						if (pruefungEF.istFeststellungspruefung && JavaObject.equalsTranspiler(pruefungEF.ersetzteSprache, (pruefungS1.ersetzteSprache)) && (pruefungEF.kannErstePflichtfremdspracheErsetzen || pruefungEF.kannZweitePflichtfremdspracheErsetzen || pruefungEF.kannWahlpflichtfremdspracheErsetzen) && pruefungEF.anspruchsniveauId === Sprachpruefungniveau.EF.daten.id && (pruefungEF.note !== null) && (pruefungEF.note <= 4))
							return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Gibt die Fremdsprache zurück, die als erste Fremdsprache der Sekundarstufe I gewertet werden kann.
	 * Im Falle einer Sprachprüfung als erste Pflichtfremdsprache wird diese mit der als Ersatz eingetragene Fremdsprache zurückgegeben.
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
		const pruefungen : ArrayList<Sprachpruefung> = sprachendaten.pruefungen;
		if (pruefungen !== null) {
			for (const pruefung of pruefungen) {
				if (pruefung.istFeststellungspruefung && pruefung.kannErstePflichtfremdspracheErsetzen && (pruefung.anspruchsniveauId === Sprachpruefungniveau.HA10.daten.id || pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id) && (pruefung.note !== null) && (pruefung.note <= 4))
					return pruefung.ersetzteSprache;
			}
		}
		const belegungen : ArrayList<Sprachbelegung> = sprachendaten.belegungen;
		if (belegungen !== null) {
			const sprachbelegungen : ArrayList<Sprachbelegung> = SprachendatenUtils.getSprachlegungenNachBeginnUndDauerEndeSekI(sprachendaten, "05", "10", 0);
			if (!sprachbelegungen.isEmpty())
				return sprachbelegungen.get(0).sprache;
		}
		return null;
	}

	/**
	 * Gibt die Fremdsprache zurück, die als zweite Fremdsprache der Sekundarstufe I gewertet werden kann.
	 * Im Falle einer Sprachprüfung als zweite Pflichtfremdsprache bzw. WP-Sprache wird diese mit der als Ersatz eingetragene Fremdsprache zurückgegeben.
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
		const pruefungen : ArrayList<Sprachpruefung> = sprachendaten.pruefungen;
		if (pruefungen !== null) {
			for (const pruefung of pruefungen) {
				if (pruefung.istFeststellungspruefung && pruefung.kannErstePflichtfremdspracheErsetzen && (pruefung.anspruchsniveauId === Sprachpruefungniveau.HA10.daten.id || pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id) && (pruefung.note !== null) && (pruefung.note <= 4)) {
					pruefungErsteSprache = pruefung.ersetzteSprache;
				}
				if (pruefung.istFeststellungspruefung && (pruefung.kannZweitePflichtfremdspracheErsetzen || pruefung.kannWahlpflichtfremdspracheErsetzen) && (pruefung.anspruchsniveauId === Sprachpruefungniveau.HA10.daten.id || pruefung.anspruchsniveauId === Sprachpruefungniveau.MSA.daten.id) && (pruefung.note !== null) && (pruefung.note <= 4)) {
					pruefungZweiteSprache = pruefung.ersetzteSprache;
				}
			}
		}
		if (!JavaObject.equalsTranspiler(pruefungZweiteSprache, ("")))
			return pruefungZweiteSprache;
		const belegungen : ArrayList<Sprachbelegung> = sprachendaten.belegungen;
		if (belegungen !== null) {
			const sprachbelegungen : ArrayList<Sprachbelegung> = SprachendatenUtils.getSprachlegungenNachBeginnUndDauerEndeSekI(sprachendaten, "05", "10", 0);
			if (!JavaObject.equalsTranspiler(pruefungErsteSprache, (""))) {
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
	 * Ermittelt, ob eine Fremdsprache ab Kasse 8/9 im Umfang von mindestens 2 Jahren belegt wurde und gibt sie zurück
	 * Dabei wird davon ausgegangen, dass Sprachen ohne Ende der Belegung am Ende der Sekundarstufe I belegt wurden.
	 *
	 * @param sprachendaten die Sprachendaten mit Sprachbelegungen und Sprachprüfungen
	 *
	 * @return Sprache, falls eine Belegung vorhanden ist, sonst null
	 */
	public static getSpracheMit2JahrenDauerEndeSekI(sprachendaten : Sprachendaten | null) : string | null {
		if (sprachendaten === null)
			return null;
		const belegungen : ArrayList<Sprachbelegung> = sprachendaten.belegungen;
		if (belegungen !== null) {
			const sprachbelegungen : ArrayList<Sprachbelegung> = SprachendatenUtils.getSprachlegungenNachBeginnUndDauerEndeSekI(sprachendaten, "08", "10", 2);
			if (!sprachbelegungen.isEmpty())
				return sprachbelegungen.get(0).sprache;
		}
		return null;
	}

	/**
	 * Hilfsfunktion, die einen ASDJahrgang nach APO-SI und APO-GOSt und in einen numerischen Wert für Vergleiche umwandelt.
	 * Dabei wird EF zu 11, Q1 zu 12 und Q2 zu 13. Die übrigen Stufen werden gemäß ihrer numerischen Stufenangaben umgewandelt.
	 *
	 * @param kuerzelJg   der in den mumerischen Wert umzuwandelnde ASDJahrgang.
	 *
	 * @return Wert des ASDJahrgangs zwischen 5 und 13, wenn dieser nicht bestimmt werden kann, wird der Wert 0 zurückgegeben.
	 */
	private static getJahrgangNumerisch(kuerzelJg : string | null) : number {
		if (kuerzelJg === null || JavaObject.equalsTranspiler(kuerzelJg, ("")))
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
