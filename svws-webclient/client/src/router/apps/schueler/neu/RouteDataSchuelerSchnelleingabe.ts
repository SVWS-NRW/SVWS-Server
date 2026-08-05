import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import type { EinschulungsartKatalogEintrag, Erzieherart, FachDaten, Fahrschuelerart, Haltestelle, JahrgangsDaten, Kindergarten, List,
	ReligionEintrag, SchuelerLernabschnittsdaten, SchuelerSchulbesuchsdaten, SchuelerTelefon, SchulEintrag,
	Telefonart, VermerkartEintrag, ErzieherStammdaten, SchuelerStammdaten, SchuelerVermerke } from "@core";
import { ArrayList, DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeApp } from "~/router/apps/RouteApp";
import { SchuelerSchnelleingabeManager } from "@ui";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";

interface RouteStateDataSchuelerSchnelleingabe extends RouteStateInterface {
	manager: SchuelerSchnelleingabeManager | undefined;
	erzieher: List<ErzieherStammdaten>;
	vermerke: List<SchuelerVermerke>;
	telefone: List<SchuelerTelefon>;
}

const defaultState = <RouteStateDataSchuelerSchnelleingabe> {
	manager: undefined,
	erzieher: new ArrayList(),
	vermerke: new ArrayList(),
	telefone: new ArrayList(),
};

export class RouteDataSchuelerSchnelleingabe extends RouteData<RouteStateDataSchuelerSchnelleingabe> {

	public constructor() {
		super(defaultState);
	}

	public async ladeDaten() {
		const idSchueler = routeSchueler.data.manager.auswahlID() ?? -1;
		const manager = await this.createManager(idSchueler);
		await this.createListen(idSchueler);
		this.setPatchedState({ manager });
	}

	private async createManager(idSchueler: number) {

		const idSchuljahresabschnitt = routeSchueler.data.manager.auswahl().idSchuljahresabschnitt;
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
		const schuljahresabschnitte = abschnittStateImpl.alle;
		const einschulungsartenById: Map<number, EinschulungsartKatalogEintrag> = routeApp.cache.kataloge.einschulungsartenById;
		const erzieherartenById: Map<number, Erzieherart> = routeApp.cache.kataloge.erzieherartenById;
		const faecherById: Map<number, FachDaten> = routeApp.cache.kataloge.faecherById;
		const fahrschuelerartenById: Map<number, Fahrschuelerart> = routeApp.cache.kataloge.fahrschuelerartenById;
		const haltestellenById: Map<number, Haltestelle> = routeApp.cache.kataloge.haltestellenById;
		const jahrgaengeById: Map<number, JahrgangsDaten> = routeApp.cache.kataloge.jahrgaengeById;
		const kindergaertenById: Map<number, Kindergarten> = routeApp.cache.kataloge.kindergaertenById;
		const religionenById: Map<number, ReligionEintrag> = routeApp.cache.kataloge.religionenById;
		const schulenById: Map<number, SchulEintrag> = routeApp.cache.kataloge.schulenById;
		const telefonartenById: Map<number, Telefonart> = routeApp.cache.kataloge.telefonartenById;
		const vermerkartenById: Map<number, VermerkartEintrag> = routeApp.cache.kataloge.vermerkartenById;

		return new SchuelerSchnelleingabeManager(stammdaten, schulbesuchsdaten, lernabschnitt, schuelerListe, schuljahresabschnitte, einschulungsartenById,
			erzieherartenById, faecherById, fahrschuelerartenById, haltestellenById, jahrgaengeById, kindergaertenById, religionenById,
			schulenById, telefonartenById, vermerkartenById);
	}

