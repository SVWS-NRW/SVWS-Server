import { JavaObject } from '../../../../../../core/src/java/lang/JavaObject';
import { Schulform } from '../../../../../../core/src/asd/types/schule/Schulform';
import { ArrayList } from '../../../../../../core/src/java/util/ArrayList';
import { JavaString } from '../../../../../../core/src/java/lang/JavaString';
import type { Comparator } from '../../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../../AuswahlManager';
import { JavaInteger } from '../../../../../../core/src/java/lang/JavaInteger';
import type { JavaFunction } from '../../../../../../core/src/java/util/function/JavaFunction';
import { JavaLong } from '../../../../../../core/src/java/lang/JavaLong';
import { FoerderschwerpunktEintrag } from '../../../../../../core/src/core/data/schule/FoerderschwerpunktEintrag';
import type { List } from '../../../../../../core/src/java/util/List';
import { Class } from '../../../../../../core/src/java/lang/Class';
import { FoerderschwerpunktKatalogEintrag } from '../../../../../../core/src/asd/data/schule/FoerderschwerpunktKatalogEintrag';
import { Foerderschwerpunkt } from '../../../../../../core/src/asd/types/schule/Foerderschwerpunkt';
import { Schuljahresabschnitt } from '../../../../../../core/src/asd/data/schule/Schuljahresabschnitt';

export class FoerderschwerpunkteListeManager extends AuswahlManager<number, FoerderschwerpunktEintrag, FoerderschwerpunktEintrag> {

	private static readonly _foerderschwerpunktToId : JavaFunction<FoerderschwerpunktEintrag, number> = { apply : (a: FoerderschwerpunktEintrag) => a.id };

	/**
	 * Ein Default-Comparator für den Vergleich von Förderschwerpunkten
	 */
	public static readonly comparator : Comparator<FoerderschwerpunktEintrag> = { compare : (a: FoerderschwerpunktEintrag, b: FoerderschwerpunktEintrag) => {
		let cmp : number;
		cmp = JavaInteger.compare(a.sortierung, b.sortierung);
		if (cmp !== 0)
			return cmp;
		if ((a.kuerzel !== null) && (b.kuerzel !== null)) {
			cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
			if (cmp !== 0)
				return cmp;
		}
		if ((a.text !== null) && (b.text !== null)) {
			cmp = JavaString.compareTo(a.text, b.text);
			if (cmp !== 0)
				return cmp;
		}
		return JavaLong.compare(a.id, b.id);
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt    	  der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           die Liste der Schuljahresabschnitte
	 * @param schulform     				  die Schulform der Schule
	 * @param foerderschwerpunkte     			  die Liste der Foerderschwerpunkte
	 */
	public constructor(idSchuljahresabschnitt : number, idSchuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, foerderschwerpunkte : List<FoerderschwerpunktEintrag>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, foerderschwerpunkte, FoerderschwerpunkteListeManager.comparator, FoerderschwerpunkteListeManager._foerderschwerpunktToId, FoerderschwerpunkteListeManager._foerderschwerpunktToId, ArrayList.of());
	}

	/**
	 * Gibt die Liste der bislang nicht für diese Schule erstellten Förderschwerpunkte zurück
	 *
	 * @return Liste der bislang nicht für diese Schule erstellten Förderschwerpunkte
	 */
	public getAvailableFoerderschwerpunkte() : List<Foerderschwerpunkt> {
		const result : List<Foerderschwerpunkt> = new ArrayList<Foerderschwerpunkt>();
		for (const f of Foerderschwerpunkt.getBySchuljahrAndSchulform(this.getSchuljahr(), this.schulform())) {
			let alreadyInList : boolean = false;
			for (const fe of this.liste.list()) {
				const daten : FoerderschwerpunktKatalogEintrag | null = f.daten(this.getSchuljahr());
				if (daten === null)
					continue;
				if (JavaObject.equalsTranspiler(daten.schluessel, (fe.kuerzelStatistik))) {
					alreadyInList = true;
					break;
				}
			}
			if (!alreadyInList)
				result.add(f);
		}
		return result;
	}

	protected checkFilter(eintrag : FoerderschwerpunktEintrag | null) : boolean {
		return true;
	}

	protected compareAuswahl(a : FoerderschwerpunktEintrag, b : FoerderschwerpunktEintrag) : number {
		return FoerderschwerpunkteListeManager.comparator.compare(a, b);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.kataloge.foerderschwerpunkte.FoerderschwerpunkteListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.kataloge.foerderschwerpunkte.FoerderschwerpunkteListeManager'].includes(name);
	}

	public static class = new Class<FoerderschwerpunkteListeManager>('de.svws_nrw.core.utils.kataloge.foerderschwerpunkte.FoerderschwerpunkteListeManager');

}

export function cast_de_svws_nrw_core_utils_kataloge_foerderschwerpunkte_FoerderschwerpunkteListeManager(obj : unknown) : FoerderschwerpunkteListeManager {
	return obj as FoerderschwerpunkteListeManager;
}
