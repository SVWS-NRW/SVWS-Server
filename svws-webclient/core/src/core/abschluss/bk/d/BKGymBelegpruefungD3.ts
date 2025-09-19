import { BKGymBelegungsfehler } from '../../../../core/abschluss/bk/d/BKGymBelegungsfehler';
import { BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit';
import { BKGymAbiturdatenManager } from '../../../../core/abschluss/bk/d/BKGymAbiturdatenManager';
import { GostAbiturFach } from '../../../../core/types/gost/GostAbiturFach';
import { BKGymBelegpruefung, cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegpruefung } from '../../../../core/abschluss/bk/d/BKGymBelegpruefung';
import { BeruflichesGymnasiumPruefungsordnungAnlage } from '../../../../asd/types/schule/BeruflichesGymnasiumPruefungsordnungAnlage';
import { BKGymAbiturFachbelegung } from '../../../../core/abschluss/bk/d/BKGymAbiturFachbelegung';
import type { List } from '../../../../java/util/List';
import { BeruflichesGymnasiumStundentafel } from '../../../../asd/data/schule/BeruflichesGymnasiumStundentafel';
import { Class } from '../../../../java/lang/Class';
import type { JavaMap } from '../../../../java/util/JavaMap';

export class BKGymBelegpruefungD3 extends BKGymBelegpruefung {


	/**
	 * Erzeugt einen neue Belegprüfung
	 *
	 * @param manager   der Manager für die Abiturdaten
	 */
	public constructor(manager : BKGymAbiturdatenManager) {
		super(manager);
	}

	public pruefe() : void {
		const lk1 : BKGymAbiturFachbelegung | null = this.manager.getAbiFachbelegung(GostAbiturFach.LK1);
		if (lk1 === null)
			this.addFehler(BKGymBelegungsfehler.LK_1);
		const lk2 : BKGymAbiturFachbelegung | null = this.manager.getAbiFachbelegung(GostAbiturFach.LK2);
		if (lk2 === null)
			this.addFehler(BKGymBelegungsfehler.LK_2);
		const mglStundentafeln : List<BeruflichesGymnasiumStundentafel> = this.manager.getStundentafelByLeistungskurse(BeruflichesGymnasiumPruefungsordnungAnlage.D3);
		if (mglStundentafeln.isEmpty())
			this.addFehler(BKGymBelegungsfehler.LK_3);
		const ab3 : BKGymAbiturFachbelegung | null = this.manager.getAbiFachbelegung(GostAbiturFach.AB3);
		if (ab3 === null)
			this.addFehler(BKGymBelegungsfehler.AB_3);
		const ab4 : BKGymAbiturFachbelegung | null = this.manager.getAbiFachbelegung(GostAbiturFach.AB4);
		if (ab4 === null)
			this.addFehler(BKGymBelegungsfehler.AB_4);
		if (mglStundentafeln.isEmpty())
			return;
		const mapWahlmoeglichkeiten : JavaMap<BeruflichesGymnasiumStundentafel, BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit> = this.manager.getWahlmoeglichekiten(mglStundentafeln);
		if (mapWahlmoeglichkeiten.isEmpty()) {
			this.addFehler(BKGymBelegungsfehler.AB_5);
			return;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungD3';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungD3', 'de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefung'].includes(name);
	}

	public static class = new Class<BKGymBelegpruefungD3>('de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungD3');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegpruefungD3(obj : unknown) : BKGymBelegpruefungD3 {
	return obj as BKGymBelegpruefungD3;
}
