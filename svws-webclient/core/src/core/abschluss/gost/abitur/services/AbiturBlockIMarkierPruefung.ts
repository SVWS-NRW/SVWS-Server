import { Service } from '../../../../../core/Service';
import { Abiturdaten } from '../../../../../core/data/gost/Abiturdaten';
import { LogLevel } from '../../../../../core/logger/LogLevel';

export class AbiturBlockIMarkierPruefung extends Service<Abiturdaten | null, boolean | null> {


	/**
	 * TODO
	 */
	public constructor() {
		super();
	}

	public handle(abidaten : Abiturdaten | null) : boolean | null {
		if (abidaten === null) {
			this.logger.logLn(LogLevel.ERROR, "Der Dienst " + this.getClass().getSimpleName()! + " hat keine gültigen Abiturdaten erhalten.");
			return false;
		}
		const ergebnis : boolean = false;
		this.logger.logLn(LogLevel.ERROR, "Der Dienst " + this.getClass().getSimpleName()! + " ist noch nicht fertig programmiert...");
		return ergebnis;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.abitur.services.AbiturBlockIMarkierPruefung', 'de.svws_nrw.core.Service'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_abitur_services_AbiturBlockIMarkierPruefung(obj : unknown) : AbiturBlockIMarkierPruefung {
	return obj as AbiturBlockIMarkierPruefung;
}
