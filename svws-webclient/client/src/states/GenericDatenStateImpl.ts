import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { StateManager } from "@ui/ui/StateManager";

import type { GenericAuswahlHost, GenericAuswahlSelectCallback } from "./GenericAuswahlState";
import type { GenericDatenState } from "./GenericDatenState";
import type { GenericDetailState } from "./GenericDetailState";

/** Der reaktive State der Daten der Einzelauswahl */
export interface GenericDatenReactiveState<TDaten> {
	daten: TDaten | null;
	vorherige: TDaten | null;
}

/**
 * Die abstrakte Basisklasse für die generischen States mit Daten zu der Einzelauswahl einer Auswahlliste (z.B. die
 * Stammdaten oder die Personaldaten). Der State hält die Daten der Einzelauswahl. Er hängt sich bei der Auswahl an und
 * lädt die Daten zusammen mit den Daten seiner Kinder (z.B. Telefoneinträge), wenn sich die Auswahl ändert. Änderungen
 * an den Daten, die auch in der Auswahlliste angezeigt werden (z.B. Namen), können über den Auswahl-State in den Eintrag
 * der Auswahlliste übernommen werden (siehe {@link uebernehmeInEintrag}).
 *
 * Optional kann der State im Lazy-Modus betrieben werden. Er lädt dann erst Daten, nachdem er mit {@link activate}
 * aktiviert wurde (z.B. beim Betreten einer Route), und folgt ab dann der Auswahl, bis er mit {@link deactivate}
 * wieder deaktiviert wird.
 *
 * @typeParam TEintrag   der Typ der Einträge der Auswahlliste
 * @typeParam TDaten     der Typ der Daten
 */
export abstract class GenericDatenStateImpl<TEintrag extends { id: number }, TDaten extends object> extends StateManager<GenericDatenReactiveState<TDaten>> implements GenericDatenState<TDaten> {

	/** Die Funktion, welche die ID zu Daten liefert */
	private readonly _getIdDaten: (daten: TDaten) => number;

	/** Die States mit Daten, die zu den Daten gehören und mit diesen geladen werden */
	private readonly _kinder: GenericDetailState[] = [];

	/** Der Auswahl-State, an den sich der State anhängt und über den Einträge der Auswahlliste angepasst werden */
	private readonly _auswahl: GenericAuswahlHost<TEintrag>;

	/** Gibt an, ob der State im Lazy-Modus betrieben wird */
	private readonly _lazy: boolean;

	/** Der Callback, mit dem sich der State bei der Auswahl angehängt hat */
	private readonly _selectCallback: GenericAuswahlSelectCallback = async idAuswahl => await this.loadForSelection(idAuswahl);

	/** Die Funktion, welche den Callback bei der Auswahl wieder entfernt, oder null, wenn der State nicht angehängt ist */
	private _removeSelectCallback: (() => void) | null = null;


	/**
	 * Erzeugt einen neuen State für die Daten der Einzelauswahl.
	 *
	 * @param getIdDaten   die Funktion, welche die ID zu Daten liefert
	 * @param auswahl      der Auswahl-State, an den sich der State anhängt
	 * @param lazy         gibt an, ob der State im Lazy-Modus betrieben wird, d.h. sich erst bei {@link activate}
	 *                     an die Auswahl anhängt (Standard: false, der State hängt sich sofort an)
	 */
	protected constructor(getIdDaten: (daten: TDaten) => number, auswahl: GenericAuswahlHost<TEintrag>, lazy: boolean = false) {
		super({ daten: null, vorherige: null });
		this._getIdDaten = getIdDaten;
		this._auswahl = auswahl;
		this._lazy = lazy;
		if (!lazy) {
			this._removeSelectCallback = auswahl.onSelect(this._selectCallback);
		}
	}

	/**
	 * Gibt zurück, ob der State aktiv ist, d.h. ob er an die Auswahl angehängt ist und deren Änderungen folgt. Ein State
	 * ohne Lazy-Modus ist stets aktiv.
	 *
	 * @returns true, wenn der State aktiv ist
	 */
	public get isActive(): boolean {
		return this._removeSelectCallback !== null;
	}

	/**
	 * Aktiviert einen State im Lazy-Modus: Die Daten zu der ID werden (mit den Daten der Kinder) geladen und übernommen und
	 * der State hängt sich an die Auswahl an, bis er mit {@link deactivate} deaktiviert wird. Ist der State bereits aktiv,
	 * so werden die Daten neu geladen. Scheitert das Laden, so bleibt der Zustand des States unverändert. Fehlt die
	 * Berechtigung zum Laden der Daten (siehe {@link hasPermission}), so geschieht nichts und der State bleibt inaktiv.
	 *
	 * @param id   die ID des Eintrags der Auswahlliste
	 *
	 * @throws DeveloperNotificationException   wenn der State nicht im Lazy-Modus betrieben wird
	 */
	public async activate(id: number): Promise<void> {
		if (!this._lazy) {
			throw new DeveloperNotificationException("Der State wird nicht im Lazy-Modus betrieben und kann daher nicht aktiviert werden.");
		}
		if (!this.hasPermission()) {
			return;
		}
		const apply = await this.loadForSelection(id);
		apply();
		if (this._removeSelectCallback === null) {
			this._removeSelectCallback = this._auswahl.onSelect(this._selectCallback);
		}
	}

	/**
	 * Deaktiviert einen State im Lazy-Modus: Der State hängt sich von der Auswahl ab und verwirft seine Daten und die
	 * Daten seiner Kinder.
	 *
	 * @throws DeveloperNotificationException   wenn der State nicht im Lazy-Modus betrieben wird
	 */
	public deactivate(): void {
		if (!this._lazy) {
			throw new DeveloperNotificationException("Der State wird nicht im Lazy-Modus betrieben und kann daher nicht deaktiviert werden.");
		}
		this._removeSelectCallback?.();
		this._removeSelectCallback = null;
		this.reset();
	}

