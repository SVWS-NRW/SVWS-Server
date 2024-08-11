import { ArrayList, KursListeManager, type KursDaten, type Schueler, DeveloperNotificationException} from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKurse } from "~/router/apps/kurse/RouteKurse";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeKursDaten } from "~/router/apps/kurse/RouteKursDaten";


interface RouteStateKurse extends RouteStateInterface {
	idSchuljahresabschnitt: number;
	kursListeManager: KursListeManager;
}

const defaultState = <RouteStateKurse> {
	idSchuljahresabschnitt: -1,
	kursListeManager: new KursListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList()),
	view: routeKursDaten,
};

export class RouteDataKurse extends RouteData<RouteStateKurse> {

	public constructor() {
		super(defaultState);
	}

	get kursListeManager(): KursListeManager {
		return this._state.value.kursListeManager;
	}

	private async ladeSchuljahresabschnitt(idSchuljahresabschnitt : number) : Promise<number | null> {
		const schuljahresabschnitt = api.mapAbschnitte.value.get(idSchuljahresabschnitt);
		if (schuljahresabschnitt === undefined)
			return null;
		// Bestimme die Kursdaten vorher, um ggf. eine neu ID für das Routing zurückzugeben
		const hatteAuswahl = (this.kursListeManager.auswahlID() !== null) ? this.kursListeManager.auswahl() : null;
		// Lade die Kataloge und erstelle den Manager
		const listKurse = await api.server.getKurseFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listSchueler = await api.server.getSchuelerFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const listLehrer = await api.server.getLehrer(api.schema);
		const listFaecher = await api.server.getFaecher(api.schema);
		const kursListeManager = new KursListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, listKurse, listSchueler, listJahrgaenge, listLehrer, listFaecher);
		kursListeManager.setFilterAuswahlPermitted(true);
		// Wählen nun einen Kurs aus, dabei wird sich ggf. an der alten Auswahl orientiert
		let result : number | null = null;
		if (hatteAuswahl && (!hatteAuswahl.idJahrgaenge.isEmpty())) {
			let auswahl = kursListeManager.getByKuerzelAndJahrgangOrNull(hatteAuswahl.kuerzel, hatteAuswahl.idJahrgaenge.get(0));
			if ((auswahl === null) && (kursListeManager.liste.size() > 0))
				auswahl = kursListeManager.liste.list().get(0);
			if (auswahl !== null) {
				kursListeManager.setDaten(auswahl);
				result = auswahl.id;
			}
		}
		// Aktualisiere den State
		this.setPatchedDefaultState({ idSchuljahresabschnitt, kursListeManager });
		return result;
	}

	public async setSchuljahresabschnitt(idSchuljahresabschnitt : number) : Promise<number | null> {
		if (idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt)
			return null;
		return await this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
	}

	/**
	 * Gibt die ID des aktuell gesetzten Schuljahresabschnittes zurück.
	 *
	 * @returns die ID des aktuell gesetzten Schuljahresabschnittes
	 */
	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}


	public async setEintrag(kurs: KursDaten | null) {
		if ((kurs === null) && (!this.kursListeManager.hasDaten()))
			return;
		if ((kurs === null) || (this.kursListeManager.liste.list().isEmpty())) {
			this.kursListeManager.setDaten(null);
			this.commit();
			return;
		}
		if ((this.kursListeManager.hasDaten() && (kurs.id === this.kursListeManager.auswahl().id)))
			return;
		let daten = this.kursListeManager.liste.get(kurs.id);
		if (daten === null)
			daten = this.kursListeManager.filtered().isEmpty() ? null : this.kursListeManager.filtered().get(0);
		this.kursListeManager.setDaten(daten);
		this.commit();
	}

	patch = async (data : Partial<KursDaten>) => {
		if (!this.kursListeManager.hasDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const idKurs = this.kursListeManager.auswahl().id;
		await api.server.patchKurs(data, api.schema, idKurs);
		const daten = this.kursListeManager.daten();
		Object.assign(daten, data);
		this.kursListeManager.setDaten(daten);
		this.commit();
	}

	gotoEintrag = async (eintrag: KursDaten) => {
		await RouteManager.doRoute(routeKurse.getRoute(eintrag.id));
	}

	gotoSchueler = async (eintrag: Schueler) => {
		await RouteManager.doRoute(routeSchueler.getRoute(eintrag.id));
	}

	setFilter = async () => {
		if (!this.kursListeManager.hasDaten()) {
			const listFiltered = this.kursListeManager.filtered();
			if (!listFiltered.isEmpty()) {
				await this.gotoEintrag(listFiltered.get(0));
				return;
			}
		}
		const kursListeManager = this.kursListeManager;
		this.setPatchedState({ kursListeManager });
	}

	/* TODO
	setzeDefaultSortierung = async () => {
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		const auswahl_id = this.kursListeManager.auswahl().id;
		await api.server.setKursSortierungFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		await this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
		await this.setEintrag(this.kursListeManager.liste.get(auswahl_id));
	}
	*/

}
