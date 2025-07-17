import { Abi30BelegpruefungFremdsprachen, cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungFremdsprachen } from '../../../../../core/abschluss/gost/belegpruefung/abi2030/Abi30BelegpruefungFremdsprachen';
import { Abi30BelegpruefungNaturwissenschaften, cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungNaturwissenschaften } from '../../../../../core/abschluss/gost/belegpruefung/abi2030/Abi30BelegpruefungNaturwissenschaften';
import { GostBelegpruefungsArt } from '../../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { Class } from '../../../../../java/lang/Class';
import { GostBelegpruefung } from '../../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler } from '../../../../../core/abschluss/gost/GostBelegungsfehler';

export class Abi30BelegpruefungSchwerpunkt extends GostBelegpruefung {


	/**
	 * Erstellt eine neue Belegprüfung für den Schwerpunkt.
	 *
	 * @param manager            der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt       die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungSprachen   das Ergebnis für die Belegprüfung der Sprachen
	 * @param pruefungNawi       das Ergebnis für die Belegprüfung der Naturwissenschaften
	 */
	public constructor(manager : AbiturdatenManager, pruefungsArt : GostBelegpruefungsArt, pruefungSprachen : Abi30BelegpruefungFremdsprachen, pruefungNawi : Abi30BelegpruefungNaturwissenschaften) {
		super(manager, pruefungsArt, pruefungSprachen, pruefungNawi);
	}

	protected init() : void {
		// empty block
	}

	protected pruefeEF1() : void {
		const pruefung_sprachen : Abi30BelegpruefungFremdsprachen = (cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungFremdsprachen(this.pruefungen_vorher[0]));
		const pruefung_nawi : Abi30BelegpruefungNaturwissenschaften = (cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungNaturwissenschaften(this.pruefungen_vorher[1]));
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
		const pruefung_sprachen : Abi30BelegpruefungFremdsprachen = (cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungFremdsprachen(this.pruefungen_vorher[0]));
		const pruefung_nawi : Abi30BelegpruefungNaturwissenschaften = (cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungNaturwissenschaften(this.pruefungen_vorher[1]));
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
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungSchwerpunkt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungSchwerpunkt'].includes(name);
	}

	public static class = new Class<Abi30BelegpruefungSchwerpunkt>('de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungSchwerpunkt');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungSchwerpunkt(obj : unknown) : Abi30BelegpruefungSchwerpunkt {
	return obj as Abi30BelegpruefungSchwerpunkt;
}
