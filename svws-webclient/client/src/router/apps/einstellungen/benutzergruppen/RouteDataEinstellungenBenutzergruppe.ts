import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { routeEinstellungenBenutzergruppeDaten } from "~/router/apps/einstellungen/benutzergruppen/RouteEinstellungenBenutzergruppeDaten";
import { routeEinstellungenBenutzergruppe } from "~/router/apps/einstellungen/benutzergruppen/RouteEinstellungenBenutzergruppe";
import { RouteNode } from "~/router/RouteNode";
import { BenutzergruppeDaten } from "@core/core/data/benutzer/BenutzergruppeDaten";
import { BenutzergruppeListeEintrag } from "@core/core/data/benutzer/BenutzergruppeListeEintrag";
import type { BenutzerListeEintrag } from "@core/core/data/benutzer/BenutzerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { BenutzerKompetenzGruppe } from "@core/core/types/benutzer/BenutzerKompetenzGruppe";
import { BenutzergruppenManager } from "@core/core/utils/benutzer/BenutzergruppenManager";
import { ArrayList } from "@core/java/util/ArrayList";
import { Arrays } from "@core/java/util/Arrays";
import type { List } from "@core/java/util/List";

interface RoutStateEinstellungenBenutzergruppe extends RouteStateInterface {
	auswahl: BenutzergruppeListeEintrag | undefined;
	mapBenutzergruppe: Map<number, BenutzergruppeListeEintrag>;
	manager: BenutzergruppenManager;
	benutzergruppen: List<BenutzergruppeListeEintrag>;
	daten: BenutzergruppeDaten | undefined;
	alleBenutzer: List<BenutzerListeEintrag>;
	benutzerInBenutzergruppe: List<BenutzerListeEintrag>;
}

const defaultState = <RoutStateEinstellungenBenutzergruppe> {
	auswahl: undefined,
	benutzergruppen: new ArrayList(),
	mapBenutzergruppe: new Map<number, BenutzergruppeListeEintrag>,
	manager: new BenutzergruppenManager(new BenutzergruppeDaten()),
	alleBenutzer: new ArrayList(),
	benutzerInBenutzergruppe: new ArrayList(),
	daten: undefined,
	view: routeEinstellungenBenutzergruppeDaten,
};

export class RouteDataEinstellungenBenutzergruppe extends RouteData<RoutStateEinstellungenBenutzergruppe> {

	public constructor() {
		super(defaultState);
	}

	private firstBenutzer(mapBenutzergruppe: Map<number, BenutzergruppeListeEintrag>): BenutzergruppeListeEintrag | undefined {
		if (mapBenutzergruppe.size === 0) {
			return undefined;
		}
		return mapBenutzergruppe.values().next().value;
	}

	private async ladeBenutzergruppen(eintrag: BenutzergruppeListeEintrag | undefined): Promise<BenutzergruppeDaten | undefined> {
		if (eintrag === undefined) {
			return undefined;
		}
		return await api.server.getBenutzergruppeDaten(api.schema, eintrag.id);
	}

	public async ladeListe() {
		const benutzergruppen = await api.server.getBenutzergruppenliste(api.schema);
		const alleBenutzer = await api.server.getBenutzerliste(api.schema);

		const mapBenutzergruppe = new Map<number, BenutzergruppeListeEintrag>();
		for (const benutzergruppe of benutzergruppen) {
			mapBenutzergruppe.set(benutzergruppe.id, benutzergruppe);
		}

		this.setPatchedState({ benutzergruppen, mapBenutzergruppe, alleBenutzer });
	}

	public async setBenutzergruppe(benutzerGruppe: BenutzergruppeListeEintrag | undefined) {
		if (benutzerGruppe?.id === this._state.value.auswahl?.id && this.hatDaten) {
			return;
		}
		if ((benutzerGruppe === undefined) || (this.mapBenutzergruppe.size === 0)) {
			this.setPatchedDefaultState({
				auswahl: undefined,
				benutzergruppen: new ArrayList(),
				mapBenutzergruppe: new Map(),
				manager: new BenutzergruppenManager(new BenutzergruppeDaten()),
				alleBenutzer: new ArrayList(),
				daten: undefined,
			});
			await this.ladeListe();
		}
		const neueAuswahl = benutzerGruppe ?? this.firstBenutzer(this.mapBenutzergruppe);
		const daten = await this.ladeBenutzergruppen(neueAuswahl);
		const benutzergruppenManager = (daten === undefined) ? undefined : new BenutzergruppenManager(daten);
		const listBenutzergruppenBenutzer = (neueAuswahl === undefined) ? undefined : await api.server.getBenutzerMitGruppenID(api.schema, neueAuswahl.id);
		this.setPatchedState({
			auswahl: neueAuswahl,
			manager: benutzergruppenManager,
			benutzerInBenutzergruppe: listBenutzergruppenBenutzer,
			daten: daten,
		});
	}

