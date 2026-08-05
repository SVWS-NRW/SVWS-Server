import { BenutzerKompetenz, DeveloperNotificationException, ENMv2Daten, type ENMv2Klasse, BenutzerTyp, OpenApiError, type List, ArrayList, type ENMv2Leistung, type ENMv2Teilleistung, type ENMv2LeistungBemerkungen, type ENMv2Lernabschnitt, type ENMv2SchuelerAnkreuzkompetenz } from "@core";
import { EnmManager, EnmSpaltenManager, EnmSperrManager, StateManager, type EnmLerngruppenAuswahlEintrag, type NotenmodulState } from "@ui";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { benutzerStateImpl } from "./BenutzerStateImpl";

interface NotenmodulReactiveState {
	// Die ENM-Daten, welche für den angemeldeten Lehrer-Benutzer über die API geladen werden
	daten: ENMv2Daten | null;

	// Der Manager für die ENM-Daten, welche für den angemeldeten Lehrer-Benutzer über die API geladen werden
	manager: EnmManager | null;

	// Die aktuell ausgewählten Lerngruppen bei den Ansichten für Leistungen und Teilleistungen (bei Mehrfachauswahl)
	auswahlLerngruppen: Array<EnmLerngruppenAuswahlEintrag>;

	// Die aktuell ausgewählte Lerngruppe bei den Ansichten für Leistungen und Teilleistungen (bei Einzelauswahl)
	auswahlLerngruppe: EnmLerngruppenAuswahlEintrag | null;

	// Die aktuell ausgewählten Klassen bei der Ansicht für die Klassenleitung (bei Mehrfachauswahl)
	auswahlKlassen: Array<ENMv2Klasse>;

	// Die aktuell ausgewählte Klasse bei der Ansicht für die Klassenleitung (bei Einzelauswahl)
	auswahlKlasse: ENMv2Klasse | null;

	// Dia aktuell ausgewählte Ansicht, admin oder lehrer, null, wenn der Nutzer nicht wechseln darf
	istAdminLehrer: boolean | null;
}

/**
 * Die Schnittstelle für den Zustand des Notenmoduls
 */
export class NotenmodulStateImpl extends StateManager<NotenmodulReactiveState> implements NotenmodulState {

	public constructor() {
		super({
			daten: null,
			manager: null,
			auswahlLerngruppen: [],
			auswahlLerngruppe: null,
			auswahlKlassen: [],
			auswahlKlasse: null,
			istAdminLehrer: null,
		});
	}

	public async ladeDaten(reload = false) {
		if ((this._state.value.daten !== null) && !reload) {
			return;
		}
		api.status.start();
		const patchedState = <Partial<NotenmodulReactiveState>>{ daten: null, manager: null, auswahlKlassen: [], auswahlLerngruppen: [], istAdminLehrer: this.istAdminLehrer };
		if ((patchedState.istAdminLehrer === null) && (benutzerStateImpl.benutzertyp === BenutzerTyp.LEHRER) && benutzerStateImpl.benutzerHatEineKompetenz([BenutzerKompetenz.NOTENMODUL_ADMINISTRATION])) {
			patchedState.istAdminLehrer = true;
		}
		try {
			if (!benutzerStateImpl.istAdmin && !benutzerStateImpl.benutzerHatEineKompetenz([
				BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
				BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION,
				BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN,
				BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION])) {
				throw new DeveloperNotificationException("Der Benutzer hat keine Berechtigung, um auf das Notenmodul zuzugreifen. Diese Stelle sollte daher nicht erreichbar sein und es handelt sich um einen Programmierfehler.");
			}
			if ((benutzerStateImpl.benutzertyp === BenutzerTyp.LEHRER) && (patchedState.istAdminLehrer !== true)) {
				patchedState.daten = await api.server.getLehrerENMv2Daten(api.schema, benutzerStateImpl.benutzerIDLehrer);
			} else {
				patchedState.daten = await api.server.getENMv2Daten(api.schema);
			}
			patchedState.manager = new EnmManager(patchedState.daten);
			const lerngruppen = patchedState.manager.mapLerngruppenAuswahl.values();
			patchedState.auswahlLerngruppe = lerngruppen.isEmpty() ? null : lerngruppen.iterator().next();
			const klassen = patchedState.manager.listKlassenKlassenlehrer;
			patchedState.auswahlKlasse = klassen.isEmpty() ? null : klassen.getFirst();

			const config = await api.server.getNotenmodulLocalClientConfig(api.schema);
			let jsonSperrungen = "[]";
			let jsonSpalten = "[]";
			for (const element of config) {
				if (element.key === "noteneingabe.gesperrt") {
					jsonSperrungen = element.value;
				}	else if (element.key === "table.columns") {
					jsonSpalten = element.value;
				}
			}
			patchedState.manager.sperrungen = new EnmSperrManager(jsonSperrungen);
			patchedState.manager.spalten = new EnmSpaltenManager(jsonSpalten);

		} catch (error) {
			if ((error instanceof OpenApiError) && (error.response instanceof Response) && (error.response.status === 404)) {
				patchedState.daten = new ENMv2Daten();
				patchedState.manager = new EnmManager(patchedState.daten);
			}
		} finally {
			this.setPatchedState(patchedState);
			api.status.stop();
		}
	}


