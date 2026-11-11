import type { ShallowRef } from "vue";
import { shallowRef } from "vue";

import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ViewType } from "@ui/ui/nav/ViewType";

import { RoutingStatus } from "~/router/RoutingStatus";

import type { GenericAuswahlStateRoutingAdapter } from "./GenericAuswahlStateRoutingAdapter";

/**
 * Die Schnittstelle zu dem Auswahl-State, den der Navigator für die Navigation benötigt.
 */
export interface GenericAuswahlNavigatorHost {

	/** Gibt zurück, ob die Auswahlliste einen Eintrag mit der ID enthält. */
	hasEntry(id: number): boolean;

	/** Gibt die ID des ersten Eintrags der gefilterten Liste zurück oder null, wenn diese leer ist. */
	getFirstFilteredId(): number | null;

	/** Setzt den Eintrag mit der ID als Auswahl und lädt dessen Daten. */
	setEntry(id: number): Promise<void>;

	/** Entfernt eine Mehrfachauswahl. */
	clearSelection(): void;

	/** Lädt die Daten der Mehrfachauswahl nach. */
	loadMultiple(): Promise<void>;

	/** Triggert die Reaktivität des Auswahl-States. */
	commit(): void;

}

/**
 * Der Navigator eines Auswahl-States. Er hält die aktive Ansicht (Default, Hinzufügen, Gruppenprozesse, Schnelleingabe)
 * und führt die Wechsel der Ansicht und der Auswahl zusammen mit dem Routing durch.
 */
export class GenericAuswahlNavigator {

	/** Der Adapter für das Routing zu den unterstützten Ansichten */
	private readonly _routingAdapter: GenericAuswahlStateRoutingAdapter;

	/** Der State des Auswahl-States, mit dem die Auswahl gesetzt wird */
	private readonly _host: GenericAuswahlNavigatorHost;

	/** Die aktuell aktive Ansicht */
	private readonly _viewType: ShallowRef<ViewType> = shallowRef(ViewType.DEFAULT);

	/**
	 * Erstellt einen neuen Navigator.
	 *
	 * @param routingAdapter   der Adapter für das Routing zu den unterstützten Ansichten
	 * @param host             der Auswahl-State, auf dem navigiert wird
	 */
	public constructor(routingAdapter: GenericAuswahlStateRoutingAdapter, host: GenericAuswahlNavigatorHost) {
		this._routingAdapter = routingAdapter;
		this._host = host;
	}

	/**
	 * Gibt die Art der aktuell aktiven Ansicht zurück (Default, Hinzufügen, Gruppenprozesse oder Schnelleingabe).
	 *
	 * @returns die Art der Ansicht
	 */
	public get activeViewType(): ViewType {
		return this._viewType.value;
	}

	/**
	 * Setzt die aktive Ansicht auf die Default-Ansicht zurück, ohne die Route zu ändern.
	 */
	public reset(): void {
		this._viewType.value = ViewType.DEFAULT;
	}

	/**
	 * Setzt die aktive Ansicht und die Ansicht der Route auf die Default-Ansicht zurück.
	 */
	public resetView(): void {
		this.reset();
		this._routingAdapter.setDefaultView(ViewType.DEFAULT);
	}

	/**
	 * Wählt den Eintrag mit der ID in der Default-Ansicht - also nicht in der Ansicht für das Hinzufügen oder die Gruppenprozesse.
	 * Ist keine ID angegeben oder in der Liste nicht vorhanden, so wird der erste Eintrag der gefilterten Liste gewählt.
	 *
	 * @param id   die ID des Eintrags oder null
	 */
	public async select(id?: number | null): Promise<void> {
		if ((id !== null) && (id !== undefined) && this._host.hasEntry(id)) {
			// Führe das Routing aus
			const useDefaultRoute = this.activeViewType !== ViewType.DEFAULT;
			const result = await this._routingAdapter.goto(id, useDefaultRoute);

			// Wenn das Routing unterbrochen wurde, dann wird der Eintrag beim Manager gesetzt
			if (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE) {
				await this._host.setEntry(id);
			}

			// Entferne ggf. eine Mehrfachauswahl, wenn von den Gruppenprozessen zur Einzelauswahl gewechselt wurde
			if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE)) {
				this._host.clearSelection();
				this._viewType.value = ViewType.DEFAULT;
			}

