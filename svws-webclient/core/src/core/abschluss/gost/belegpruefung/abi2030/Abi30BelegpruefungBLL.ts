import { GostFach } from '../../../../../core/data/gost/GostFach';
import { GostAbiturFach } from '../../../../../core/types/gost/GostAbiturFach';
import { GostFachUtils } from '../../../../../core/utils/gost/GostFachUtils';
import { GostBelegpruefungsArt } from '../../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostBesondereLernleistung } from '../../../../../core/types/gost/GostBesondereLernleistung';
import { Class } from '../../../../../java/lang/Class';
import { GostBelegpruefung } from '../../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler } from '../../../../../core/abschluss/gost/GostBelegungsfehler';

export class Abi30BelegpruefungBLL extends GostBelegpruefung {


	/**
	 * Erstellt eine neue Belegprüfung für die besondere Lernleistung.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager: AbiturdatenManager, pruefungsArt: GostBelegpruefungsArt) {
		super(manager, pruefungsArt);
	}

	protected init(): void {
		// empty block
	}

	protected pruefeEF1(): void {
		// empty block
	}

	protected pruefeGesamt(): void {
		const istExtern: boolean = GostBesondereLernleistung.EXTERNE.is(this.manager.daten().besondereLernleistung);
		const istImProjektkurs: boolean = this.manager.istProjektKursBesondereLernleistung();
		if (!istExtern && !istImProjektkurs) {
			return;
		}
		let hatProjektkurs: boolean = false;
		let istProjektkursAbi5: boolean = false;
		for (const fachbelegung of this.manager.getRelevanteFachbelegungen()) {
			const fach: GostFach | null = this.manager.getFach(fachbelegung);
			if ((fach === null) || !GostFachUtils.istProjektkurs(fach)) {
				continue;
			}
			if (this.manager.zaehleBelegung(fachbelegung) > 0) {
				hatProjektkurs = true;
			}
			if (GostAbiturFach.fromID(fachbelegung.abiturFach) as unknown === GostAbiturFach.AB5 as unknown) {
				istProjektkursAbi5 = true;
			}
		}
		if (istExtern && istProjektkursAbi5) {
			this.addFehler(GostBelegungsfehler.GOST30_BLL_10);
		}
		if (istImProjektkurs && hatProjektkurs && !istProjektkursAbi5) {
			this.addFehler(GostBelegungsfehler.GOST30_BLL_11);
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungBLL';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungBLL'].includes(name);
	}

	public static readonly class = new Class<Abi30BelegpruefungBLL>('de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungBLL');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungBLL(obj: unknown): Abi30BelegpruefungBLL {
	return obj as Abi30BelegpruefungBLL;
}
