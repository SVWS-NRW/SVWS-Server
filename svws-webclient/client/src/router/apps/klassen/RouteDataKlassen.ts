
import type { KlassenDaten, KlassenListeEintrag, Schueler} from "@core";
import { ArrayList, DeveloperNotificationException, KlassenListeManager } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { type RouteNode } from "~/router/RouteNode";

import { routeKlassen } from "~/router/apps/klassen/RouteKlassen";
import { routeKlasseDaten } from "~/router/apps/klassen/RouteKlasseDaten";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";


interface RouteStateKlassen extends RouteStateInterface {
	idSchuljahresabschnitt: number;
	klassenListeManager: KlassenListeManager;
}

const defaultState = <RouteStateKlassen> {
	idSchuljahresabschnitt: -1,
	klassenListeManager: new KlassenListeManager(null, new ArrayList(), new ArrayList(), new ArrayList()),
	view: routeKlasseDaten,
};

export class RouteDataKlassen extends RouteData<RouteStateKlassen> {

	public constructor() {
		super(defaultState);
	}

	get klassenListeManager(): KlassenListeManager {
		return this._state.value.klassenListeManager;
	}

	public async setSchuljahresabschnitt(idSchuljahresabschnitt : number) {
		if (idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt)
			 return;
		// Lade die Kataloge und erstelle den Manager
		const listKlassen = await api.server.getKlassenFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const listLehrer = await api.server.getLehrer(api.schema);
		const klassenListeManager = new KlassenListeManager(api.schulform, listKlassen, listJahrgaenge, listLehrer);
		this.setPatchedDefaultState({ idSchuljahresabschnitt, klassenListeManager });
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
		const auswahl = this.klassenListeManager.liste.get(idKlasse);
		if (auswahl != null) {
			if (data.kuerzel !== undefined)
				auswahl.kuerzel = data.kuerzel;
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

}
