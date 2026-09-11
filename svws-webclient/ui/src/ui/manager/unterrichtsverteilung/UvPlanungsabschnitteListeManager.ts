import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { UvPlanungsabschnitt } from '../../../../../core/src/core/data/uv/UvPlanungsabschnitt';
import { DeveloperNotificationException } from '../../../../../core/src/core/exceptions/DeveloperNotificationException';
import { DateUtils } from "../../../../../core/src/core/utils/DateUtils";
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import { JavaObject } from '../../../../../core/src/java/lang/JavaObject';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import type { List } from '../../../../../core/src/java/util/List';
import { AuswahlManager } from '../AuswahlManager';

export class UvPlanungsabschnitteListeManager extends AuswahlManager<number, UvPlanungsabschnitt, UvPlanungsabschnitt> {

	/**
	 * Ein Default-Comparator für den Vergleich von Planungsabschnitten in Listen.
	 */
	public static readonly comparator: Comparator<UvPlanungsabschnitt> = { compare: (a: UvPlanungsabschnitt, b: UvPlanungsabschnitt) => {
		let cmp: number = a.schuljahr - b.schuljahr;
		if (cmp !== 0) {
			return cmp;
		}
		cmp = a.schuljahr - b.schuljahr;
		if (cmp !== 0) {
			return cmp;
		}
		cmp = (a.aktiv === b.aktiv) ? 0 : (a.aktiv ? -1 : 1);
		if (cmp !== 0) {
			return cmp;
		}
		cmp = JavaString.compareTo(a.gueltigVon, b.gueltigVon);
		if (cmp !== 0) {
			return cmp;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _planungsabschnittToId = (s: UvPlanungsabschnitt) => s.id;

	/**
	 * Das Filter-Attribut auf nur aktive Stundenpläne
	 */
	private _filterNurAktiv: boolean = false;

	/**
	 * Die gefilterte Liste, sofern sie schon berechnet wurde
	 */
	protected _aktive: List<UvPlanungsabschnitt> | null = null;

	private _vorlage: UvPlanungsabschnitt | null = null;

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Klassenauswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param planungsabschnittListe    die Liste der Planungsabschnitte
	 */
	public constructor(schuljahresabschnitt: number, schuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>, schulform: Schulform | null, planungsabschnittListe: List<UvPlanungsabschnitt>, createVorlage: boolean) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, planungsabschnittListe, UvPlanungsabschnitteListeManager.comparator, UvPlanungsabschnitteListeManager._planungsabschnittToId, UvPlanungsabschnitteListeManager._planungsabschnittToId, []);
		if (createVorlage) {
			this.addVorlage();
		}
	}

	protected checkFilter(eintrag: UvPlanungsabschnitt): boolean {
		if (this._filterNurAktiv && !eintrag.aktiv) {
			return false;
		}
		return true;
	}

	/**
	 * Setzt die Filtereinstellung auf nur aktive Stundenpläne.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public setFilterNurAktiv(value: boolean): void {
		this._filterNurAktiv = value;
		this._eventHandlerFilterChanged();
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf nur aktive Stundenpläne zurück.
	 *
	 * @return true, wenn nur aktive Stundenpläne angezeigt werden und ansonsten false
	 */
	public filterNurAktiv(): boolean {
		return this._filterNurAktiv;
	}

	protected compareAuswahl(a: UvPlanungsabschnitt, b: UvPlanungsabschnitt): number {
		let cmp: number = a.schuljahr - b.schuljahr;
		if (cmp !== 0) {
			return cmp;
		}
		cmp = a.schuljahr - b.schuljahr;
		if (cmp !== 0) {
			return cmp;
		}
		cmp = (a.aktiv === b.aktiv) ? 0 : (a.aktiv ? -1 : 1);
		if (cmp !== 0) {
			return cmp;
		}
		for (const { field, ascending } of this._order) {
			if (JavaObject.equalsTranspiler("gueltigVon", (field))) {
				cmp = JavaString.compareTo(a.gueltigVon, b.gueltigVon);
			} else
				if (JavaObject.equalsTranspiler("beschreibung", (field))) {
					cmp = JavaString.compareTo(a.beschreibung ?? "", b.beschreibung ?? "");
				} else {
					throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
				}
			if (cmp === 0) {
				continue;
			}
			return ascending ? cmp : -cmp;
		}
		return JavaLong.compare(a.id, b.id);
	}

	/**
	 * Gibt eine gefilterte Auswahl-Liste zurück. Für die Filterung
	 * muss der Manager die Methode onFilter überschreiben.
	 *
	 * @return die gefilterte Liste
	 */
	public filtered(): List<UvPlanungsabschnitt> {
		const hasCache: boolean = this._filtered !== null;
		const filtered: List<UvPlanungsabschnitt> | null = super.filtered();
		if (hasCache) {
			return filtered;
		}
		if (this._vorlage !== null) {
			filtered.addFirst(this._vorlage);
		}
		return filtered;
	}

	/**
	 * Gibt eine Auswahl-Liste aller aktiven Stundenpläne zurück.
	 *
	 * @return die Liste der aktiven Stundenpläne
	 */
	public aktive(): List<UvPlanungsabschnitt> {
		if (this._filtered === null || this._aktive === null) {
			this._aktive = new ArrayList();
			for (const planungsabschnitt of this.liste.list()) {
				if (planungsabschnitt.aktiv) {
					this._aktive.add(planungsabschnitt);
				}
			}
		}
		return this._aktive;
	}

	/**
	 * Gibt eine alle UvPlanungsabschnitte zurück.
	 *
	 * @return die Liste der UvPlanungsabschnitte
	 */
	public alle(): List<UvPlanungsabschnitt> {
		return this.liste.list();
	}

	/**
	 * Gibt den Schuljahresabschnitt zurück, auf den sich die Auswahl aktuell bezieht.
	 *
	 * @return der Schuljahresabschnitt oder null, falls die ID nicht in der Auswahlliste existiert
	 */
	public getSchuljahresabschnittAuswahl(): Schuljahresabschnitt | null {
		return this.schuljahresabschnitte.get(this._schuljahresabschnitt);
	}

	/**
	 * Die Bezeichnung ist obligatorisch und darf maximal 150 Zeichen lang sein.
	 *
	 * @param bezeichnung die Bezeichnung des Stundenplans
	 *
	 * @return <code>true</code> wenn die Bezeichnung des Stundenplans gültig ist, ansonsten <code>false</code>
	 */
	public static validateBezeichnung(bezeichnung: string | null): boolean {
		if (bezeichnung === null) {
			return false;
		}
		return bezeichnung.trim().length <= 150;
	}

	/**
	 * Prüft für die aktuelle Auswahl eine neue Gültigkeit. Wenn das Datum leer oder hinter dem Gültigkeitsende befindet,
	 * wird <code>false</code>, andernfalls <code>true</code> zurückgegeben. Je nach Parameter aktiv wird auch geprüft, ob es sich innerhalb der Gültigkeit eines anderen aktiven Planungsabschnitts befindet.
	 *
	 * @param gueltigAb das Datum, ab wann der Planungsabschnitt gültig sein soll
	 * @param gueltigBis das Datum, bis wann der Planungsabschnitt gültig sein soll. Falls null übergeben wird, wird das Datum der Auswahl verwendet.
	 * @param aktiv falls true, werden zusätzlich die anderen aktiven Planungsabschnitte geprüft
	 * @param warn falls true, wird zusätzlich geprüft, ob es eine Überschneidung mit einem anderen Planungsabschnitt gibt
	 * @param neu gibt an, ob es sich um einen neuen Planungsabschnitt handelt
	 *
	 * @return <code>true</code> wenn das Datum gültig ist, ansonsten <code>false</code>
	 */
	public validateGueltigVon(gueltigAb: string | null, gueltigBis: string | null, aktiv: boolean, warn: boolean, neu: boolean): boolean {
		if (gueltigAb === null || !DateUtils.isValidDate(gueltigAb)) {
			return false;
		}
		const gueltigBisComputed: string | null = (gueltigBis ?? this.auswahl().gueltigBis);
		if (gueltigBisComputed !== null) {
			if (JavaString.compareTo(gueltigAb, gueltigBisComputed) > 0) {
				return false;
			}
		}
		if (aktiv || warn) {
			for (const abschnitt of this.aktive()) {
				if ((!this.hasDaten() || abschnitt.id !== this.auswahl().id || neu) && (JavaString.compareTo(abschnitt.gueltigVon, gueltigAb) <= 0) && (abschnitt.gueltigBis === null || JavaString.compareTo(abschnitt.gueltigBis, gueltigAb) >= 0)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Prüft für die aktuelle Auswahl eine neue Gültigkeit. Wenn das Datum leer oder sich vor dem Gültigkeitsbeginn befindet,
	 * wird <code>false</code>, andernfalls <code>true</code> zurückgegeben. Je nach Parameter aktiv wird auch geprüft, ob es sich innerhalb der Gültigkeit eines anderen aktiven Planungsabschnitts befindet.
	 *
	 * @param gueltigAb das Datum, ab wann der Planungsabschnitt gültig sein soll. Falls null übergeben wird, wird das Datum der Auswahl verwendet.
	 * @param gueltigBis das Datum, bis zu dem der Planungsabschnitt gültig sein soll
	 * @param aktiv falls true, werden zusätzlich die anderen aktiven Planungsabschnitte geprüft
	 * @param warn falls true, wird zusätzlich geprüft, ob es eine Überschneidung mit einem anderen Planungsabschnitt gibt
	 * @param neu gibt an, ob es sich um einen neuen Planungsabschnitt handelt
	 *
	 * @return <code>true</code> wenn das Datum gültig ist, ansonsten <code>false</code>
	 */
	public validateGueltigBis(gueltigAb: string | null, gueltigBis: string | null, aktiv: boolean, warn: boolean, neu: boolean): boolean {
		if (gueltigBis === null || !DateUtils.isValidDate(gueltigBis)) {
			return false;
		}
		const gueltigAbComputed: string = (gueltigAb ?? this.auswahl().gueltigVon);
		if (JavaString.compareTo(gueltigBis, gueltigAbComputed) < 0) {
			return false;
		}
		if (aktiv || warn) {
			for (const abschnitt of this.aktive()) {
				if ((!this.hasDaten() || abschnitt.id !== this.auswahl().id || neu) && (JavaString.compareTo(abschnitt.gueltigVon, gueltigBis) <= 0) && (abschnitt.gueltigBis === null || JavaString.compareTo(abschnitt.gueltigBis, gueltigBis) >= 0)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Prüft, ob der aktuell ausgewählte Planungsabschnitt mit den übergebenen Gültigkeitsdaten eine Überschneidung mit einem anderen Planungsabschnitt gibt.
	 *
	 * @param gueltigAb das Datum, ab wann der Planungsabschnitt gültig sein soll
	 * @param gueltigBis das Datum, bis zu dem der Planungsabschnitt gültig sein soll
	 * @param neu gibt an, ob es sich um einen neuen Planungsabschnitt handelt
	 *
	 *
	 * @return <code>true</code> wenn es eine Überschneidung gibt, ansonsten <code>false</code>
	 */
	public istKonfliktfreiZuAktivenPlanungsabschnitten(gueltigAb: string | null, gueltigBis: string | null, neu: boolean = false): boolean {
		const ab = (gueltigAb ?? this.auswahl().gueltigVon);
		const bis = (gueltigBis ?? this.auswahl().gueltigBis);
		for (const sp of this.aktive()) {
			if (!this.hasDaten() || this.auswahl().id !== sp.id || neu) {
				if (bis !== null && sp.gueltigBis !== null && DateUtils.berechneGemeinsameTage(ab, bis, sp.gueltigVon, sp.gueltigBis).length > 0) {
					return false;
				} else if (bis === null && sp.gueltigBis !== null && JavaString.compareTo(sp.gueltigBis, ab) >= 0) {
					return false;
				} else if (bis !== null && sp.gueltigBis === null && JavaString.compareTo(bis, sp.gueltigVon) >= 0) {
					return false;
				} else if (bis === null && sp.gueltigBis === null) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Gibt den auf die Gültigkeit bezogen letzten Planungsabschnitt in der Liste zurück.
	 *
	 * @return der letzte Planungsabschnitt in der Liste oder <code>null</code>, falls die Liste leer ist.
	 */
	public getLastAktiv(): UvPlanungsabschnitt | null {
		return this.aktive().isEmpty() ? null : this.aktive().getLast();
	}

	/**
	 * Erstellt eine Planungsabschnitt-Vorlage und fügt sie der Auswahl-Liste hinzu.
	 */
	private addVorlage(): void {
		this._vorlage = new UvPlanungsabschnitt();
		this._vorlage.id = -1;
		this._vorlage.beschreibung = "Grunddaten";
	}

	/**
	 * Setzt die Daten. Dabei wird ggf. die Auswahl angepasst. Die vorherige Auswahl wird gespeichert.
	 *
	 * @param daten   die neuen Daten
	 *
	 * @throws DeveloperNotificationException   falls die Daten nicht in der Auswahlliste vorhanden ist
	 */
	public setDaten(daten: UvPlanungsabschnitt | null): void {
		if (daten !== null && daten.id === -1) {
			this._vorherigeAuswahl = this._daten;
			this._daten = daten;
			this._filtered = null;
		} else {
			super.setDaten(daten);
		}
	}

	/**
	 * Gibt den Eintrag der aktuellen Auswahl in der Liste zurück. Hiefür muss eine
	 * gültige Auswahl vorliegen. Dies kann ggf. vorher über hasDaten geprüft werden.
	 *
	 * @return der Eintrag in der Auswahlliste
	 *
	 * @throws DeveloperNotificationException wenn keine gültige Auswahl vorliegt
	 */
	public auswahl(): UvPlanungsabschnitt {
		if (this._daten === null) {
			return super.auswahl();
		}
		if (this._daten.id !== -1) {
			return super.auswahl();
		}
		if (this._vorlage === null) {
			throw new DeveloperNotificationException("Es existiert kein Vorlagen-Stundenplan.");
		}
		return this._vorlage;
	}

}
