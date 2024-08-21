import type { StundenplanListeEintrag, List, Raum, Stundenplan, JahrgangsDaten, LehrerListeEintrag, StundenplanPausenaufsichtBereichUpdate, StundenplanKalenderwochenzuordnung} from "@core";
import { StundenplanPausenaufsicht, Wochentag, StundenplanRaum, StundenplanAufsichtsbereich, StundenplanPausenzeit, StundenplanUnterricht, StundenplanZeitraster, StundenplanManager, DeveloperNotificationException, ArrayList, StundenplanJahrgang, UserNotificationException, StundenplanUnterrichtListeManager } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import { routeStundenplanDaten } from "./RouteStundenplanDaten";
import { routeKatalogPausenzeiten } from "../kataloge/pausenzeit/RouteKatalogPausenzeiten";
import { routeKatalogAufsichtsbereiche } from "../kataloge/aufsichtsbereich/RouteKatalogAufsichtsbereiche";
import { routeKatalogRaeume } from "../kataloge/raum/RouteKatalogRaeume";
import { routeApp } from "../RouteApp";

interface RouteStateStundenplan extends RouteStateInterface {
	idSchuljahresabschnitt: number;
	auswahl: StundenplanListeEintrag | undefined;
	mapKatalogeintraege: Map<number, StundenplanListeEintrag>;
	daten: Stundenplan | undefined;
	stundenplanManager: StundenplanManager | undefined;
	stundenplanUnterrichtListeManager: StundenplanUnterrichtListeManager | undefined;
	listRaeume: List<Raum>;
	listPausenzeiten: List<StundenplanPausenzeit>;
	listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
	listJahrgaenge: List<JahrgangsDaten>;
	listLehrer: List<LehrerListeEintrag>;
	selected: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined;
}

const defaultState = <RouteStateStundenplan> {
	idSchuljahresabschnitt: -1,
	auswahl: undefined,
	mapKatalogeintraege: new Map(),
	daten: undefined,
	stundenplanManager: undefined,
	stundenplanUnterrichtListeManager: undefined,
	listRaeume: new ArrayList(),
	listPausenzeiten: new ArrayList(),
	listAufsichtsbereiche: new ArrayList(),
	listJahrgaenge: new ArrayList(),
	listLehrer: new ArrayList(),
	selected: undefined,
	view: routeStundenplanDaten,
};

