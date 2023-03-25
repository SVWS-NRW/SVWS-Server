import { GostFachbereich } from '../../../../core/types/gost/GostFachbereich';
import { GostAbiturFach } from '../../../../core/types/gost/GostAbiturFach';
import { AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { GostSchriftlichkeit } from '../../../../core/types/gost/GostSchriftlichkeit';
import { List } from '../../../../java/util/List';
import { GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class Allgemeines extends GostBelegpruefung {


	/**
	 * Erstellt eine neue allgemeine Belegprüfung.
	 *
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungs_art : GostBelegpruefungsArt) {
		super(manager, pruefungs_art);
	}

	protected init() : void {
		// empty block
	}

	protected pruefeEF1() : void {
		if (this.manager.zaehleBelegungInHalbjahren(this.manager.getFachbelegungen(GostFachbereich.RELIGION), GostHalbjahr.EF1) > 1)
			this.addFehler(GostBelegungsfehler.IGF_10);
		if (this.manager.hatDoppelteFachbelegungInHalbjahr(GostHalbjahr.EF1))
			this.addFehler(GostBelegungsfehler.IGF_10);
	}

	protected pruefeGesamt() : void {
		const alleFachbelegungen : List<AbiturFachbelegung> = this.manager.getFachbelegungen();
		for (let i : number = 0; i < alleFachbelegungen.size(); i++) {
			const fachbelegung : AbiturFachbelegung | null = alleFachbelegungen.get(i);
			if (!this.manager.istBelegtSeitEF(fachbelegung))
				this.addFehler(GostBelegungsfehler.E1BEL_10);
		}
		for (let i : number = 0; i < alleFachbelegungen.size(); i++) {
			const fachbelegung : AbiturFachbelegung | null = alleFachbelegungen.get(i);
			const abiturFach : GostAbiturFach | null = GostAbiturFach.fromID(fachbelegung.abiturFach);
			if (abiturFach !== null)
				continue;
			if (this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q22)) {
				this.addFehler(GostBelegungsfehler.ABI_16);
				break;
			}
		}
		for (const halbjahr of GostHalbjahr.values()) {
			if (this.manager.zaehleBelegungInHalbjahren(this.manager.getFachbelegungen(GostFachbereich.RELIGION), halbjahr) > 1)
				this.addFehler(GostBelegungsfehler.IGF_10);
		}
		if (this.manager.hatDoppelteFachbelegung(GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			this.addFehler(GostBelegungsfehler.IGF_10);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.Allgemeines'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Allgemeines(obj : unknown) : Allgemeines {
	return obj as Allgemeines;
}
