import { AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { SprachendatenUtils } from '../../../../core/utils/schueler/SprachendatenUtils';
import { GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class Latinum extends GostBelegpruefung {

	private latein : AbiturFachbelegung | null = null;


	/**
	 * Erstellt eine neue Belegprüfung in Bezug auf den Erwerb des Latinums.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungsArt : GostBelegpruefungsArt) {
		super(manager, pruefungsArt);
	}

	protected init() : void {
		this.latein = this.manager.getSprachbelegung("L");
	}

	protected pruefeEF1() : void {
		if (SprachendatenUtils.hatSprachbelegungInSekI(this.manager.getSprachendaten(), "L") && (!this.manager.pruefeBelegung(this.latein, GostHalbjahr.EF1)))
			this.addFehler(GostBelegungsfehler.L_10_INFO);
	}

	protected pruefeGesamt() : void {
		if (!SprachendatenUtils.hatSprachbelegungInSekI(this.manager.getSprachendaten(), "L"))
			return;
		if (SprachendatenUtils.hatSprachbelegungInSekIMitDauer(this.manager.getSprachendaten(), "L", 4)) {
			if (!this.manager.pruefeBelegung(this.latein, GostHalbjahr.EF1, GostHalbjahr.EF2))
				this.addFehler(GostBelegungsfehler.L_10_INFO);
			return;
		}
		if ((SprachendatenUtils.hatSprachbelegungInSekIMitDauer(this.manager.getSprachendaten(), "L", 2)) && (!this.manager.pruefeBelegung(this.latein, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)))
			this.addFehler(GostBelegungsfehler.L_11_INFO);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.belegpruefung.Latinum', 'de.svws_nrw.core.abschluss.gost.GostBelegpruefung'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Latinum(obj : unknown) : Latinum {
	return obj as Latinum;
}
