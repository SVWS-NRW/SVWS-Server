import { GostFachbereich } from '../../../../core/types/gost/GostFachbereich';
import { AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { List } from '../../../../java/util/List';
import { GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { Vector } from '../../../../java/util/Vector';
import { GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

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
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.Sport'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Sport(obj : unknown) : Sport {
	return obj as Sport;
}