export class RouteDataStundenplan extends RouteData<RouteStateStundenplan> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): StundenplanListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapKatalogeintraege;
	}

	get daten(): Stundenplan {
		if (this._state.value.daten === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Stundenplandaten nicht initialisiert");
		return this._state.value.daten;
	}

	get stundenplanUnterrichtListeManager(): StundenplanUnterrichtListeManager {
		if (this._state.value.stundenplanUnterrichtListeManager === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: stundenplanUnterrichtListeManager nicht initialisiert");
		return this._state.value.stundenplanUnterrichtListeManager;
	}

	get stundenplanManager(): StundenplanManager {
		if (this._state.value.stundenplanManager === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Stundenplandaten nicht initialisiert");
		return this._state.value.stundenplanManager;
	}

	get listRaeume(): List<Raum> {
		return this._state.value.listRaeume;
	}

	get listPausenzeiten(): List<StundenplanPausenzeit> {
		return this._state.value.listPausenzeiten;
	}

	get listAufsichtsbereiche(): List<StundenplanAufsichtsbereich> {
		return this._state.value.listAufsichtsbereiche;
	}

	get listJahrgaenge(): List<JahrgangsDaten> {
		return this._state.value.listJahrgaenge;
	}

	get listLehrer(): List<LehrerListeEintrag> {
		return this._state.value.listLehrer;
	}

	get selected(): Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined {
		return this._state.value.selected;
	}

	private async ladeSchuljahresabschnitt(idSchuljahresabschnitt : number) : Promise<void> {
		api.status.start();
		const listKatalogeintraege = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, idSchuljahresabschnitt)
		const mapKatalogeintraege = new Map<number, StundenplanListeEintrag>();
		const auswahl = (listKatalogeintraege.size() > 0) ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		const listRaeume = await api.server.getRaeume(api.schema);
		const listPausenzeiten = await api.server.getPausenzeiten(api.schema);
		const listAufsichtsbereiche = await api.server.getAufsichtsbereiche(api.schema);
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const listLehrer = await api.server.getLehrer(api.schema);
		const { daten, stundenplanManager, stundenplanUnterrichtListeManager } = (auswahl !== undefined)
			? await this.ladeEintrag(auswahl)
			: { daten: undefined, stundenplanManager: undefined, stundenplanUnterrichtListeManager: undefined };
		this.setPatchedDefaultState({ idSchuljahresabschnitt, auswahl, mapKatalogeintraege, listRaeume, listPausenzeiten, listAufsichtsbereiche, listJahrgaenge, listLehrer,
			daten, stundenplanManager, stundenplanUnterrichtListeManager })
		api.status.stop();
	}

	public async setSchuljahresabschnitt(idSchuljahresabschnitt : number) : Promise<void> {
		if (idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt)
			return;
		await this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
	}

	/**
	 * Gibt die ID des aktuell gesetzten Schuljahresabschnittes zurück.
	 *
	 * @returns die ID des aktuell gesetzten Schuljahresabschnittes
	 */
	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}


	public setSelection = (selected: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined) => {
		this.setPatchedState({selected});
	}

	patch = async (data : Partial<Stundenplan>) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		await api.server.patchStundenplan(data, api.schema, this.auswahl.id);
		const daten = this.daten;
		Object.assign(daten, data);
		if (data.wochenTypModell !== undefined)
			this.stundenplanManager.stundenplanSetWochenTypModell(data.wochenTypModell);
		if (data.bezeichnungStundenplan)
			this.auswahl.bezeichnung = data.bezeichnungStundenplan;
		if (data.gueltigAb)
			this.auswahl.gueltigAb = data.gueltigAb;
		if (data.gueltigBis)
			this.auswahl.gueltigBis = data.gueltigBis;
		this.mapKatalogeintraege.set(this.auswahl.id, this.auswahl);
		this.setPatchedState({daten, auswahl: this.auswahl, mapKatalogeintraege: this.mapKatalogeintraege, stundenplanManager: this.stundenplanManager});
		api.status.stop();
	}

	patchKalenderwochenzuordnungen = async (data: List<StundenplanKalenderwochenzuordnung>) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const listHinzuzufuegen = new ArrayList<Partial<StundenplanKalenderwochenzuordnung>>();
		const listPatch = new ArrayList<StundenplanKalenderwochenzuordnung>();
		for (const e of data)
			if (e.id < 0) {
				const ee = e.clone() as Partial<StundenplanKalenderwochenzuordnung>;
				delete ee.id;
				listHinzuzufuegen.add(ee);
			} else
				listPatch.add(e);
		if (!listPatch.isEmpty()) {
			await api.server.patchStundenplanKalenderwochenzuordnungen(listPatch, api.schema, this.auswahl.id);
			this.stundenplanManager.kalenderwochenzuordnungPatchAll(listPatch);
		}
		if (!listHinzuzufuegen.isEmpty()) {
			const resHinzufuegen = await api.server.addStundenplanKalenderwochenzuordnungen(listHinzuzufuegen, api.schema, this.auswahl.id);
			this.stundenplanManager.kalenderwochenzuordnungAddAll(resHinzufuegen);
		}
		this.commit();
		api.status.stop();
	}

	addRaum = async (raum: Partial<StundenplanRaum>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		if (!raum.kuerzel || this.stundenplanManager.raumExistsByKuerzel(raum.kuerzel))
			throw new UserNotificationException('Ein Raum mit diesem Kürzel existiert bereits');
		delete raum.id;
		api.status.start();
		const _raum = await api.server.addStundenplanRaum(raum, api.schema, id);
		this.stundenplanManager.raumAdd(_raum);
		this.commit();
		api.status.stop();
	}

	patchRaum = async (data : Partial<StundenplanRaum>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		if (!data.kuerzel)
			return;
		if (data.groesse && data.groesse < 1)
			throw new UserNotificationException("Ein Raum muss mindestens eine Größe von 1 haben.");
		delete data.id;
		api.status.start();
		await api.server.patchStundenplanRaum(data, api.schema, id);
		const raum = this.stundenplanManager.raumGetByIdOrException(id);
		this.stundenplanManager.raumPatchAttributes(Object.assign(raum, data));
		this.commit();
		api.status.stop();
	}

	removeRaeume = async (raeume: Iterable<StundenplanRaum>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const listID = new ArrayList<number>();
		for (const raum of raeume)
			listID.add(raum.id);
		const list = await api.server.deleteStundenplanRaeume(listID, api.schema, id);
		this.stundenplanManager.raumRemoveAll(list);
		if (this.selected instanceof StundenplanRaum && list.contains(this.selected))
			this._state.value.selected = undefined;
		this.commit();
		api.status.stop();
	}

	importRaeume = async (raeume: Partial<StundenplanRaum>[]) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const list = new ArrayList<Partial<StundenplanRaum>>()
		for (const item of raeume) {
			delete item.id;
			list.add(item);
		}
		const res = await api.server.addStundenplanRaeume(list, api.schema, id);
		this.stundenplanManager.raumAddAll(res);
		this.commit();
		api.status.stop();
	}

	addPausenzeiten = async (pausenzeiten: Iterable<Partial<StundenplanPausenzeit>>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		const list = new ArrayList<Partial<StundenplanPausenzeit>>();
		for (const p of pausenzeiten) {
			if (p.wochentag && p.beginn && p.ende) {
				delete p.id;
				list.add(p);
			}
		}
		api.status.start();
		const _pausenzeiten = await api.server.addStundenplanPausenzeiten(list, api.schema, id)
		this.stundenplanManager.pausenzeitAddAll(_pausenzeiten);
		this.commit();
		api.status.stop();
	}

	patchPausenzeit = async (pausenzeit : Partial<StundenplanPausenzeit>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		await api.server.patchStundenplanPausenzeit(pausenzeit, api.schema, id);
		const _pausenzeit = this.stundenplanManager.pausenzeitGetByIdOrException(id);
		this.stundenplanManager.pausenzeitPatchAttributes(Object.assign(_pausenzeit, pausenzeit));
		this.commit();
		api.status.stop();
	}

	removePausenzeiten = async (pausenzeiten: Iterable<StundenplanPausenzeit>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const listID = new ArrayList<number>();
		for (const pausenzeit of pausenzeiten)
			listID.add(pausenzeit.id);
		if (!listID.isEmpty()) {
			const list = await api.server.deleteStundenplanPausenzeiten(listID, api.schema, id);
			this.stundenplanManager.pausenzeitRemoveAll(list);
			if (this.selected instanceof StundenplanPausenzeit && list.contains(this.selected))
				this._state.value.selected = undefined;
			this.commit();
		}
		api.status.stop();
	}

	importPausenzeiten = async (pausenzeiten: Iterable<Partial<StundenplanPausenzeit>>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const list = new ArrayList<Partial<StundenplanPausenzeit>>();
		for (const item of pausenzeiten) {
			delete item.id;
			delete item.klassen;
			list.add(item);
		}
		const res = await api.server.addStundenplanPausenzeiten(list, api.schema, id);
		this.stundenplanManager.pausenzeitAddAll(res);
		this.commit();
		api.status.stop();
	}

	updateAufsichtBereich = async (update: StundenplanPausenaufsichtBereichUpdate, idPausenzeit?: number, idLehrer?: number) => {
		if (update.listEntfernen.isEmpty() && update.listHinzuzufuegen.isEmpty())
			return;
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		/** Der folgende Teil ist falsch, weil von einer einzigen Pausenzeit und einem einzigen Lehrer ausgegangen wird. Daher ist auch nur ein Element in der List zulässig. */
		const listAdd = new ArrayList<Partial<StundenplanPausenaufsicht>>();
		for (const aufsichtsbereich of update.listHinzuzufuegen)
			if (aufsichtsbereich.idPausenaufsicht < 0 && idPausenzeit && idLehrer) {
				const aufsichtNeu: Partial<StundenplanPausenaufsicht> = new StundenplanPausenaufsicht();
				aufsichtNeu.idPausenzeit = idPausenzeit;
				aufsichtNeu.idLehrer = idLehrer;
				delete aufsichtNeu.id;
				listAdd.add(aufsichtNeu);
				// Abbrechen, damit nicht zu viele gleiche angelegt werden.
				break;
			}
		if (!listAdd.isEmpty()) {
			const res = await api.server.addStundenplanPausenaufsichten(listAdd, api.schema, id);
			this.stundenplanManager.pausenaufsichtAddAll(res);
			if (res.size()) {
				const aufsichtErstellt = res.get(0);
				for (const aufsichtsbereich of update.listHinzuzufuegen)
					if (aufsichtsbereich.idPausenaufsicht < 0)
						aufsichtsbereich.idPausenaufsicht = aufsichtErstellt.id;
			}
		}
		const res = await api.server.updateStundenplanPausenaufsichtenBereiche(update, api.schema, id);
		this.stundenplanManager.pausenaufsichtbereichRemoveAll(update.listEntfernen);
		this.stundenplanManager.pausenaufsichtbereichAddAll(res);
		const listRemove = new ArrayList<number>();
		for (const aufsicht of this.stundenplanManager.pausenaufsichtGetMengeAsList())
			if (aufsicht.bereiche.isEmpty())
				listRemove.add(aufsicht.id);
		await api.server.deleteStundenplanPausenaufsichten(listRemove, api.schema, id);
		this.stundenplanManager.pausenaufsichtRemoveAllById(listRemove);
		this.setPatchedState({ stundenplanManager: this.stundenplanManager });
		api.status.stop();
	}

	addAufsicht = async (data: Partial<StundenplanPausenaufsicht>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const pausenaufsicht = await api.server.addStundenplanPausenaufsicht(data, api.schema, id);
		this.stundenplanManager.pausenaufsichtAdd(pausenaufsicht);
		this.commit();
		api.status.stop();
	}

	removeAufsicht = async (aufsichtID: number) => {
		api.status.start();
		const pausenaufsicht = await api.server.deleteStundenplanPausenaufsicht(api.schema, aufsichtID);
		this.stundenplanManager.pausenaufsichtRemoveById(pausenaufsicht.id);
		this.commit();
		api.status.stop();
	}

	patchAufsicht = async (aufsicht: Partial<StundenplanPausenaufsicht>, id: number) => {
		const idStundenplan = this._state.value.auswahl?.id;
		if (idStundenplan === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const _aufsicht = this.stundenplanManager.pausenaufsichtGetByIdOrException(id);
		await api.server.patchStundenplanPausenaufsicht(aufsicht, api.schema, idStundenplan, id);
		this.stundenplanManager.pausenaufsichtPatchAttributes(Object.assign(_aufsicht, aufsicht));
		this.commit();
		api.status.stop();
	}

	addAufsichtsbereich = async (aufsichtsbereich: Partial<StundenplanAufsichtsbereich>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		if (!aufsichtsbereich.kuerzel || this.stundenplanManager.aufsichtsbereichExistsByKuerzel(aufsichtsbereich.kuerzel))
			throw new UserNotificationException('Eine Aufsichtsbereich mit diesem Kürzel existiert bereits');
		delete aufsichtsbereich.id;
		api.status.start();
		const _aufsichtsbereich = await api.server.addStundenplanAufsichtsbereich(aufsichtsbereich, api.schema, id)
		this.stundenplanManager.aufsichtsbereichAdd(_aufsichtsbereich);
		this.commit();
		api.status.stop();
	}

	patchAufsichtsbereich = async (aufsichtsbereich : Partial<StundenplanAufsichtsbereich>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		if (!aufsichtsbereich.kuerzel)
			return;
		api.status.start();
		await api.server.patchStundenplanAufsichtsbereich(aufsichtsbereich, api.schema, id);
		const _aufsichtsbereich = this.stundenplanManager.aufsichtsbereichGetByIdOrException(id);
		this.stundenplanManager.aufsichtsbereichPatchAttributes(Object.assign(_aufsichtsbereich, aufsichtsbereich));
		this.commit();
		api.status.stop();
	}

	removeAufsichtsbereiche = async (aufsichtsbereiche: Iterable<StundenplanAufsichtsbereich>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const listID = new ArrayList<number>();
		for (const aufsichtsbereich of aufsichtsbereiche)
			listID.add(aufsichtsbereich.id);
		const list = await api.server.deleteStundenplanAufsichtsbereiche(listID, api.schema, id);
		this.stundenplanManager.aufsichtsbereichRemoveAll(list);
		if (this.selected instanceof StundenplanAufsichtsbereich && list.contains(this.selected))
			this._state.value.selected = undefined;
		this.commit();
		api.status.stop();
	}

	importAufsichtsbereiche = async (aufsichtsbereiche: Iterable<Partial<StundenplanAufsichtsbereich>>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const list = new ArrayList<Partial<StundenplanAufsichtsbereich>>()
		for (const item of aufsichtsbereiche) {
			delete item.id;
			list.add(item);
		}
		const bereich = await api.server.addStundenplanAufsichtsbereiche(list, api.schema, id);
		this.stundenplanManager.aufsichtsbereichAddAll(bereich);
		this.commit();
		api.status.stop();
	}

	addZeitraster = async (zeitraster: Iterable<Partial<StundenplanZeitraster>>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const list = new ArrayList<Partial<StundenplanZeitraster>>();
		for (const z of zeitraster) {
			delete z.id;
			list.add(z);
		}
		const res = await api.server.addStundenplanZeitrasterEintraege(list, api.schema, id);
		this.stundenplanManager.zeitrasterAddAll(res);
		this.commit();
		api.status.stop();
	}

	patchZeitraster = async (zeitraster : Iterable<StundenplanZeitraster>) => {
		api.status.start();
		const list: List<StundenplanZeitraster> = new ArrayList();
		for (const z of zeitraster)
			list.add(z);
		await api.server.patchStundenplanZeitrasterEintraege(list, api.schema);
		this.stundenplanManager.zeitrasterPatchAttributesAll(list);
		this.commit();
		api.status.stop();
	}

	removeZeitraster = async (multi: Iterable<StundenplanZeitraster>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const listID = new ArrayList<number>()
		for (const zeitraster of multi)
			listID.add(zeitraster.id);
		if (!listID.isEmpty()) {
			const list = await api.server.deleteStundenplanZeitrasterEintraege(listID, api.schema, id);
			this.stundenplanManager.zeitrasterRemoveAll(list);
			if ((this.selected instanceof StundenplanZeitraster && list.contains(this.selected))
				|| (typeof this.selected === 'number')
				|| (this.selected instanceof Wochentag))
				this._state.value.selected = undefined;
			this.commit();
		}
		api.status.stop();
	}

	importZeitraster = async () => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const listKatalogeintraege: List<Partial<StundenplanZeitraster>> = await api.server.getZeitraster(api.schema);
		for (const item of listKatalogeintraege)
			delete item.id;
		await api.server.addStundenplanZeitrasterEintraege(listKatalogeintraege, api.schema, id);
		// kein Aufruf an den Manager notwendig, da wir die Route nun neu laden
		api.status.stop();
		await this.setEintrag(this.auswahl);
	}

	addUnterrichtKlasse = async (data: Iterable<Partial<StundenplanUnterricht>>) => {
		api.status.start();
		const list = new ArrayList<Partial<StundenplanUnterricht>>();
		for (const datum of data)
			list.add(datum);
		const unterrichte = await api.server.addStundenplanUnterrichte(list, api.schema);
		this.stundenplanManager.unterrichtAddAll(unterrichte);
		this.commit();
		api.status.stop();
	}

	patchUnterricht = async (data: Iterable<StundenplanUnterricht>, zeitraster?: StundenplanZeitraster, wochentyp?: number) => {
		api.status.start();
		const list: List<StundenplanUnterricht> = new ArrayList();
		if ((zeitraster !== undefined) && (wochentyp !== undefined))
			loop: for (const datum of data) {
				if (!this.stundenplanManager.unterrichtIstVerschiebenErlaubtNach(datum, zeitraster, wochentyp))
					continue loop;
				datum.idZeitraster = zeitraster.id;
				datum.wochentyp = wochentyp;
				list.add(datum);
			}
		else
			for (const datum of data)
				list.add(datum);
		await api.server.patchStundenplanUnterrichte(list, api.schema);
		this.stundenplanManager.unterrichtPatchAttributesAll(list);
		this.commit();
		api.status.stop();
	}

	removeUnterrichtKlasse = async (unterrichte: Iterable<StundenplanUnterricht>) => {
		const id = this._state.value.auswahl?.id;
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Stundenplan ausgewählt');
		api.status.start();
		const listID = new ArrayList<number>();
		for (const unterricht of unterrichte)
			listID.add(unterricht.id);
		const list = await api.server.deleteStundenplanUnterrichte(listID, api.schema, id);
		this.stundenplanManager.unterrichtRemoveAll(list);
		if (this.selected instanceof StundenplanUnterricht && list.contains(this.selected))
			this._state.value.selected = undefined;
		this.commit();
		api.status.stop();
	}

	addJahrgang = async (id: number) => {
		api.status.start();
		const jahrgang = new StundenplanJahrgang();
		for (const j of this.listJahrgaenge)
			if (j.id === id) {
				jahrgang.kuerzel = j.kuerzel || j.kuerzelStatistik;
				jahrgang.id = j.id;
			}
		this.stundenplanManager.jahrgangAdd(jahrgang);
		this.commit();
		api.status.stop();
	}

	removeJahrgang = async (id: number) => {
		api.status.start();
		this.stundenplanManager.jahrgangRemoveById(id);
		this.commit();
		api.status.stop();
	}

	gotoKatalog = async (katalog: 'raeume'|'aufsichtsbereiche'|'pausenzeiten') => {
		switch (katalog) {
			case 'aufsichtsbereiche':
				return await RouteManager.doRoute(routeKatalogAufsichtsbereiche.getRoute(undefined));
			case 'pausenzeiten':
				return await RouteManager.doRoute(routeKatalogPausenzeiten.getRoute(undefined));
			case 'raeume':
				return await RouteManager.doRoute(routeKatalogRaeume.getRoute(undefined));
		}
	}

	private async ladeEintrag(auswahl: StundenplanListeEintrag) : Promise<{ daten: Stundenplan, stundenplanManager: StundenplanManager, stundenplanUnterrichtListeManager: StundenplanUnterrichtListeManager }> {
		const daten = await api.server.getStundenplan(api.schema, auswahl.id);
		const unterrichtsdaten = await api.server.getStundenplanUnterrichte(api.schema, auswahl.id);
		const pausenaufsichten = await api.server.getStundenplanPausenaufsichten(api.schema, auswahl.id);
		const unterrichtsverteilung = await api.server.getStundenplanUnterrichtsverteilung(api.schema, auswahl.id);
		const stundenplanManager = new StundenplanManager(daten, unterrichtsdaten, pausenaufsichten, unterrichtsverteilung);
		const stundenplanUnterrichtListeManager = new StundenplanUnterrichtListeManager(api.schulform, stundenplanManager, api.schuleStammdaten.abschnitte, daten.idSchuljahresabschnitt);
		return { daten, stundenplanManager, stundenplanUnterrichtListeManager };
	}

	setFilter = async () => {
		this.stundenplanUnterrichtListeManager.filterInvalidateCache();
		this.setPatchedState({ stundenplanUnterrichtListeManager: this.stundenplanUnterrichtListeManager });
	}

	setEintrag = async (auswahl?: StundenplanListeEintrag) => {
		api.status.start();
		if (auswahl === undefined && this.mapKatalogeintraege.size > 0)
			auswahl = this.mapKatalogeintraege.values().next().value;
		if (auswahl === undefined)
			this.setPatchedState({ auswahl, daten: undefined, stundenplanManager: undefined });
		else {
			const { daten, stundenplanManager, stundenplanUnterrichtListeManager } = await this.ladeEintrag(auswahl);
			this.setPatchedState({ auswahl, daten, stundenplanManager, stundenplanUnterrichtListeManager });
		}
		api.status.stop();
	}

	addEintrag = async () => {
		api.status.start();
		const eintrag = await api.server.addStundenplan(api.schema, routeApp.data.idSchuljahresabschnitt);
		this.mapKatalogeintraege.set(eintrag.id, eintrag)
		api.status.stop();
		await this.gotoEintrag(eintrag);
	}

	removeEintraege = async (eintraege: StundenplanListeEintrag[]) => {
		api.status.start();
		for (const eintrag of eintraege) {
			if (this._state.value.auswahl?.id === eintrag.id)
				this._state.value.auswahl = undefined;
			await api.server.deleteStundenplan(api.schema, eintrag.id);
			this.mapKatalogeintraege.delete(eintrag.id);
		}
		if ((this.auswahl === undefined) && (this.mapKatalogeintraege.size > 0))
			this._state.value.auswahl = this.mapKatalogeintraege.values().next().value;
		api.status.stop();
		await this.gotoEintrag(this.auswahl);
	}

	gotoEintrag = async (eintrag?: StundenplanListeEintrag) => await RouteManager.doRoute(routeStundenplan.getRoute(eintrag?.id));

}
