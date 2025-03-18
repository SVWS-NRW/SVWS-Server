import type { ApiFile, List, ReportingParameter, SchuelerListeEintrag, SchuelerStammdaten, SimpleOperationResponse, StundenplanListeEintrag } from "@core";
import { BenutzerKompetenz, ArrayList, SchuelerListe, SchuelerListeManager, SchuelerStatus, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";
import { ViewType } from "@ui";
import { routeSchuelerNeu } from "~/router/apps/schueler/RouteSchuelerNeu";
import { routeSchuelerGruppenprozesse } from "~/router/apps/schueler/RouteSchuelerGruppenprozesse";
import type { RouteParamsRawGeneric } from "vue-router";


interface RouteStateSchueler extends RouteStateAuswahlInterface<SchuelerListeManager>{
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
};

const defaultState = <RouteStateSchueler> {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeSchuelerIndividualdaten,
	activeViewType: ViewType.DEFAULT,
	mapStundenplaene: new Map(),
};

export class RouteDataSchueler extends RouteDataAuswahl<SchuelerListeManager, RouteStateSchueler> {

	public constructor() {
		super(defaultState, routeSchuelerGruppenprozesse, routeSchuelerNeu);
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	protected async createManager(idSchuljahresabschnitt : number) : Promise<Partial<RouteStateSchueler>> {
		// Lade die Daten von der API
		const auswahllisteGzip = await api.server.getSchuelerAuswahllisteFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const auswahllisteBlob = await new Response(auswahllisteGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
		const schuelerListe: SchuelerListe = SchuelerListe.transpilerFromJSON(await auswahllisteBlob.text());
		const lehrer = await api.server.getLehrer(api.schema);

		// Erstelle den Schüler-Liste-Manager
		const manager = new SchuelerListeManager(api.schulform, schuelerListe, lehrer, api.schuleStammdaten.abschnitte, api.schuleStammdaten.idSchuljahresabschnitt);

		// Übernehme den Filter von dem vorigen Manager oder initialisiere ihn neu, falls kein voriger Manager vorhanden ist
		if (this._state.value.manager === undefined) {
			manager.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
			manager.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		} else {
			manager.useFilter(this._state.value.manager);
		}
		return { manager };
	}

	public async ladeDaten(auswahl: SchuelerListeEintrag | null) : Promise<SchuelerStammdaten | null> {
		if (auswahl === null)
			return null;
		const res = await api.server.getSchuelerStammdaten(api.schema, auswahl.id);
		this.manager.schuelerstatus.auswahlAdd(SchuelerStatus.data().getWertByID(res.status));
		return res;
	}

	public async updateMapStundenplaene() {
		const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, this.idSchuljahresabschnitt);
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		for (const l of listStundenplaene)
			mapStundenplaene.set(l.id, l);
		this.setPatchedState({ mapStundenplaene });
	}

	get schuelerListeManager(): SchuelerListeManager {
		return this.hasManager ? this.manager // oder gib einen Dummy zurück...
			: new SchuelerListeManager(api.schulform, new SchuelerListe(), new ArrayList(), api.schuleStammdaten.abschnitte, api.schuleStammdaten.idSchuljahresabschnitt);
	}

	protected async doPatch(data : Partial<SchuelerStammdaten>, id: number) : Promise<void> {
		await api.server.patchSchuelerStammdaten(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteSchueler(ids, api.schema);
	}

	protected deleteMessage(id: number, schueler: SchuelerListeEintrag | null) : string {
		return `Schüler ${(schueler?.vorname ?? '???') + ' ' + (schueler?.nachname ?? '???')} (ID: ${id.toString()}) wurde erfolgreich gelöscht.`;
	}

	public deleteSchuelerCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();
		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LOESCHEN))
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Schülern vor.');

		if (!this.schuelerListeManager.liste.auswahlExists())
			errorLog.add('Es wurde kein Schüler zum Löschen ausgewählt.');

		return [errorLog.isEmpty(), errorLog];
	}

	getPDF = api.call(async (reportingParameter: ReportingParameter, idStundenplan: number): Promise<ApiFile> => {
		if (!this.manager.liste.auswahlExists())
			throw new DeveloperNotificationException("Dieser Stundenplan kann nur gedruckt werden, wenn mindestens eine Klasse ausgewählt ist.");
		reportingParameter.idSchuljahresabschnitt = this.idSchuljahresabschnitt;
		reportingParameter.idsHauptdaten.add(idStundenplan);
		for (const l of this.manager.liste.auswahl())
			reportingParameter.idsDetaildaten.add(l.id);
		return await api.server.pdfReport(reportingParameter, api.schema);
	})

}
