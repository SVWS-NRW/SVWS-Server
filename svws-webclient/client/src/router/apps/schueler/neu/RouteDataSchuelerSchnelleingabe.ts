import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import type { SchuelerListeEintrag, SchuelerStammdaten, SchulEintrag, SchulformKatalogEintrag, SchuelerLernabschnittListeEintrag, Merkmal, KatalogEntlassgrund,
	List } from "@core";
import { ArrayList, DeveloperNotificationException, Schulform, SchuelerNeu } from "@core";
import { api } from "~/router/Api";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { SchuelerSchulbesuchManager } from "~/components/schueler/schulbesuch/SchuelerSchulbesuchManager";
import { routeSchuelerSchulbesuch } from "~/router/apps/schueler/schulbesuch/RouteSchuelerSchulbesuch";
import { SchuelerLernabschnittManager } from "~/components/schueler/lernabschnitte/SchuelerLernabschnittManager";

interface RouteStateDataSchuelerSchnelleingabe extends RouteStateInterface {
	schulenById: Map<string, SchulEintrag>;
	schuelerSchulbesuchManager: SchuelerSchulbesuchManager | undefined;
	schuelerLernabschnittsManager: SchuelerLernabschnittManager | undefined;
}

const defaultState = <RouteStateDataSchuelerSchnelleingabe> {
	schulenById: new Map<string, SchulEintrag>(),
	schuelerSchulbesuchManager: undefined,
	schuelerLernabschnittsManager: undefined,
};

export class RouteDataSchuelerSchnelleingabe extends RouteData<RouteStateDataSchuelerSchnelleingabe> {

	public constructor() {
		super(defaultState);
	}

	public async ladeKataloge(): Promise<void> {
		const schulen = await api.server.getSchulen(api.schema);
		const schulenById = new Map<string, SchulEintrag>();
		for (const schule of schulen) {
			if (schule.schulnummerStatistik === null) {
				continue;
			}
			const sfEintrag: SchulformKatalogEintrag | null = schule.idSchulform === null ? null : Schulform.data().getEintragByID(schule.idSchulform);
			const sf: Schulform | null = sfEintrag === null ? null : Schulform.data().getWertBySchluessel(sfEintrag.schluessel);
			if (sf === api.schulform) {
				schulenById.set(schule.schulnummerStatistik, schule);
			}
		}
		this.setPatchedState({ schulenById });

	}

	private async createSchuelerSchulbesuchManager(auswahl: SchuelerListeEintrag): Promise<SchuelerSchulbesuchManager> {
		const [schuelerSchulbesuchsdaten, kindergaerten, jahrgaenge] =
			await Promise.all([
				api.server.getSchuelerSchulbesuch(api.schema, auswahl.id),
				api.server.getKindergaerten(api.schema),
				api.server.getJahrgangsdaten(api.schema),
			]);
		const schulen = new ArrayList<SchulEintrag>();
		const merkmale = new ArrayList<Merkmal>();
		const entlassgruende = new ArrayList<KatalogEntlassgrund>();
		return new SchuelerSchulbesuchManager(schuelerSchulbesuchsdaten, auswahl, api.schuleStammdaten.abschnitte, schulen, merkmale, entlassgruende,
			kindergaerten, jahrgaenge, routeSchuelerSchulbesuch.data.patch);
	}

	private selectBevorzugtenAbschnitt(listAbschnitte: List<SchuelerLernabschnittListeEintrag>): SchuelerLernabschnittListeEintrag | null {
		for (const a of listAbschnitte) {
			if ((a.schuljahresabschnitt === routeSchueler.data.idSchuljahresabschnitt) && (a.wechselNr === 0)) {
				return a;
			}
		}
		if (!listAbschnitte.isEmpty()) {
			return listAbschnitte.get(listAbschnitte.size() - 1);
		}
		return null;
	}

