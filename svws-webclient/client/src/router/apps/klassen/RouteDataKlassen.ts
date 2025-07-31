
import type { KlassenDaten, Schueler, List, LehrerListeEintrag, SimpleOperationResponse, ApiFile, ReportingParameter, StundenplanListeEintrag } from "@core";
import { ArrayList, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeKlassenDaten } from "~/router/apps/klassen/RouteKlassenDaten";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeKlasseGruppenprozesse } from "./RouteKlassenGruppenprozesse";
import type { RouteNode } from "~/router/RouteNode";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeKlassenNeu } from "~/router/apps/klassen/RouteKlassenNeu";
import { routeApp } from "~/router/apps/RouteApp";
import { ViewType, KlassenListeManager } from "@ui";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";

interface RouteStateKlassen extends RouteStateAuswahlInterface<KlassenListeManager> {
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	mapKlassenVorigerAbschnitt: Map<number, KlassenDaten>;
	mapKlassenFolgenderAbschnitt: Map<number, KlassenDaten>;
	oldView?: RouteNode<any, any>;
}

const defaultState: RouteStateKlassen = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	mapStundenplaene: new Map(),
	mapKlassenVorigerAbschnitt: new Map<number, KlassenDaten>(),
	mapKlassenFolgenderAbschnitt: new Map<number, KlassenDaten>(),
	view: routeKlassenDaten,
	oldView: undefined,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataKlassen extends RouteDataAuswahl<KlassenListeManager, RouteStateKlassen> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeKlasseGruppenprozesse, hinzufuegen: routeKlassenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	get mapKlassenVorigerAbschnitt(): Map<number, KlassenDaten> {
		return this._state.value.mapKlassenVorigerAbschnitt;
	}

	get mapKlassenFolgenderAbschnitt(): Map<number, KlassenDaten> {
		return this._state.value.mapKlassenFolgenderAbschnitt;
	}

	get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	protected async createManager(idSchuljahresabschnitt : number) : Promise<Partial<RouteStateKlassen>> {
		const schuljahresabschnitt = api.mapAbschnitte.value.get(idSchuljahresabschnitt);
		if (schuljahresabschnitt === undefined)
			throw new DeveloperNotificationException('Es ist kein gültiger Schuljahresabschnitt ausgewählt');
		// Lade die Kataloge und erstelle den Manager
		const listKlassen = await api.server.getKlassenFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const mapKlassenVorigerAbschnitt = schuljahresabschnitt.idVorigerAbschnitt === null
			? new Map<number, KlassenDaten>()
			: await api.getKlassenListe(schuljahresabschnitt.idVorigerAbschnitt);
		const mapKlassenFolgenderAbschnitt = schuljahresabschnitt.idFolgeAbschnitt === null
			? new Map<number, KlassenDaten>()
			: await api.getKlassenListe(schuljahresabschnitt.idFolgeAbschnitt);
		const listSchueler = await api.server.getSchuelerFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const listLehrer = await api.server.getLehrer(api.schema);
		const manager = new KlassenListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, listKlassen, listSchueler, listJahrgaenge, listLehrer);
		if (this._state.value.manager === undefined) {
			manager.setFilterAuswahlPermitted(true);
		} else {
			manager.useFilter(this._state.value.manager);
		}
		return { manager, mapKlassenVorigerAbschnitt, mapKlassenFolgenderAbschnitt };
	}

	public async ladeDaten(auswahl: KlassenDaten | null) : Promise<KlassenDaten | null> {
		// Die Daten sind vollständig in der Liste enthalten, kein Aufruf der API notwendig
		return auswahl;
	}

	protected async doPatch(data : Partial<KlassenDaten>, id: number) : Promise<void> {
		await api.server.patchKlasse(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteKlassen(ids, api.schema);
	}

	protected filterOnDelete(ids: List<number>): List<number> {
		const neueIDs = new ArrayList<number>();
		const set = this.manager.getKlassenIDsMitSchuelern();
		for (const id of ids)
			if (!set.contains(id))
				neueIDs.add(id);
		return neueIDs;
	}

	protected deleteMessage(id: number, klasse: KlassenDaten | null) : string {
		return `Klasse ${klasse?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	public async updateMapStundenplaene() {
		const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, this.idSchuljahresabschnitt);
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		for (const l of listStundenplaene)
			mapStundenplaene.set(l.id, l);
		this.setPatchedState({ mapStundenplaene });
	}

	addKlassenleitung = async (idLehrer : number, idKlasse : number) : Promise<void> => {
		// Prüfe zunächst, ob die Lehrer-ID bereits in der Liste der Klassenleitungen vorkommt
		if (this.manager.daten().klassenLeitungen.contains(idLehrer))
			throw new DeveloperNotificationException("Die Klassenleitung mit der Lehrer-ID " + idLehrer + " kommt bereits in der Klasse mit der ID " + idKlasse + "vor.");

		// Erstelle die neue Klassenliste durch anhängen der neuen Lehrer-ID
		const listKlassenleitungenNeu = new ArrayList<number>(this.manager.daten().klassenLeitungen);
		listKlassenleitungenNeu.add(idLehrer);

		// Führe den API-Aufruf durch
		const requestBody : Partial<KlassenDaten> = { klassenLeitungen: listKlassenleitungenNeu };
		await api.server.patchKlasse(requestBody, api.schema, idKlasse);

		// Aktualisiere die Liste der Klassenleitungen im Erfolgsfall
		this.manager.daten().klassenLeitungen.add(idLehrer);
		this.commit();
	}

	removeKlassenleitung = async (eintrag: LehrerListeEintrag) => {
		// Bestimme die Position der Klassenleitung in der zugehörigen Liste
		const listKlassenleitungenNeu = new ArrayList<number>(this.manager.daten().klassenLeitungen);
		const lehrerIndex : number = listKlassenleitungenNeu.indexOf(eintrag.id);
		listKlassenleitungenNeu.removeElementAt(lehrerIndex);

		// Führe den API-Aufruf durch
		const requestBody : Partial<KlassenDaten> = { klassenLeitungen: listKlassenleitungenNeu }
		const klassenId : number | null = this.manager.auswahlID();
		if (klassenId === null)
			throw new DeveloperNotificationException("Keine Klasse ausgewählt, Klassenleitung kann nicht entfernt werden");
		await api.server.patchKlasse(requestBody, api.schema, klassenId);

		// Aktualisiere die Liste der Klassenleitungen im Erfolgsfall
		this.manager.daten().klassenLeitungen = listKlassenleitungenNeu;
		this.commit();
	}

	updateReihenfolgeKlassenleitung = async (idLehrer : number, erhoehe : boolean) : Promise<void> => {
		const idKlasse : number | null = this.manager.auswahlID();
		if (idKlasse === null)
			throw new DeveloperNotificationException("Für das Anpassen der Reihenfolge von Klassenlehrern muss eine Klasse ausgewählt sein.")

		// Erstelle eine Kopie der Liste der Klassenleitungen und führe an dieser die Änderungen durch
		const listKlassenleitungenNeu = new ArrayList<number>(this.manager.daten().klassenLeitungen);
		if (!KlassenListeManager.updateReihenfolgeKlassenleitung(listKlassenleitungenNeu, idLehrer, erhoehe))
			return;

		// Führe den API-Aufruf durch
		const requestBody : Partial<KlassenDaten> = { klassenLeitungen: listKlassenleitungenNeu };
		await api.server.patchKlasse(requestBody, api.schema, idKlasse)

		// Aktualisiere die Liste der Klassenleitungen im Erfolgsfall
		this.manager.daten().klassenLeitungen = listKlassenleitungenNeu;
		this.commit();
	}

	setzeDefaultSortierung = async () => {
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		const auswahl_id = this.manager.auswahl().id;
		await api.server.setKlassenSortierungFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		await this.setSchuljahresabschnitt(idSchuljahresabschnitt, true);
		await this.setDaten(this.manager.liste.get(auswahl_id));
	}

	add = async (partialKlasse: Partial<KlassenDaten>): Promise<void> => {
		const neueKlasse = await api.server.addKlasse({ ...partialKlasse, idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(neueKlasse.id);
	}

	gotoSchueler = async (eintrag: Schueler) => {
		await RouteManager.doRoute(routeSchueler.getRoute({ id: eintrag.id }));
	}

	gotoLehrer = async (eintrag: LehrerListeEintrag) => {
		await RouteManager.doRoute(routeLehrer.getRoute({ id: eintrag.id }));
	}

	getPDF = api.call(async (reportingParameter: ReportingParameter): Promise<ApiFile> => {
		if (!this.manager.liste.auswahlExists())
			throw new DeveloperNotificationException("Die Ausgabe kann nur erfolgen, wenn mindestens eine Klasse ausgewählt ist.");
		reportingParameter.idSchuljahresabschnitt = this.idSchuljahresabschnitt;
		return await api.server.pdfReport(reportingParameter, api.schema);
	})
}
