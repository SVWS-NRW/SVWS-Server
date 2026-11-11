import type { LehrerUnterrichtsfach } from "@core/core/data/lehrer/LehrerUnterrichtsfach";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";

import { GenericDetailListStateImpl } from "../GenericDetailListStateImpl";
import { api } from "~/router/Api";

/**
 * Der State für die Unterrichtsfächer eines Lehrers. Die Unterrichtsfächer gehören zu den Personaldaten (siehe
 * {@link LehrerPersonaldatenState}), werden von diesem geladen und können nur bearbeitet werden, wenn diese geladen sind.
 */
export class LehrerUnterrichtsfaecherState extends GenericDetailListStateImpl<LehrerUnterrichtsfach> {

	/** Die Funktion, welche angibt, ob Personaldaten geladen sind */
	private readonly _hatPersonaldaten: () => boolean;

	/** Die Funktion, welche die ID des ausgewählten Lehrers liefert */
	private readonly _idLehrer: () => number;

	/**
	 * Erzeugt einen neuen State für die Unterrichtsfächer.
	 *
	 * @param hatPersonaldaten   die Funktion, welche angibt, ob Personaldaten geladen sind
	 * @param idLehrer           die Funktion, welche die ID des ausgewählten Lehrers liefert
	 */
	public constructor(hatPersonaldaten: () => boolean, idLehrer: () => number) {
		super(unterrichtsfach => unterrichtsfach.id);
		this._hatPersonaldaten = hatPersonaldaten;
		this._idLehrer = idLehrer;
	}

	protected async loadEntries(idAuswahl: number): Promise<List<LehrerUnterrichtsfach>> {
		return await api.server.getLehrerUnterrichtsfaecher(api.schema, idAuswahl);
	}

	/**
	 * Fügt dem ausgewählten Lehrer ein Unterrichtsfach hinzu.
	 *
	 * @param eintrag   die Daten des Unterrichtsfachs
	 *
	 * @returns true
	 */
	public async add(eintrag: Partial<LehrerUnterrichtsfach>): Promise<boolean> {
		if (!this._hatPersonaldaten()) {
			throw new DeveloperNotificationException("Unterrichtsfächer können nur hinzugefügt werden, wenn gültige Personaldaten geladen sind.");
		}
		this.applyAdd(await api.server.addLehrerUnterrichtsfach({ ...eintrag, idLehrer: this._idLehrer() }, api.schema));
		return true;
	}

	/**
	 * Passt ein Unterrichtsfach an.
	 *
	 * @param eintrag   das Unterrichtsfach
	 * @param patch     die Änderungen
	 *
	 * @returns true
	 */
	public async patch(eintrag: LehrerUnterrichtsfach, patch: Partial<LehrerUnterrichtsfach>): Promise<boolean> {
		if (!this._hatPersonaldaten()) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		await api.server.patchLehrerUnterrichtsfach(patch, api.schema, eintrag.id);
		this.applyPatch(eintrag.id, patch);
		return true;
	}

	/**
	 * Entfernt ein Unterrichtsfach.
	 *
	 * @param eintrag   das Unterrichtsfach
	 */
	public async remove(eintrag: LehrerUnterrichtsfach): Promise<void> {
		if (!this._hatPersonaldaten()) {
			throw new DeveloperNotificationException("Unterrichtsfächer können nur entfernt werden, wenn gültige Personaldaten geladen sind.");
		}
		await api.server.deleteLehrerUnterrichtsfach(api.schema, eintrag.id);
		this.applyRemove([eintrag.id]);
	}

}
