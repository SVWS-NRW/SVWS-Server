import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import type { EinschulungsartKatalogEintrag, Erzieherart, FachDaten, Fahrschuelerart, Haltestelle, JahrgangsDaten, Kindergarten, List,
	OrtKatalogEintrag, OrtsteilKatalogEintrag, ReligionEintrag, SchuelerLernabschnittsdaten, SchulEintrag, Telefonart, VermerkartEintrag } from "@core";
import { DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeApp } from "~/router/apps/RouteApp";
import { SchuelerSchnelleingabeManager } from "../../../../../../ui/src/ui/manager/schueler/SchuelerSchnelleingabeManager";

interface RouteStateDataSchuelerSchnelleingabe extends RouteStateInterface {
	manager: SchuelerSchnelleingabeManager | undefined;
}

const defaultState = <RouteStateDataSchuelerSchnelleingabe> {
	manager: undefined,
};

export class RouteDataSchuelerSchnelleingabe extends RouteData<RouteStateDataSchuelerSchnelleingabe> {

	public constructor() {
		super(defaultState);
	}

	public async ladeDaten() {
		try {
			const manager = await this.createManager();
			this.setPatchedState({ manager });
		} catch (error) {
			throw new DeveloperNotificationException("Der SchuelerSchnelleingabeManager konnte nicht initialisiert werden.");
		}
	}

	private async createManager() {
		const idSchueler = routeSchueler.data.manager.auswahlID() ?? -1;
		const idSchuljahresabschnitt = routeSchueler.data.idSchuljahresabschnitt;
		const [stammdaten, schulbesuchsdaten, lernabschnitte, schuelerListe] =
			await Promise.all([
				api.server.getSchuelerStammdaten(api.schema, idSchueler),
				api.server.getSchuelerSchulbesuch(api.schema, idSchueler),
				api.server.getSchuelerLernabschnittsdatenByIdSchuelerAndIdJahresabschnitt(api.schema, idSchueler, idSchuljahresabschnitt),
				api.server.getSchuelerAuswahllisteFuerAbschnitt(api.schema, idSchuljahresabschnitt),
			]);
		const lernabschnitt = this.selectLernabschnitt(lernabschnitte);
		if (lernabschnitt === null) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schüler-Lernabschnittsdaten nicht initialisiert");
		}
		const schuljahresabschnitte = api.schuleStammdaten.abschnitte;
		const einschulungsartenById: Map<number, EinschulungsartKatalogEintrag> = routeApp.cache.kataloge.einschulungsartenById;
		const erzieherartenById: Map<number, Erzieherart> = routeApp.cache.kataloge.erzieherartenById;
		const faecherById: Map<number, FachDaten> = routeApp.cache.kataloge.faecherById;
		const fahrschuelerartenById: Map<number, Fahrschuelerart> = routeApp.cache.kataloge.fahrschuelerartenById;
		const haltestellenById: Map<number, Haltestelle> = routeApp.cache.kataloge.haltestellenById;
		const jahrgaengeById: Map<number, JahrgangsDaten> = routeApp.cache.kataloge.jahrgaengeById;
		const kindergaertenById: Map<number, Kindergarten> = routeApp.cache.kataloge.kindergaertenById;
		const orteById: Map<number, OrtKatalogEintrag> = routeApp.cache.kataloge.orteById;
		const ortsteileById: Map<number, OrtsteilKatalogEintrag> = routeApp.cache.kataloge.ortsteileById;
		const religionenById: Map<number, ReligionEintrag> = routeApp.cache.kataloge.religionenById;
		const schulenById: Map<number, SchulEintrag> = routeApp.cache.kataloge.schulenById;
		const telefonartenById: Map<number, Telefonart> = routeApp.cache.kataloge.telefonartenById;
		const vermerkartenById: Map<number, VermerkartEintrag> = routeApp.cache.kataloge.vermerkartenById;

		return new SchuelerSchnelleingabeManager(stammdaten, schulbesuchsdaten, lernabschnitt, schuelerListe, schuljahresabschnitte, einschulungsartenById, erzieherartenById,
			faecherById, fahrschuelerartenById, haltestellenById, jahrgaengeById, kindergaertenById, orteById, ortsteileById, religionenById, schulenById,
			telefonartenById, vermerkartenById);
	}

	private selectLernabschnitt(abschnitte: List<SchuelerLernabschnittsdaten>) {
		for (const l of abschnitte) {
			if (l.wechselNr === 0) {
				return l;
			}
		}
		if (!abschnitte.isEmpty()) {
			return abschnitte.get(abschnitte.size() - 1);
		}
		return null;
	}

	get manager(): SchuelerSchnelleingabeManager {
		if (this._state.value.manager === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: SchuelerSchnelleingabeManager nicht initialisiert");
		}
		return this._state.value.manager;
	}

}
