import type { ApiFile, List, ReportingReportvorlageParameter, ReportingReportvorlageParameterGruppe, SimpleOperationResponse } from "@core";
import { BenutzerKompetenz, ReportingAusgabeformat, ReportingEinstellungenBenutzerVorlage, ReportingEinstellungenBenutzerVorlageGruppe, ReportingEinstellungenBenutzerVorlagenParameterWert, ReportingParameter, ServerMode, ReportingReportvorlage, ReportingReportvorlageUtils } from "@core";
import type { ElementMitAnforderung, ReportingState } from "@ui";
import { StateManager } from "@ui";
import { api } from "~/router/Api";
import { abschnittStateImpl } from "./AbschnittStateImpl";
import { serverStateImpl } from "./ServerStateImpl";
import { configStateImpl } from "./ConfigStateImpl";

interface ReportingReactiveState {
}

/**
 * Die Schnittstelle für den Zustand des Reportings
 */
export class ReportingStateImpl extends StateManager<ReportingReactiveState> implements ReportingState {

	public constructor() {
		super({});
	}

	public async fetchEMailJobStatus(jobId: number): Promise<SimpleOperationResponse> {
		return await api.server.getEmailJobStatus(api.schema, jobId);
	}

	public async fetchEMailJobLog(jobId: number): Promise<SimpleOperationResponse> {
		return await api.server.getEmailJobLog(api.schema, jobId);
	}

	public createPDFReport = async (reportingParameter: ReportingParameter): Promise<void> => {
		await this.speichereVorlagenEinstellungen(reportingParameter);
		reportingParameter.ausgabeformat = ReportingAusgabeformat.PDF.getId();
		reportingParameter.idSchuljahresabschnitt = abschnittStateImpl.auswahl.id;
		api.status.start();
		try {
			const apiFile = await api.server.pdfReport(reportingParameter, api.schema);
			await this.downloadApiFile(apiFile);
		} finally {
			api.status.stop();
		}
	};

	public createEMailReport = async (reportingParameter: ReportingParameter): Promise<SimpleOperationResponse> => {
		await this.speichereVorlagenEinstellungen(reportingParameter);
		reportingParameter.ausgabeformat = ReportingAusgabeformat.EMAIL.getId();
		reportingParameter.idSchuljahresabschnitt = abschnittStateImpl.auswahl.id;
		api.status.start();
		try {
			return await api.server.emailReport(reportingParameter, api.schema);
		} finally {
			api.status.stop();
		}
	};

	public createHTMLReport = async (reportingParameter: ReportingParameter): Promise<string> => {
		await this.speichereVorlagenEinstellungen(reportingParameter);
		reportingParameter.ausgabeformat = ReportingAusgabeformat.HTML.getId();
		reportingParameter.idSchuljahresabschnitt = abschnittStateImpl.auswahl.id;
		api.status.start();
		try {
			return await api.server.htmlReport(reportingParameter, api.schema);
		} finally {
			api.status.stop();
		}
	};

	public createJSONReportingParameter = async (reportingParameter: ReportingParameter): Promise<void> => {
		const json = JSON.stringify(JSON.parse(ReportingParameter.transpilerToJSON(reportingParameter)), null, '\t');
		const data = new Blob([json], { type: "application/json" });
		const apiFile = { name: reportingParameter.reportvorlage + "_ReportingParameter.json", data };
		await this.downloadApiFile(apiFile);
	};

