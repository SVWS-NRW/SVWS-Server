import { GostJahrgangFachkombination } from '../../../../core/data/gost/GostJahrgangFachkombination';
import { AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { AbiturFachbelegungHalbjahr } from '../../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { GostKursart } from '../../../../core/types/gost/GostKursart';
import { GostLaufbahnplanungFachkombinationTyp } from '../../../../core/types/gost/GostLaufbahnplanungFachkombinationTyp';
import { GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class Fachkombinationen extends GostBelegpruefung {


	/**
	 * Erstellt eine neue Belegprüfung für die schulspezifischen Fachkombinationen.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungsArt : GostBelegpruefungsArt) {
		super(manager, pruefungsArt);
	}

	protected init() : void {
		// empty block
	}

	private static pruefeHatBelegungFach2InHalbjahr(kombi : GostJahrgangFachkombination, belegung2 : AbiturFachbelegung | null, halbjahr : GostHalbjahr) : boolean {
		if (belegung2 === null)
			return false;
		const belegung2Halbjahr : AbiturFachbelegungHalbjahr | null = belegung2.belegungen[halbjahr.id];
		return ((belegung2Halbjahr !== null) && (!AbiturdatenManager.istNullPunkteBelegungInQPhase(belegung2Halbjahr)) && ((kombi.kursart2 === null) || GostKursart.fromKuerzel(belegung2Halbjahr.kursartKuerzel) as unknown === GostKursart.fromKuerzel(kombi.kursart2) as unknown));
	}

	private pruefeHatFachkombination(kombi : GostJahrgangFachkombination, ...halbjahre : Array<GostHalbjahr>) : void {
		const belegung1 : AbiturFachbelegung | null = this.manager.getFachbelegungByID(kombi.fachID1);
		if (belegung1 === null)
			return;
		const belegung2 : AbiturFachbelegung | null = this.manager.getFachbelegungByID(kombi.fachID2);
		for (const halbjahr of halbjahre) {
			const belegung1Halbjahr : AbiturFachbelegungHalbjahr | null = belegung1.belegungen[halbjahr.id];
			if ((belegung1Halbjahr === null) || (AbiturdatenManager.istNullPunkteBelegungInQPhase(belegung1Halbjahr)))
				continue;
			if ((kombi.kursart1 === null) || GostKursart.fromKuerzel(belegung1Halbjahr.kursartKuerzel) as unknown === GostKursart.fromKuerzel(kombi.kursart1) as unknown) {
				if ((kombi.typ === GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue()) && Fachkombinationen.pruefeHatBelegungFach2InHalbjahr(kombi, belegung2, halbjahr)) {
					this.addFehler(GostBelegungsfehler.KOMBI_1);
					return;
				} else
					if (kombi.typ === GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH.getValue() && !Fachkombinationen.pruefeHatBelegungFach2InHalbjahr(kombi, belegung2, halbjahr)) {
						this.addFehler(GostBelegungsfehler.KOMBI_2);
						return;
					}
			}
		}
	}

	protected pruefeEF1() : void {
		for (const kombi of this.manager.getFachkombinationenEF1())
			this.pruefeHatFachkombination(kombi, GostHalbjahr.EF1);
	}

	protected pruefeGesamt() : void {
		for (const kombi of this.manager.getFachkombinationenGesamt())
			this.pruefeHatFachkombination(kombi, ...GostHalbjahr.values());
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.Fachkombinationen';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.belegpruefung.Fachkombinationen', 'de.svws_nrw.core.abschluss.gost.GostBelegpruefung'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Fachkombinationen(obj : unknown) : Fachkombinationen {
	return obj as Fachkombinationen;
}
