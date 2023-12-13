import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostFach } from '../../../../core/data/gost/GostFach';
import { AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { AbiturFachbelegungHalbjahr } from '../../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class FachWaehlbar extends GostBelegpruefung {


	/**
	 * Erstellt eine neue Belegprüfung für die schul-spezifische Wählbarkeit von Fächern.
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

	private pruefeFachbelegungHalbjahr(fach : GostFach, fachbelegung : AbiturFachbelegung, halbjahr : GostHalbjahr) : void {
		const fbHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
		if (fbHalbjahr === null)
			return;
		let istwaehlbar : boolean;
		const _seexpr_358622684 = (halbjahr);
		if (_seexpr_358622684 === GostHalbjahr.EF1) {
			istwaehlbar = fach.istMoeglichEF1;
		} else if (_seexpr_358622684 === GostHalbjahr.EF2) {
			istwaehlbar = fach.istMoeglichEF2;
		} else if (_seexpr_358622684 === GostHalbjahr.Q11) {
			istwaehlbar = fach.istMoeglichQ11;
		} else if (_seexpr_358622684 === GostHalbjahr.Q12) {
			istwaehlbar = fach.istMoeglichQ12;
		} else if (_seexpr_358622684 === GostHalbjahr.Q21) {
			istwaehlbar = fach.istMoeglichQ21;
		} else if (_seexpr_358622684 === GostHalbjahr.Q22) {
			istwaehlbar = fach.istMoeglichQ22;
		} else {
			istwaehlbar = false;
		}
		;
		if (!istwaehlbar)
			this.addFehler(GostBelegungsfehler.WAEHLBARKEIT_1);
	}

	private static hatLKFachbelegung(fachbelegung : AbiturFachbelegung) : boolean {
		for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
			const fbHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if ((fbHalbjahr !== null) && JavaObject.equalsTranspiler("LK", (fbHalbjahr.kursartKuerzel)))
				return true;
		}
		return false;
	}

	private pruefeFachbelegungAbitur(fach : GostFach, fachbelegung : AbiturFachbelegung) : void {
		if (fachbelegung.abiturFach === null)
			return;
		if ((!fach.istMoeglichAbiLK) && ((fachbelegung.abiturFach === 1) || (fachbelegung.abiturFach === 2) || (FachWaehlbar.hatLKFachbelegung(fachbelegung))))
			this.addFehler(GostBelegungsfehler.WAEHLBARKEIT_3);
		if ((!fach.istMoeglichAbiGK) && ((fachbelegung.abiturFach === 3) || (fachbelegung.abiturFach === 4)))
			this.addFehler(GostBelegungsfehler.WAEHLBARKEIT_2);
	}

	protected pruefeEF1() : void {
		for (const fachbelegung of this.manager.getFachbelegungen()) {
			const fach : GostFach | null = this.manager.faecher().get(fachbelegung.fachID);
			if (fach === null) {
				this.addFehler(GostBelegungsfehler.WAEHLBARKEIT_0);
				continue;
			}
			this.pruefeFachbelegungHalbjahr(fach, fachbelegung, GostHalbjahr.EF1);
		}
	}

	protected pruefeGesamt() : void {
		for (const fachbelegung of this.manager.getFachbelegungen()) {
			const fach : GostFach | null = this.manager.faecher().get(fachbelegung.fachID);
			if (fach === null) {
				this.addFehler(GostBelegungsfehler.WAEHLBARKEIT_0);
				continue;
			}
			for (const halbjahr of GostHalbjahr.values())
				this.pruefeFachbelegungHalbjahr(fach, fachbelegung, halbjahr);
			this.pruefeFachbelegungAbitur(fach, fachbelegung);
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.FachWaehlbar';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.belegpruefung.FachWaehlbar', 'de.svws_nrw.core.abschluss.gost.GostBelegpruefung'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_FachWaehlbar(obj : unknown) : FachWaehlbar {
	return obj as FachWaehlbar;
}
