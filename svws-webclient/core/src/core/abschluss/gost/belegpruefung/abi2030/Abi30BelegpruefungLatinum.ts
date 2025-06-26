import { AbiturFachbelegung } from '../../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt } from '../../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostHalbjahr } from '../../../../../core/types/gost/GostHalbjahr';
import { Class } from '../../../../../java/lang/Class';
import { GostBelegpruefung } from '../../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../../core/abschluss/gost/AbiturdatenManager';
import { SprachendatenUtils } from '../../../../../core/utils/schueler/SprachendatenUtils';
import { GostBelegungsfehler } from '../../../../../core/abschluss/gost/GostBelegungsfehler';

export class Abi30BelegpruefungLatinum extends GostBelegpruefung {

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
		if (SprachendatenUtils.hatSprachbelegungMitMin2JahrenDauerEndeSekI(this.manager.getSprachendaten(), "L") && (!this.manager.pruefeBelegung(this.latein, GostHalbjahr.EF1)))
			this.addFehler(GostBelegungsfehler.L_10_INFO);
	}

	protected pruefeGesamt() : void {
		if (SprachendatenUtils.hatSprachbelegungMitMin2JahrenDauerEndeSekI(this.manager.getSprachendaten(), "L")) {
			if (SprachendatenUtils.hatSprachbelegungMitMin4JahrenDauerEndeSekI(this.manager.getSprachendaten(), "L")) {
				if (!this.manager.pruefeBelegung(this.latein, GostHalbjahr.EF1, GostHalbjahr.EF2))
					this.addFehler(GostBelegungsfehler.L_10_INFO);
			} else {
				if (!this.manager.pruefeBelegung(this.latein, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
					this.addFehler(GostBelegungsfehler.L_11_INFO);
			}
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungLatinum';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungLatinum'].includes(name);
	}

	public static class = new Class<Abi30BelegpruefungLatinum>('de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungLatinum');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungLatinum(obj : unknown) : Abi30BelegpruefungLatinum {
	return obj as Abi30BelegpruefungLatinum;
}
