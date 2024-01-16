import type { SchuelerListeEintrag, SchuelerStammdaten, KlassenListeEintrag, JahrgangsListeEintrag, KursListeEintrag, GostJahrgang, Schuljahresabschnitt } from "@core";

import { SchuelerListeManager, ArrayList, DeveloperNotificationException } from "@core";
import { SchuelerStatus } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";


interface RouteStateSchueler extends RouteStateInterface {
	idSchuljahresabschnitt: number,
	schuelerListeManager: SchuelerListeManager;
}

const defaultState = <RouteStateSchueler> {
	idSchuljahresabschnitt: -1,
	schuelerListeManager: new SchuelerListeManager(null, new ArrayList<SchuelerListeEintrag>(), new ArrayList<JahrgangsListeEintrag>, new ArrayList<KlassenListeEintrag>, new ArrayList<KursListeEintrag>(), new ArrayList<Schuljahresabschnitt>(), new ArrayList<GostJahrgang>()),
	view: routeSchuelerIndividualdaten,
};

export class RouteDataSchueler extends RouteData<RouteStateSchueler> {

	public constructor() {
		super(defaultState);
	}

	private async ladeStammdaten(eintrag: SchuelerListeEintrag | null): Promise<SchuelerStammdaten | null> {
		if (eintrag === null)
			return null;
		return await api.server.getSchuelerStammdaten(api.schema, eintrag.id);
	}

	/**
	 * Setzt den Schuljahresabschnitt und triggert damit das Laden der Defaults für diesen Abschnitt
	 *
	 * @param {number} idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	public async setSchuljahresabschnitt(idSchuljahresabschnitt: number) {
		// Lese die grundlegenden Daten für den Schuljahresabschnitt ein und erstelle den Schülerlisten-Manager zunächst ohne Auswahl
		const listSchueler = await api.server.getSchuelerFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listKlassen = await api.server.getKlassenFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listKurse = await api.server.getKurseFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const listAbiturjahrgaenge = api.schulform.daten.hatGymOb ? await api.server.getGostAbiturjahrgaenge(api.schema) : new ArrayList<GostJahrgang>();
		const schuelerListeManager = new SchuelerListeManager(api.schulform, listSchueler, listJahrgaenge, listKlassen, listKurse, api.schuleStammdaten.abschnitte, listAbiturjahrgaenge);
		schuelerListeManager.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		schuelerListeManager.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		// Ermittle eine ggf. zuvor vorhandene Auswahl und versuche diese wiederherzustellen
		const auswahlVorher = this.schuelerListeManager.hasDaten() ? this.schuelerListeManager.auswahl() : null;
		let auswahl = null;
		if ((auswahlVorher !== null) && schuelerListeManager.liste.has(auswahlVorher.id))
			auswahl = schuelerListeManager.liste.getOrException(auswahlVorher.id);
		if (auswahl === null)
			auswahl = schuelerListeManager.filtered().isEmpty() ? null : schuelerListeManager.filtered().get(0);
		const stammdaten = await this.ladeStammdaten(auswahl);
		schuelerListeManager.setDaten(stammdaten);
		// Setze ggf. den Tab in der Schüler-Applikation und setze den neu erzeugten Routing-State
		const view = auswahlVorher === null ? routeSchuelerIndividualdaten : this._state.value.view;
		this.setPatchedDefaultState({
			idSchuljahresabschnitt,
			schuelerListeManager,
			view
		});
	}

	/**
	 * Setzt den ausgewählten Schueler und lädt dessen Stammddaten.
	 *
	 * @param schueler   der ausgewählte Schüler
	 */
	public async setSchueler(schueler: SchuelerListeEintrag | null) {
		if ((schueler === null) && (!this.schuelerListeManager.hasDaten()))
			return;
		if ((schueler === null) || (this.schuelerListeManager.liste.list().isEmpty())) {
			this.schuelerListeManager.setDaten(null);
			this.commit();
			return;
		}
		if ((schueler !== null) && (this.schuelerListeManager.hasDaten() && (schueler.id === this.schuelerListeManager.auswahl().id)))
			return;
		let auswahl = this.schuelerListeManager.liste.get(schueler.id);
		if (auswahl === null)
			auswahl = this.schuelerListeManager.filtered().isEmpty() ? null : this.schuelerListeManager.filtered().get(0);
		const stammdaten = await this.ladeStammdaten(auswahl);
		this.schuelerListeManager.setDaten(stammdaten);
		// this.commit();
	}

	get schuelerListeManager(): SchuelerListeManager {
		return this._state.value.schuelerListeManager;
	}

	patch = async (data : Partial<SchuelerStammdaten>) => {
		if (!this.schuelerListeManager.hasDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const idSchueler = this.schuelerListeManager.auswahl().id;
		const stammdaten = this.schuelerListeManager.daten();
		await api.server.patchSchuelerStammdaten(data, api.schema, idSchueler);
		Object.assign(stammdaten, data);
		this.schuelerListeManager.setDaten(stammdaten);
		this.commit();
	}

	gotoSchueler = async (value: SchuelerListeEintrag | null) => {
		if (value === null || value === undefined) {
			await RouteManager.doRoute({ name: routeSchueler.name, params: { } });
			return;
		}
		const redirect_name: string = (routeSchueler.selectedChild === undefined) ? routeSchuelerIndividualdaten.name : routeSchueler.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}

	setFilter = async () => {
		if (!this.schuelerListeManager.hasDaten()) {
			const listFiltered = this.schuelerListeManager.filtered();
			if (!listFiltered.isEmpty()) {
				await this.gotoSchueler(listFiltered.get(0));
				return;
			}
		}
		this.commit();
	}

}
