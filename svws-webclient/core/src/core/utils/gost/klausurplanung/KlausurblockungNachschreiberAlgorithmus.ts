import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { Logger, cast_de_svws_nrw_core_logger_Logger } from '../../../../core/logger/Logger';
import { GostSchuelerklausurTermin } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTermin';
import { GostSchuelerklausur } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausur';
import { Random } from '../../../../java/util/Random';
import { KursblockungMatrix } from '../../../../core/kursblockung/KursblockungMatrix';
import { GostKursklausurManager } from '../../../../core/utils/gost/klausurplanung/GostKursklausurManager';
import type { List } from '../../../../java/util/List';
import { ListUtils } from '../../../../core/utils/ListUtils';
import { GostKlausurtermin } from '../../../../core/data/gost/klausurplanung/GostKlausurtermin';
import { Pair } from '../../../../core/adt/Pair';

export class KlausurblockungNachschreiberAlgorithmus extends JavaObject {

	private static readonly _random : Random = new Random();

	/**
	 * Ein Logger für Debug-Zwecke.
	 */
	private readonly _logger : Logger;

	private _regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen : boolean = false;


	/**
	 * Der Konstruktor.
	 */
	public constructor();

	/**
	 * Der Konstruktor.
	 *
	 * @param pLogger  Ein Logger für Debug-Zwecke.
	 */
	public constructor(pLogger : Logger);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : Logger) {
		super();
		if ((typeof __param0 === "undefined")) {
			this._logger = new Logger();
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.logger.Logger'))))) {
			const pLogger : Logger = cast_de_svws_nrw_core_logger_Logger(__param0);
			this._logger = pLogger;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Aktiviert/Deaktiviert die Regel. Falls TRUE, werden NachschreiberInnen der selben Klausur auf den selben Termin geblockt.
	 *
	 * @param b falls TRUE, wird die Regel angewandt.
	 */
	public set_regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen(b : boolean) : void {
		this._regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen = b;
	}

	/**
	 * @param nachschreiber   Alle "GostSchuelerklausurTermin", die auf die "GostKlausurtermin" verteilt werden sollen.
	 * @param termine         Alle "GostKlausurtermin", auf die potentiell Nachschreiber verteilt werden.
	 * @param klausurManager  Der Kursklausur-Manager.
	 * @param maxTimeMillis   Die maximal erlaubte Berechnungszeit (in Millisekunden).
	 *
	 * @return Eine Liste von Paaren: 1. Element = GostSchuelerklausurtermin (Nachschreiber), 2. Element = ID des Termins / der Schiene
	 */
	public berechne(nachschreiber : List<GostSchuelerklausurTermin>, termine : List<GostKlausurtermin>, klausurManager : GostKursklausurManager, maxTimeMillis : number) : List<Pair<GostSchuelerklausurTermin, number>> {
		const nachschreiberGruppen : List<List<GostSchuelerklausurTermin>> = new ArrayList();
		for (const skt of nachschreiber) {
			const sk : GostSchuelerklausur | null = klausurManager.schuelerklausurBySchuelerklausurtermin(skt);
			const idSchueler : number = sk.idSchueler;
			const idKurs : number = sk.idKursklausur;
			DeveloperNotificationException.ifTrue("Ungültige Schüler-ID = " + idSchueler, idSchueler < 0);
			DeveloperNotificationException.ifTrue("Ungültige Kurs-ID = " + idKurs, idKurs < 0);
			let added : boolean = false;
			for (const gruppe of nachschreiberGruppen)
				if (this.istHinzufuegenErlaubt(gruppe, skt, klausurManager)) {
					gruppe.add(skt);
					added = true;
					break;
				}
			if (!added)
				nachschreiberGruppen.add(ListUtils.create1(skt));
		}
		const rows : number = nachschreiberGruppen.size();
		const cols : number = termine.size();
		const matrix : KursblockungMatrix = new KursblockungMatrix(KlausurblockungNachschreiberAlgorithmus._random, rows, cols);
		const data : Array<Array<number>> = matrix.getMatrix();
		for (let r : number = 0; r < nachschreiberGruppen.size(); r++)
			for (let c : number = 0; c < termine.size(); c++)
				data[r][c] = KlausurblockungNachschreiberAlgorithmus.gibBewertung(nachschreiberGruppen.get(r), termine.get(c), klausurManager);
		const r2c : Array<number> | null = matrix.gibMaximalesBipartitesMatching(true);
		const ergebnis : List<Pair<GostSchuelerklausurTermin, number>> = new ArrayList();
		let dummyTerminID : number = -1;
		for (let r : number = 0; r < nachschreiberGruppen.size(); r++) {
			let idTermin : number = dummyTerminID;
			const c : number = r2c[r];
			if (c >= 0) {
				idTermin = termine.get(c).id;
			} else {
				dummyTerminID--;
			}
			for (const nachschreiberDerGruppe of nachschreiberGruppen.get(r))
				ergebnis.add(new Pair(nachschreiberDerGruppe, idTermin));
		}
		return ergebnis;
	}

	private static gibBewertung(nachschreiberGruppe : List<GostSchuelerklausurTermin>, termin : GostKlausurtermin, klausurManager : GostKursklausurManager) : number {
		for (const sk1 of klausurManager.schuelerklausurGetMengeByTerminid(termin.id)) {
			for (const skt of nachschreiberGruppe) {
				const sk2 : GostSchuelerklausur | null = klausurManager.schuelerklausurBySchuelerklausurtermin(skt);
				if (sk1.idSchueler === sk2.idSchueler)
					return 0;
			}
		}
		return 1;
	}

	private istHinzufuegenErlaubt(gruppe : List<GostSchuelerklausurTermin>, skt1 : GostSchuelerklausurTermin, klausurManager : GostKursklausurManager) : boolean {
		const sk1 : GostSchuelerklausur = klausurManager.schuelerklausurBySchuelerklausurtermin(skt1);
		for (const skt2 of gruppe) {
			const sk2 : GostSchuelerklausur = klausurManager.schuelerklausurBySchuelerklausurtermin(skt2);
			if (sk1.idSchueler === sk2.idSchueler)
				return false;
		}
		if (this._regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen) {
			const skt2 : GostSchuelerklausurTermin = ListUtils.getNonNullElementAtOrException(gruppe, 0);
			const sk2 : GostSchuelerklausur = klausurManager.schuelerklausurBySchuelerklausurtermin(skt2);
			return (sk1.idKursklausur === sk2.idKursklausur);
		}
		return false;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungNachschreiberAlgorithmus';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungNachschreiberAlgorithmus'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_KlausurblockungNachschreiberAlgorithmus(obj : unknown) : KlausurblockungNachschreiberAlgorithmus {
	return obj as KlausurblockungNachschreiberAlgorithmus;
}
