import { Naturwissenschaften, cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Naturwissenschaften } from '../../../../core/abschluss/gost/belegpruefung/Naturwissenschaften';
import { Fremdsprachen, cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Fremdsprachen } from '../../../../core/abschluss/gost/belegpruefung/Fremdsprachen';
import { GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { Class } from '../../../../java/lang/Class';
import { GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class Schwerpunkt extends GostBelegpruefung {

	private _hatSchwerpunktFremdsprachen : boolean = false;

	private _hatSchwerpunktNaturwissenschaften : boolean = false;


	/**
	 * Erstellt eine neue Belegprüfung für den Schwerpunkt.
	 *
	 * @param manager            der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt       die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungSprachen   das Ergebnis für die Belegprüfung der Sprachen
	 * @param pruefungNawi       das Ergebnis für die Belegprüfung der Naturwissenschaften
	 */
	public constructor(manager : AbiturdatenManager, pruefungsArt : GostBelegpruefungsArt, pruefungSprachen : Fremdsprachen, pruefungNawi : Naturwissenschaften) {
		super(manager, pruefungsArt, pruefungSprachen, pruefungNawi);
	}

	protected init() : void {
		// empty block
	}

	protected pruefeEF1() : void {
		const pruefung_sprachen : Fremdsprachen = (cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Fremdsprachen(this.pruefungen_vorher[0]));
		const pruefung_nawi : Naturwissenschaften = (cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Naturwissenschaften(this.pruefungen_vorher[1]));
		this._hatSchwerpunktFremdsprachen = (pruefung_sprachen.getAnzahlDurchgehendSchritflichBelegt() >= 2);
		this._hatSchwerpunktNaturwissenschaften = (pruefung_nawi.getAnzahlDurchgehendBelegt() >= 2) && (pruefung_nawi.getAnzahlDurchgehendSchritflichBelegt() >= 1);
		if (this._hatSchwerpunktFremdsprachen && (this._hatSchwerpunktNaturwissenschaften))
			return;
		if (this._hatSchwerpunktFremdsprachen) {
			this.addFehler(GostBelegungsfehler.NW_FS_12_INFO);
			return;
		}
		if (this._hatSchwerpunktNaturwissenschaften) {
			this.addFehler(GostBelegungsfehler.NW_FS_13_INFO);
			return;
		}
		this.addFehler(GostBelegungsfehler.NW_FS_10);
	}

	protected pruefeGesamt() : void {
		const pruefung_sprachen : Fremdsprachen = (cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Fremdsprachen(this.pruefungen_vorher[0]));
		const pruefung_nawi : Naturwissenschaften = (cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Naturwissenschaften(this.pruefungen_vorher[1]));
		this._hatSchwerpunktFremdsprachen = (pruefung_sprachen.getAnzahlDurchgehendSchritflichBelegt() >= 2);
		this._hatSchwerpunktNaturwissenschaften = (pruefung_nawi.getAnzahlDurchgehendBelegt() >= 2) && (pruefung_nawi.getAnzahlDurchgehendSchritflichBelegt() >= 1);
		if (this._hatSchwerpunktFremdsprachen && this._hatSchwerpunktNaturwissenschaften)
			return;
		if (this._hatSchwerpunktFremdsprachen) {
			this.addFehler(GostBelegungsfehler.NW_FS_12_INFO);
			return;
		}
		if (this._hatSchwerpunktNaturwissenschaften) {
			this.addFehler(GostBelegungsfehler.NW_FS_13_INFO);
			return;
		}
		this.addFehler(GostBelegungsfehler.NW_FS_10);
	}

	/**
	 * Gibt an, ob ein fremsprachlicher Schwerpunkt vorliegt oder nicht.
	 *
	 * @return true, wenn ein fremdsprachlicher Schwerpunkt vorliegt
	 */
	public hatSchwerpunktFremdsprachen() : boolean {
		return this._hatSchwerpunktFremdsprachen;
	}

	/**
	 * Gibt an, ob ein naturwissenschaftlicher Schwerpunkt vorliegt oder nicht.
	 *
	 * @return true, wenn ein naturwissenschaftlicher Schwerpunkt vorliegt
	 */
	public hatSchwerpunktNaturwissenschaften() : boolean {
		return this._hatSchwerpunktNaturwissenschaften;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.Schwerpunkt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.Schwerpunkt'].includes(name);
	}

	public static class = new Class<Schwerpunkt>('de.svws_nrw.core.abschluss.gost.belegpruefung.Schwerpunkt');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Schwerpunkt(obj : unknown) : Schwerpunkt {
	return obj as Schwerpunkt;
}