	public async ladeDaten(auswahl: SchuelerListeEintrag | null): Promise<SchuelerStammdaten | null> {
		if (auswahl === null) {
			return null;
		}
		const [schuelerStammdaten, schuelerSchulbesuchManager, listAbschnitte] =
			await Promise.all([
				api.server.getSchuelerStammdaten(api.schema, auswahl.id),
				this.createSchuelerSchulbesuchManager(auswahl),
				api.server.getSchuelerLernabschnittsliste(api.schema, auswahl.id),
			]);
		// wähle bevorzugt einen Eintrag für den aktuellen Schuljahresabschnitt, WechselNr = 0, sonst letzten Eintrag
		let schuelerLernabschnittsManager: SchuelerLernabschnittManager | undefined = undefined;
		const found = this.selectBevorzugtenAbschnitt(listAbschnitte);
		if (found !== null) {
			const [daten, listFaecher, listJahrgaenge] = await Promise.all([
				api.server.getSchuelerLernabschnittsdatenByID(api.schema, found.id),
				api.server.getFaecher(api.schema),
				api.server.getJahrgaenge(api.schema),
			]);
			const schuljahresabschnitt = api.mapAbschnitte.value.get(daten.schuljahresabschnitt);
			if (schuljahresabschnitt !== undefined) {
				schuelerLernabschnittsManager = new SchuelerLernabschnittManager(
					api.schulform, auswahl, daten, schuljahresabschnitt,
					listFaecher, new ArrayList(), listJahrgaenge,
					new ArrayList(), new ArrayList(), new ArrayList()
				);
			}
		}

		this.setPatchedState({ schuelerSchulbesuchManager, schuelerLernabschnittsManager });
		return schuelerStammdaten;
	}

	public async ladeInitialeDatenFuerWeiterenSchueler(auswahl: SchuelerListeEintrag | null): Promise<any> {
		if (auswahl === null) {
			return null;
		}
		const schuelerDaten: SchuelerNeu = new SchuelerNeu();
		const [schuelerStammdaten, listAbschnitte] =
			await Promise.all([
				api.server.getSchuelerStammdaten(api.schema, auswahl.id),
				api.server.getSchuelerLernabschnittsliste(api.schema, auswahl.id),
			]);

		schuelerDaten.anmeldedatum = schuelerStammdaten.anmeldedatum;
		schuelerDaten.aufnahmedatum = schuelerStammdaten.aufnahmedatum;
		schuelerDaten.beginnBildungsgang = schuelerStammdaten.beginnBildungsgang;
		schuelerDaten.dauerBildungsgang = schuelerStammdaten.dauerBildungsgang;

		// wähle bevorzugt einen Eintrag für den aktuellen Schuljahresabschnitt, WechselNr = 0, sonst letzten Eintrag
		const found = this.selectBevorzugtenAbschnitt(listAbschnitte);
		if (found !== null) {
			const daten = await api.server.getSchuelerLernabschnittsdatenByID(api.schema, found.id);
			schuelerDaten.idSchuljahresabschnitt = daten.schuljahresabschnitt;
			schuelerDaten.idJahrgang = daten.jahrgangID;
			schuelerDaten.idKlasse = daten.klassenID;
		}
		return schuelerDaten;
	}

	get mapSchulen(): Map<string, SchulEintrag> {
		return this._state.value.schulenById;
	}

	get schuelerLernabschnittManager(): SchuelerLernabschnittManager {
		if (this._state.value.schuelerLernabschnittsManager === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schüler-Lernabschnittsdaten nicht initialisiert");
		}
		return this._state.value.schuelerLernabschnittsManager;
	}

	get schuelerSchulbesuchManager(): SchuelerSchulbesuchManager {
		if (this._state.value.schuelerSchulbesuchManager === undefined) {
			throw new DeveloperNotificationException("SchülerSchulbesuchManager nicht initialisiert.");
		}
		return this._state.value.schuelerSchulbesuchManager;
	}
}
