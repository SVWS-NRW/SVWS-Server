import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { KlausurblockungSchienenAlgorithmusAbstract, cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenAlgorithmusAbstract } from '../../core/klausurblockung/KlausurblockungSchienenAlgorithmusAbstract';
import { KlausurblockungSchienenDynDaten, cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenDynDaten } from '../../core/klausurblockung/KlausurblockungSchienenDynDaten';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';
import { Logger, cast_de_nrw_schule_svws_core_logger_Logger } from '../../core/logger/Logger';
import { System, cast_java_lang_System } from '../../java/lang/System';

export class KlausurblockungSchienenAlgorithmusGreedy1b extends KlausurblockungSchienenAlgorithmusAbstract {


	/**
	 *Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger   Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDaten Die aktuellen Blockungsdaten. 
	 */
	public constructor(pRandom : Random, pLogger : Logger, pDynDaten : KlausurblockungSchienenDynDaten) {
		super(pRandom, pLogger, pDynDaten);
	}

	public toString() : String {
		return "Klausuren zufällig & Schienen nacheinander";
	}

	public berechne(pZeitEnde : number) : void {
		this._dynDaten.aktionEntferneAllesFuelleSchienenNacheinandeAuf();
		this._dynDaten.aktionZustand1Speichern();
		while (System.currentTimeMillis() < pZeitEnde) {
			this._dynDaten.aktionEntferneAllesFuelleSchienenNacheinandeAuf();
			if (this._dynDaten.gibIstBesserAlsZustand1()) 
				this._dynDaten.aktionZustand1Speichern(); else 
				this._dynDaten.aktionZustand1Laden();
		}
		if (this._dynDaten.gibIstBesserAlsZustand2()) 
			this._dynDaten.aktionZustand2Speichern();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.klausurblockung.KlausurblockungSchienenAlgorithmusGreedy1b', 'de.nrw.schule.svws.core.klausurblockung.KlausurblockungSchienenAlgorithmusAbstract'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenAlgorithmusGreedy1b(obj : unknown) : KlausurblockungSchienenAlgorithmusGreedy1b {
	return obj as KlausurblockungSchienenAlgorithmusGreedy1b;
}
