import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { Logger, cast_de_svws_nrw_core_logger_Logger } from '../../../../core/logger/Logger';
import { GostSchuelerklausurTermin } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTermin';
import { GostKlausurplanManager } from '../../../../core/utils/gost/klausurplanung/GostKlausurplanManager';
import { GostSchuelerklausur } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausur';
import { KlausurblockungNachschreiberAlgorithmusBewertung } from '../../../../core/utils/gost/klausurplanung/KlausurblockungNachschreiberAlgorithmusBewertung';
import { System } from '../../../../java/lang/System';
import { Random } from '../../../../java/util/Random';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { GostNachschreibterminblockungKonfiguration } from '../../../../core/data/gost/klausurplanung/GostNachschreibterminblockungKonfiguration';
import { ListUtils } from '../../../../core/utils/ListUtils';
import { GostKlausurtermin } from '../../../../core/data/gost/klausurplanung/GostKlausurtermin';
import { Pair } from '../../../../asd/adt/Pair';
import { HashSet } from '../../../../java/util/HashSet';

export class KlausurblockungNachschreiberAlgorithmus extends JavaObject {

	private static readonly _random : Random = new Random();

	/**
	 * Ein Logger für Debug-Zwecke.
	 */
	private readonly _logger : Logger;


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
		if ((__param0 === undefined)) {
			this._logger = new Logger();
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.logger.Logger'))))) {
			const pLogger : Logger = cast_de_svws_nrw_core_logger_Logger(__param0);
			this._logger = pLogger;
		} else throw new Error('invalid method overload');
	}

	/**
	 * @param config   		  Die Konfiguration
	 * @param klausurManager  Der Kursklausur-Manager.
	 *
	 * @return Eine Liste von Paaren: 1. Element = GostSchuelerklausurtermin (Nachschreiber), 2. Element = ID des Termins / der Schiene
	 */
	public berechne(config : GostNachschreibterminblockungKonfiguration, klausurManager : GostKlausurplanManager) : List<Pair<GostSchuelerklausurTermin, number>> {
		const nachschreiberGruppen : List<List<GostSchuelerklausurTermin>> = new ArrayList<List<GostSchuelerklausurTermin>>();
		for (const skt of config.schuelerklausurtermine) {
			const sk : GostSchuelerklausur | null = klausurManager.schuelerklausurBySchuelerklausurtermin(skt);
			const idSchueler : number = sk.idSchueler;
			const idKurs : number = sk.idKursklausur;
			DeveloperNotificationException.ifTrue("Ungültige Schüler-ID = " + idSchueler, idSchueler < 0);
			DeveloperNotificationException.ifTrue("Ungültige Kurs-ID = " + idKurs, idKurs < 0);
			let added : boolean = false;
			for (const gruppe of nachschreiberGruppen)
				if (KlausurblockungNachschreiberAlgorithmus._istHinzufuegenErlaubt(gruppe, skt, config, klausurManager)) {
					gruppe.add(skt);
					added = true;
					break;
				}
			if (!added)
				nachschreiberGruppen.add(ListUtils.create1(skt));
		}
		const zeitEnde : number = System.currentTimeMillis() + config.maxTimeMillis;
		let bestBewertung : KlausurblockungNachschreiberAlgorithmusBewertung = new KlausurblockungNachschreiberAlgorithmusBewertung();
		let bestErgebnis : List<Pair<GostSchuelerklausurTermin, number>> = KlausurblockungNachschreiberAlgorithmus._algorithmusProTerminZufaelligGruppenVerteilenZufaellig(bestBewertung, config.termine, nachschreiberGruppen, klausurManager);
		while (System.currentTimeMillis() < zeitEnde) {
			const bewertung : KlausurblockungNachschreiberAlgorithmusBewertung | null = new KlausurblockungNachschreiberAlgorithmusBewertung();
			const ergebnis : List<Pair<GostSchuelerklausurTermin, number>> = KlausurblockungNachschreiberAlgorithmus._algorithmusProTerminZufaelligGruppenVerteilenZufaellig(bewertung, config.termine, nachschreiberGruppen, klausurManager);
			if (bewertung.compare(bestBewertung) < 0) {
				bestBewertung = bewertung;
				bestErgebnis = ergebnis;
			}
		}
		return bestErgebnis;
	}

	private static _istHinzufuegenErlaubt(gruppe : List<GostSchuelerklausurTermin>, skt1 : GostSchuelerklausurTermin, config : GostNachschreibterminblockungKonfiguration, klausurManager : GostKlausurplanManager) : boolean {
		DeveloperNotificationException.ifTrue("Die Gruppe muss mindestens ein Element enthalten!", gruppe.isEmpty());
		const sk1 : GostSchuelerklausur = klausurManager.schuelerklausurBySchuelerklausurtermin(skt1);
		const idFach : number = klausurManager.vorgabeBySchuelerklausurTermin(skt1).idFach;
		const kursart : string = klausurManager.vorgabeBySchuelerklausurTermin(skt1).kursart;
		for (const skt2 of gruppe) {
			const sk2 : GostSchuelerklausur = klausurManager.schuelerklausurBySchuelerklausurtermin(skt2);
			if (sk1.idSchueler === sk2.idSchueler)
				return false;
		}
		if (config._regel_gleiche_fachart_auf_selbe_termine_verteilen) {
			const first : GostSchuelerklausurTermin = ListUtils.getNonNullElementAtOrException(gruppe, 0);
			const fachGleich : boolean = klausurManager.vorgabeBySchuelerklausurTermin(first).idFach === idFach;
			const kursartGleich : boolean = JavaObject.equalsTranspiler(klausurManager.vorgabeBySchuelerklausurTermin(first).kursart, (kursart));
			return fachGleich && kursartGleich;
		}
		if (config._regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen) {
			const first : GostSchuelerklausurTermin = ListUtils.getNonNullElementAtOrException(gruppe, 0);
			const sk2 : GostSchuelerklausur = klausurManager.schuelerklausurBySchuelerklausurtermin(first);
			return (sk1.idKursklausur === sk2.idKursklausur);
		}
		return false;
	}

	private static _algorithmusProTerminZufaelligGruppenVerteilenZufaellig(bewertung : KlausurblockungNachschreiberAlgorithmusBewertung, termine : List<GostKlausurtermin>, nachschreiberGruppen : List<List<GostSchuelerklausurTermin>>, klausurManager : GostKlausurplanManager) : List<Pair<GostSchuelerklausurTermin, number>> {
		const ergebnis : List<Pair<GostSchuelerklausurTermin, number>> = new ArrayList<Pair<GostSchuelerklausurTermin, number>>();
		const gruppen : List<List<GostSchuelerklausurTermin>> = new ArrayList<List<GostSchuelerklausurTermin>>(nachschreiberGruppen);
		for (const termin of ListUtils.getCopyPermuted(termine, KlausurblockungNachschreiberAlgorithmus._random)) {
			const gruppenanzahl : number = gruppen.size();
			KlausurblockungNachschreiberAlgorithmus._verteileMoeglichstVieleGruppenZufaelligAufDenTermin(termin.id, klausurManager, gruppen, ergebnis);
			if (gruppen.size() < gruppenanzahl)
				bewertung.anzahl_termine++;
		}
		let fakeID : number = -1;
		while (!gruppen.isEmpty()) {
			KlausurblockungNachschreiberAlgorithmus._verteileMoeglichstVieleGruppenZufaelligAufDenTermin(fakeID, klausurManager, gruppen, ergebnis);
			fakeID--;
			bewertung.anzahl_zusatztermine++;
		}
		return ergebnis;
	}

	private static _verteileMoeglichstVieleGruppenZufaelligAufDenTermin(idTermin : number, klausurManager : GostKlausurplanManager, gruppen : List<List<GostSchuelerklausurTermin>>, ergebnis : List<Pair<GostSchuelerklausurTermin, number>>) : void {
		const schuelerIDsDesTermin : HashSet<number> | null = new HashSet<number>();
		if (idTermin >= 0) {
			const termin : GostKlausurtermin = klausurManager.terminGetByIdOrException(idTermin);
			for (const sk of klausurManager.schuelerklausurGetMengeByTermin(termin))
				schuelerIDsDesTermin.add(sk.idSchueler);
		}
		for (const gruppe of ListUtils.getCopyPermuted(gruppen, KlausurblockungNachschreiberAlgorithmus._random)) {
			let kollision : boolean = false;
			const schuelerIDsDerGruppe : List<number> = new ArrayList<number>();
			for (const skt of gruppe) {
				const sk : GostSchuelerklausur = klausurManager.schuelerklausurBySchuelerklausurtermin(skt);
				schuelerIDsDerGruppe.add(sk.idSchueler);
				kollision = kollision || schuelerIDsDesTermin.contains(sk.idSchueler);
			}
			if (!kollision) {
				for (const skt of gruppe)
					ergebnis.add(new Pair<GostSchuelerklausurTermin, number>(skt, idTermin));
				schuelerIDsDesTermin.addAll(schuelerIDsDerGruppe);
				gruppen.remove(gruppe);
			}
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungNachschreiberAlgorithmus';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungNachschreiberAlgorithmus'].includes(name);
	}

	public static class = new Class<KlausurblockungNachschreiberAlgorithmus>('de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungNachschreiberAlgorithmus');

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_KlausurblockungNachschreiberAlgorithmus(obj : unknown) : KlausurblockungNachschreiberAlgorithmus {
	return obj as KlausurblockungNachschreiberAlgorithmus;
}
