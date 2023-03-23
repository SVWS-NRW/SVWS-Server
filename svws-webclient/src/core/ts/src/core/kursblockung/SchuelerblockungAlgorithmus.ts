import { SchuelerblockungOutput } from '../../core/data/kursblockung/SchuelerblockungOutput';
import { SchuelerblockungInput } from '../../core/data/kursblockung/SchuelerblockungInput';
import { Random } from '../../java/util/Random';
import { Service } from '../../core/Service';
import { LogLevel } from '../../core/logger/LogLevel';
import { SchuelerblockungDynDaten } from '../../core/kursblockung/SchuelerblockungDynDaten';

export class SchuelerblockungAlgorithmus extends Service<SchuelerblockungInput, SchuelerblockungOutput> {


	public constructor() {
		super();
	}

	public handle(pInput : SchuelerblockungInput) : SchuelerblockungOutput {
		this.logger.modifyIndent(+4);
		let seed : number = new Random().nextLong();
		let random : Random = new Random(seed);
		this.logger.log(LogLevel.APP, "SchuelerblockungAlgorithmus.handle(): Seed (" + seed + ") verwendet.");
		let dynDaten : SchuelerblockungDynDaten = new SchuelerblockungDynDaten(random, pInput);
		this.logger.modifyIndent(-4);
		return dynDaten.gibBestesMatching();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.Service', 'de.nrw.schule.svws.core.kursblockung.SchuelerblockungAlgorithmus'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_SchuelerblockungAlgorithmus(obj : unknown) : SchuelerblockungAlgorithmus {
	return obj as SchuelerblockungAlgorithmus;
}
