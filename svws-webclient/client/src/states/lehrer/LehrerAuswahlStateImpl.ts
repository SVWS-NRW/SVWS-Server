import type { LehrerFachrichtungEintrag } from "@core/asd/data/lehrer/LehrerFachrichtungEintrag";
import type { LehrerLehramtEintrag } from "@core/asd/data/lehrer/LehrerLehramtEintrag";
import type { LehrerLehrbefaehigungEintrag } from "@core/asd/data/lehrer/LehrerLehrbefaehigungEintrag";
import type { LehrerPersonalabschnittsdaten } from "@core/asd/data/lehrer/LehrerPersonalabschnittsdaten";
import type { LehrerPersonalabschnittsdatenAnrechnungsstunden } from "@core/asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden";
import type { LehrerPersonaldaten } from "@core/asd/data/lehrer/LehrerPersonaldaten";
import type { LehrerStammdaten } from "@core/asd/data/lehrer/LehrerStammdaten";
import type { Schulleitung } from "@core/asd/data/schule/Schulleitung";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { LehrerUnterrichtsfach } from "@core/core/data/lehrer/LehrerUnterrichtsfach";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { useAbschnittState } from "@ui/states/AbschnittState";
import { useBenutzerState } from "@ui/states/BenutzerState";
import { useConfigState } from "@ui/states/ConfigState";
import { useSchuleState } from "@ui/states/SchuleState";
import { LehrerListeManager } from "@ui/ui/manager/lehrer/LehrerListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";
import type { PendingStateManager } from "@ui/ui/wrapper/PendingStateManager";

import type { GenericAuswahlReactiveState } from "../GenericAuswahlStateImpl";
import { GenericAuswahlStateImpl } from "../GenericAuswahlStateImpl";
import { api } from "~/router/Api";

import type { LehrerAuswahlState } from "./LehrerAuswahlState";
import { lehrerAuswahlStateRoutingAdapter } from "./LehrerAuswahlStateRoutingAdapter";

interface LehrerAuswahlReactiveState extends GenericAuswahlReactiveState<LehrerListeManager> {
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	mapSchulen: Map<string, SchulEintrag>;
	mapFaecher: Map<number, FachDaten>;
	lehrerUnterrichtsfaecher: List<LehrerUnterrichtsfach>;
	listLeitungsfunktionen: List<Schulleitung>;
}

/**
 * Der State für die Auswahlliste der Lehrer
 */
export class LehrerAuswahlStateImpl extends GenericAuswahlStateImpl<LehrerListeManager, LehrerAuswahlReactiveState> implements LehrerAuswahlState {

	public constructor() {
		super({
			idSchuljahresabschnitt: -1,
			manager: undefined,
			activeViewType: ViewType.DEFAULT,
			mapStundenplaene: new Map(),
			mapSchulen: new Map(),
			mapFaecher: new Map(),
			lehrerUnterrichtsfaecher: new ArrayList(),
			listLeitungsfunktionen: new ArrayList(),
		}, lehrerAuswahlStateRoutingAdapter);
	}

	/**
	 * Initialisiert den State für den ausgewählten Schuljahresabschnitt. Es werden die Daten für diesen Abschnitt geladen.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param force                    gibt an, ob das Laden der Daten auch erzwungen werden soll, wenn die ID des
	 *                                 Schuljahresabschnittes bereits gesetzt ist (z.B. beim Betreten einer Route)
	 *
	 * @returns ein Promise mit der ID der Auswahl oder null
	 */
	public async init(idSchuljahresabschnitt: number, force: boolean): Promise<number | null> {
		return await super.setSchuljahresabschnitt(idSchuljahresabschnitt, force);
	}

	/**
	 * Gibt die ID des aktuell gewählten Schuljahresabschnittes zurück
	 *
	 * @returns die ID des aktuell gewählten Schuljahresabschnittes
	 */
	public get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	public get filterNurSichtbar(): boolean {
		const configState = useConfigState();
		return configState.config.getValue("lehrer.auswahl.filterNurSichtbar") === 'true';
	}

	public async setFilterNurSichtbar(value: boolean): Promise<void> {
		const configState = useConfigState();
		await configState.config.setValue('lehrer.auswahl.filterNurSichtbar', value ? "true" : "false");
	};

