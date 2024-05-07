import type { Comparator, List, SchuelerListeEintrag, Sprachbelegung, Sprachpruefung} from "@core";
import { ArrayList, DeveloperNotificationException, JavaInteger } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";


interface RouteStateSchuelerLaufbahninfo extends RouteStateInterface {
	auswahl: SchuelerListeEintrag | undefined;
	sprachbelegungen: List<Sprachbelegung>;
	sprachpruefungen: List<Sprachpruefung>;
}

const defaultState = <RouteStateSchuelerLaufbahninfo> {
	auswahl: undefined,
	sprachbelegungen: new ArrayList<Sprachbelegung>(),
	sprachpruefungen: new ArrayList<Sprachpruefung>(),
};

export class RouteDataSchuelerLaufbahninfo extends RouteData<RouteStateSchuelerLaufbahninfo> {

	public constructor() {
		super(defaultState);
	}

	public async clear() {
		this.setPatchedDefaultState({});
	}

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zur Laufbahnplanung abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get sprachbelegungen(): List<Sprachbelegung> {
		return new ArrayList(this._state.value.sprachbelegungen);
	}

	get sprachpruefungen(): List<Sprachpruefung> {
		return new ArrayList(this._state.value.sprachpruefungen);
	}

	public async auswahlSchueler(auswahl: SchuelerListeEintrag | null) {
		if (auswahl === this._state.value.auswahl)
			return;
		if ((auswahl === null) || (auswahl === undefined)) {
			this.setPatchedDefaultState({});
			return;
		}
		try {
			api.status.start();
			const sprachbelegungen = await api.server.getSchuelerSprachbelegungen(api.schema, auswahl.id);
			const sprachpruefungen = await api.server.getSchuelerSprachpruefungen(api.schema, auswahl.id);
			sprachbelegungen.sort(<Comparator<Sprachbelegung>>{ compare(n1: Sprachbelegung, n2: Sprachbelegung) { return JavaInteger.compare(n1.reihenfolge ?? 0, n2.reihenfolge ?? 0); }})
			this.setPatchedState({ auswahl, sprachbelegungen, sprachpruefungen })
		} catch(error) {
			throw new DeveloperNotificationException("Die Laufbahninformationen konnten nicht eingeholt werden.")
		}
		api.status.stop();
	}

	getSprachbelegung(data: Partial<Sprachbelegung>) : Sprachbelegung | null {
		if ((data.sprache !== undefined) && (data.sprache !== ""))
			for (const b of this._state.value.sprachbelegungen)
				if (b.sprache === data.sprache)
					return b;
		return null;
	}

	patchSprachbelegung = async (data: Partial<Sprachbelegung>, sprache: string): Promise<void> => {
		const belegung : Sprachbelegung | null = this.getSprachbelegung({sprache});
		if (belegung === null)
			return;
		api.status.start();
		await api.server.patchSchuelerSprachbelegung(data, api.schema, this.auswahl.id, sprache);
		Object.assign(belegung, data);
		this.commit();
		api.status.stop();
	}

	addSprachbelegung = async (data: Partial<Sprachbelegung>): Promise<Sprachbelegung | null> => {
		// Prüfe, ob bereits eine Sprachbelegung für das angegeben Sprach-Kürzel existiert
		let belegung : Sprachbelegung | null = this.getSprachbelegung(data);
		if (belegung !== null)
			return null;
		// Füge die Sprachbelegung hinzu
		api.status.start();
		belegung = await api.server.addSchuelerSprachbelegung(data, api.schema, this.auswahl.id);
		this._state.value.sprachbelegungen.add(belegung);
		this.commit();
		api.status.stop();
		return belegung;
	}

	removeSprachbelegung = async (data: Sprachbelegung): Promise<Sprachbelegung> => {
		// Prüfe, ob die Sprachbelegung existiert
		const belegung : Sprachbelegung | null = this.getSprachbelegung(data);
		if (belegung === null)
			throw new DeveloperNotificationException("Die zu löschende Sprachbelegung existiert nicht.");
		// Entferne die Sprachbelegung
		api.status.start();
		const result = await api.server.deleteSchuelerSprachbelegung(api.schema, this.auswahl.id, belegung.sprache);
		const liste = this._state.value.sprachbelegungen;
		liste.removeElementAt(liste.indexOf(belegung));
		this.commit();
		api.status.stop();
		return result;
	}

	getSprachpruefung(data: Partial<Sprachpruefung>) : Sprachpruefung | null {
		if ((data.sprache === undefined) || (data.sprache === ""))
			return null;
		let pruefung : Sprachpruefung | null = null;
		for (const p of this._state.value.sprachpruefungen) {
			if (p.sprache === data.sprache) {
				pruefung = p;
				break;
			}
		}
		return pruefung;
	}

	patchSprachpruefung = async (data: Partial<Sprachpruefung>, sprache: string): Promise<void> => {
		const pruefung = this.getSprachpruefung({sprache});
		if ((pruefung === null) || (pruefung.sprache === null))
			return;
		api.status.start();
		await api.server.patchSchuelerSprachpruefung(data, api.schema, this.auswahl.id, pruefung.sprache);
		Object.assign(pruefung, data);
		this.commit();
		api.status.stop();
	}

	addSprachpruefung = async (data: Partial<Sprachpruefung>): Promise<Sprachpruefung | null> => {
		// Prüfe, ob bereits eine Sprachpruefung für das angegeben Sprach-Kürzel existiert
		let pruefung : Sprachpruefung | null = this.getSprachpruefung(data);
		if (pruefung !== null)
			return null;
		// Füge die Sprachprüfung hinzu
		api.status.start();
		pruefung = await api.server.addSchuelerSprachpruefung(data, api.schema, this.auswahl.id);
		this._state.value.sprachpruefungen.add(pruefung);
		this.commit();
		api.status.stop();
		return pruefung;
	}

	removeSprachpruefung = async (data: Sprachpruefung): Promise<Sprachpruefung> => {
		// Prüfe, ob die Sprachprüfung existiert
		const pruefung : Sprachpruefung | null = this.getSprachpruefung(data);
		if ((pruefung === null) || (pruefung.sprache === null))
			throw new DeveloperNotificationException("Die zu löschende Sprachpruefung existiert nicht.");
		// Entferne die Sprachprüfung
		api.status.start();
		const result = await api.server.deleteSchuelerSprachpruefung(api.schema, this.auswahl.id, pruefung.sprache);
		const liste = this._state.value.sprachpruefungen;
		liste.removeElementAt(liste.indexOf(pruefung));
		this.commit();
		api.status.stop();
		return result;
	}

}

