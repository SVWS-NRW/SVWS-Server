import { Schulform } from '../../../asd/types/schule/Schulform';
import { StundenplanManager } from '../../../core/utils/stundenplan/StundenplanManager';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { JavaString } from '../../../java/lang/JavaString';
import { DateUtils } from '../../../core/utils/DateUtils';
import type { Comparator } from '../../../java/util/Comparator';
import { AuswahlManager } from '../../../core/utils/AuswahlManager';
import { StundenplanListeEintrag } from '../../../core/data/stundenplan/StundenplanListeEintrag';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { JavaLong } from '../../../java/lang/JavaLong';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Arrays } from '../../../java/util/Arrays';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';

export class StundenplanListeManager extends AuswahlManager<number, StundenplanListeEintrag, StundenplanManager> {

	/**
	 * Ein Default-Comparator für den Vergleich von Klassen in Klassenlisten.
	 */
	public static readonly comparator : Comparator<StundenplanListeEintrag> = { compare : (a: StundenplanListeEintrag, b: StundenplanListeEintrag) => {
		let cmp : number = a.schuljahr - b.schuljahr;
		if (cmp !== 0)
			return cmp;
		cmp = a.abschnitt - b.abschnitt;
		if (cmp !== 0)
			return cmp;
		if ((a.gueltigAb !== null) && (b.gueltigAb !== null))
			cmp = JavaString.compareTo(a.gueltigAb, b.gueltigAb);
		return cmp !== 0 ? cmp : JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _listeEintragToId : JavaFunction<StundenplanListeEintrag, number> = { apply : (s: StundenplanListeEintrag) => s.id };

	private static readonly _stundenplanToId : JavaFunction<StundenplanManager, number> = { apply : (s: StundenplanManager) => s.stundenplanGetID() };

	private _stundenplanVorlage : StundenplanListeEintrag | null = null;


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Klassenauswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param stundenplanListe    die Liste der Stundenpläne
	 * @param createVorlage ob eine Vorlage erstellt werden soll
	 */
	public constructor(schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, stundenplanListe : List<StundenplanListeEintrag>, createVorlage : boolean) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, stundenplanListe, StundenplanListeManager.comparator, StundenplanListeManager._listeEintragToId, StundenplanListeManager._stundenplanToId, Arrays.asList());
		if (createVorlage)
			this.addStundenplanVorlage();
	}

	protected checkFilter(eintrag : StundenplanListeEintrag) : boolean {
		return true;
	}

	protected compareAuswahl(a : StundenplanListeEintrag, b : StundenplanListeEintrag) : number {
		return 0;
	}

	/**
	 * Gibt eine gefilterte Auswahl-Liste zurück. Für die Filterung
	 * muss der Manager die Methode onFilter überschreiben.
	 *
	 * @return die gefilterte Liste
	 */
	public filtered() : List<StundenplanListeEintrag> {
		const hasCache : boolean = this._filtered !== null;
		const filtered : List<StundenplanListeEintrag> | null = super.filtered();
		if (hasCache)
			return filtered;
		if (this._stundenplanVorlage !== null)
			filtered.addFirst(this._stundenplanVorlage);
		return filtered;
	}