	private async downloadApiFile(apiFile: ApiFile) {
		const { data, name } = apiFile;
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	private istServerModeErfuellt(erforderlicherServerMode: string): boolean {
		return ServerMode.getByText(erforderlicherServerMode).checkServerMode(serverStateImpl.mode);
	}

	/** Prüft, ob der Benutzer mindestens eine der erforderlichen Kompetenzen besitzt (leere übergebene Liste liefert true).
	 *  AKTUELL: Die Funktion liefert immer true für jede korrekt übergebene Kompetenz, da BenutzerState für Kompetenz-Prüfung noch nicht implementiert ist. */
	private hatKompetenz(erforderlicheKompetenzen: List<number>): boolean {
		if (erforderlicheKompetenzen.isEmpty()) {
			return true;
		}
		for (const id of erforderlicheKompetenzen) {
			const kompetenz = BenutzerKompetenz.getByID(id);
			// TODO: Wenn BenutzerState implementiert wurde, dann mittels '(kompetenz !== null) && benutzerState.benutzerKompetenzen.has(kompetenz)' prüfen.
			if (kompetenz !== null) {
				return true;
			}
		}
		return false;
	}

	/** Liefert die Bezeichnungen der Kompetenzen, die dem Benutzer für diese Anforderung fehlen (leer, wenn die Anforderung erfüllt ist). */
	public fehlendeKompetenzNamen(erforderlicheKompetenzen: List<number>): string[] {
		if (this.hatKompetenz(erforderlicheKompetenzen)) {
			return [];
		}
		const namen: string[] = [];
		for (const id of erforderlicheKompetenzen) {
			const kompetenz = BenutzerKompetenz.getByID(id);
			if (kompetenz !== null) {
				namen.push(kompetenz.daten.bezeichnung);
			}
		}
		return namen;
	}

	public istSichtbar(element: ElementMitAnforderung): boolean {
		return (element.uiIstSichtbar === true) && this.istServerModeErfuellt(element.uiErforderlicherServerMode);
	}

	public istAktiv(element: ElementMitAnforderung): boolean {
		return this.hatKompetenz(element.uiErforderlicheKompetenzen);
	}

	public istParameterSichtbar(gruppe: ReportingReportvorlageParameterGruppe, vp: ReportingReportvorlageParameter): boolean {
		return this.istSichtbar(gruppe) && this.istSichtbar(vp);
	}

	public istParameterAktiv(gruppe: ReportingReportvorlageParameterGruppe, vp: ReportingReportvorlageParameter): boolean {
		return this.istAktiv(gruppe) && this.istAktiv(vp);
	}

	private async speichereVorlagenEinstellungen(parameter: ReportingParameter): Promise<void> {

		const vorlage = ReportingReportvorlage.getByBezeichnung(parameter.reportvorlage);
		if (vorlage === null) {
			return;
		}
		const einstellungen = new ReportingEinstellungenBenutzerVorlage();
		einstellungen.version = 1;

		// Parameterwerte – nur die sichtbaren (schlankes Delta; ausgeblendete/benutzerweite bleiben außen vor).
		for (const gruppe of parameter.reportvorlageParameterGruppen) {
			for (const vp of gruppe.reportvorlageParameter) {
				if (this.istParameterSichtbar(gruppe, vp)) {
					const pw = new ReportingEinstellungenBenutzerVorlagenParameterWert();
					pw.name = vp.name;
					pw.wert = vp.wert;
					einstellungen.parameterWerte.add(pw);
				}
			}
		}

		// Sortierauswahl je Gruppe (nur die Bezeichnungen der ausgewählten Definitionen).
		for (const gruppe of parameter.sortierungDefinitionenGruppen) {
			const auswahl = new ReportingEinstellungenBenutzerVorlageGruppe();
			auswahl.gruppe = gruppe.bezeichnung;
			for (const sd of gruppe.sortierungDefinitionen) {
				auswahl.bezeichnungen.add(sd.bezeichnung);
			}
			einstellungen.sortierungsauswahlen.add(auswahl);
		}

		// Filterauswahl je Gruppe.
		for (const gruppe of parameter.filterDefinitionenGruppen) {
			const auswahl = new ReportingEinstellungenBenutzerVorlageGruppe();
			auswahl.gruppe = gruppe.bezeichnung;
			for (const fd of gruppe.filterDefinitionen) {
				auswahl.bezeichnungen.add(fd.bezeichnung);
			}
			einstellungen.filterungsauswahlen.add(auswahl);
		}

		await configStateImpl.config.setObjectValue(vorlage.getConfigKeyBenutzerVorlage(), einstellungen,
			(obj) => ReportingEinstellungenBenutzerVorlage.transpilerToJSON(obj));
	}

	/** Liest die gespeicherten Einstellungen der Vorlage und mergt sie in die frischen Default-Parameter. */
	createParameter(vorlage: ReportingReportvorlage): ReportingParameter {
		const p = vorlage.getReportingParameter();
		const gespeichert = configStateImpl.config.getObjectValue(vorlage.getConfigKeyBenutzerVorlage(),
			(json) => ReportingEinstellungenBenutzerVorlage.transpilerFromJSON(json));
		if (gespeichert === null) {
			return p;
		}

		// (a) Parameterwerte namensbasiert übernehmen. Unbekannte Namen (Vorlage geändert) werden ignoriert,
		//     fehlende behalten ihren Default.
		const werte = new Map<string, string>();
		for (const pw of gespeichert.parameterWerte) {
			werte.set(pw.name, pw.wert);
		}
		for (const gruppe of p.reportvorlageParameterGruppen) {
			for (const vp of gruppe.reportvorlageParameter) {
				const wert = werte.get(vp.name);
				// Optional zusätzlich absichern: nur übernehmen, wenn istParameterAktiv(gruppe, vp).
				if (wert !== undefined) {
					vp.wert = wert;
				}
			}
		}

		// (b) Sortierauswahl je Gruppe über den Helper auf die Options-Instanzen abbilden.
		for (const auswahl of gespeichert.sortierungsauswahlen) {
			for (const gruppe of p.sortierungDefinitionenGruppen) {
				if (gruppe.bezeichnung === auswahl.gruppe) {
					gruppe.sortierungDefinitionen = ReportingReportvorlageUtils.waehleGespeicherteAuswahl(
						gruppe.sortierungDefinitionenOptionen, { apply: (sd) => sd.bezeichnung }, auswahl.bezeichnungen);
				}
			}
		}

		// (c) Filterauswahl je Gruppe – identisch, inkl. Mehrfachauswahl (Reihenfolge bleibt erhalten).
		for (const auswahl of gespeichert.filterungsauswahlen) {
			for (const gruppe of p.filterDefinitionenGruppen) {
				if (gruppe.bezeichnung === auswahl.gruppe) {
					gruppe.filterDefinitionen = ReportingReportvorlageUtils.waehleGespeicherteAuswahl(
						gruppe.filterDefinitionenOptionen, { apply: (fd) => fd.bezeichnung }, auswahl.bezeichnungen);
				}
			}
		}
		return p;
	}

}

export const reportingStateImpl = new ReportingStateImpl();
