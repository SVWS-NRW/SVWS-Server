import { JavaObject, cast_java_lang_Object } from '../../../../../java/lang/JavaObject';
import { Service, cast_de_nrw_schule_svws_core_Service } from '../../../../Service';
import { Abiturdaten, cast_de_nrw_schule_svws_core_data_gost_Abiturdaten } from '../../../../data/gost/Abiturdaten';
import { LogLevel, cast_de_nrw_schule_svws_core_logger_LogLevel } from '../../../../logger/LogLevel';

export class AbiturBlockIMarkierAlgorithmus extends Service<Abiturdaten | null, Abiturdaten | null> {


	/**
	 * Erzeugt einen Markierungs-Dienst zur Markierung der Kurse aus dem Block I der Abiturdaten, welche
	 * in die Punktewertung für die Abiturzulassung und in das Abitur einfliessen. 
	 */
	public constructor() {
		super();
	}

	public handle(abidaten : Abiturdaten | null) : Abiturdaten | null {
		if (abidaten === null) {
			this.logger.logLn(LogLevel.ERROR, "Der Dienst " + this.getClass().getSimpleName()! + " hat keine gültigen Abiturdaten erhalten.");
			return null;
		}
		this.logger.logLn(LogLevel.ERROR, "Der Dienst " + this.getClass().getSimpleName()! + " ist noch nicht fertig programmiert...");
		return abidaten;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.Service', 'de.nrw.schule.svws.core.abschluss.gost.abitur.services.AbiturBlockIMarkierAlgorithmus'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_gost_abitur_services_AbiturBlockIMarkierAlgorithmus(obj : unknown) : AbiturBlockIMarkierAlgorithmus {
	return obj as AbiturBlockIMarkierAlgorithmus;
}
