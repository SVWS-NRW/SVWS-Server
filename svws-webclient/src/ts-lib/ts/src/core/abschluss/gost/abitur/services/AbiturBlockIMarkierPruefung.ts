import { JavaObject, cast_java_lang_Object } from '../../../../../java/lang/JavaObject';
import { Service, cast_de_nrw_schule_svws_core_Service } from '../../../../../core/Service';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../../../java/lang/JavaBoolean';
import { Abiturdaten, cast_de_nrw_schule_svws_core_data_gost_Abiturdaten } from '../../../../../core/data/gost/Abiturdaten';
import { LogLevel, cast_de_nrw_schule_svws_core_logger_LogLevel } from '../../../../../core/logger/LogLevel';

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
			return null;
		}
		let ergebnis : boolean = false;
		this.logger.logLn(LogLevel.ERROR, "Der Dienst " + this.getClass().getSimpleName()! + " ist noch nicht fertig programmiert...");
		return ergebnis;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.Service', 'de.nrw.schule.svws.core.abschluss.gost.abitur.services.AbiturBlockIMarkierPruefung'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_gost_abitur_services_AbiturBlockIMarkierPruefung(obj : unknown) : AbiturBlockIMarkierPruefung {
	return obj as AbiturBlockIMarkierPruefung;
}
