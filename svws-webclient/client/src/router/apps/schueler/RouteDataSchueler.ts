import type { SchuelerListeEintrag, SchuelerStammdaten, Schuljahresabschnitt } from "@core";

import { SchuelerListeManager, ArrayList, DeveloperNotificationException, SchuelerListe } from "@core";
import { SchuelerStatus } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";
import { RouteNode } from "~/router/RouteNode";


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
	 */
	public async reload(idSchuljahresabschnitt: number, idSchueler: number | undefined) {
		// Lese die grundlegenden Daten für den Schuljahresabschnitt ein und erstelle den SchülerListeManager
		const auswahllisteGzip = await api.server.getSchuelerAuswahllisteFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const auswahllisteBlob = await new Response(auswahllisteGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
		const auswahllisteDaten = SchuelerListe.transpilerFromJSON(await auswahllisteBlob.text());

        // Erzeuge neuen SchuelerListeManager mit default SchuelerStatus Filter
        const schuelerListeManager = new SchuelerListeManager(api.schulform, auswahllisteDaten, api.schuleStammdaten.abschnitte, api.schuleStammdaten.idSchuljahresabschnitt);
		schuelerListeManager.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		schuelerListeManager.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);

		// bisher ausgewählte Filter übernehmen, damit sie nicht verloren gehen
		this.restoreFilter(schuelerListeManager);

        // Lade und setze Schüler Stammdaten falls ein Schüler ausgewählt ist
        const auswahlSchueler = this.getSchuelerAuswahl(idSchueler, schuelerListeManager);
        const schuelerStammdaten = await this.ladeSchuelerStammdaten(auswahlSchueler);
		schuelerListeManager.setDaten(schuelerStammdaten);

		// Lade view
        const view = this.getView();

		this.setPatchedDefaultState({
			idSchuljahresabschnitt,
			schuelerListeManager,
			view
		});
	}


	/**
	 * Übernimmt die bestehenden Filter in den übergebenen SchuelerListeManager
	 *
	 * @param {SchuelerListeManager} newManager   SchuelerListeManager
	 */
	private restoreFilter(newManager: SchuelerListeManager): void {
		for (let keyKlasse of this.schuelerListeManager.klassen.auswahlKeyList()) {
			if (newManager.klassen.has(keyKlasse)) {
				newManager.klassen.auswahlAddByKey(keyKlasse);
			}
		}

		for (let keyKurs of this.schuelerListeManager.kurse.auswahlKeyList()) {
			if (newManager.kurse.has(keyKurs)) {
				newManager.kurse.auswahlAddByKey(keyKurs);
			}
		}

		for (let keyJahrgang of this.schuelerListeManager.jahrgaenge.auswahlKeyList()) {
			if (newManager.jahrgaenge.has(keyJahrgang)) {
				newManager.jahrgaenge.auswahlAddByKey(keyJahrgang);
			}
		}

		for (let keySchulgliederung of this.schuelerListeManager.schulgliederungen.auswahlKeyList()) {
			if (newManager.schulgliederungen.has(keySchulgliederung)) {
				newManager.schulgliederungen.auswahlAddByKey(keySchulgliederung);
			}
		}
	}


	/**
	 * Liefert den aktuellen View State
	 */
	private getView(): RouteNode<any, any> | undefined {
        // Setze ggf. den Tab in der Schüler-Applikation und setze den neu erzeugten Routing-State
        const auswahlSchuelerVorher = this.schuelerListeManager.hasDaten() ? this.schuelerListeManager.auswahl() : null;
        if (auswahlSchuelerVorher) {
            return this._state.value.view;
        } else {
            return routeSchuelerIndividualdaten;
        }
    }

	/**
	 * Liefert den aktuell ausgewählten SchuelerListeEintrag oder null falls kein Schüler ausgewählt ist.
	 *
	 * @param {number | undefined} idSchueler   ID des Schülers
	 * @param {SchuelerListeManager} newManager   SchuelerListeManager
	 *
	 * @returns den ausgewählten SchuelerListeEintrag oder null
	 */
    private getSchuelerAuswahl(idSchueler: number | undefined, newManager: SchuelerListeManager): SchuelerListeEintrag | null {
        let auswahlSchueler;

        if (idSchueler !== undefined) {
            // Hier wird der ausgewählte Schüler geholt
            auswahlSchueler = newManager.liste.get(idSchueler);

        } else if (!newManager.filtered().isEmpty()) {
            // Ermittle einen ggf. zuvor ausgewählten Schüler und versucht diesen erneut zu holen
			const auswahlSchuelerVorher = this.schuelerListeManager.hasDaten() ? this.schuelerListeManager.auswahl() : null;
			if (auswahlSchuelerVorher && newManager.liste.has(auswahlSchuelerVorher.id)) {
                auswahlSchueler = newManager.liste.getOrException(auswahlSchuelerVorher.id);
            }
        }

        // Wenn kein Schüler ausgewählt/gefunden wurde, wird entweder der oberste Eintrag aus der Schüler Liste oder null zurückgegeben
        if (!auswahlSchueler) {
            auswahlSchueler = newManager.filtered().isEmpty() ? null : newManager.filtered().get(0);
        }

        return auswahlSchueler
    }


    private async ladeSchuelerStammdaten(schuelerEintrag: SchuelerListeEintrag | null): Promise<SchuelerStammdaten | null> {
        if (schuelerEintrag === null)
            return null;
        return await api.server.getSchuelerStammdaten(api.schema, schuelerEintrag.id);
    }


    /**
	 * Gibt die ID des aktuell gesetzten Schuljahresabschnittes zurück.
	 *
	 * @returns die ID des aktuell gesetzten Schuljahresabschnittes
	 */
	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
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