	public get filterNurStatistikrelevant(): boolean {
		const configState = useConfigState();
		return configState.config.getValue("lehrer.auswahl.filterNurStatistikrelevant") === 'true';
	}

	public async setFilterNurStatistikrelevant(value: boolean): Promise<void> {
		const configState = useConfigState();
		await configState.config.setValue('lehrer.auswahl.filterNurStatistikrelevant', value ? "true" : "false");
	};

	/**
	 * Gibt eine Map mit den Stundenplänen des Schuljahresabschnittes zugeordnet zu deren ID zurück.
	 *
	 * @returns die Map mit den Stundenplänen
	 */
	public get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	public get mapSchulen(): Map<string, SchulEintrag> {
		return this._state.value.mapSchulen;
	}

	public get mapFaecher(): Map<number, FachDaten> {
		return this._state.value.mapFaecher;
	}

	/**
	 * Erstellt einen neunen Auswahl-Manager für den angegebenen Schuljahresabschnitt. Die Daten dafür werden
	 * über die API abgefragt.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @returns Eine Promise mit den Anpassungen für den Manager und dessen Daten reaktiven State
	 */
	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<LehrerAuswahlReactiveState>> {
		const abschnittState = useAbschnittState();
		const schuljahresabschnitt = abschnittState.getOrNull(idSchuljahresabschnitt);
		if (schuljahresabschnitt === null) {
			throw new DeveloperNotificationException('Es ist kein gültiger Schuljahresabschnitt ausgewählt');
		}

		// Lade die Daten von der API
		const listLehrer = await api.server.getLehrer(api.schema);

		// Erstelle den Lehrer-Liste-Manager
		const schuleState = useSchuleState();
		const manager = new LehrerListeManager(
			idSchuljahresabschnitt,
			schuleState.abschnitt.id,
			abschnittState.alle,
			schuleState.schulform,
			listLehrer);

		// Übernehme den Filter von dem vorigen Manager oder initialisiere ihn neu, falls kein voriger Manager vorhanden ist
		if (this._state.value.manager === undefined) {
			manager.setFilterAuswahlPermitted(true);
			manager.setFilterNurSichtbar(this.filterNurSichtbar);
			manager.setFilterNurStatistikRelevant(this.filterNurStatistikrelevant);
		} else {
			manager.useFilter(this._state.value.manager);
		}

		return { manager };
	}

	/**
	 * Lädt die Daten für den übergebenen Listeneintrag eines Lehrers
	 *
	 * @param auswahl   der ausgewählte Listeneintrag eines Lehrers
	 *
	 * @returns eine Promise mit den Daten zu dem Lehrer
	 */
	public async ladeDaten(auswahl: LehrerListeEintrag | null, state: Partial<LehrerAuswahlReactiveState>): Promise<LehrerStammdaten | null> {
		if (auswahl === null) {
			return null;
		}

		if (this.manager.hasPersonalDaten()) {
			state.lehrerUnterrichtsfaecher = await api.server.getLehrerUnterrichtsfaecher(api.schema, auswahl.id);
		}

		const [stammdaten, listLeitungsfunktionen] = await Promise.all([
			api.server.getLehrerStammdaten(api.schema, auswahl.id),
			api.server.getAllSchulleitungenByLehrer(api.schema, auswahl.id),
		]);

		state.listLeitungsfunktionen = listLeitungsfunktionen;
		return stammdaten;
	}

	public async ladeDatenMultiple(auswahlList: List<LehrerListeEintrag>, state: Partial<LehrerAuswahlReactiveState>): Promise<List<LehrerStammdaten> | null> {
		if (auswahlList.isEmpty()) {
			return null;
		}

		const ids: List<number> = new ArrayList();
		for (const eintrag of auswahlList) {
			ids.add(eintrag.id);
		}
		return await api.server.getLehrerStammdatenMultiple(ids, api.schema);
	}

	/**
	 * Führt einen Patch auf den Lehrerstammdaten über die API aus.
	 *
	 * @param data   die Daten des Patches
	 * @param id     die ID des zu patchenden Lehrers
	 *
	 * @returns eine Promise, die bei Erfolg true zurückgibt
	 */
	protected async doPatch(data: Partial<LehrerStammdaten>, id: number): Promise<boolean> {
		await api.server.patchLehrerStammdaten(data, api.schema, id);
		return true;
	}

