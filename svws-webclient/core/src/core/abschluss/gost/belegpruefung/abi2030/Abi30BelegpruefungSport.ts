import { GostFachbereich } from '../../../../../core/types/gost/GostFachbereich';
import { AbiturFachbelegung } from '../../../../../core/data/gost/AbiturFachbelegung';
import { ArrayList } from '../../../../../java/util/ArrayList';
import { GostBelegpruefungsArt } from '../../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostHalbjahr } from '../../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../../java/util/List';
import { Class } from '../../../../../java/lang/Class';
import { GostBelegpruefung } from '../../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler } from '../../../../../core/abschluss/gost/GostBelegungsfehler';

export class Abi30BelegpruefungSport extends GostBelegpruefung {

	private _sport : List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();


	/**
	 * Erstellt eine neue Belegprüfung für das Fach Sport.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungsArt : GostBelegpruefungsArt) {
		super(manager, pruefungsArt);
	}

	protected init() : void {
		this._sport = this.manager.getRelevanteFachbelegungen(GostFachbereich.SPORT);
	}

	protected pruefeEF1() : void {
		if ((this._sport === null) || (!this.manager.pruefeBelegungExistiertEinzeln(this._sport, GostHalbjahr.EF1)))
			this.addFehler(GostBelegungsfehler.SP_10);
	}

	protected pruefeGesamt() : void {
		if ((this._sport === null) || (!this.manager.pruefeBelegungExistiert(this._sport, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)))
			this.addFehler(GostBelegungsfehler.SP_10);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungSport';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungSport'].includes(name);
	}

	public static class = new Class<Abi30BelegpruefungSport>('de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungSport');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungSport(obj : unknown) : Abi30BelegpruefungSport {
	return obj as Abi30BelegpruefungSport;
}