	gotoBenutzer = async (idBenutzer: number) => {
		const node = RouteNode.getNodeByName("einstellungen.benutzer.daten");
		if (node !== undefined) {
			await RouteManager.doRoute(node.getRoute({ id: idBenutzer }));
		}
	};

	gotoBenutzergruppe = async (value: BenutzergruppeListeEintrag | undefined) => {
		const route = (value === undefined) ? routeEinstellungenBenutzergruppe.getRoute() : routeEinstellungenBenutzergruppe.getRouteSelectedChild({ id: value.id });
		await RouteManager.doRoute(route);
	};

	get manager(): BenutzergruppenManager {
		return this._state.value.manager;
	}

	set manager(value: BenutzergruppenManager) {
		this._state.value.manager = value;
	}

	get auswahl(): BenutzergruppeListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	set auswahl(value: BenutzergruppeListeEintrag | undefined) {
		this._state.value.auswahl = value;
	}

	get benutzergruppen(): List<BenutzergruppeListeEintrag> {
		return this._state.value.benutzergruppen;
	}

	set benutzergruppen(value: List<BenutzergruppeListeEintrag>) {
		this._state.value.benutzergruppen = value;
	}

	get alleBenutzer(): List<BenutzerListeEintrag> {
		return this._state.value.alleBenutzer;
	}

	set alleBenutzer(value: List<BenutzerListeEintrag>) {
		this._state.value.alleBenutzer = value;
	}

	get benutzerInBenutzergruppe(): List<BenutzerListeEintrag> {
		return this._state.value.benutzerInBenutzergruppe;
	}

	set benutzerInBenutzergruppe(value: List<BenutzerListeEintrag>) {
		this._state.value.benutzerInBenutzergruppe = value;
	}

	get hatDaten(): boolean {
		return this._state.value.daten !== undefined;
	}

	get daten(): BenutzergruppeDaten {
		if (this._state.value.daten === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		}
		return this._state.value.daten;
	}

	set daten(value: BenutzergruppeDaten | undefined) {
		this._state.value.daten = value;
	}

	get mapBenutzergruppe(): Map<number, BenutzergruppeListeEintrag> {
		return this._state.value.mapBenutzergruppe;
	}

	set mapBenutzergruppe(value: Map<number, BenutzergruppeListeEintrag>) {
		this._state.value.mapBenutzergruppe = value;
	}


	/**
	 * Setzt die Bezeichnung der Benutzergruppe
	 *
	 * @param {string} bezeichnung
	 *
	 * @returns {Promise<void>}
	 */
	setBezeichnung = async (bezeichnung: string | null) => {
		if (bezeichnung === null) {
			return;
		}
		await api.server.setBenutzergruppeBezeichnung(bezeichnung, api.schema, this.manager.getID());
		this.manager.setBezeichnung(bezeichnung);
		const neueAuswahl = this.mapBenutzergruppe.get(this.daten.id);
		this.mapBenutzergruppe.set(this.daten.id, this.daten);
		this.setPatchedState({
			auswahl: neueAuswahl,
			mapBenutzergruppe: this.mapBenutzergruppe,
			manager: this.manager,
		});
	};

	/**
	 * Setzt, ob die Benutzergruppe eine administrative Gruppe ist oder nicht
	 *
	 * @param {boolean} istAdmin
	 *
	 * @returns {Promise<void>}
	 */
	setIstAdmin = async (istAdmin: boolean) => {
		if (istAdmin) {
			await api.server.addBenutzergruppeAdmin(api.schema, this.manager.getID());
		} else {
			await api.server.removeBenutzergruppeAdmin(api.schema, this.manager.getID());
		}
		this.manager.setAdmin(istAdmin);
		this.setPatchedState({
			manager: this.manager,
		});
	};


	/**
	 * Fügt die übergebene Kompetenz zu dieser Benutzergruppe hinzu
	 *
	 * @param kompetenz   die hinzuzufügende Kompetenz
	 */
	addKompetenz = async (kompetenz: BenutzerKompetenz) => {
		if (this.manager.hatKompetenz(kompetenz)) {
			return false;
		}

		const idsKompetenzen = new ArrayList<number>();
		idsKompetenzen.add(kompetenz.daten.id);

		await api.server.addBenutzergruppeKompetenzen(idsKompetenzen, api.schema, this.manager.getID());

		this.manager.addKompetenz(kompetenz);
		this.setPatchedState({
			manager: this.manager,
		});
		return true;
	};

	/**
	 * Entfernt die übergebene Kompetenz aus dieser Benutzergruppe
	 *
	 * @param kompetenz   die zu entfernende Kompetenz
	 */
	removeKompetenz = async (kompetenz: BenutzerKompetenz) => {
		const kid = new ArrayList<number>();
		kid.add(kompetenz.daten.id);
		if (!this.manager.hatKompetenz(kompetenz)) {
			return false;
		}
		await api.server.removeBenutzergruppeKompetenzen(kid, api.schema, this.manager.getID());
		this.manager.removeKompetenz(kompetenz);
		this.setPatchedState({
			manager: this.manager,
		});
		return true;
	};

