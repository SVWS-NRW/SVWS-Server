import type { ReligionKatalogEintrag } from "../../../../../../core/src/asd/data/schule/ReligionKatalogEintrag";
import type { Schuljahresabschnitt } from "../../../../../../core/src/asd/data/schule/Schuljahresabschnitt";
import { Religion } from "../../../../../../core/src/asd/types/schule/Religion";
import type { Schulform } from "../../../../../../core/src/asd/types/schule/Schulform";
import { HashMap2D } from "../../../../../../core/src/core/adt/map/HashMap2D";
import type { ReligionEintrag } from "../../../../../../core/src/core/data/schule/ReligionEintrag";
import { DeveloperNotificationException } from "../../../../../../core/src/core/exceptions/DeveloperNotificationException";
import { Class } from "../../../../../../core/src/java/lang/Class";
import { JavaLong } from "../../../../../../core/src/java/lang/JavaLong";
import { JavaObject } from "../../../../../../core/src/java/lang/JavaObject";
import { JavaString } from "../../../../../../core/src/java/lang/JavaString";
import { ArrayList } from "../../../../../../core/src/java/util/ArrayList";
import { Arrays } from "../../../../../../core/src/java/util/Arrays";
import type { Comparator } from "../../../../../../core/src/java/util/Comparator";
import type { JavaFunction } from "../../../../../../core/src/java/util/function/JavaFunction";
import { HashMap } from "../../../../../../core/src/java/util/HashMap";
import type { List } from "../../../../../../core/src/java/util/List";
import {AuswahlManager} from "../../../AuswahlManager";


export class ReligionenListeManager extends AuswahlManager<number, ReligionEintrag, ReligionEintrag> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _religionToId: JavaFunction<ReligionEintrag, number> = { apply : (r: ReligionEintrag) => r.id };

	/**
	 * Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können
	 */
	private readonly _religionenByIstSichtbar: HashMap2D<boolean, number, ReligionEintrag> = new HashMap2D<boolean, number, ReligionEintrag>();

	private readonly _religionenByKuerzel: HashMap<string, ReligionEintrag> = new HashMap<string, ReligionEintrag>();

	/**
	 * Das Filter-Attribut auf nur sichtbare ReligionEintragen
	 */
	private _filterNurSichtbar: boolean = true;

	/**
	 * Ein Default-Comparator für den Vergleich von Religion-Einträgen.
	 */
	public static readonly _comparatorKuerzel: Comparator<ReligionEintrag> = { compare : (a: ReligionEintrag, b: ReligionEintrag) => {
		let cmp: number = a.sortierung - b.sortierung;
		if (cmp !== 0)
			return cmp;

		if ((a.kuerzel === null) || (b.kuerzel === null)) {
			if ((a.kuerzel === null) && (b.kuerzel === null))
				return 0;
			return (a.kuerzel === null) ? -1 : 1;
		}
		cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	/**
	 * Ein Comparator für den Vergleich von Religion-Einträgen anhand ihres Textes.
	 */
	public static readonly _comparatorText : Comparator<ReligionEintrag> = { compare : (a: ReligionEintrag, b: ReligionEintrag) => {
		const cmp : number = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param religionen                   die Liste der Katalog-Einträge
	 */
	public constructor(schuljahresabschnitt: number, schuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, religionen: List<ReligionEintrag>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, religionen, ReligionenListeManager._comparatorKuerzel,
			ReligionenListeManager._religionToId, ReligionenListeManager._religionToId, Arrays.asList());
		this.initEintrage();
	}

	private initEintrage(): void {
		for (const r of this.liste.list()) {
			this._religionenByIstSichtbar.put(r.istSichtbar, r.id, r);
			if (r.kuerzel !== null)
				this._religionenByKuerzel.put(r.kuerzel, r);
		}
	}

	/**
	 * Gibt die Liste der bislang nicht für diese Schule erstellten Förderschwerpunkte zurück
	 *
	 * @return Liste der bislang nicht für diese Schule erstellten Förderschwerpunkte
	 */
	public getAvailableReligionenForCreate(): List<Religion> {
		const result = new ArrayList<Religion>()
		for (const r of Religion.data().getListBySchuljahrAndSchulform(this.getSchuljahr(), this.schulform())) {
			let alreadyInList: boolean = false;
			for (const religionEintrag of this.liste.list()) {
				const daten: ReligionKatalogEintrag | null = r.daten(this.getSchuljahr());
				if (daten === null)
					continue;
				if (JavaObject.equalsTranspiler(daten.kuerzel, religionEintrag.kuerzel)) {
					alreadyInList = true;
					break;
				}
			}
			if (!alreadyInList)
				result.add(r);
		}
		return result;
	}

	/**
	 * Gibt die Liste der bislang nicht für diese Schule erstellten Förderschwerpunkte inklusive des ausgewählten Eintrags zurück
	 *
	 * @return Liste der bislang nicht für diese Schule erstellten Förderschwerpunkte inklusive des ausgewählten Eintrags
	 */
	public getAvailableReligionenForPatch(): List<Religion> {
		const result = this.getAvailableReligionenForCreate();
		if (this.hasDaten())
			result.add(Religion.data().getWertBySchluessel(this.auswahl().kuerzel ?? ""));
		return result;
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	protected onSetDaten(eintrag: ReligionEintrag, daten: ReligionEintrag) : boolean {
		let updateEintrag : boolean = false;
		if (!JavaObject.equalsTranspiler(daten.kuerzel, (eintrag.kuerzel))) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		if (!JavaObject.equalsTranspiler(daten.bezeichnung, (eintrag.bezeichnung))) {
			eintrag.bezeichnung = daten.bezeichnung;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Fächer zurück.
	 *
	 * @return true, wenn nur sichtbare Fächer angezeigt werden und ansonsten false
	 */
	public filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Fächer.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public setFilterNurSichtbar(value: boolean): void {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	/**
	 * Vergleicht zwei Religionslisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleiner, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a: ReligionEintrag, b: ReligionEintrag): number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("kuerzel", (field))) {
				cmp = ReligionenListeManager._comparatorKuerzel.compare(a, b);
			} else
				if (JavaObject.equalsTranspiler("text", (field))) {
					cmp = ReligionenListeManager._comparatorText.compare(a, b);
				} else
					throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return ReligionenListeManager._comparatorKuerzel.compare(a, b);
	}

	protected checkFilter(eintrag: ReligionEintrag) : boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar)
			return false;
		return true;
	}

	/**
	 * Methode übernimmt Filterinformationen aus dem übergebenen {@link AuswahlManager}
	 *
	 * @param srcManager Manager, aus dem die Filterinformationen übernommen werden
	 */
	public useFilter(srcManager: ReligionenListeManager) : void {
		this.setFilterNurSichtbar(srcManager.filterNurSichtbar());
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.religion.ReligionenListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.religion.ReligionenListeManager'].includes(name);
	}

	public static class = new Class<ReligionenListeManager>('de.svws_nrw.core.utils.religion.ReligionenListeManager');

}
