import { GostFachbereich } from '../../../../../core/types/gost/GostFachbereich';
import { AbiturFachbelegung } from '../../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt } from '../../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostSchriftlichkeit } from '../../../../../core/types/gost/GostSchriftlichkeit';
import { GostHalbjahr } from '../../../../../core/types/gost/GostHalbjahr';
import { Class } from '../../../../../java/lang/Class';
import { GostBelegpruefung } from '../../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler } from '../../../../../core/abschluss/gost/GostBelegungsfehler';

export class Abi30BelegpruefungMathematik extends GostBelegpruefung {

	private _mathematik : AbiturFachbelegung | null = null;


	/**
	 * Erstellt eine neue Belegprüfung für das Fach Mathematik.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungsArt : GostBelegpruefungsArt) {
		super(manager, pruefungsArt);
	}

	protected init() : void {
		this._mathematik = this.manager.getRelevanteFachbelegung(GostFachbereich.MATHEMATIK);
	}

	protected pruefeEF1() : void {
		if ((this._mathematik === null) || !this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(this._mathematik, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1)) {
			this.addFehler(GostBelegungsfehler.M_10);
			return;
		}
		if (!this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(this._mathematik, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
			this.addFehler(GostBelegungsfehler.M_11);
	}

	protected pruefeGesamt() : void {
		if (this._mathematik === null) {
			this.addFehler(GostBelegungsfehler.M_10);
			return;
		}
		if (!this.manager.pruefeBelegung(this._mathematik, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			this.addFehler(GostBelegungsfehler.M_10);
		if (!this.manager.pruefeBelegungMitSchriftlichkeit(this._mathematik, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21))
			this.addFehler(GostBelegungsfehler.M_11);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungMathematik';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungMathematik', 'de.svws_nrw.core.abschluss.gost.GostBelegpruefung'].includes(name);
	}

	public static class = new Class<Abi30BelegpruefungMathematik>('de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungMathematik');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungMathematik(obj : unknown) : Abi30BelegpruefungMathematik {
	return obj as Abi30BelegpruefungMathematik;
}