	/**
	 * Fügt die übergebene Kompetenzen einer Benutzerkompetenzgruppe zu dieser Benutzergruppe hinzu
	 *
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen hinzugefügt werden.
	 */
	addBenutzerKompetenzGruppe = async (kompetenzgruppe: BenutzerKompetenzGruppe) => {
		const kids = new ArrayList<number>();
		if (!this.manager.istAdmin()) {
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				kids.add(komp.daten.id);
			}
			await api.server.addBenutzergruppeKompetenzen(kids, api.schema, this.manager.getID());
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (!this.manager.hatKompetenz(komp)) {
					this.manager.addKompetenz(komp);
				}
			}
		}
		this.setPatchedState({
			manager: this.manager,
		});
		return true;
	};

	/**
	 * Entfernt die übergebene Kompetenzen einer Benutzerkompetenzgruppe von dieser Benutzergruppe
	 *
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen entfernt werden.
	 */
	removeBenutzerKompetenzGruppe = async (kompetenzgruppe: BenutzerKompetenzGruppe) => {
		const kids = new ArrayList<number>();
		if (!this.manager.istAdmin()) {
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				kids.add(komp.daten.id);
			}
			await api.server.removeBenutzergruppeKompetenzen(kids, api.schema, this.manager.getID());
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (this.manager.hatKompetenz(komp)) {
					this.manager.removeKompetenz(komp);
				}
			}
		}
		this.setPatchedState({
			manager: this.manager,
		});
		return true;
	};

	/**
	 * Erstellt eine neue Benutzergruppe
	 *
	 * @param bezeichnung die Bezeichnung der neuen Benutzergruppe
	 * @param istAdmin    True, wenn die neue Benutzergruppe administrativ ist.
	 */
	create = async (bezeichnung: string, istAdmin: boolean) => {
		const bg = new BenutzergruppeDaten();
		bg.bezeichnung = bezeichnung;
		bg.istAdmin = istAdmin;
		const result = await api.server.createBenutzergruppe(bg, api.schema);
		const bgle = new BenutzergruppeListeEintrag();
		bgle.id = result.id;
		bgle.bezeichnung = result.bezeichnung;
		bgle.istAdmin = result.istAdmin;
		this.benutzergruppen.add(bgle);
		this.mapBenutzergruppe.set(bgle.id, bgle);
		this.setPatchedState({
			manager: this.manager,
			benutzergruppen: this.benutzergruppen,
			mapBenutzergruppe: this.mapBenutzergruppe,
		});
		await this.gotoBenutzergruppe(bgle);
	};

	/**
	 * Entfernt die ausgewählten Benutzergruppen
	 *
	 * @param {BenutzergruppeListeEintrag[]} benutzergruppenToDelete
	 */
	deleteBenutzergruppen = async (benutzergruppenToDelete: BenutzergruppeListeEintrag[]) => {
		const idsToDelete = benutzergruppenToDelete.map(e => e.id);
		await api.server.removeBenutzerGruppe(Arrays.asList(idsToDelete), api.schema);

		for (const benutzergruppe of benutzergruppenToDelete) {
			this.mapBenutzergruppe.delete(benutzergruppe.id);
			this.benutzergruppen.remove(benutzergruppe);
		}

		alert("Benutzergruppe gelöscht.");

		const auswahlDeleted = benutzergruppenToDelete.some(gruppe => gruppe.id === this.auswahl?.id);
		if (auswahlDeleted) {
			await this.gotoBenutzergruppe(this.benutzergruppen.get(0));
		}

		this.setPatchedState({
			benutzergruppen: this.benutzergruppen,
			mapBenutzergruppe: this.mapBenutzergruppe,
		});
	};

	/**
	 * Fügt den Benutzer in die ausgewählte Benutzergruppe ein
	 *
	 * @param {BenutzerListeEintrag} benutzerToAdd
	 *
	 * @returns {Promise<void>}
	 */
	addBenutzerToBenutzergruppe = async (benutzerToAdd: BenutzerListeEintrag): Promise<void> => {
		await api.server.addBenutzergruppeBenutzer(Arrays.asList([benutzerToAdd.id]), api.schema, this.manager.getID());

		this.benutzerInBenutzergruppe.add(benutzerToAdd);

		this.commit();
	};

	/**
	 * Entfernt einen Benutzer aus der Gruppe
	 *
	 * @param {BenutzerListeEintrag} benutzerToRemove
	 *
	 * @returns {Promise<void>}
	 */
	removeBenutzerFromBenutzergruppe = async (benutzerToRemove: BenutzerListeEintrag): Promise<void> => {
		await api.server.removeBenutzergruppeBenutzer(Arrays.asList([benutzerToRemove.id]), api.schema, this.manager.getID());

		this.benutzerInBenutzergruppe.remove(benutzerToRemove);

		this.commit();
	};

}