	/**
	 * Setzt die Daten. Dabei wird ggf. die Auswahl angepasst. Die vorherige Auswahl wird gespeichert.
	 *
	 * @param daten   die neuen Daten
	 *
	 * @throws DeveloperNotificationException   falls die Daten nicht in der Auswahlliste vorhanden ist
	 */
	public setDaten(daten : StundenplanManager | null) : void {
		if (daten !== null && daten.stundenplanGetID() === -1) {
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
	public auswahl() : StundenplanListeEintrag {
		if (this._daten === null || this._daten.stundenplanGetID() !== -1)
			return super.auswahl();
		if (this._stundenplanVorlage === null)
			throw new DeveloperNotificationException("Es existiert kein Vorlagen-Stundenplan.")
		return this._stundenplanVorlage;
	}

	/**
	 * Die Bezeichnung ist obligatorisch und darf maximal 150 Zeichen lang sein.
	 *
	 * @param bezeichnung die Bezeichnung des Stundenplans
	 *
	 * @return <code>true</code> wenn die Bezeichnung des Stundenplans gültig ist, ansonsten <code>false</code>
	 */
	public static validateBezeichnung(bezeichnung : string | null) : boolean {
		if (bezeichnung === null)
			return false;
		return bezeichnung.trim().length <= 150;
	}

	/**
	 * Wenn das Datum nicht leer oder sich innerhalb der Gültigkeit eines anderen Stundenplans befindet oder hinter dem Gültigkeitsende befindet,
	 * wird <code>false</code>, andernfalls <code>true</code> zurückgegeben.
	 *
	 * @param gueltigAb das Datum, ab wann der Stundenplan gültig sein soll
	 * @param gueltigBis das Datum, bis zu dem der Stundenplan gültig sein soll
	 *
	 * @return <code>true</code> wenn das Datum gültig ist, ansonsten <code>false</code>
	 */
	public validateGueltigAb(gueltigAb : string | null, gueltigBis : string | null) : boolean {
		if (gueltigAb === null || !DateUtils.isValidDate(gueltigAb))
			return false;
		if (gueltigBis !== null && DateUtils.isValidDate(gueltigBis) && JavaString.compareTo(gueltigAb, gueltigBis) > 0)
			return false;
		for (const stundenplan of this.liste.list())
			if ((!this.hasDaten() || stundenplan.id !== this.auswahl().id) && (JavaString.compareTo(stundenplan.gueltigAb, gueltigAb) <= 0) && (JavaString.compareTo(stundenplan.gueltigBis, gueltigAb) >= 0))
				return false;
		return true;
	}

	/**
	 * Wenn das Datum nicht leer oder sich innerhalb der Gültigkeit eines anderen Stundenplans befindet oder vor dem Gültigkeitsbeginn befindet,
	 * wird <code>false</code>, andernfalls <code>true</code> zurückgegeben.
	 *
	 * @param gueltigAb das Datum, ab wann der Stundenplan gültig sein soll
	 * @param gueltigBis das Datum, bis zu dem der Stundenplan gültig sein soll
	 *
	 * @return <code>true</code> wenn das Datum gültig ist, ansonsten <code>false</code>
	 */
	public validateGueltigBis(gueltigAb : string | null, gueltigBis : string | null) : boolean {
		if (gueltigBis === null || !DateUtils.isValidDate(gueltigBis))
			return false;
		if (gueltigAb !== null && DateUtils.isValidDate(gueltigAb) && JavaString.compareTo(gueltigAb, gueltigBis) > 0)
			return false;
		for (const stundenplan of this.liste.list())
			if ((!this.hasDaten() || stundenplan.id !== this.auswahl().id) && (JavaString.compareTo(stundenplan.gueltigAb, gueltigBis) <= 0) && (JavaString.compareTo(stundenplan.gueltigBis, gueltigBis) >= 0))
				return false;
		return true;
	}

	/**
	 * Prüft, ob es eine Überschneidung mit einem anderen Stundenplan gibt.
	 *
	 * @param stundenplan der Stundenplan, der geprüft werden soll
	 *
	 * @return <code>true</code> wenn es eine Überschneidung gibt, ansonsten <code>false</code>
	 */
	public hatUeberschneidungMitAnderemStundenplan(stundenplan : StundenplanListeEintrag) : boolean {
		for (const sp of this.liste.list())
			if ((stundenplan.id !== sp.id) && (DateUtils.berechneGemeinsameTage(stundenplan.gueltigAb, stundenplan.gueltigBis, sp.gueltigAb, sp.gueltigBis).length > 0))
				return true;
		return false;
	}

	/**
	 * Gibt den auf die Gültigkeit bezogen letzten Stundenplan in der Liste zurück.
	 *
	 * @return der letzte Stundenplan in der Liste oder <code>null</code>, falls die Liste leer ist.
	 */
	public getLastValidStundenplan() : StundenplanListeEintrag | null {
		return this.liste.list().isEmpty() ? null : this.liste.list().getLast();
	}

	/**
	 * Erstellt eine Stundenplan-Vorlage und fügt sie der Auswahl-Liste hinzu.
	 */
	private addStundenplanVorlage() : void {
		this._stundenplanVorlage = new StundenplanListeEintrag();
		this._stundenplanVorlage.id = -1;
		this._stundenplanVorlage.bezeichnung = "Allgemeine Vorlagen";
	}

	/**
	 * Gibt den Vorlagen-Stundenplan zurück.
	 *
	 * @return den Vorlagen-Stundenplan.
	 * @throws DeveloperNotificationException wenn kein Vorlagen-Stundenplan gesetzt ist.
	 */
	public getStundenplanVorlage() : StundenplanListeEintrag {
		if (this._stundenplanVorlage === null)
			throw new DeveloperNotificationException("Kein Vorlagen-Stundenplan gesetzt.")
		return this._stundenplanVorlage;
	}

	/**
	 * Überprüft, ob es sich beim ausgewählten Stundenplan um die Vorlage handelt.
	 *
	 * @return <code>true</code> wenn es sich um die Vorlage handelt, ansonsten <code>false</code>
	 */
	public auswahlIsVorlage() : boolean {
		return this.hasDaten() && this.auswahl() === this._stundenplanVorlage;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.stundenplan.StundenplanListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.stundenplan.StundenplanListeManager'].includes(name);
	}

	public static class = new Class<StundenplanListeManager>('de.svws_nrw.core.utils.stundenplan.StundenplanListeManager');

}

export function cast_de_svws_nrw_core_utils_stundenplan_StundenplanListeManager(obj : unknown) : StundenplanListeManager {
	return obj as StundenplanListeManager;
}