	private async createListen(idSchueler: number) {
		const [telefone, erzieher, vermerke] = await Promise.all([
			api.server.getSchuelerTelefone(api.schema, idSchueler),
			api.server.getSchuelerErzieher(api.schema, idSchueler),
			api.server.getVermerkdaten(api.schema, idSchueler),
		]);

		this._state.value.erzieher = erzieher;
		this._state.value.telefone = telefone;
		this._state.value.vermerke = vermerke;
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

	patchSchueler = async (data: Partial<SchuelerStammdaten>, id: number): Promise<void> => {
		await api.server.patchSchuelerStammdaten(data, api.schema, id);
		this.commit();
	};

	patchLernabschnittsdaten = async (data: Partial<SchuelerLernabschnittsdaten>, idEintrag: number): Promise<void> => {
		await api.server.patchSchuelerLernabschnittsdaten(data, api.schema, idEintrag);
		this.commit();
	};

	patchSchulbesuchsdaten = async (data: Partial<SchuelerSchulbesuchsdaten>, idEintrag: number): Promise<void> => {
		await api.server.patchSchuelerSchulbesuch(data, api.schema, idEintrag);
		this.commit();
	};

	// --- Erzieher ---

	get getErzieher(): List<ErzieherStammdaten> {
		const list = new ArrayList<ErzieherStammdaten>();
		list.addAll(this._state.value.erzieher);
		return list;
	}

	addErzieher = async (data: Partial<ErzieherStammdaten>, idEintrag: number, pos: number): Promise<ErzieherStammdaten> => {
		const result = await api.server.addSchuelerErzieher(data, api.schema, idEintrag, pos);
		const erzieher = this.getErzieher;
		erzieher.add(result);
		this.setPatchedState({ erzieher });
		return result;
	};

	patchErzieher = async (data: Partial<ErzieherStammdaten>, idEintrag: number) => {
		await api.server.patchErzieherStammdaten(data, api.schema, idEintrag);
		const erzieher = this.getErzieher;
		for (const e of erzieher) {
			if (e.id === idEintrag) {
				Object.assign(e, data);
				break;
			}
		}
		this.setPatchedState({ erzieher });
	};

	patchErzieherAnPosition = async (data: Partial<ErzieherStammdaten>, idEintrag: number, idSchueler: number, pos: number) => {
		await api.server.patchErzieherStammdatenZweitePosition(data, api.schema, idEintrag, pos);
		const erzieher = await api.server.getSchuelerErzieher(api.schema, idSchueler);
		this.setPatchedState({ erzieher });
	};

	deleteErzieher = async (ids: List<number>): Promise<void> => {
		await api.server.deleteErzieherStammdaten(ids, api.schema);
		const erzieher = this.getErzieher;
		for (const id of ids) {
			for (let i = 0; i < erzieher.size(); i++) {
				const eintrag = erzieher.get(i);
				if (eintrag.id === id) {
					erzieher.removeElementAt(i);
					break;
				}
			}
		}
		this.setPatchedState({ erzieher });
	};

	// --- Vermerke ---

	get getVermerke(): List<SchuelerVermerke> {
		const list = new ArrayList<SchuelerVermerke>();
		list.addAll(this._state.value.vermerke);
		return list;
	}

	addVermerk = async (data: Partial<SchuelerVermerke>): Promise<void> => {
		const result = await api.server.addVermerk(data, api.schema);
		const vermerke = this.getVermerke;
		vermerke.add(result);
		this.setPatchedState({ vermerke });
	};

	patchVermerk = async (data: Partial<SchuelerVermerke>, idEintrag: number): Promise<void> => {
		await api.server.patchSchuelerVermerke(data, api.schema, idEintrag);
		const vermerke = this.getVermerke;
		for (const l of vermerke) {
			if (l.id === idEintrag) {
				Object.assign(l, data);
				break;
			}
		}
		this.setPatchedState({ vermerke });
	};

	deleteVermerke = async (idsEintraege: List<number>): Promise<void> => {
		await api.server.deleteSchuelerVermerke(idsEintraege, api.schema);
		const vermerke = this.getVermerke;
		for (const id of idsEintraege) {
			for (let i = 0; i < vermerke.size(); i++) {
				const eintrag = vermerke.get(i);
				if (eintrag.id === id) {
					vermerke.removeElementAt(i);
					break;
				}
			}
		}
		this.setPatchedState({ vermerke });
	};

	// --- Telefone ---

	get getTelefone(): List<SchuelerTelefon> {
		const list = new ArrayList<SchuelerTelefon>();
		list.addAll(this._state.value.telefone);
		return list;
	}

	addTelefon = async (data: Partial<SchuelerTelefon>, idSchueler: number): Promise<void> => {
		const result = await api.server.addSchuelerTelefon(data, api.schema, idSchueler);
		const telefone = this.getTelefone;
		telefone.add(result);
		this.setPatchedState({ telefone });
	};

	patchTelefon = async (data: Partial<SchuelerTelefon>, idEintrag: number): Promise<void> => {
		await api.server.patchSchuelerTelefon(data, api.schema, idEintrag);
		const telefone = this.getTelefone;
		for (const l of telefone) {
			if (l.id === idEintrag) {
				Object.assign(l, data);
				break;
			}
		}
		this.setPatchedState({ telefone });
	};

	deleteTelefone = async (idsEintraege: List<number>): Promise<void> => {
		await api.server.deleteSchuelerTelefone(idsEintraege, api.schema);
		const telefone = this.getTelefone;
		for (const id of idsEintraege) {
			for (let i = 0; i < telefone.size(); i++) {
				const eintrag = telefone.get(i);
				if (eintrag.id === id) {
					telefone.removeElementAt(i);
					break;
				}
			}
		}
		this.setPatchedState({ telefone });
	};

}
