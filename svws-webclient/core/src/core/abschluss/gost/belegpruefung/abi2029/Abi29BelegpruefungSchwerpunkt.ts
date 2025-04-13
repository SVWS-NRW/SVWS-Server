import { Abi29BelegpruefungNaturwissenschaften, cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2029_Abi29BelegpruefungNaturwissenschaften } from '../../../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungNaturwissenschaften';
import { Abi29BelegpruefungFremdsprachen, cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2029_Abi29BelegpruefungFremdsprachen } from '../../../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungFremdsprachen';
import { GostBelegpruefungsArt } from '../../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { Class } from '../../../../../java/lang/Class';
import { GostBelegpruefung } from '../../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler } from '../../../../../core/abschluss/gost/GostBelegungsfehler';

export class Abi29BelegpruefungSchwerpunkt extends GostBelegpruefung {


	/**
	 * Erstellt eine neue Belegprüfung für den Schwerpunkt.
	 *
	 * @param manager            der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt       die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungSprachen   das Ergebnis für die Belegprüfung der Sprachen
	 * @param pruefungNawi       das Ergebnis für die Belegprüfung der Naturwissenschaften
	 */
	public constructor(manager : AbiturdatenManager, pruefungsArt : GostBelegpruefungsArt, pruefungSprachen : Abi29BelegpruefungFremdsprachen, pruefungNawi : Abi29BelegpruefungNaturwissenschaften) {
		super(manager, pruefungsArt, pruefungSprachen, pruefungNawi);
	}

	protected init() : void {
		// empty block
	}

	protected pruefeEF1() : void {
		const pruefung_sprachen : Abi29BelegpruefungFremdsprachen = (cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2029_Abi29BelegpruefungFremdsprachen(this.pruefungen_vorher[0]));
		const pruefung_nawi : Abi29BelegpruefungNaturwissenschaften = (cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2029_Abi29BelegpruefungNaturwissenschaften(this.pruefungen_vorher[1]));
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
		const pruefung_sprachen : Abi29BelegpruefungFremdsprachen = (cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2029_Abi29BelegpruefungFremdsprachen(this.pruefungen_vorher[0]));
		const pruefung_nawi : Abi29BelegpruefungNaturwissenschaften = (cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2029_Abi29BelegpruefungNaturwissenschaften(this.pruefungen_vorher[1]));
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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2029.Abi29BelegpruefungSchwerpunkt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2029.Abi29BelegpruefungSchwerpunkt'].includes(name);
	}

	public static class = new Class<Abi29BelegpruefungSchwerpunkt>('de.svws_nrw.core.abschluss.gost.belegpruefung.abi2029.Abi29BelegpruefungSchwerpunkt');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2029_Abi29BelegpruefungSchwerpunkt(obj : unknown) : Abi29BelegpruefungSchwerpunkt {
	return obj as Abi29BelegpruefungSchwerpunkt;
}
