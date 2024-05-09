import { JavaObject } from '../../../../java/lang/JavaObject';
import { Random } from '../../../../java/util/Random';
import { KlausurraumblockungAlgorithmusDynDaten } from '../../../../core/utils/gost/klausurplanung/KlausurraumblockungAlgorithmusDynDaten';
import { GostKlausurraumblockungKonfiguration } from '../../../../core/data/gost/klausurplanung/GostKlausurraumblockungKonfiguration';
import { System } from '../../../../java/lang/System';

export class KlausurraumblockungAlgorithmus extends JavaObject {

	private readonly random : Random;


	/**
	 * Konstruktor.
	 */
	constructor() {
		super();
		this.random = new Random();
	}

	/**
	 * Verteilt die Klausuren auf die zur Verfügung stehenden Räume.
	 * <br>Die Zuordnung ist im {@link GostKlausurraumRich#schuelerklausurterminIDs}-Objekt zu finden.
	 *
	 * <br>
	 * <br>Obligatorische Kriterien:
	 * <br>- Die Raumkapazität darf nicht überschritten werden
	 * <br>- Es dürfen nur Klausuren in einen Raum geblockt werden, die dieselbe Startzeit haben.
	 * <br>
	 * <br>Fakultative Kriterien:
	 * <br>- {@link GostKlausurraumblockungKonfiguration#_regel_forciere_selbe_klausurdauer_pro_raum}
	 * <br>- {@link GostKlausurraumblockungKonfiguration#_regel_forciere_selbe_kursklausur_im_selben_raum}
	 * <br>- {@link GostKlausurraumblockungKonfiguration#_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume}
	 * <br>- {@link GostKlausurraumblockungKonfiguration#_regel_optimiere_blocke_in_moeglichst_wenig_raeume}
	 *
	 * @param config   		  Die Konfiguration und die Eingabedaten.
	 */
	public berechne(config : GostKlausurraumblockungKonfiguration) : void {
		const dynDaten : KlausurraumblockungAlgorithmusDynDaten | null = new KlausurraumblockungAlgorithmusDynDaten(this.random, config);
		dynDaten.aktionKlausurenVerteilenAlgorithmus00_zufaellig();
		const zeitEnde : number = System.currentTimeMillis() + config.maxTimeMillis;
		do {
			dynDaten.aktionKlausurenVerteilenAlgorithmus00_zufaellig();
			dynDaten.aktionKlausurenVerteilenAlgorithmus01();
			dynDaten.aktionKlausurenVerteilenAlgorithmus02();
			dynDaten.aktionKlausurenVerteilenAlgorithmus03();
		} while (System.currentTimeMillis() < zeitEnde);
		dynDaten.aktionLadeGespeichertenZustand();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurraumblockungAlgorithmus';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.KlausurraumblockungAlgorithmus'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_KlausurraumblockungAlgorithmus(obj : unknown) : KlausurraumblockungAlgorithmus {
	return obj as KlausurraumblockungAlgorithmus;
}
