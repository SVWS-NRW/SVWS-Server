import { JavaObject, cast_java_lang_Object } from '../../../../java/lang/JavaObject';
import { GostFachbereich, cast_de_nrw_schule_svws_core_types_gost_GostFachbereich } from '../../../../core/types/gost/GostFachbereich';
import { AbiturFachbelegung, cast_de_nrw_schule_svws_core_data_gost_AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostSchriftlichkeit, cast_de_nrw_schule_svws_core_types_gost_GostSchriftlichkeit } from '../../../../core/types/gost/GostSchriftlichkeit';
import { GostHalbjahr, cast_de_nrw_schule_svws_core_types_gost_GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { GostBelegpruefung, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager, cast_de_nrw_schule_svws_core_abschluss_gost_AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class Mathematik extends GostBelegpruefung {

	private mathematik : AbiturFachbelegung | null = null;


	/**
	 * Erstellt eine neue Belegprüfung für das Fach Mathematik.
	 * 
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungs_art : GostBelegpruefungsArt) {
		super(manager, pruefungs_art);
	}

	protected init() : void {
		this.mathematik = this.manager.getFachbelegung(GostFachbereich.MATHEMATIK);
	}

	protected pruefeEF1() : void {
		if ((this.mathematik === null) || !this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(this.mathematik, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1)) {
			this.addFehler(GostBelegungsfehler.M_10);
			return;
		}
		if (!this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(this.mathematik, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) 
			this.addFehler(GostBelegungsfehler.M_11);
	}

	protected pruefeGesamt() : void {
		if (this.mathematik === null) {
			this.addFehler(GostBelegungsfehler.M_10);
			return;
		}
		if (!this.manager.pruefeBelegung(this.mathematik, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)) 
			this.addFehler(GostBelegungsfehler.M_10);
		if (!this.manager.pruefeBelegungMitSchriftlichkeit(this.mathematik, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21)) 
			this.addFehler(GostBelegungsfehler.M_11);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.gost.belegpruefung.Mathematik', 'de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefung'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_gost_belegpruefung_Mathematik(obj : unknown) : Mathematik {
	return obj as Mathematik;
}
