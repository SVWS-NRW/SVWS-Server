import type { ENMKlasse, ENMLeistung, ENMLeistungBemerkungen, ENMLernabschnitt, ENMTeilleistung } from "@core";
import { ENMDaten} from "@core";
import { BenutzerKompetenz, BenutzerTyp, DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeNotenmodulLeistungen } from "./RouteNotenmodulLeistungen";
import type { EnmLerngruppenAuswahlEintrag} from "@ui";
import { EnmManager } from "@ui";


interface RouteStateNotenmodul extends RouteStateInterface {
	// Die ENM-Daten, welche für den angemeldeten Lehrer-Benutzer über die API geladen werden
	daten: ENMDaten | null;

	// Der Manager für die ENM-Daten, welche für den angemeldeten Lehrer-Benutzer über die API geladen werden
	manager: EnmManager | null;

	// Die aktuell ausgewählten Lerngruppen bei den Ansichten für Leistungen und Teilleistungen (bei Mehrfachauswahl)
	auswahlLerngruppen: Array<EnmLerngruppenAuswahlEintrag>;

	// Die aktuell ausgewählte Lerngruppe bei den Ansichten für Leistungen und Teilleistungen (bei Einzelauswahl)
	auswahlLerngruppe: EnmLerngruppenAuswahlEintrag | null;

	// Die aktuell ausgewählten Klassen bei der Ansicht für die Klassenleitung (bei Mehrfachauswahl)
	auswahlKlassen: Array<ENMKlasse>;

	// Die aktuell ausgewählte Klasse bei der Ansicht für die Klassenleitung (bei Einzelauswahl)
	auswahlKlasse: ENMKlasse | null;
}


export class RouteDataNotenmodul extends RouteData<RouteStateNotenmodul> {

	public constructor() {
		super(<RouteStateNotenmodul>{
			daten: null,
			manager: null,
			view: routeNotenmodulLeistungen,
			auswahlLerngruppen: [],
			auswahlLerngruppe: null,
			auswahlKlassen: [],
			auswahlKlasse: null,
		});
	}

	public async ladeDaten() {
		const patchedState = <Partial<RouteStateNotenmodul>>{};
		try {
			if (!api.benutzerIstAdmin && !api.benutzerHatEineKompetenz([
				BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
				BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION,
				BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN,
				BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION ])) {
				throw new DeveloperNotificationException("Der Benutzer hat keine Berechtigung, um auf das Notenmodul zuzugreifen. Diese Stelle sollte daher nicht erreichbar sein und es handelt sich um einen Programmierfehler.");
			}
			if (api.benutzertyp === BenutzerTyp.LEHRER) {
				patchedState.daten = await api.server.getLehrerENMDaten(api.schema, api.benutzerIDLehrer);
			} else {
				const file = await api.server.getENMDatenGZip(api.schema);
				const blob = await new Response(file.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
				patchedState.daten = ENMDaten.transpilerFromJSON(await blob.text());
			}
			patchedState.manager = new EnmManager(patchedState.daten, patchedState.daten.lehrerID);
			const lerngruppen = patchedState.manager.mapLerngruppenAuswahl.values();
			patchedState.auswahlLerngruppe = lerngruppen.isEmpty() ? null : lerngruppen.iterator().next();
			patchedState.auswahlLerngruppen = [];
			const klassen = patchedState.manager.listKlassenKlassenlehrer;
			patchedState.auswahlKlasse = klassen.isEmpty() ? null : klassen.getFirst();
			patchedState.auswahlKlassen = [];

		} catch (error) {
			patchedState.daten = null;
			patchedState.manager = null;
		}
		this.setPatchedState(patchedState);
	}

	public async entferneDaten() {
		this.setPatchedState({
			daten: null,
			manager: null,
			auswahlLerngruppen: [],
			auswahlLerngruppe: null,
			auswahlKlassen: [],
			auswahlKlasse: null,
		});
	}

	public get manager() : EnmManager {
		if (this._state.value.manager === null)
			throw new DeveloperNotificationException("Die ENM-Daten wurden nicht geladen.");
		return this._state.value.manager;
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
		if (this._state.value.auswahlLerngruppe === null)
			return this._state.value.auswahlLerngruppen;
		return [ this._state.value.auswahlLerngruppe ];
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
	get auswahlKlasse(): ENMKlasse | null {
		return this._state.value.auswahlKlasse;
	}

	/**
	 * Gibt die aktuelle Klassen-Auswahl für die Ansicht der Klassenleitungen zurück.
	 * (die Einzelauswahl oder die Mehrfachauswahl)
	 *
	 * @returns die Klassen-Auswahl
	 */
	get auswahlKlassen(): Array<ENMKlasse> {
		if (this._state.value.auswahlKlasse === null)
			return this._state.value.auswahlKlassen;
		return [ this._state.value.auswahlKlasse ];
	}

	/**
	 * Gibt die aktuelle Klassen-Auswahl für die Ansicht der Klassenleitungen zurück. (nur die Mehrfachauswahl)
	 *
	 * @returns die Klassen-Auswahl
	 */
	get auswahlKlassenNurMehrfachauswahl() : Array<ENMKlasse> {
		return this._state.value.auswahlKlassen;
	}

	/**
	 * Setzt die Auswahl der Klasse (bei Einzelauswahl)
	 *
	 * @param value   die neue Auswahl
	 */
	public setAuswahlKlasse = (value: ENMKlasse | null) => {
		this._state.value.auswahlKlasse = value;
		this.commit();
	};

	/**
	 * Setzt die Auswahl der Klassen (bei Mehrfachauswahl)
	 *
	 * @param value   die neue Auswahl
	 */
	public setAuswahlKlassen = (value: Array<ENMKlasse>) => {
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
	public patchLeistung = async (data: ENMLeistung, patch: Partial<ENMLeistung>): Promise<void> => {
		patch.id = data.id;
		await api.server.patchENMLeistung(patch, api.schema);
		Object.assign(data, patch);
		this.commit();
	}

	/**
	 * Passt die übergebenen Teilleistungen an.
	 *
	 * @param patch   der Patch für die Teilleistungen
	 *
	 * @returns true im Erfolgsfall und ansonsten false
	 */
	public patchTeilleistung = async (data: ENMTeilleistung, patch: Partial<ENMTeilleistung>): Promise<void> => {
		patch.id = data.id;
		await api.server.patchENMTeilleistung(patch, api.schema);
		Object.assign(data, patch);
		this.commit();
	}

	/**
	 * Passt die übergebenen Bemerkungen zu dem Lernabschnitt an.
	 *
	 * @param patch   der Patch für die Bemerkungen zu dem Lernabschnitt
	 *
	 * @returns true im Erfolgsfall und ansonsten false
	 */
	public patchBemerkungen = async (id: number, data: ENMLeistungBemerkungen, patch: Partial<ENMLeistungBemerkungen>): Promise<void> => {
		// TODO await api.server.patchENMSchuelerBemerkungen(id, patch, api.schema);
		Object.assign(data, patch);
		this.commit();
	}

	/**
	 * Passt die übergebenen Lernabschnittsdaten an.
	 *
	 * @param patch   der Patch für die Lernabschnittsdaten
	 *
	 * @returns true im Erfolgsfall und ansonsten false
	 */
	public patchLernabschnitt = async (data: ENMLernabschnitt, patch: Partial<ENMLernabschnitt>): Promise<void> => {
		patch.id = data.id;
		// TODO await api.server.patchENMSchuelerLernabschnitt(patch, api.schema);
		Object.assign(data, patch);
		this.commit();
	}

}
