import type { SchuelerListeEintrag, SchuelerStammdaten, Schuljahresabschnitt } from "@core";

import { SchuelerListeManager, ArrayList, DeveloperNotificationException, SchuelerListe } from "@core";
import { SchuelerStatus } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";
import { type RouteNode } from "~/router/RouteNode";


interface RouteStateSchueler extends RouteStateInterface {
	idSchuljahresabschnitt: number,
	schuelerListeManager: SchuelerListeManager;
}

const defaultState = <RouteStateSchueler> {
	idSchuljahresabschnitt: -1,
	schuelerListeManager: new SchuelerListeManager(null, new SchuelerListe(), new ArrayList<Schuljahresabschnitt>(), -1),
	view: routeSchuelerIndividualdaten,
};

export class RouteDataSchueler extends RouteData<RouteStateSchueler> {

	public constructor() {
		super(defaultState);
	}

	/**
	 * Setzt die Daten zum ausgewählten Schuljahresabschnitt und Schülers und triggert damit das Laden der Defaults für diesen Abschnitt
	 *
	 * @param {number} idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param {number | undefined} idSchueler   die ID des Schülers
	 * @param {boolean} isEntering              gibt an, ob die Route neu betreten wird
	 */
	public async reload(idSchuljahresabschnitt: number, idSchueler: number | undefined, isEntering: boolean): Promise<void> {
		let schuelerListeManager = this.schuelerListeManager;

		// Erzeuge neuen SchuelerListeManager, wenn die Route neu betreten wird
		if (isEntering) {
			const schuelerListe = await this.loadSchuelerListe(idSchuljahresabschnitt);

			schuelerListeManager = new SchuelerListeManager(api.schulform, schuelerListe, api.schuleStammdaten.abschnitte, api.schuleStammdaten.idSchuljahresabschnitt);
			schuelerListeManager.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
			schuelerListeManager.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
			schuelerListeManager.useFilter(this.schuelerListeManager);
		}

		// Lade und setze Schüler Stammdaten falls ein Schüler ausgewählt ist
		const schuelerAuswahl = await this.getSchuelerAuswahl(idSchueler, schuelerListeManager, isEntering);
		const schuelerStammdaten = await this.loadSchuelerStammdaten(schuelerAuswahl);
		schuelerListeManager.setDaten(schuelerStammdaten);
		schuelerListeManager.filterInvalidateCache()

		// Lade view
		const view = this.getCurrentViewOrDefault(schuelerListeManager);

		this.setPatchedDefaultState({
			idSchuljahresabschnitt,
			schuelerListeManager,
			view
		});
	}

	/**
	 * Lädt das {@link SchuelerListe} Objekt von der API, für die übergebene Schuljahresabschnitt ID
	 *
	 * @param {number} idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @returns Gibt SchuelerListe zurück
	 */
	private async loadSchuelerListe(idSchuljahresabschnitt: number): Promise<SchuelerListe> {
		// Lese die grundlegenden Daten für den Schuljahresabschnitt ein und erstelle den SchülerListeManager
		const auswahllisteGzip = await api.server.getSchuelerAuswahllisteFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const auswahllisteBlob = await new Response(auswahllisteGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
		return SchuelerListe.transpilerFromJSON(await auswahllisteBlob.text());
	}

	/**
	 * Liefert die bisher ausgewählte View oder die Default View {@link RouteSchuelerIndividualdaten}
	 *
	 * @returns aktuelle View oder Default View
	 */
	private getCurrentViewOrDefault(schuelerManager: SchuelerListeManager): RouteNode<any, any> | undefined {
		if (schuelerManager.hasDaten() && schuelerManager.auswahl()) {
			return this._state.value.view;
		} else {
			return routeSchuelerIndividualdaten;
		}
	}

	/**
	 * Liefert den aktuell ausgewählten {@link SchuelerListeEintrag} oder <code>null</code> falls kein Schüler ausgewählt ist.
	 *
	 * @param {number | undefined} idSchueler          ID des Schülers
	 * @param {SchuelerListeManager} schuelerManager   SchuelerListeManager
	 * @param {boolean} isEntering                     gibt an ob die Route das erste mal betreten wird
	 *
	 * @returns den ausgewählten {@link SchuelerListeEintrag} oder <code>null</code>
	 */
	private async getSchuelerAuswahl(idSchueler: number | undefined, schuelerManager: SchuelerListeManager, isEntering: boolean): Promise<SchuelerListeEintrag | null> {
		if (schuelerManager.filtered().isEmpty())
			return null;

		// Wenn keine Schüler-ID ausgewählt ist, dann gebe null zurück oder beim Einsteigen in die Route den ersten Schüler der gefilterten Liste
		if (idSchueler === undefined)
			return (isEntering) ? schuelerManager.filtered().get(0) : null;

		// Wenn ein Schüler ausgewählt ist, wird dieser zurückgegeben, falls keine Auswahl vorliegt, wird der erste Eintrag aus der gefilterten Schülerliste zurückgegeben
		return schuelerManager.liste.get(idSchueler) !== null ? schuelerManager.liste.get(idSchueler) : schuelerManager.filtered().get(0);
	}

	/**
	 * Lädt das {@link SchuelerStammdaten} Objekt zum übergebenen {@link SchuelerListeEintrag}.
	 * Die Methode liefert <code>null</code>, wenn der Parameter <code>schuelerEintrag</code> <code>null</code> ist.
	 *
	 * @param {SchuelerListeEintrag | null} schuelerEintrag     Eintrag des Schülers
	 *
	 * @returns Gibt Stammdaten des Schülers oder <code>null</code> zurück
	 */
	private async loadSchuelerStammdaten(schuelerEintrag: SchuelerListeEintrag | null): Promise<SchuelerStammdaten | null> {
		if (schuelerEintrag === null)
			return null;

		return await api.server.getSchuelerStammdaten(api.schema, schuelerEintrag.id);
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
		this.schuelerListeManager.setFilterAuswahlPermitted(false);
		const listFiltered = this.schuelerListeManager.filtered();
		this.schuelerListeManager.setFilterAuswahlPermitted(true);
		this.schuelerListeManager.filterInvalidateCache();
		if ((this.schuelerListeManager.hasDaten()) && (listFiltered.contains(this.schuelerListeManager.auswahl()))) {
			this.commit();
		} else {
			if (listFiltered.isEmpty())
				await this.gotoSchueler(null)
			else
				await this.gotoSchueler(listFiltered.get(0));
		}
	}

}