	public async toggleAdmin() {
		let istAdminLehrer = this.istAdminLehrer;
		if ((istAdminLehrer === false) && benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.NOTENMODUL_ADMINISTRATION)) {
			istAdminLehrer = true;
		} else if (istAdminLehrer === true) {
			istAdminLehrer = false;
		} else {
			istAdminLehrer = null;
		}
		this.setPatchedState({ istAdminLehrer });
		const routeName = RouteManager.instance.getRouteNode()?.name ?? "";
		if (routeName.startsWith("notenmodul.administration") || routeName.startsWith("notenmodul.zugangsdaten")) {
			await RouteManager.doRoute({ name: "notenmodul.leistungen" });
		}
		await this.ladeDaten(true);
	}

	public get istAdminLehrer(): boolean | null {
		return this._state.value.istAdminLehrer;
	}

	public get manager(): EnmManager {
		if (this._state.value.manager === null) {
			throw new DeveloperNotificationException("Die ENM-Daten wurden nicht geladen.");
		}
		return this._state.value.manager;
	}

	public get idsLehrer(): List<number> {
		const result = new ArrayList<number>();
		if (this._state.value.manager === null) {
			return result;
		}
		for (const lehrer of this._state.value.manager.daten.lehrer) {
			result.add(lehrer.id);
		}
		return result;
	}

	public get hideAnkreuzkompetenzen(): boolean {
		if (this._state.value.manager === null) {
			return true;
		}
		return this._state.value.manager.listKlassenMitAnkreuzkompetenzen.isEmpty();
	}

	/**
	 * Gibt die aktuelle Lerngruppen-Auswahl für die Ansicht der Leistungen und Teilleistungen zurück. (die Einzelauswahl)
	 *
	 * @returns die Lerngruppen-Auswahl
	 */
	get auswahlLerngruppe(): EnmLerngruppenAuswahlEintrag | null {
		return this._state.value.auswahlLerngruppe;
	}

	/**
	 * Gibt die aktuelle Lerngruppen-Auswahl für die Ansichten der Leistungen und Teilleistungen zurück.
	 * (die Einzelauswahl oder die Mehrfachauswahl)
	 *
	 * @returns die Lerngruppen-Auswahl
	 */
	get auswahlLerngruppen(): Array<EnmLerngruppenAuswahlEintrag> {
		if (this._state.value.auswahlLerngruppe === null) {
			return this._state.value.auswahlLerngruppen;
		}
		return [this._state.value.auswahlLerngruppe];
	}

	/**
	 * Gibt die aktuelle Lerngruppen-Auswahl für die Ansichten der Leistungen und Teilleistungen zurück. (nur die Mehrfachauswahl)
	 *
	 * @returns die Lerngruppen-Auswahl
	 */
	get auswahlLerngruppenNurMehrfachauswahl(): Array<EnmLerngruppenAuswahlEintrag> {
		return this._state.value.auswahlLerngruppen;
	}

	/**
	 * Setzt die Auswahl der Lerngruppe (bei Einzelauswahl)
	 *
	 * @param value   die neue Auswahl
	 */
	public setAuswahlLerngruppe = (value: EnmLerngruppenAuswahlEintrag | null) => {
		this._state.value.auswahlLerngruppe = value;
		this.commit();
	};

	/**
	 * Setzt die Auswahl der Lerngruppen (bei Mehrfachauswahl)
	 *
	 * @param value   die neue Auswahl
	 */
	public setAuswahlLerngruppen = (value: Array<EnmLerngruppenAuswahlEintrag>) => {
		this._state.value.auswahlLerngruppen = value;
		this.commit();
	};

	/**
	 * Gibt die aktuelle Klassen-Auswahl für die Ansicht der Klassenleitungen zurück. (die Einzelauswahl)
	 *
	 * @returns die Klassen-Auswahl
	 */
	get auswahlKlasse(): ENMv2Klasse | null {
		return this._state.value.auswahlKlasse;
	}

	/**
	 * Gibt die aktuelle Klassen-Auswahl für die Ansicht der Klassenleitungen zurück.
	 * (die Einzelauswahl oder die Mehrfachauswahl)
	 *
	 * @returns die Klassen-Auswahl
	 */
	get auswahlKlassen(): Array<ENMv2Klasse> {
		if (this._state.value.auswahlKlasse === null) {
			return this._state.value.auswahlKlassen;
		}
		return [this._state.value.auswahlKlasse];
	}

	/**
	 * Gibt die aktuelle Klassen-Auswahl für die Ansicht der Klassenleitungen zurück. (nur die Mehrfachauswahl)
	 *
	 * @returns die Klassen-Auswahl
	 */
	get auswahlKlassenNurMehrfachauswahl(): Array<ENMv2Klasse> {
		return this._state.value.auswahlKlassen;
	}

	/**
	 * Setzt die Auswahl der Klasse (bei Einzelauswahl)
	 *
	 * @param value   die neue Auswahl
	 */
	public setAuswahlKlasse = (value: ENMv2Klasse | null) => {
		this._state.value.auswahlKlasse = value;
		this.commit();
	};

	/**
	 * Setzt die Auswahl der Klassen (bei Mehrfachauswahl)
	 *
	 * @param value   die neue Auswahl
	 */
	public setAuswahlKlassen = (value: Array<ENMv2Klasse>) => {
		this._state.value.auswahlKlassen = value;
		this.commit();
	};

	/**
	 * Passt die übergebenen Leistungsdaten an.
	 *
	 * @param patch   der Patch für die Leistungsdaten
	 *
	 * @returns true im Erfolgsfall und ansonsten false
	 */
	public patchLeistung = async (data: ENMv2Leistung, patch: Partial<ENMv2Leistung>): Promise<void> => {
		patch.id = data.id;
		await api.server.patchENMLeistung(patch, api.schema);
		Object.assign(data, patch);
		this.commit();
	};

	/**
	 * Passt die übergebenen Teilleistungen an.
	 *
	 * @param patch   der Patch für die Teilleistungen
	 *
	 * @returns true im Erfolgsfall und ansonsten false
	 */
	public patchTeilleistung = async (data: ENMv2Teilleistung, patch: Partial<ENMv2Teilleistung>): Promise<void> => {
		patch.id = data.id;
		await api.server.patchENMTeilleistung(patch, api.schema);
		Object.assign(data, patch);
		this.commit();
	};

	/**
	 * Passt die übergebenen Bemerkungen zu dem Lernabschnitt an.
	 *
	 * @param patch   der Patch für die Bemerkungen zu dem Lernabschnitt
	 *
	 * @returns true im Erfolgsfall und ansonsten false
	 */
	public patchBemerkungen = async (id: number, data: ENMv2LeistungBemerkungen, patch: Partial<ENMv2LeistungBemerkungen>): Promise<void> => {
		await api.server.patchENMSchuelerBemerkungen(patch, api.schema, id);
		Object.assign(data, patch);
		this.commit();
	};

	/**
	 * Passt die übergebenen Lernabschnittsdaten an.
	 *
	 * @param patch   der Patch für die Lernabschnittsdaten
	 *
	 * @returns true im Erfolgsfall und ansonsten false
	 */
	public patchLernabschnitt = async (data: ENMv2Lernabschnitt, patch: Partial<ENMv2Lernabschnitt>): Promise<void> => {
		patch.id = data.id;
		await api.server.patchENMSchuelerLernabschnitt(patch, api.schema);
		Object.assign(data, patch);
		this.commit();
	};

	/**
	 * Passt die übergebenen Ankreuzkompetenzen an.
	 *
	 * @param patch   der Patch für die Ankreuzkompetenzen
	 *
	 * @returns true im Erfolgsfall und ansonsten false
	 */
	public patchAnkreuzkompetenz = async (data: ENMv2SchuelerAnkreuzkompetenz, patch: Partial<ENMv2SchuelerAnkreuzkompetenz>): Promise<void> => {
		patch.id = data.id;
		console.log(patch, 'für ID', data.id);
		await api.server.patchENMSchuelerAnkreuzkompetenz(patch, api.schema);
		Object.assign(data, patch);
		this.commit();
	};

}

export const notenmodulStateImpl = new NotenmodulStateImpl();