			this._host.commit();
			return;
		}

		// Wenn keine ID übergeben wurde, so wird ein Default-Eintrag ausgewählt, sofern der aktuelle Filter ein nicht leeres Ergebnis liefert
		const idDefault = this._host.getFirstFilteredId();
		if (idDefault === null) {
			return;
		}
		const result = await this._routingAdapter.goto(idDefault, true);
		if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE)) {
			this._host.clearSelection();
			this._viewType.value = ViewType.DEFAULT;
			this._host.commit();
		}
		await this._host.setEntry(idDefault);
	}

	/**
	 * Wechselt in die Ansicht für die Gruppenprozesse.
	 *
	 * @param navigate   gibt an, ob ein Routing durchgeführt werden soll oder nur die Ansicht gesetzt werden soll
	 */
	public async gotoGruppenprozesse(navigate: boolean = true): Promise<void> {
		if (!this._routingAdapter.hatGruppenprozesse) {
			throw new DeveloperNotificationException("Es wird keine Ansicht für Gruppenprozesse unterstützt. Daher darf diese Methode nicht aufgerufen werden.");
		}

		// Lade die Daten der Mehrfachauswahl nach
		await this._host.loadMultiple();

		if (this.activeViewType !== ViewType.GRUPPENPROZESSE) {
			this._viewType.value = ViewType.GRUPPENPROZESSE;
			if (navigate) {
				await this._routingAdapter.gotoGruppenprozesse(true);
			}
		}
		this._host.commit();
	}

	/**
	 * Wechselt in die Ansicht für das Hinzufügen von Daten.
	 *
	 * @param navigate   gibt an, ob ein Routing durchgeführt werden soll oder nur die Ansicht gesetzt werden soll
	 */
	public async gotoHinzufuegen(navigate: boolean): Promise<void> {
		if (!this._routingAdapter.hatHinzufuegen) {
			throw new DeveloperNotificationException("Es wird keine Ansicht für das Hinzufügen von Daten unterstützt. Daher darf diese Methode nicht aufgerufen werden.");
		}
		if (this.activeViewType !== ViewType.HINZUFUEGEN) {
			this._viewType.value = ViewType.HINZUFUEGEN;
			if (navigate) {
				const result = await this._routingAdapter.gotoHinzufuegen();
				if (result === RoutingStatus.SUCCESS) {
					this._host.clearSelection();
				}
			} else {
				this._routingAdapter.setViewHinzufuegen();
			}
			this._host.commit();
		}
	}

	/**
	 * Wechselt in die Ansicht für die Schnelleingabe von Daten.
	 *
	 * @param navigate   gibt an, ob ein Routing durchgeführt werden soll oder nur die Ansicht gesetzt werden soll
	 * @param id         die ID des Eintrags, zu dessen Schnelleingabe navigiert werden soll
	 */
	public async gotoSchnelleingabe(navigate: boolean, id?: number | null): Promise<void> {
		if (!this._routingAdapter.hatSchnelleingabe) {
			throw new DeveloperNotificationException("Es wird keine Ansicht für eine Schnelleingabe von Daten unterstützt. Daher darf diese Methode nicht aufgerufen werden.");
		}
		if (this.activeViewType !== ViewType.NEU) {
			this._viewType.value = ViewType.NEU;
			if (navigate && (id !== undefined) && (id !== null) && this._host.hasEntry(id)) {
				await this._routingAdapter.gotoSchnelleingabe(id);
			}
			this._host.commit();
		}
	}

}
