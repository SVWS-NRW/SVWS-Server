import { JavaObject, cast_java_lang_Object } from '../../../../java/lang/JavaObject';
import { AbschlussManagerBerufsbildend, cast_de_nrw_schule_svws_core_abschluss_AbschlussManagerBerufsbildend } from '../../AbschlussManagerBerufsbildend';
import { SchulabschlussAllgemeinbildend, cast_de_nrw_schule_svws_core_types_schule_SchulabschlussAllgemeinbildend } from '../../../types/schule/SchulabschlussAllgemeinbildend';
import { Service, cast_de_nrw_schule_svws_core_Service } from '../../../Service';
import { Sprachreferenzniveau, cast_de_nrw_schule_svws_core_types_fach_Sprachreferenzniveau } from '../../../types/fach/Sprachreferenzniveau';
import { AbschlussErgebnisBerufsbildend, cast_de_nrw_schule_svws_core_data_abschluss_AbschlussErgebnisBerufsbildend } from '../../../data/abschluss/AbschlussErgebnisBerufsbildend';
import { BKAnlageAFaecher, cast_de_nrw_schule_svws_core_abschluss_bk_a_BKAnlageAFaecher } from './BKAnlageAFaecher';
import { LogLevel, cast_de_nrw_schule_svws_core_logger_LogLevel } from '../../../logger/LogLevel';

export class BKAnlageA01Abschluss extends Service<BKAnlageAFaecher, AbschlussErgebnisBerufsbildend> {


	public constructor() {
		super();
	}

	public handle(input : BKAnlageAFaecher) : AbschlussErgebnisBerufsbildend {
		this.logger.log(LogLevel.INFO, "Prüfe BSA:");
		if (AbschlussManagerBerufsbildend.getAnzahlUngenuegend(input) > 0) {
			this.logger.logLn(LogLevel.INFO, " nicht erreicht (kein ungenügend erlaubt, insgesamt " + AbschlussManagerBerufsbildend.getAnzahlUngenuegend(input) + ").");
			return AbschlussManagerBerufsbildend.getErgebnis(false, AbschlussManagerBerufsbildend.getDurchschnitt(input), false, SchulabschlussAllgemeinbildend.OA);
		} else 
			if (AbschlussManagerBerufsbildend.getAnzahlDefizite(input) > 1) {
				this.logger.logLn(LogLevel.INFO, " nicht erreicht (mehr als 1 Defizit, insgesamt " + AbschlussManagerBerufsbildend.getAnzahlDefizite(input) + ").");
				return AbschlussManagerBerufsbildend.getErgebnis(false, AbschlussManagerBerufsbildend.getDurchschnitt(input), false, SchulabschlussAllgemeinbildend.OA);
			}
		this.logger.logLn(LogLevel.INFO, " erreicht.");
		if ((input.englischGeR === null) || (Sprachreferenzniveau.B1.vergleiche(input.englischGeR) < 0)) {
			if (input.englischGeR === null) {
				this.logger.logLn(LogLevel.INFO, "Das Sprachreferenzniveau in Englisch wurde nicht angegeben. Eine Prüfung auf MSA ist daher nicht möglich.");
			} else {
				this.logger.logLn(LogLevel.INFO, "Das Sprachreferenzniveau in Englisch ist für den MSA nicht ausreichend.");
			}
			this.logger.logLn(LogLevel.INFO, "HSA10 wurde erreicht.");
			return AbschlussManagerBerufsbildend.getErgebnis(true, AbschlussManagerBerufsbildend.getDurchschnitt(input), input.hatBestandenBerufsAbschlussPruefung, SchulabschlussAllgemeinbildend.HA10);
		}
		if (AbschlussManagerBerufsbildend.getDurchschnitt(input) <= 2.5) {
			this.logger.logLn(LogLevel.INFO, "Die Durschnittsnote ist besser als oder gleich 2,5.");
			this.logger.logLn(LogLevel.INFO, "MSA-Q wurde erreicht.");
			return AbschlussManagerBerufsbildend.getErgebnis(false, AbschlussManagerBerufsbildend.getDurchschnitt(input), input.hatBestandenBerufsAbschlussPruefung, SchulabschlussAllgemeinbildend.MSA_Q);
		} else 
			if (AbschlussManagerBerufsbildend.getDurchschnitt(input) <= 3.5) {
				this.logger.logLn(LogLevel.INFO, "Die Durschnittsnote ist besser als oder gleich 3,5, aber schlechter als 2,5.");
				this.logger.logLn(LogLevel.INFO, "MSA wurde erreicht.");
				return AbschlussManagerBerufsbildend.getErgebnis(false, AbschlussManagerBerufsbildend.getDurchschnitt(input), input.hatBestandenBerufsAbschlussPruefung, SchulabschlussAllgemeinbildend.MSA);
			}
		this.logger.logLn(LogLevel.INFO, "Die Durschnittsnote ist schlechter als 3,5.");
		this.logger.logLn(LogLevel.INFO, "HSA10 wurde erreicht.");
		return AbschlussManagerBerufsbildend.getErgebnis(false, AbschlussManagerBerufsbildend.getDurchschnitt(input), input.hatBestandenBerufsAbschlussPruefung, SchulabschlussAllgemeinbildend.HA10);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.Service', 'de.nrw.schule.svws.core.abschluss.bk.a.BKAnlageA01Abschluss'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_bk_a_BKAnlageA01Abschluss(obj : unknown) : BKAnlageA01Abschluss {
	return obj as BKAnlageA01Abschluss;
}
