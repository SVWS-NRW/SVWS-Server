import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GEAbschlussFach, cast_de_nrw_schule_svws_core_data_abschluss_GEAbschlussFach } from '../../../core/data/abschluss/GEAbschlussFach';
import { ServiceAbschlussHA10, cast_de_nrw_schule_svws_core_abschluss_ge_ServiceAbschlussHA10 } from '../../../core/abschluss/ge/ServiceAbschlussHA10';
import { Service, cast_de_nrw_schule_svws_core_Service } from '../../../core/Service';
import { ServiceBerechtigungMSAQ, cast_de_nrw_schule_svws_core_abschluss_ge_ServiceBerechtigungMSAQ } from '../../../core/abschluss/ge/ServiceBerechtigungMSAQ';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { LogLevel, cast_de_nrw_schule_svws_core_logger_LogLevel } from '../../../core/logger/LogLevel';
import { GEAbschlussFaecher, cast_de_nrw_schule_svws_core_data_abschluss_GEAbschlussFaecher } from '../../../core/data/abschluss/GEAbschlussFaecher';
import { AbschlussErgebnis, cast_de_nrw_schule_svws_core_data_abschluss_AbschlussErgebnis } from '../../../core/data/abschluss/AbschlussErgebnis';
import { SchulabschlussAllgemeinbildend, cast_de_nrw_schule_svws_core_types_schule_SchulabschlussAllgemeinbildend } from '../../../core/types/schule/SchulabschlussAllgemeinbildend';
import { List, cast_java_util_List } from '../../../java/util/List';
import { ServiceAbschlussMSA, cast_de_nrw_schule_svws_core_abschluss_ge_ServiceAbschlussMSA } from '../../../core/abschluss/ge/ServiceAbschlussMSA';
import { AbschlussManager, cast_de_nrw_schule_svws_core_abschluss_AbschlussManager } from '../../../core/abschluss/AbschlussManager';
import { ServiceAbschlussHA9, cast_de_nrw_schule_svws_core_abschluss_ge_ServiceAbschlussHA9 } from '../../../core/abschluss/ge/ServiceAbschlussHA9';

export class ServicePrognose extends Service<GEAbschlussFaecher, AbschlussErgebnis> {


	public constructor() {
		super();
	}

	/**
	 * Prüft, ob Lernbereichsnoten bei den Abschlussfächern zur Verfügung stehen oder nicht.
	 * 
	 * @param faecher   die Abschlussfächer
	 * 
	 * @return true, falls die Lernbereichsnoten vorhanden sind, ansonsten false
	 */
	private static hatLernbereichsnoten(faecher : GEAbschlussFaecher) : boolean {
		let hatLBNW : boolean = false;
		let hatLBAL : boolean = false;
		if (faecher.faecher === null) 
			return false;
		let tmp : List<GEAbschlussFach> = faecher.faecher;
		for (let fach of tmp) {
			if (fach === null) 
				continue;
			hatLBNW = hatLBNW || JavaObject.equalsTranspiler("LBNW", (fach.kuerzel));
			hatLBAL = hatLBAL || JavaObject.equalsTranspiler("LBAL", (fach.kuerzel));
		}
		return hatLBNW && hatLBAL;
	}

	/**
	 * Führt die Prognoseberechnung anhand der übergebenen Abschlussfächer durch
	 * und gibt das Berechnungsergebnis zurück.
	 * 
	 * @param input    die Abschlussfächer
	 * 
	 * @return das Ergebnis der Prognoseberechnung
	 */
	public handle(input : GEAbschlussFaecher) : AbschlussErgebnis {
		if (!AbschlussManager.pruefeHat4LeistungsdifferenzierteFaecher(input)) {
			this.logger.logLn(LogLevel.DEBUG, "Fehler: Es wurden nicht genügend leistungsdiffernzierte Fächer gefunden.");
			let prognose : AbschlussErgebnis = AbschlussManager.getErgebnis(null, false);
			prognose.log = this.log.getStrings();
			return prognose;
		}
		if (!AbschlussManager.pruefeKuerzelDuplikate(input)) {
			this.logger.logLn(LogLevel.DEBUG, "Fehler: Es wurden Fächer mit dem gleichen Kürzel zur Abschlussprüfung übergeben. Dies ist nicht zulässig.");
			let prognose : AbschlussErgebnis = AbschlussManager.getErgebnis(null, false);
			prognose.log = this.log.getStrings();
			return prognose;
		}
		let abschluss : SchulabschlussAllgemeinbildend = SchulabschlussAllgemeinbildend.OA;
		let np_faecher : List<string> | null = null;
		if (!JavaObject.equalsTranspiler("10", (input.jahrgang))) {
			let ha9 : ServiceAbschlussHA9 = new ServiceAbschlussHA9();
			let ha9output : AbschlussErgebnis = ha9.handle(input);
			np_faecher = ha9output.npFaecher;
			if (ha9output.erworben) 
				abschluss = SchulabschlussAllgemeinbildend.HA9;
			this.log.append(ha9.getLog());
			this.logger.logLn(LogLevel.INFO, "");
		} else 
			if (JavaObject.equalsTranspiler("10", (input.jahrgang))) {
				abschluss = SchulabschlussAllgemeinbildend.HA9;
			}
		let ha10 : ServiceAbschlussHA10 = new ServiceAbschlussHA10();
		let ha10output : AbschlussErgebnis = ha10.handle(input);
		if (ha10output.erworben) 
			abschluss = SchulabschlussAllgemeinbildend.HA10; else 
			if (JavaObject.equalsTranspiler("10", (input.jahrgang)) || (JavaObject.equalsTranspiler(SchulabschlussAllgemeinbildend.HA9, (abschluss)))) 
				np_faecher = ha10output.npFaecher;
		this.log.append(ha10.getLog());
		if ((!JavaObject.equalsTranspiler(SchulabschlussAllgemeinbildend.OA, (abschluss))) || (!ServicePrognose.hatLernbereichsnoten(input))) {
			let msa : ServiceAbschlussMSA = new ServiceAbschlussMSA();
			let msaOutput : AbschlussErgebnis = msa.handle(input);
			this.logger.logLn(LogLevel.INFO, "");
			this.log.append(msa.getLog());
			if (msaOutput.erworben) {
				abschluss = SchulabschlussAllgemeinbildend.MSA;
				let msaq : ServiceBerechtigungMSAQ = new ServiceBerechtigungMSAQ();
				let msaqOutput : AbschlussErgebnis = msaq.handle(input);
				if (msaqOutput.erworben) {
					abschluss = SchulabschlussAllgemeinbildend.MSA_Q;
				} else {
					np_faecher = msaqOutput.npFaecher;
				}
				this.logger.logLn(LogLevel.INFO, "");
				this.log.append(msaq.getLog());
			} else {
				np_faecher = msaOutput.npFaecher;
			}
		}
		let prognose : AbschlussErgebnis = AbschlussManager.getErgebnisNachpruefung(abschluss, np_faecher);
		prognose.erworben = (!JavaObject.equalsTranspiler(SchulabschlussAllgemeinbildend.OA, (abschluss)));
		prognose.log = this.log.getStrings();
		return prognose;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.Service', 'de.nrw.schule.svws.core.abschluss.ge.ServicePrognose'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_ge_ServicePrognose(obj : unknown) : ServicePrognose {
	return obj as ServicePrognose;
}
