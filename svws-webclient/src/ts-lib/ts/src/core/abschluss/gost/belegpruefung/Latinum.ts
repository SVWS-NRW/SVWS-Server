import { JavaObject, cast_java_lang_Object } from '../../../../java/lang/JavaObject';
import { SprachendatenManager, cast_de_nrw_schule_svws_core_utils_schueler_SprachendatenManager } from '../../../../core/utils/schueler/SprachendatenManager';
import { AbiturFachbelegung, cast_de_nrw_schule_svws_core_data_gost_AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostHalbjahr, cast_de_nrw_schule_svws_core_types_gost_GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { GostBelegpruefung, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager, cast_de_nrw_schule_svws_core_abschluss_gost_AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class Latinum extends GostBelegpruefung {

	private latein : AbiturFachbelegung | null = null;


	/**
	 * Erstellt eine neue Belegprüfung in Bezug auf den Erwerb des Latinums.
	 * 
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungs_art : GostBelegpruefungsArt) {
		super(manager, pruefungs_art);
	}

	protected init() : void {
		this.latein = this.manager.getSprachbelegung("L");
	}

	protected pruefeEF1() : void {
		if (SprachendatenManager.hatSprachbelegungInSekI(this.manager.getSprachendaten(), "L") && (!this.manager.pruefeBelegung(this.latein, GostHalbjahr.EF1))) 
			this.addFehler(GostBelegungsfehler.L_10_INFO);
	}

	protected pruefeGesamt() : void {
		if (!SprachendatenManager.hatSprachbelegungInSekI(this.manager.getSprachendaten(), "L")) {
			return;
		}
		if (SprachendatenManager.hatSprachbelegungInSekIMitDauer(this.manager.getSprachendaten(), "L", 4)) {
			if (!this.manager.pruefeBelegung(this.latein, GostHalbjahr.EF1, GostHalbjahr.EF2)) {
				this.addFehler(GostBelegungsfehler.L_10_INFO);
			}
			return;
		}
		if (SprachendatenManager.hatSprachbelegungInSekIMitDauer(this.manager.getSprachendaten(), "L", 2)) {
			if (!this.manager.pruefeBelegung(this.latein, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)) 
				this.addFehler(GostBelegungsfehler.L_11_INFO);
		}
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.gost.belegpruefung.Latinum', 'de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefung'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_gost_belegpruefung_Latinum(obj : unknown) : Latinum {
	return obj as Latinum;
}
