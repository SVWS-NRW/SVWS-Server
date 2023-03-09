import { JavaObject, cast_java_lang_Object } from '../../../../java/lang/JavaObject';
import { GostFachbereich, cast_de_nrw_schule_svws_core_types_gost_GostFachbereich } from '../../../../core/types/gost/GostFachbereich';
import { AbiturFachbelegung, cast_de_nrw_schule_svws_core_data_gost_AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostHalbjahr, cast_de_nrw_schule_svws_core_types_gost_GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { List, cast_java_util_List } from '../../../../java/util/List';
import { GostBelegpruefung, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager, cast_de_nrw_schule_svws_core_abschluss_gost_AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { Vector, cast_java_util_Vector } from '../../../../java/util/Vector';
import { GostBelegungsfehler, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class Sport extends GostBelegpruefung {

	private sport : List<AbiturFachbelegung> = new Vector();


	/**
	 * Erstellt eine neue Belegprüfung für das Fach Sport.
	 * 
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungs_art : GostBelegpruefungsArt) {
		super(manager, pruefungs_art);
	}

	protected init() : void {
		this.sport = this.manager.getFachbelegungen(GostFachbereich.SPORT);
	}

	protected pruefeEF1() : void {
		if ((this.sport === null) || (!this.manager.pruefeBelegungExistiertEinzeln(this.sport, GostHalbjahr.EF1))) {
			this.addFehler(GostBelegungsfehler.SP_10);
			return;
		}
	}

	protected pruefeGesamt() : void {
		if ((this.sport === null) || (!this.manager.pruefeBelegungExistiert(this.sport, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))) 
			this.addFehler(GostBelegungsfehler.SP_10);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefung', 'de.nrw.schule.svws.core.abschluss.gost.belegpruefung.Sport'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_gost_belegpruefung_Sport(obj : unknown) : Sport {
	return obj as Sport;
}