	public async patchMultiple(pendingStateManager: PendingStateManager<any>): Promise<void> {
		api.status.start();

		const partialsToPatch = pendingStateManager.partials;
		// TODO einbauen
		// await api.server.patchLehrerStammdatenMultiple(partialsToPatch, api.schema);

		// Übernehme nur geänderte LehrerStammdaten Objekte in den AuswahlManager, damit nicht alle Stammdaten neugeladen werden müssen
		for (const partialToPatch of partialsToPatch) {
			if (partialToPatch.id !== undefined) {
				const patchId = (partialToPatch as Record<string, any>)[pendingStateManager.idFieldName];
				const currentStammdaten = this._state.value.manager?.getListeDaten().get(patchId);
				this._state.value.manager?.getListeDaten().put(patchId, Object.assign(Object.assign({}, currentStammdaten), partialToPatch));
			}
		}

		pendingStateManager.resetPendingState();
		this.commit();
		api.status.stop();
	};

	protected filterOnDelete(ids: List<number>): List<number> {
		const list = new ArrayList<number>();
		for (const id of ids) {
			if (!this.manager.getIdsReferenzierterLehrer().contains(id)) {
				list.add(id);
			}
		}
		return list;
	}

	/**
	 * Führt eine Lösch-Operation auf dem Server für die Lehrer mit den übergebenen IDs aus.
	 *
	 * @param ids   die IDs der Lehrer
	 *
	 * @returns eine Promise mit dem Ergebis der Lösch-Operation
	 */
	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteLehrer(ids, api.schema);
	}

	protected deleteMessage(id: number, lehrer: LehrerListeEintrag | null): string {
		return `Lehrer ${(lehrer?.vorname ?? '???') + ' ' + (lehrer?.nachname ?? '???')} (ID: ${id.toString()}) wurde erfolgreich gelöscht.`;
	}

	public deleteCheck(): [boolean, List<string>] {
		const errorLog = new ArrayList<string>();
		const benutzerState = useBenutzerState();
		if (!benutzerState.benutzerHatKompetenz(BenutzerKompetenz.LEHRERDATEN_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Lehrern vor.');
		}

		for (const id of this.manager.getIdsReferenzierterLehrer()) {
			const lehrer = this.manager.liste.get(id);
			if (lehrer) {
				errorLog.add(`Die Lehrkraft ${lehrer.vorname} ${lehrer.nachname} ist an anderer Stelle referenziert und kann daher nicht gelöscht werden.`);
			}
		}

		return [errorLog.isEmpty(), errorLog];
	};

	public async updateMapStundenplaene(): Promise<void> {
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		const benutzerState = useBenutzerState();
		if (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
			const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, this.idSchuljahresabschnitt);
			for (const l of listStundenplaene) {
				mapStundenplaene.set(l.id, l);
			}
		}
		this.setPatchedState({ mapStundenplaene });
	}

	private async createPersonalabschnittsdaten(idLehrer: number): Promise<LehrerPersonalabschnittsdaten> {
		const idSchuljahresabschnitt = this.idSchuljahresabschnitt;
		return await api.server.createLehrerPersonalabschnittsdaten({ idLehrer, idSchuljahresabschnitt }, api.schema);
	}

	public async add(data: Partial<LehrerStammdaten>): Promise<LehrerStammdaten> {
		const result = await api.server.addLehrerStammdaten(data, api.schema);
		await this.createPersonalabschnittsdaten(result.id);
		await this.setSchuljahresabschnitt(this.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(result.id);
		return result;
	};



	/**
	 * Aktualisiere beim Manager bei den neuen Daten ggf. auch die Personaldaten
	 *
	 * @param daten   die neuen Daten des Lehrers
	 */
	protected async updateDaten(daten: LehrerStammdaten | null) {
		const hattePersonaldaten = this.manager.hasPersonalDaten();
		this.manager.setDaten(daten);
		const personaldaten = (this.manager.hasDaten() && hattePersonaldaten)
			? await api.server.getLehrerPersonaldaten(api.schema, this.manager.auswahl().id)
			: null;
		this.manager.setPersonalDaten(personaldaten);
	}

	protected async updateManager(manager: LehrerListeManager, managerAlt: LehrerListeManager, daten: LehrerStammdaten) {
		if (managerAlt.hasPersonalDaten()) {
			manager.setPersonalDaten(await api.server.getLehrerPersonaldaten(api.schema, daten.id));
		}
	}

	public async loadPersonaldaten(): Promise<void> {
		if (!this.manager.hasDaten()) {
			return;
		}
		const listSchulen = await api.server.getSchulenMitKuerzel(api.schema);
		const mapSchulen = new Map<string, SchulEintrag>();
		for (const s of listSchulen) {
			if (s.schulnummerStatistik !== null) {
				mapSchulen.set(s.schulnummerStatistik, s);
			}
		}
		const personaldaten = await api.server.getLehrerPersonaldaten(api.schema, this.manager.auswahl().id);
		if (personaldaten.abschnittsdaten.isEmpty()) {
			const result = await this.createPersonalabschnittsdaten(personaldaten.id);
			personaldaten.abschnittsdaten.add(result);
		}
		this.manager.setPersonalDaten(personaldaten);
		const faecher = await api.server.getFaecher(api.schema);
		const mapFaecher = new Map<number, FachDaten>();
		for (const f of faecher) {
			mapFaecher.set(f.id, f);
		}
		const lehrerUnterrichtsfaecher = await api.server.getLehrerUnterrichtsfaecher(api.schema, this.manager.auswahl().id);
		this.setPatchedState({ mapSchulen, mapFaecher, lehrerUnterrichtsfaecher });
	}

	public async unloadPersonaldaten(): Promise<void> {
		this.manager.setPersonalDaten(null);
		this._state.value.lehrerUnterrichtsfaecher = new ArrayList();
		this.commit();
	}


	public async patchPersonaldaten(data: Partial<LehrerPersonaldaten>): Promise<boolean> {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		const personaldaten = this.manager.personalDaten();
		await api.server.patchLehrerPersonaldaten(data, api.schema, personaldaten.id);
		Object.assign(personaldaten, data);
		this.manager.setPersonalDaten(personaldaten);
		this.commit();
		return true;
	};


	private getAbschnitt(id: number): LehrerPersonalabschnittsdaten {
		const abschnitt = this.manager.getAbschnittBySchuljahresabschnittsId(this.idSchuljahresabschnitt);
		if (abschnitt === null) {
			throw new DeveloperNotificationException("Es konnten keine gültigen Lehrerabschnittsdaten mit der ID " + id.toString() + " geladen werden.");
		}
		return abschnitt;
	};


	public async patchPersonalAbschnittsdaten(data: Partial<LehrerPersonalabschnittsdaten>, id: number): Promise<void> {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		const abschnittsdaten = this.manager.getAbschnittById(id);
		if (abschnittsdaten === null) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten mit der ID " + id.toString() + " geladen.");
		}
		await api.server.patchLehrerPersonalabschnittsdaten(data, api.schema, abschnittsdaten.id);
		Object.assign(abschnittsdaten, data);
		this.commit();
	};


	public async addMehrleistung(data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>): Promise<void> {
		const abschnitt = this.getAbschnitt(this.idSchuljahresabschnitt);
		const result = await api.server.addLehrerPersonalabschnittsdatenMehrleistung(data, api.schema);
		abschnitt.mehrleistung.add(result);
		this.commit();
	};

	public async patchMehrleistung(data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number): Promise<void> {
		await api.server.patchLehrerPersonalabschnittsdatenMehrleistung(data, api.schema, id);
		this.commit();
	};

	public async removeMehrleistung(data: LehrerPersonalabschnittsdatenAnrechnungsstunden): Promise<void> {
		const abschnitt = this.getAbschnitt(this.idSchuljahresabschnitt);
		await api.server.deleteLehrerPersonalabschnittsdatenMehrleistung(api.schema, data.id);
		abschnitt.mehrleistung.remove(data);
		this.commit();
	};

	public async addMinderleistung(data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>): Promise<void> {
		const abschnitt = this.getAbschnitt(this.idSchuljahresabschnitt);
		const result = await api.server.addLehrerPersonalabschnittsdatenMinderleistung(data, api.schema);
		abschnitt.minderleistung.add(result);
		this.commit();
	};

	public async patchMinderleistung(data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number): Promise<void> {
		await api.server.patchLehrerPersonalabschnittsdatenMinderleistung(data, api.schema, id);
		this.commit();
	};

	public async removeMinderleistung(data: LehrerPersonalabschnittsdatenAnrechnungsstunden): Promise<void> {
		const abschnitt = this.getAbschnitt(this.idSchuljahresabschnitt);
		await api.server.deleteLehrerPersonalabschnittsdatenMinderleistung(api.schema, data.id);
		abschnitt.minderleistung.remove(data);
		this.commit();
	};

	public async addAnrechnung(data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>): Promise<void> {
		const abschnitt = this.getAbschnitt(this.idSchuljahresabschnitt);
		const result = await api.server.addLehrerPersonalabschnittsdatenAllgemeineAnrechnung(data, api.schema);
		abschnitt.anrechnungen.add(result);
		this.commit();
	};

	public async patchAnrechnungen(data: List<Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>>): Promise<void> {
		await api.server.patchLehrerPersonalabschnittsdatenAllgemeineAnrechnungen(data, api.schema);
		this.commit();
	};

	public async removeAnrechnung(data: LehrerPersonalabschnittsdatenAnrechnungsstunden): Promise<void> {
		const abschnitt = this.getAbschnitt(this.idSchuljahresabschnitt);
		await api.server.deleteLehrerPersonalabschnittsdatenAllgemeineAnrechnung(api.schema, data.id);
		abschnitt.anrechnungen.remove(data);
		this.commit();
	};

	public async addLehramt(eintrag: Partial<LehrerLehramtEintrag>): Promise<void> {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Lehrämter können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		}
		const result = await api.server.addLehrerLehramt(eintrag, api.schema);
		this.manager.personalDaten().lehraemter.add(result);
		this.commit();
	};

	public async removeLehraemter(eintraege: List<LehrerLehramtEintrag>): Promise<void> {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Lehrämter können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		}
		// TODO ggf. zu einem API-Aufruf zusammenfassen - Server-API muss dafür noch erweitert werden
		for (const eintrag of eintraege) {
			await api.server.deleteLehrerLehramt(api.schema, eintrag.id);
		}
		this.manager.personalDaten().lehraemter.removeAll(eintraege);
		this.commit();
	};

	public async patchLehramt(eintrag: LehrerLehramtEintrag, patch: Partial<LehrerLehramtEintrag>): Promise<boolean> {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		await api.server.patchLehrerLehramt(patch, api.schema, eintrag.id);
		Object.assign(eintrag, patch);
		this.commit();
		return true;
	};

	public async addLehrbefaehigung(eintrag: Partial<LehrerLehrbefaehigungEintrag>): Promise<void> {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Lehrbefähigungen können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		}
		if (eintrag.idLehramt === undefined) {
			throw new DeveloperNotificationException("Lehrbefähigungen können nur mit einer Lehramts-ID hinzugefügt werden.");
		}
		const result = await api.server.addLehrerLehrbefaehigung(eintrag, api.schema);
		for (const lehramt of this.manager.personalDaten().lehraemter) {
			if (lehramt.id === eintrag.idLehramt) {
				lehramt.lehrbefaehigungen.add(result);
			}
		}
		this.commit();
	};

	public async removeLehrbefaehigungen(eintraege: List<LehrerLehrbefaehigungEintrag>): Promise<void> {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Lehrbefähigungen können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		}

		const ids: List<number> = new ArrayList();
		for (const eintrag of eintraege) {
			ids.add(eintrag.id);
		}
		await api.server.deleteLehrerLehrbefaehigungen(ids, api.schema);
		for (const lehramt of this.manager.personalDaten().lehraemter) {
			lehramt.lehrbefaehigungen.removeAll(eintraege);
		}
		this.commit();
	};

	public async patchLehrbefaehigung(eintrag: LehrerLehrbefaehigungEintrag, patch: Partial<LehrerLehrbefaehigungEintrag>): Promise<boolean> {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		await api.server.patchLehrerLehrbefaehigung(patch, api.schema, eintrag.id);
		Object.assign(eintrag, patch);
		this.commit();
		return true;
	};

	public async addFachrichtung(eintrag: Partial<LehrerFachrichtungEintrag>): Promise<void> {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Fachrichtungen können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		}
		if (eintrag.idLehramt === undefined) {
			throw new DeveloperNotificationException("Fachrichtungen können nur mit einer Lehramts-ID hinzugefügt werden.");
		}
		const result = await api.server.addLehrerFachrichtung(eintrag, api.schema);
		for (const lehramt of this.manager.personalDaten().lehraemter) {
			if (lehramt.id === eintrag.idLehramt) {
				lehramt.fachrichtungen.add(result);
			}
		}
		this.commit();
	};

	public async removeFachrichtungen(eintraege: List<LehrerFachrichtungEintrag>): Promise<void> {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Fachrichtungen können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		}
		const ids: List<number> = new ArrayList();
		for (const eintrag of eintraege) {
			ids.add(eintrag.id);
		}
		await api.server.deleteLehrerFachrichtungen(ids, api.schema);
		for (const lehramt of this.manager.personalDaten().lehraemter) {
			lehramt.fachrichtungen.removeAll(eintraege);
		}
		this.commit();
	};

	public async patchFachrichtung(eintrag: LehrerFachrichtungEintrag, patch: Partial<LehrerFachrichtungEintrag>): Promise<boolean> {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		await api.server.patchLehrerFachrichtung(patch, api.schema, eintrag.id);
		Object.assign(eintrag, patch);
		this.commit();
		return true;
	};


	public get lehrerUnterrichtsfaecher(): List<LehrerUnterrichtsfach> {
		return this._state.value.lehrerUnterrichtsfaecher;
	}

	public async addLehrerUnterrichtsfach(eintrag: Partial<LehrerUnterrichtsfach>): Promise<void> {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Unterrichtsfächer können nur hinzugefügt werden, wenn gültige Personaldaten geladen sind.");
		}
		const result = await api.server.addLehrerUnterrichtsfach({ ...eintrag, idLehrer: this.manager.auswahl().id }, api.schema);
		this._state.value.lehrerUnterrichtsfaecher.add(result);
		this.commit();
	};

	public async patchLehrerUnterrichtsfach(eintrag: LehrerUnterrichtsfach, patch: Partial<LehrerUnterrichtsfach>): Promise<void> {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		await api.server.patchLehrerUnterrichtsfach(patch, api.schema, eintrag.id);
		Object.assign(eintrag, patch);
		this.commit();
	};

	public async removeLehrerUnterrichtsfach(eintrag: LehrerUnterrichtsfach): Promise<void> {
		if (!this.manager.hasPersonalDaten()) {
			throw new DeveloperNotificationException("Unterrichtsfächer können nur entfernt werden, wenn gültige Personaldaten geladen sind.");
		}
		await api.server.deleteLehrerUnterrichtsfach(api.schema, eintrag.id);
		this._state.value.lehrerUnterrichtsfaecher.remove(eintrag);
		this.commit();
	};



	public get leitungsfunktionen(): List<Schulleitung> {
		const list = new ArrayList<Schulleitung>();
		list.addAll(this._state.value.listLeitungsfunktionen);
		return list;
	}

	public async addLeitungsfunktion(data: Partial<Schulleitung>, idLehrer: number): Promise<void> {
		const eintrag = await api.server.addSchulleitung({ ...data, idLehrer }, api.schema);
		const listLeitungsfunktionen = this.leitungsfunktionen;
		listLeitungsfunktionen.add(eintrag);
		this.setPatchedState({ listLeitungsfunktionen });
	};

	public async patchLeitungsfunktion(data: Partial<Schulleitung>, idEintrag: number): Promise<void> {
		await api.server.patchSchulleitung(data, api.schema, idEintrag);
		const listLeitungsfunktionen = this.leitungsfunktionen;
		for (const l of listLeitungsfunktionen) {
			if (l.id === idEintrag) {
				Object.assign(l, data);
				break;
			}
		}
		this.setPatchedState({ listLeitungsfunktionen });
	};

	public async deleteLeitungsfunktionen(idsEintraege: List<number>): Promise<void> {
		await api.server.deleteSchulleitungen(idsEintraege, api.schema);
		const listLeitungsfunktionen = this.leitungsfunktionen;
		for (const id of idsEintraege) {
			for (let i = 0; i < listLeitungsfunktionen.size(); i++) {
				if (listLeitungsfunktionen.get(i).id === id) {
					listLeitungsfunktionen.removeElementAt(i);
					break;
				}
			}
		}
		this.setPatchedState({ listLeitungsfunktionen });
	};

}

export const lehrerAuswahlStateImpl = new LehrerAuswahlStateImpl();
