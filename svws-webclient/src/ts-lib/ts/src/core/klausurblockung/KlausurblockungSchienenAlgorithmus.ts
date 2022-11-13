import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { KlausurblockungSchienenAlgorithmusGreedy3, cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenAlgorithmusGreedy3 } from '../../core/klausurblockung/KlausurblockungSchienenAlgorithmusGreedy3';
import { KlausurblockungSchienenDynDaten, cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenDynDaten } from '../../core/klausurblockung/KlausurblockungSchienenDynDaten';
import { KlausurblockungSchienenAlgorithmusGreedy2, cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenAlgorithmusGreedy2 } from '../../core/klausurblockung/KlausurblockungSchienenAlgorithmusGreedy2';
import { KlausurblockungSchienenAlgorithmusGreedy5, cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenAlgorithmusGreedy5 } from '../../core/klausurblockung/KlausurblockungSchienenAlgorithmusGreedy5';
import { KlausurblockungSchienenAlgorithmusGreedy4, cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenAlgorithmusGreedy4 } from '../../core/klausurblockung/KlausurblockungSchienenAlgorithmusGreedy4';
import { KlausurblockungSchienenInput, cast_de_nrw_schule_svws_core_data_klausurblockung_KlausurblockungSchienenInput } from '../../core/data/klausurblockung/KlausurblockungSchienenInput';
import { KlausurblockungSchienenAlgorithmusGreedy1, cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenAlgorithmusGreedy1 } from '../../core/klausurblockung/KlausurblockungSchienenAlgorithmusGreedy1';
import { Service, cast_de_nrw_schule_svws_core_Service } from '../../core/Service';
import { KlausurblockungSchienenAlgorithmusGreedy6, cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenAlgorithmusGreedy6 } from '../../core/klausurblockung/KlausurblockungSchienenAlgorithmusGreedy6';
import { System, cast_java_lang_System } from '../../java/lang/System';
import { LogLevel, cast_de_nrw_schule_svws_core_logger_LogLevel } from '../../core/logger/LogLevel';
import { KlausurblockungSchienenAlgorithmusGreedy1b, cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenAlgorithmusGreedy1b } from '../../core/klausurblockung/KlausurblockungSchienenAlgorithmusGreedy1b';
import { KlausurblockungSchienenAlgorithmusAbstract, cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenAlgorithmusAbstract } from '../../core/klausurblockung/KlausurblockungSchienenAlgorithmusAbstract';
import { KlausurblockungSchienenAlgorithmusGreedy2b, cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenAlgorithmusGreedy2b } from '../../core/klausurblockung/KlausurblockungSchienenAlgorithmusGreedy2b';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { KlausurblockungSchienenOutputs, cast_de_nrw_schule_svws_core_data_klausurblockung_KlausurblockungSchienenOutputs } from '../../core/data/klausurblockung/KlausurblockungSchienenOutputs';

export class KlausurblockungSchienenAlgorithmus extends Service<KlausurblockungSchienenInput | null, KlausurblockungSchienenOutputs | null> {


	public constructor() {
		super();
	}

	public handle(pInput : KlausurblockungSchienenInput) : KlausurblockungSchienenOutputs {
		let zeitEndeGesamt : number = System.currentTimeMillis() + pInput.maxTimeMillis;
		this.logger.modifyIndent(+4);
		let seed : number = new Random().nextLong();
		let random : Random = new Random(seed);
		this.logger.log(LogLevel.APP, "KlausurblockungSchienenAlgorithmus: pInput.datenbankID=" + pInput.datenbankID + ", pInput.maxTimeMillis=" + pInput.maxTimeMillis + ", seed=" + seed);
		let dynDaten : KlausurblockungSchienenDynDaten | null = new KlausurblockungSchienenDynDaten(random, this.logger, pInput);
		let algorithmen : Array<KlausurblockungSchienenAlgorithmusAbstract> = [new KlausurblockungSchienenAlgorithmusGreedy3(random, this.logger, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy4(random, this.logger, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy1(random, this.logger, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy1b(random, this.logger, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy2(random, this.logger, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy2b(random, this.logger, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy5(random, this.logger, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy6(random, this.logger, dynDaten)];
		let result : KlausurblockungSchienenOutputs | null = new KlausurblockungSchienenOutputs();
		let zeitProAlgorithmus : number = 10;
		do {
			for (let iAlgo : number = 0; iAlgo < algorithmen.length; iAlgo++){
				let zeitEndeRunde : number = System.currentTimeMillis() + zeitProAlgorithmus;
				algorithmen[iAlgo].berechne(zeitEndeRunde);
				result.ergebnisse.add(dynDaten.gibErzeugeOutput());
			}
			zeitProAlgorithmus *= 2;
		} while (System.currentTimeMillis() + algorithmen.length * zeitProAlgorithmus <= zeitEndeGesamt);
		this.logger.modifyIndent(-4);
		return result;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.klausurblockung.KlausurblockungSchienenAlgorithmus', 'de.nrw.schule.svws.core.Service'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenAlgorithmus(obj : unknown) : KlausurblockungSchienenAlgorithmus {
	return obj as KlausurblockungSchienenAlgorithmus;
}
