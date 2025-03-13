import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeKatalogZeitraster } from "./kataloge/RouteKatalogZeitraster";
import { api } from "~/router/Api";
import { StundenplanListeEintrag } from "@core";
import { routeApp } from "../RouteApp";
import { RouteManager } from "~/router/RouteManager";
import { routeStundenplan } from "./RouteStundenplan";

interface RouteStateStundenplanKataloge extends RouteStateInterface {
	idSchuljahresabschnitt: number;
	auswahl: StundenplanListeEintrag | undefined;
	mapKatalogeintraege: Map<number, StundenplanListeEintrag>;
}

const defaultState = <RouteStateStundenplanKataloge> {
	idSchuljahresabschnitt: -1,
	auswahl: undefined,
	mapKatalogeintraege: new Map(),
	view: routeKatalogZeitraster,
};

export class RouteDataStundenplanKataloge extends RouteData<RouteStateStundenplanKataloge> {

	private vorlageEintrag : StundenplanListeEintrag;

	public constructor() {
		super(defaultState);
		this.vorlageEintrag = new StundenplanListeEintrag();
		this.vorlageEintrag.bezeichnung = "Allgemeine Vorlagen";
		this.vorlageEintrag.schuljahr = -1;
	}

	get auswahl(): StundenplanListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapKatalogeintraege;
	}

	private async ladeSchuljahresabschnitt(idSchuljahresabschnitt : number) : Promise<void> {
		api.status.start();
		const listKatalogeintraege = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const mapKatalogeintraege = new Map<number, StundenplanListeEintrag>();
		mapKatalogeintraege.set(this.vorlageEintrag.id, this.vorlageEintrag);
		const auswahl = this.vorlageEintrag;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ idSchuljahresabschnitt, auswahl, mapKatalogeintraege })
		api.status.stop();
	}

	public async setSchuljahresabschnitt(idSchuljahresabschnitt : number) : Promise<void> {
		if (idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt)
			return;
		await this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
	}

	addEintrag = async () => {
		api.status.start();
		const eintrag = await api.server.addStundenplan(api.schema, routeApp.data.idSchuljahresabschnitt);
		this.mapKatalogeintraege.set(eintrag.id, eintrag)
		api.status.stop();
		await this.gotoEintrag(eintrag);
	}

	removeEintraege = async (eintraege: StundenplanListeEintrag[]) => {
		api.status.start();
		for (const eintrag of eintraege) {
			if (this._state.value.auswahl?.id === eintrag.id)
				this._state.value.auswahl = undefined;
			await api.server.deleteStundenplan(api.schema, eintrag.id);
			this.mapKatalogeintraege.delete(eintrag.id);
		}
		if ((this.auswahl === undefined) && (this.mapKatalogeintraege.size > 0))
			this._state.value.auswahl = this.mapKatalogeintraege.values().next().value;
		api.status.stop();
		await this.gotoEintrag(this.auswahl);
	}

	gotoEintrag = async (eintrag?: StundenplanListeEintrag) => await RouteManager.doRoute(routeStundenplan.getRoute({ id: eintrag?.id }));

}
