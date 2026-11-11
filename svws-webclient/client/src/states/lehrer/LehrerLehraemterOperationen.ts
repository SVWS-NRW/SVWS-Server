import type { LehrerFachrichtungEintrag } from "@core/asd/data/lehrer/LehrerFachrichtungEintrag";
import type { LehrerLehramtEintrag } from "@core/asd/data/lehrer/LehrerLehramtEintrag";
import type { LehrerLehrbefaehigungEintrag } from "@core/asd/data/lehrer/LehrerLehrbefaehigungEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";

import { api } from "~/router/Api";

import type { LehrerPersonaldatenState } from "./LehrerPersonaldatenState";

/**
 * Die Operationen für die Lehrämter in den Personaldaten eines Lehrers
 */
export class LehrerLehraemterOperationen {

	private readonly _personaldaten: LehrerPersonaldatenState;

	/**
	 * Erzeugt die Operationen für die Lehrämter.
	 *
	 * @param personaldaten   der State mit den Personaldaten
	 */
	public constructor(personaldaten: LehrerPersonaldatenState) {
		this._personaldaten = personaldaten;
	}

	/**
	 * Fügt ein Lehramt hinzu.
	 *
	 * @param eintrag   die Daten des Lehramts
	 */
	public async add(eintrag: Partial<LehrerLehramtEintrag>): Promise<void> {
		if (!this._personaldaten.hasDaten) {
			throw new DeveloperNotificationException("Lehrämter können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		}
		const result = await api.server.addLehrerLehramt(eintrag, api.schema);
		this._personaldaten.datenOrException().lehraemter.add(result);
		this._personaldaten.commit();
	}

	/**
	 * Entfernt Lehrämter.
	 *
	 * @param eintraege   die zu entfernenden Lehrämter
	 */
	public async remove(eintraege: List<LehrerLehramtEintrag>): Promise<void> {
		if (!this._personaldaten.hasDaten) {
			throw new DeveloperNotificationException("Lehrämter können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		}
		const ids: List<number> = new ArrayList();
		for (const eintrag of eintraege) {
			ids.add(eintrag.id);
		}
		await api.server.deleteLehrerLehraemter(ids, api.schema);
		this._personaldaten.datenOrException().lehraemter.removeAll(eintraege);
		this._personaldaten.commit();
	}

	/**
	 * Passt ein Lehramt an.
	 *
	 * @param eintrag   das Lehramt
	 * @param patch     die Änderungen
	 *
	 * @returns true
	 */
	public async patch(eintrag: LehrerLehramtEintrag, patch: Partial<LehrerLehramtEintrag>): Promise<boolean> {
		if (!this._personaldaten.hasDaten) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		await api.server.patchLehrerLehramt(patch, api.schema, eintrag.id);
		Object.assign(eintrag, patch);
		this._personaldaten.commit();
		return true;
	}

}

/**
 * Die Operationen für die Lehrbefähigungen der Lehrämter in den Personaldaten eines Lehrers
 */
export class LehrerLehrbefaehigungenOperationen {

	private readonly _personaldaten: LehrerPersonaldatenState;

	/**
	 * Erzeugt die Operationen für die Lehrbefähigungen.
	 *
	 * @param personaldaten   der State mit den Personaldaten
	 */
	public constructor(personaldaten: LehrerPersonaldatenState) {
		this._personaldaten = personaldaten;
	}

	/**
	 * Fügt einem Lehramt eine Lehrbefähigung hinzu.
	 *
	 * @param eintrag   die Daten der Lehrbefähigung inklusive der ID des Lehramts
	 */
	public async add(eintrag: Partial<LehrerLehrbefaehigungEintrag>): Promise<void> {
		if (!this._personaldaten.hasDaten) {
			throw new DeveloperNotificationException("Lehrbefähigungen können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		}
		if (eintrag.idLehramt === undefined) {
			throw new DeveloperNotificationException("Lehrbefähigungen können nur mit einer Lehramts-ID hinzugefügt werden.");
		}
		const result = await api.server.addLehrerLehrbefaehigung(eintrag, api.schema);
		for (const lehramt of this._personaldaten.datenOrException().lehraemter) {
			if (lehramt.id === eintrag.idLehramt) {
				lehramt.lehrbefaehigungen.add(result);
			}
		}
		this._personaldaten.commit();
	}

	/**
	 * Entfernt Lehrbefähigungen.
	 *
	 * @param eintraege   die zu entfernenden Lehrbefähigungen
	 */
	public async remove(eintraege: List<LehrerLehrbefaehigungEintrag>): Promise<void> {
		if (!this._personaldaten.hasDaten) {
			throw new DeveloperNotificationException("Lehrbefähigungen können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		}
		const ids: List<number> = new ArrayList();
		for (const eintrag of eintraege) {
			ids.add(eintrag.id);
		}
		await api.server.deleteLehrerLehrbefaehigungen(ids, api.schema);
		for (const lehramt of this._personaldaten.datenOrException().lehraemter) {
			lehramt.lehrbefaehigungen.removeAll(eintraege);
		}
		this._personaldaten.commit();
	}

	/**
	 * Passt eine Lehrbefähigung an.
	 *
	 * @param eintrag   die Lehrbefähigung
	 * @param patch     die Änderungen
	 *
	 * @returns true
	 */
	public async patch(eintrag: LehrerLehrbefaehigungEintrag, patch: Partial<LehrerLehrbefaehigungEintrag>): Promise<boolean> {
		if (!this._personaldaten.hasDaten) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		await api.server.patchLehrerLehrbefaehigung(patch, api.schema, eintrag.id);
		Object.assign(eintrag, patch);
		this._personaldaten.commit();
		return true;
	}

}

/**
 * Die Operationen für die Fachrichtungen der Lehrämter in den Personaldaten eines Lehrers
 */
export class LehrerFachrichtungenOperationen {

	private readonly _personaldaten: LehrerPersonaldatenState;

	/**
	 * Erzeugt die Operationen für die Fachrichtungen.
	 *
	 * @param personaldaten   der State mit den Personaldaten
	 */
	public constructor(personaldaten: LehrerPersonaldatenState) {
		this._personaldaten = personaldaten;
	}

	/**
	 * Fügt einem Lehramt eine Fachrichtung hinzu.
	 *
	 * @param eintrag   die Daten der Fachrichtung inklusive der ID des Lehramts
	 */
	public async add(eintrag: Partial<LehrerFachrichtungEintrag>): Promise<void> {
		if (!this._personaldaten.hasDaten) {
			throw new DeveloperNotificationException("Fachrichtungen können nur hinzugefügt werden, wenn gültige Personal-Daten geladen sind.");
		}
		if (eintrag.idLehramt === undefined) {
			throw new DeveloperNotificationException("Fachrichtungen können nur mit einer Lehramts-ID hinzugefügt werden.");
		}
		const result = await api.server.addLehrerFachrichtung(eintrag, api.schema);
		for (const lehramt of this._personaldaten.datenOrException().lehraemter) {
			if (lehramt.id === eintrag.idLehramt) {
				lehramt.fachrichtungen.add(result);
			}
		}
		this._personaldaten.commit();
	}

	/**
	 * Entfernt Fachrichtungen.
	 *
	 * @param eintraege   die zu entfernenden Fachrichtungen
	 */
	public async remove(eintraege: List<LehrerFachrichtungEintrag>): Promise<void> {
		if (!this._personaldaten.hasDaten) {
			throw new DeveloperNotificationException("Fachrichtungen können nur entfernt werden, wenn gültige Personal-Daten geladen sind.");
		}
		const ids: List<number> = new ArrayList();
		for (const eintrag of eintraege) {
			ids.add(eintrag.id);
		}
		await api.server.deleteLehrerFachrichtungen(ids, api.schema);
		for (const lehramt of this._personaldaten.datenOrException().lehraemter) {
			lehramt.fachrichtungen.removeAll(eintraege);
		}
		this._personaldaten.commit();
	}

	/**
	 * Passt eine Fachrichtung an.
	 *
	 * @param eintrag   die Fachrichtung
	 * @param patch     die Änderungen
	 *
	 * @returns true
	 */
	public async patch(eintrag: LehrerFachrichtungEintrag, patch: Partial<LehrerFachrichtungEintrag>): Promise<boolean> {
		if (!this._personaldaten.hasDaten) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		await api.server.patchLehrerFachrichtung(patch, api.schema, eintrag.id);
		Object.assign(eintrag, patch);
		this._personaldaten.commit();
		return true;
	}

}
