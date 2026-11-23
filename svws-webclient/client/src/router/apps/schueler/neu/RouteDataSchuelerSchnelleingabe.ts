import type { SchuelerLernabschnittsdaten } from "@core/asd/data/schueler/SchuelerLernabschnittsdaten";
import type { SchuelerSchulbesuchsdaten } from "@core/asd/data/schueler/SchuelerSchulbesuchsdaten";
import type { ErzieherStammdaten } from "@core/core/data/erzieher/ErzieherStammdaten";
import type { SchuelerVermerke } from "@core/core/data/schueler/SchuelerVermerke";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { SchuelerSchnelleingabeManager } from "@ui/ui/manager/schueler/SchuelerSchnelleingabeManager";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { useSchuelerAuswahlState } from "~/states/schueler/SchuelerAuswahlState";

interface RouteStateDataSchuelerSchnelleingabe extends RouteStateInterface {
	manager: SchuelerSchnelleingabeManager | undefined;
	erzieher: List<ErzieherStammdaten>;
	vermerke: List<SchuelerVermerke>;
}

const defaultState = <RouteStateDataSchuelerSchnelleingabe> {
	manager: undefined,
	erzieher: new ArrayList(),
	vermerke: new ArrayList(),
};

export class RouteDataSchuelerSchnelleingabe extends RouteData<RouteStateDataSchuelerSchnelleingabe> {

	public constructor() {
		super(defaultState);
	}

	public async ladeDaten() {
		const schuelerAuswahlState = useSchuelerAuswahlState();
		const idSchueler = schuelerAuswahlState.idSelected ?? -1;
		const manager = await this.createManager(idSchueler);
		await this.createListen(idSchueler);
		this.setPatchedState({ manager });
	}

	private async createManager(idSchueler: number) {
		const schuelerAuswahlState = useSchuelerAuswahlState();
		const idSchuljahresabschnitt = schuelerAuswahlState.selected.idSchuljahresabschnitt;
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

		return new SchuelerSchnelleingabeManager(
			stammdaten,
			schulbesuchsdaten,
			lernabschnitt,
			schuelerListe,
			schuljahresabschnitte
		);
	}

	private async createListen(idSchueler: number) {
		const [erzieher, vermerke] = await Promise.all([
			api.server.getSchuelerErzieher(api.schema, idSchueler),
			api.server.getVermerkdaten(api.schema, idSchueler),
		]);

		this._state.value.erzieher = erzieher;
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

	patchLernabschnittsdaten = async (data: Partial<SchuelerLernabschnittsdaten>, idEintrag: number): Promise<boolean> => {
		await api.server.patchSchuelerLernabschnittsdaten(data, api.schema, idEintrag);
		Object.assign(this.manager.lernabschnittsdaten, data);
		if (data.klassenID !== undefined) {
			// Die Klasse steht auch im Eintrag der Schülerliste und muss dort nachgezogen werden
			const idKlasse = data.klassenID ?? -1;
			useSchuelerAuswahlState().updateEntry(this.manager.stammdaten.id, eintrag => eintrag.idKlasse = idKlasse);
		}
		this.commit();
		return true;
	};

	patchSchulbesuchsdaten = async (data: Partial<SchuelerSchulbesuchsdaten>, idEintrag: number): Promise<boolean> => {
		await api.server.patchSchuelerSchulbesuch(data, api.schema, idEintrag);
		Object.assign(this.manager.schulbesuchsdaten, data);
		this.commit();
		return true;
	};

	// --- Erzieher ---

	get getErzieher(): List<ErzieherStammdaten> {
		const list = new ArrayList<ErzieherStammdaten>();
		list.addAll(this._state.value.erzieher);
		return list;
	}

	addErzieher = async (data: Partial<ErzieherStammdaten>, pos: number): Promise<ErzieherStammdaten> => {
		const idSchueler = this.manager.stammdaten.id;
		const result = await api.server.addSchuelerErzieher(data, api.schema, idSchueler, pos);
		const erzieher = this.getErzieher;
		erzieher.add(result);
		this.setPatchedState({ erzieher });
		return result;
	};

	patchErzieher = async (data: Partial<ErzieherStammdaten>, id: number): Promise<boolean> => {
		await api.server.patchErzieherStammdaten(data, api.schema, id);
		const erzieher = this.getErzieher;
		for (const e of erzieher) {
			if (e.id === id) {
				Object.assign(e, data);
				break;
			}
		}
		this.setPatchedState({ erzieher });
		return true;
	};

	patchErzieherAnPosition = async (data: Partial<ErzieherStammdaten>, id: number, pos: number): Promise<boolean> => {
		const idSchueler = this.manager.stammdaten.id;
		await api.server.patchErzieherStammdatenZweitePosition(data, api.schema, id, pos);
		const erzieher = await api.server.getSchuelerErzieher(api.schema, idSchueler);
		this.setPatchedState({ erzieher });
		return true;
	};

	deleteErzieher = async (ids: List<number>): Promise<boolean> => {
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
		return true;
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

	patchVermerk = async (data: Partial<SchuelerVermerke>, idEintrag: number): Promise<boolean> => {
		await api.server.patchSchuelerVermerke(data, api.schema, idEintrag);
		const vermerke = this.getVermerke;
		for (const l of vermerke) {
			if (l.id === idEintrag) {
				Object.assign(l, data);
				break;
			}
		}
		this.setPatchedState({ vermerke });
		return true;
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

}
