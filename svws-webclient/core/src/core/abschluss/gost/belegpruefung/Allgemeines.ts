import { GostFachbereich } from '../../../../core/types/gost/GostFachbereich';
import { GostAbiturFach } from '../../../../core/types/gost/GostAbiturFach';
import { AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { GostSchriftlichkeit } from '../../../../core/types/gost/GostSchriftlichkeit';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class Allgemeines extends GostBelegpruefung {


	/**
	 * Erstellt eine neue allgemeine Belegprüfung.
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

	protected pruefeEF1() : void {
		if (this.manager.zaehleBelegungInHalbjahren(this.manager.getRelevanteFachbelegungen(GostFachbereich.RELIGION), GostHalbjahr.EF1) > 1)
			this.addFehler(GostBelegungsfehler.IGF_10);
		if (this.manager.hatDoppelteFachbelegungInHalbjahr(GostHalbjahr.EF1))
			this.addFehler(GostBelegungsfehler.IGF_10);
	}

	protected pruefeGesamt() : void {
		const alleFachbelegungen : List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen();
		for (const fachbelegung of alleFachbelegungen) {
			if (!this.manager.istBelegtSeitEF(fachbelegung))
				this.addFehler(GostBelegungsfehler.E1BEL_10);
		}
		for (const fachbelegung of alleFachbelegungen) {
			const abiturFach : GostAbiturFach | null = GostAbiturFach.fromID(fachbelegung.abiturFach);
			if (abiturFach !== null)
				continue;
			if (this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q22)) {
				this.addFehler(GostBelegungsfehler.ABI_16);
				break;
			}
		}
		for (const halbjahr of GostHalbjahr.values()) {
			if (this.manager.zaehleBelegungInHalbjahren(this.manager.getRelevanteFachbelegungen(GostFachbereich.RELIGION), halbjahr) > 1)
				this.addFehler(GostBelegungsfehler.IGF_10);
		}
		if (this.manager.hatDoppelteFachbelegung(GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			this.addFehler(GostBelegungsfehler.IGF_10);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.Allgemeines';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.Allgemeines'].includes(name);
	}

	public static class = new Class<Allgemeines>('de.svws_nrw.core.abschluss.gost.belegpruefung.Allgemeines');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Allgemeines(obj : unknown) : Allgemeines {
	return obj as Allgemeines;
}