	/**
	 * Meldet einen State mit Daten an, die zu den Daten gehören (z.B. Telefoneinträge). Dieser wird zusammen mit den
	 * Daten geladen und übernommen und zusammen mit diesen zurückgesetzt.
	 *
	 * @param kind   der State mit den Daten
	 */
	protected registerChild(kind: GenericDetailState): void {
		this._kinder.push(kind);
	}

	/**
	 * Lädt die Daten und die Daten der Kinder zu der Auswahl, ohne sie zu übernehmen. Dies erlaubt es, alles
	 * parallel zu laden und erst zu übernehmen, wenn alles erfolgreich geladen wurde.
	 *
	 * @param idAuswahl   die ID des gewählten Eintrags oder null, wenn die Auswahl entfernt wurde
	 *
	 * @returns eine Promise mit der Funktion, welche die geladenen Daten übernimmt
	 */
	public async loadForSelection(idAuswahl: number | null): Promise<() => void> {
		if ((idAuswahl === null) || !this.hasPermission()) {
			// Ohne Auswahl oder Berechtigung werden keine Daten geladen (der Server würde das Laden ablehnen)
			return () => {
				this.setDaten(null);
				this._kinder.forEach(kind => kind.reset());
			};
		}
		const [daten, applyKinder] = await Promise.all([
			this.ladeDaten(idAuswahl),
			Promise.all(this._kinder.map(kind => kind.loadForSelection(idAuswahl))),
		]);
		return () => {
			this.setDaten(daten);
			applyKinder.forEach(apply => apply());
		};
	}


	/**
	 * Diese Methode kann überschrieben werden und gibt zurück, ob der angemeldete Benutzer die Berechtigung zum Laden der
	 * Daten hat. Ohne Berechtigung werden weder Daten geladen noch wird der State aktiviert, da der Server das Laden
	 * ablehnen würde.
	 *
	 * @returns true, wenn die Daten geladen werden dürfen (Standard)
	 */
	protected hasPermission(): boolean {
		return true;
	}

	/**
	 * Diese Methode muss überschrieben werden und lädt die Daten zu dem Eintrag der Auswahlliste.
	 *
	 * @param id   die ID des Eintrags der Auswahlliste
	 *
	 * @returns die geladenen Daten
	 */
	public abstract ladeDaten(id: number): Promise<TDaten>;

	/**
	 * Diese Methode muss überschrieben werden und führt den eigentlichen Patch über die API aus.
	 *
	 * @param data   die Daten für den Patch
	 * @param id     die ID der zu patchenden Daten
	 */
	protected abstract doPatch(data: Partial<TDaten>, id: number): Promise<boolean>;

	/**
	 * Diese Methode kann überschrieben werden und übernimmt Änderungen der Daten in den Eintrag der Auswahlliste.
	 *
	 * @param eintrag   der Eintrag in der Auswahlliste
	 * @param daten     die neuen Daten zu dem Eintrag
	 */
	protected uebernehmeInEintrag(eintrag: TEintrag, daten: TDaten): void {
		// nichts zu tun
	}

	/**
	 * Gibt den Eintrag der Auswahlliste zu der ID zurück.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @returns der Eintrag
	 *
	 * @throws DeveloperNotificationException   wenn der Eintrag nicht in der Auswahlliste enthalten ist
	 */
	protected getEintragOrException(id: number): TEintrag {
		const eintrag = this._auswahl.getEntry(id);
		if (eintrag === null) {
			throw new DeveloperNotificationException("Der Eintrag mit der ID " + id.toString() + " ist nicht in der Auswahlliste enthalten.");
		}
		return eintrag;
	}


	/**
	 * Gibt die ID zu den übergebenen Daten zurück.
	 *
	 * @param daten   die Daten
	 *
	 * @returns die ID
	 */
	protected getIdDaten(daten: TDaten): number {
		return this._getIdDaten(daten);
	}


	public get daten(): TDaten | null {
		return this._state.value.daten;
	}

	public get vorherige(): TDaten | null {
		return this._state.value.vorherige;
	}

	public get hasDaten(): boolean {
		return this._state.value.daten !== null;
	}

	public datenOrException(): TDaten {
		const daten = this._state.value.daten;
		if (daten === null) {
			throw new DeveloperNotificationException("Es existiert derzeit keine Auswahl und damit auch keine Daten");
		}
		return daten;
	}

	public get id(): number | null {
		const daten = this._state.value.daten;
		return (daten === null) ? null : this._getIdDaten(daten);
	}

	public setDaten(daten: TDaten | null): void {
		const vorherige = this._state.value.daten;
		if (daten === null) {
			this.setPatchedState({ daten: null, vorherige });
			return;
		}
		this._auswahl.updateEntry(this._getIdDaten(daten), eintrag => this.uebernehmeInEintrag(eintrag, daten));
		this.setPatchedState({ daten, vorherige });
	}

	public clear(): void {
		this.setDaten(null);
	}

	public async laden(id: number): Promise<TDaten> {
		const daten = await this.ladeDaten(id);
		this.setDaten(daten);
		return daten;
	}

	public async patch(data: Partial<TDaten>): Promise<boolean> {
		const daten = this._state.value.daten;
		if (daten === null) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		await this.doPatch(data, this._getIdDaten(daten));
		Object.assign(daten, data);
		this.setDaten(daten);
		return true;
	}

	public reset(): void {
		this.setPatchedDefaultState({});
		this._kinder.forEach(kind => kind.reset());
	}

}
