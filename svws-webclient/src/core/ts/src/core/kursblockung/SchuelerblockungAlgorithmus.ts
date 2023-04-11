import { SchuelerblockungOutput } from '../../core/data/kursblockung/SchuelerblockungOutput';
import { SchuelerblockungInput } from '../../core/data/kursblockung/SchuelerblockungInput';
import { Random } from '../../java/util/Random';
import { Service } from '../../core/Service';
import { LogLevel } from '../../core/logger/LogLevel';
import { SchuelerblockungDynDaten } from '../../core/kursblockung/SchuelerblockungDynDaten';

export class SchuelerblockungAlgorithmus extends Service<SchuelerblockungInput, SchuelerblockungOutput> {

	private static readonly _random : Random = new Random();


	public constructor() {
		super();
	}

	public handle(pInput : SchuelerblockungInput) : SchuelerblockungOutput {
		this.logger.modifyIndent(+4);
		const seed : number = SchuelerblockungAlgorithmus._random.nextLong();
		const random : Random = new Random(seed);
		this.logger.log(LogLevel.APP, "SchuelerblockungAlgorithmus.handle(): Seed (" + seed + ") verwendet.");
		const dynDaten : SchuelerblockungDynDaten = new SchuelerblockungDynDaten(random, pInput);
		this.logger.modifyIndent(-4);
		return dynDaten.gibBestesMatching();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.Service', 'de.svws_nrw.core.kursblockung.SchuelerblockungAlgorithmus'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_SchuelerblockungAlgorithmus(obj : unknown) : SchuelerblockungAlgorithmus {
	return obj as SchuelerblockungAlgorithmus;
}
