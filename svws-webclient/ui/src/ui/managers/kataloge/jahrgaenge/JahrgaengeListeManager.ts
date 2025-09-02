import { JavaObject } from '../../../../../../core/src/java/lang/JavaObject';
import type { Schulform } from '../../../../../../core/src/asd/types/schule/Schulform';
import type { JahrgangsDaten } from '../../../../../../core/src/core/data/jahrgang/JahrgangsDaten';
import { JavaString } from '../../../../../../core/src/java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../../../core/src/core/exceptions/DeveloperNotificationException';
import type { Comparator } from '../../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../../AuswahlManager';
import type { JavaFunction } from '../../../../../../core/src/java/util/function/JavaFunction';
import { JavaLong } from '../../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../../core/src/java/util/List';
import { Class } from '../../../../../../core/src/java/lang/Class';
import { Arrays } from '../../../../../../core/src/java/util/Arrays';
import type { Schuljahresabschnitt } from '../../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import { HashSet } from '../../../../../../core/src/java/util/HashSet';
import type { JavaSet } from '../../../../../../core/src/java/util/JavaSet';
import { Jahrgaenge } from '../../../../../../core/src/asd/types/jahrgang/Jahrgaenge';



export class JahrgaengeListeManager extends AuswahlManager<number, JahrgangsDaten, JahrgangsDaten> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _jahrgangToId : JavaFunction<JahrgangsDaten, number> = { apply : (j: JahrgangsDaten) => j.id };

	/**
	 * Set der IDs von Jahrgängen, die in anderen Datenbanktabellen referenziert werden.
	 */
	private readonly idsReferencedJahrgaenge : HashSet<number> = new HashSet<number>();
	/**
	 * Ein Default-Comparator für den Vergleich von Jahrgängen in Jahrgangslisten.
	 */
	public static readonly comparator : Comparator<JahrgangsDaten> = { compare : (a: JahrgangsDaten, b: JahrgangsDaten) => {
		let cmp : number = a.sortierung - b.sortierung;
		if (cmp !== 0)
			return cmp;
		if ((a.kuerzel !== null) && (b.kuerzel !== null)) {
			cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
			if (cmp !== 0)
				return cmp;
		}
		return JavaLong.compare(a.id, b.id);
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param jahrgaenge       die Liste der Jahrgänge
	 */
	public constructor(schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, jahrgaenge : List<JahrgangsDaten>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, jahrgaenge, JahrgaengeListeManager.comparator, JahrgaengeListeManager._jahrgangToId, JahrgaengeListeManager._jahrgangToId, Arrays.asList());
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	protected onSetDaten(eintrag : JahrgangsDaten, daten : JahrgangsDaten) : boolean {
		let updateEintrag : boolean = false;
		if (!JavaObject.equalsTranspiler(daten.kuerzel, (eintrag.kuerzel))) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		if (!JavaObject.equalsTranspiler(daten.bezeichnung, (eintrag.bezeichnung))) {
			eintrag.bezeichnung = daten.bezeichnung;
			updateEintrag = true;
		}
		if (!JavaObject.equalsTranspiler(daten.anzahlRestabschnitte, (eintrag.anzahlRestabschnitte))) {
			eintrag.anzahlRestabschnitte = daten.anzahlRestabschnitte;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	/**
	 * Vergleicht zwei JahrgangsDaten Einträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a : JahrgangsDaten, b : JahrgangsDaten) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("kuerzel", (field))) {
				if ((a.kuerzel === null) && (b.kuerzel !== null)) {
					cmp = -1;
				} else
					if ((a.kuerzel !== null) && (b.kuerzel === null)) {
						cmp = 1;
					} else
						if ((a.kuerzel !== null) && (b.kuerzel !== null)) {
							cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
						}
			} else
				if (JavaObject.equalsTranspiler("bezeichnung", (field))) {
					cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
				} else
					throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return JahrgaengeListeManager.comparator.compare(a, b);
	}
	/**
	 *Gibt das Set mit den Ids der Jahrgänge zurück, die in der Auswahl sind und in anderen Datenbanktabellen referenziert werden
	 *
	 * @return Das Set mit IDs von Jahrgängen, die in anderen Datenbanktabellen referenziert werden
	 */
	public getIdsReferencedJahrgaenge() : JavaSet<number> {
		return this.idsReferencedJahrgaenge;
	}

	protected onMehrfachauswahlChanged() : void {
		this.idsReferencedJahrgaenge.clear();
		for (const l of this.liste.auswahl())
			if ((l.referenziertInAnderenTabellen !== null) && l.referenziertInAnderenTabellen)
				this.idsReferencedJahrgaenge.add(l.id);
	}

	/**
	 * Gibt alle bislang nicht in der Datenbank gespeicherten Einträge des Coretypes Jahrgaenge zurück.
	 *
	 * @param currentJahrgang Der Jahrgang des aktuellen Eintrags oder null
	 *
	 * @return Die bislang nicht in der Datenbank gespeicherten Einträge des Coretypes Jahrgaenge zurück.
	 */
	public getAvailableJahrgaenge(currentJahrgang : Jahrgaenge | null) : Jahrgaenge[] {
		const alleJahrgaenge = [...Jahrgaenge.getListBySchuljahrAndSchulform(this.getSchuljahr(), this.schulform())];
		const verwendeteJahrgaenge = [... this.liste.list()].map((j : JahrgangsDaten) => j.kuerzelStatistik);
		const result = alleJahrgaenge.filter((j: Jahrgaenge) => !verwendeteJahrgaenge.includes(j.daten(this.getSchuljahr())?.kuerzel ?? ''));
		if (currentJahrgang !== null)
			result.push(currentJahrgang)
		return result
	}

	protected checkFilter(eintrag : JahrgangsDaten) : boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.kataloge.jahrgaenge.JahrgaengeListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.kataloge.jahrgaenge.JahrgaengeListeManager'].includes(name);
	}

	public static class = new Class<JahrgaengeListeManager>('de.svws_nrw.core.utils.kataloge.jahrgaenge.JahrgaengeListeManager');

}

export function cast_de_svws_nrw_core_utils_kataloge_jahrgaenge_JahrgaengeListeManager(obj : unknown) : JahrgaengeListeManager {
	return obj as JahrgaengeListeManager;
}
