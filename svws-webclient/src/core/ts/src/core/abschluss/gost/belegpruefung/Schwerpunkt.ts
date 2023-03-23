import { Naturwissenschaften, cast_de_nrw_schule_svws_core_abschluss_gost_belegpruefung_Naturwissenschaften } from '../../../../core/abschluss/gost/belegpruefung/Naturwissenschaften';
import { Fremdsprachen, cast_de_nrw_schule_svws_core_abschluss_gost_belegpruefung_Fremdsprachen } from '../../../../core/abschluss/gost/belegpruefung/Fremdsprachen';
import { GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class Schwerpunkt extends GostBelegpruefung {


	/**
	 * Erstellt eine neue Belegprüfung für den Schwerpunkt.
	 *
	 * @param manager             der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art       die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefung_sprachen   das Ergebnis für die Belegprüfung der Sprachen
	 * @param pruefung_nawi       das Ergebnis für die Belegprüfung der Naturwissenschaften
	 */
	public constructor(manager : AbiturdatenManager, pruefungs_art : GostBelegpruefungsArt, pruefung_sprachen : Fremdsprachen, pruefung_nawi : Naturwissenschaften) {
		super(manager, pruefungs_art, pruefung_sprachen, pruefung_nawi);
	}

	protected init() : void {
	}

	protected pruefeEF1() : void {
		const pruefung_sprachen : Fremdsprachen = (cast_de_nrw_schule_svws_core_abschluss_gost_belegpruefung_Fremdsprachen(this.pruefungen_vorher[0]));
		const pruefung_nawi : Naturwissenschaften = (cast_de_nrw_schule_svws_core_abschluss_gost_belegpruefung_Naturwissenschaften(this.pruefungen_vorher[1]));
		if ((pruefung_sprachen.getAnzahlDurchgehendSchritflichBelegt() >= 2) && (pruefung_nawi.getAnzahlDurchgehendBelegt() >= 2) && (pruefung_nawi.getAnzahlDurchgehendSchritflichBelegt() >= 1))
			return;
		if (pruefung_sprachen.getAnzahlDurchgehendSchritflichBelegt() >= 2) {
			this.addFehler(GostBelegungsfehler.NW_FS_12_INFO);
			return;
		}
		if ((pruefung_nawi.getAnzahlDurchgehendBelegt() >= 2) && (pruefung_nawi.getAnzahlDurchgehendSchritflichBelegt() >= 1)) {
			this.addFehler(GostBelegungsfehler.NW_FS_13_INFO);
			return;
		}
		this.addFehler(GostBelegungsfehler.NW_FS_10);
	}

	protected pruefeGesamt() : void {
		const pruefung_sprachen : Fremdsprachen = (cast_de_nrw_schule_svws_core_abschluss_gost_belegpruefung_Fremdsprachen(this.pruefungen_vorher[0]));
		const pruefung_nawi : Naturwissenschaften = (cast_de_nrw_schule_svws_core_abschluss_gost_belegpruefung_Naturwissenschaften(this.pruefungen_vorher[1]));
		if ((pruefung_sprachen.getAnzahlDurchgehendSchritflichBelegt() >= 2) && (pruefung_nawi.getAnzahlDurchgehendBelegt() >= 2) && (pruefung_nawi.getAnzahlDurchgehendSchritflichBelegt() >= 1))
			return;
		if (pruefung_sprachen.getAnzahlDurchgehendSchritflichBelegt() >= 2) {
			this.addFehler(GostBelegungsfehler.NW_FS_12_INFO);
			return;
		}
		if ((pruefung_nawi.getAnzahlDurchgehendBelegt() >= 2) && (pruefung_nawi.getAnzahlDurchgehendSchritflichBelegt() >= 1)) {
			this.addFehler(GostBelegungsfehler.NW_FS_13_INFO);
			return;
		}
		this.addFehler(GostBelegungsfehler.NW_FS_10);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefung', 'de.nrw.schule.svws.core.abschluss.gost.belegpruefung.Schwerpunkt'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_gost_belegpruefung_Schwerpunkt(obj : unknown) : Schwerpunkt {
	return obj as Schwerpunkt;
}
