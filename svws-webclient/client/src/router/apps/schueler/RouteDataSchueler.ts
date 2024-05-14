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
	public async reload(idSchuljahresabschnitt: number, idSchueler: number | undefined): Promise<void> {
        const schuelerListe = await this.loadSchuelerListe(idSchuljahresabschnitt);

        // Erzeuge neuen SchuelerListeManager mit default SchuelerStatus Filter
        const schuelerListeManager = new SchuelerListeManager(api.schulform, schuelerListe, api.schuleStammdaten.abschnitte, api.schuleStammdaten.idSchuljahresabschnitt);
        schuelerListeManager.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
        schuelerListeManager.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);

        // bisher ausgewählte Filter übernehmen, damit sie nicht verloren gehen
        this.restoreFilter(schuelerListeManager);

        // Lade und setze Schüler Stammdaten falls ein Schüler ausgewählt ist
        const auswahlSchueler = this.getSchuelerAuswahl(idSchueler, schuelerListeManager);
        const schuelerStammdaten = await this.loadSchuelerStammdaten(auswahlSchueler);
        schuelerListeManager.setDaten(schuelerStammdaten);

        // Lade view
        const view = this.getCurrentViewOrDefault();
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
    private async loadSchuelerListe(idSchuljahresabschnitt: number): Promise<SchuelerListe>{
        // Lese die grundlegenden Daten für den Schuljahresabschnitt ein und erstelle den SchülerListeManager
        const auswahllisteGzip = await api.server.getSchuelerAuswahllisteFuerAbschnitt(api.schema, idSchuljahresabschnitt);
        const auswahllisteBlob = await new Response(auswahllisteGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
        return SchuelerListe.transpilerFromJSON(await auswahllisteBlob.text());
    }


	/**
	 * Übernimmt die bestehenden Auswahl Filter in den übergebenen {@link SchuelerListeManager}
	 *
	 * @param {SchuelerListeManager} targetAuswahlManager   Der {@link SchuelerListeManager} für den die Filter des aktuellen {@link SchuelerListeManager} übernommen werden sollen
	 */
	private restoreFilter(targetAuswahlManager: SchuelerListeManager): void {
		for (const keyKlasse of this.schuelerListeManager.klassen.auswahlKeyList()) {
			if (targetAuswahlManager.klassen.has(keyKlasse)) {
				targetAuswahlManager.klassen.auswahlAddByKey(keyKlasse);
			}
		}

		for (const keyKurs of this.schuelerListeManager.kurse.auswahlKeyList()) {
			if (targetAuswahlManager.kurse.has(keyKurs)) {
				targetAuswahlManager.kurse.auswahlAddByKey(keyKurs);
			}
		}

		for (const keyJahrgang of this.schuelerListeManager.jahrgaenge.auswahlKeyList()) {
			if (targetAuswahlManager.jahrgaenge.has(keyJahrgang)) {
				targetAuswahlManager.jahrgaenge.auswahlAddByKey(keyJahrgang);
			}
		}

		for (const keySchulgliederung of this.schuelerListeManager.schulgliederungen.auswahlKeyList()) {
			if (targetAuswahlManager.schulgliederungen.has(keySchulgliederung)) {
				targetAuswahlManager.schulgliederungen.auswahlAddByKey(keySchulgliederung);
			}
		}
	}


	/**
	 * Liefert die bisher ausgewählte View oder die Default View {@link RouteSchuelerIndividualdaten}
     *
     * @returns aktuelle View oder Default View
	 */
	private getCurrentViewOrDefault(): RouteNode<any, any> | undefined {
        // Setze ggf. den Tab in der Schüler-Applikation und setze den neu erzeugten Routing-State
        const auswahlSchuelerVorher = this.schuelerListeManager.hasDaten() ? this.schuelerListeManager.auswahl() : null;
        if (auswahlSchuelerVorher) {
            return this._state.value.view;
        } else {
            return routeSchuelerIndividualdaten;
        }
    }


	/**
	 * Liefert den aktuell ausgewählten {@link SchuelerListeEintrag} oder <code>null</code> falls kein Schüler ausgewählt ist.
	 *
	 * @param {number | undefined} idSchueler     ID des Schülers
	 * @param {SchuelerListeManager} targetAuswahlManager   SchuelerListeManager
	 *
	 * @returns den ausgewählten {@link SchuelerListeEintrag} oder <code>null</code>
	 */
    private getSchuelerAuswahl(idSchueler: number | undefined, targetAuswahlManager: SchuelerListeManager): SchuelerListeEintrag | null {
        let auswahlSchueler;

        if (idSchueler !== undefined) {
            // Hier wird der ausgewählte Schüler geholt
            auswahlSchueler = targetAuswahlManager.liste.get(idSchueler);

        } else if (!targetAuswahlManager.filtered().isEmpty()) {
            // Ermittle einen ggf. zuvor ausgewählten Schüler und versucht diesen erneut zu holen
			const auswahlSchuelerVorher = this.schuelerListeManager.hasDaten() ? this.schuelerListeManager.auswahl() : null;
			if (auswahlSchuelerVorher && targetAuswahlManager.liste.has(auswahlSchuelerVorher.id)) {
                auswahlSchueler = targetAuswahlManager.liste.getOrException(auswahlSchuelerVorher.id);
            }
        }

        // Wenn kein Schüler ausgewählt/gefunden wurde, wird entweder der oberste Eintrag aus der Schüler Liste oder null zurückgegeben
        if (!auswahlSchueler) {
            auswahlSchueler = targetAuswahlManager.filtered().isEmpty() ? null : targetAuswahlManager.filtered().get(0);
        }

        return auswahlSchueler
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
