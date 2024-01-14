
import type { KlassenDaten, KlassenListeEintrag, List, Schueler} from "@core";
import { ArrayList, DeveloperNotificationException, KlassenListeManager } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKlasseDaten } from "~/router/apps/klassen/RouteKlasseDaten";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";


interface RouteStateKlassen extends RouteStateInterface {
	idSchuljahresabschnitt: number;
	klassenListeManager: KlassenListeManager;
	mapKlassenVorigerAbschnitt: Map<number, KlassenListeEintrag>;
	mapKlassenFolgenderAbschnitt: Map<number, KlassenListeEintrag>;
}

const defaultState = <RouteStateKlassen> {
	idSchuljahresabschnitt: -1,
	klassenListeManager: new KlassenListeManager(null, new ArrayList(), new ArrayList(), new ArrayList()),
	mapKlassenVorigerAbschnitt: new Map<number, KlassenListeEintrag>(),
	mapKlassenFolgenderAbschnitt: new Map<number, KlassenListeEintrag>(),
	view: routeKlasseDaten,
};

export class RouteDataKlassen extends RouteData<RouteStateKlassen> {

	public constructor() {
		super(defaultState);
	}

	get klassenListeManager(): KlassenListeManager {
		return this._state.value.klassenListeManager;
	}

	get mapKlassenVorigerAbschnitt(): Map<number, KlassenListeEintrag> {
		return this._state.value.mapKlassenVorigerAbschnitt;
	}

	get mapKlassenFolgenderAbschnitt(): Map<number, KlassenListeEintrag> {
		return this._state.value.mapKlassenFolgenderAbschnitt;
	}

	private async ladeSchuljahresabschnitt(idSchuljahresabschnitt : number) {
		const schuljahresabschnitt = api.mapAbschnitte.value.get(idSchuljahresabschnitt);
		if (schuljahresabschnitt === undefined)
			return;
		// Lade die Kataloge und erstelle den Manager
		const listKlassen = await api.server.getKlassenFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const mapKlassenVorigerAbschnitt = schuljahresabschnitt.idVorigerAbschnitt === null
			? new Map<number, KlassenListeEintrag>()
			: await api.getKlassenListe(schuljahresabschnitt.idVorigerAbschnitt);
		const mapKlassenFolgenderAbschnitt = schuljahresabschnitt.idFolgeAbschnitt === null
			? new Map<number, KlassenListeEintrag>()
			: await api.getKlassenListe(schuljahresabschnitt.idFolgeAbschnitt);
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const listLehrer = await api.server.getLehrer(api.schema);
		const klassenListeManager = new KlassenListeManager(api.schulform, listKlassen, listJahrgaenge, listLehrer);
		this.setPatchedDefaultState({ idSchuljahresabschnitt, klassenListeManager, mapKlassenVorigerAbschnitt, mapKlassenFolgenderAbschnitt });
	}

	public async setSchuljahresabschnitt(idSchuljahresabschnitt : number) {
		if (idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt)
			 return;
		await this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
	}

	public async setEintrag(klasse: KlassenListeEintrag | null) {
		if ((klasse === null) && (!this.klassenListeManager.hasDaten()))
			return;
		if ((klasse === null) || (this.klassenListeManager.liste.list().isEmpty())) {
			this.klassenListeManager.setDaten(null);
			this.commit();
			return;
		}
		if ((klasse !== null) && (this.klassenListeManager.hasDaten() && (klasse.id === this.klassenListeManager.auswahl().id)))
			return;
		let auswahl = this.klassenListeManager.liste.get(klasse.id);
		if (auswahl === null)
			auswahl = this.klassenListeManager.filtered().isEmpty() ? null : this.klassenListeManager.filtered().get(0);
		const daten = auswahl === null ? null : await api.server.getKlasse(api.schema, auswahl.id);
		this.klassenListeManager.setDaten(daten);
		this.commit();
	}

	patch = async (data : Partial<KlassenDaten>) => {
		if (!this.klassenListeManager.hasDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const idKlasse = this.klassenListeManager.auswahl().id;
		const daten = this.klassenListeManager.daten();
		if (daten === null)
			return;
		await api.server.patchKlasse(data, api.schema, idKlasse);
		Object.assign(daten, data);
		const eintrag = this.klassenListeManager.liste.get(idKlasse);
		if (eintrag != null) {
			if (data.kuerzel !== undefined)
				eintrag.kuerzel = data.kuerzel;
			if (data.sortierung !== undefined)
				eintrag.sortierung = data.sortierung;
		}
		this.klassenListeManager.setDaten(daten);
		this.commit();
	}

	gotoEintrag = async (eintrag: KlassenListeEintrag) => {
		await RouteManager.doRoute(this.view.getRoute(eintrag.id));
	}

	gotoSchueler = async (eintrag: Schueler) => {
		await RouteManager.doRoute(routeSchueler.getRoute(eintrag.id));
	}

	setFilter = async () => {
		if (!this.klassenListeManager.hasDaten()) {
			const listFiltered = this.klassenListeManager.filtered();
			if (!listFiltered.isEmpty()) {
				await this.gotoEintrag(listFiltered.get(0));
				return;
			}
		}
		this.commit();
	}

	setzeDefaultSortierung = async () => {
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		const auswahl_id = this.klassenListeManager.auswahl().id;
		await api.server.setKlassenSortierungFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		await this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
		await this.setEintrag(this.klassenListeManager.liste.get(auswahl_id));
	}

}
