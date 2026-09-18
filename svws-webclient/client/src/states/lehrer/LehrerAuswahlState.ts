import type { InjectionKey } from "vue";

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
import type { LehrerUnterrichtsfach } from "@core/core/data/lehrer/LehrerUnterrichtsfach";
import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";
import { AppContext } from "@ui/AppContext";
import type { LehrerListeManager } from "@ui/ui/manager/lehrer/LehrerListeManager";
import type { PendingStateManager } from "@ui/ui/wrapper/PendingStateManager";

import type { GenericAuswahlState } from "../GenericAuswahlState";

/**
 * Das Interface für den State für die Auswahlliste der Lehrer
 */
export interface LehrerAuswahlState extends GenericAuswahlState<LehrerListeManager> {

	/**
	 * Initialisiert den State für den ausgewählten Schuljahresabschnitt. Es werden die Daten für diesen Abschnitt geladen.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param force                    gibt an, ob das Laden der Daten auch erzwungen werden soll, wenn die ID des
	 *                                 Schuljahresabschnittes bereits gesetzt ist (z.B. beim Betreten einer Route)
	 *
	 * @returns ein Promise mit der ID der Auswahl oder null
	 */
	init(idSchuljahresabschnitt: number, force: boolean): Promise<number | null>;

	get filterNurSichtbar(): boolean;

	setFilterNurSichtbar(value: boolean): Promise<void>;

	get filterNurStatistikrelevant(): boolean;

	setFilterNurStatistikrelevant(value: boolean): Promise<void>;

	/**
	 * Fügt einen neuen Lehrer hinzu
	 *
	 * @param data   die Daten zum Anlegen des neuen Lehrers
	 *
	 * @returns eine leere Promise
	 */
	add(data: Partial<LehrerStammdaten>): Promise<LehrerStammdaten>;

	/**
	 * Führt die Patches aus dem übergebenen Pending State aus
	 *
	 * @param pendingStateManager   der Manager mit dem Pending State
	 *
	 * @returns eine leere Promise
	 */
	patchMultiple(pendingStateManager: PendingStateManager<any>): Promise<void>;

	/**
	 * Prüft, ob die Berechtigung zum Löschen vorliegt oder nicht.
	 *
	 * @returns die Information, ob die Berechtigung vorliegt mit einem Log, falls dies nicht der Fall ist
	 */
	deleteCheck(): [boolean, List<string>];

	/**
	 * Aktualisiert die Map für die Stundenpläne über eine Abfrage bei der API
	 *
	 * @returns eine leere Promise
	 */
	updateMapStundenplaene(): Promise<void>;

	/**
	 * Gibt die Map der Stundenpläne für den aktuellen Schuljahresabschnitt zurück.
	 *
	 * @returns die Map mit den Stundenplänen.
	 */
	get mapStundenplaene(): Map<number, StundenplanListeEintrag>;

	get mapSchulen(): Map<string, SchulEintrag>;

	get mapFaecher(): Map<number, FachDaten>;

	loadPersonaldaten(): Promise<void>;

	unloadPersonaldaten(): Promise<void>;

	patchPersonaldaten(data: Partial<LehrerPersonaldaten>): Promise<boolean>;

	patchPersonalAbschnittsdaten(data: Partial<LehrerPersonalabschnittsdaten>, id: number): Promise<void>;


	get leitungsfunktionen(): List<Schulleitung>;

	addLeitungsfunktion(data: Partial<Schulleitung>, idLehrer: number): Promise<void>;

	patchLeitungsfunktion(data: Partial<Schulleitung>, idEintrag: number): Promise<void>;

	deleteLeitungsfunktionen(idsEintraege: List<number>): Promise<void>;


	addLehramt(eintrag: Partial<LehrerLehramtEintrag>): Promise<void>;

	removeLehraemter(eintraege: List<LehrerLehramtEintrag>): Promise<void>;

	patchLehramt(eintrag: LehrerLehramtEintrag, patch: Partial<LehrerLehramtEintrag>): Promise<boolean>;

	addLehrbefaehigung(eintrag: Partial<LehrerLehrbefaehigungEintrag>): Promise<void>;

	removeLehrbefaehigungen(eintraege: List<LehrerLehrbefaehigungEintrag>): Promise<void>;

	patchLehrbefaehigung(eintrag: LehrerLehrbefaehigungEintrag, patch: Partial<LehrerLehrbefaehigungEintrag>): Promise<boolean>;

	addFachrichtung(eintrag: Partial<LehrerFachrichtungEintrag>): Promise<void>;

	removeFachrichtungen(eintraege: List<LehrerFachrichtungEintrag>): Promise<void>;

	patchFachrichtung(eintrag: LehrerFachrichtungEintrag, patch: Partial<LehrerFachrichtungEintrag>): Promise<boolean>;


	addMehrleistung(data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>): Promise<void>;

	patchMehrleistung(data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number): Promise<void>;

	removeMehrleistung(data: LehrerPersonalabschnittsdatenAnrechnungsstunden): Promise<void>;

	addMinderleistung(data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>): Promise<void>;

	patchMinderleistung(data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, id: number): Promise<void>;

	removeMinderleistung(data: LehrerPersonalabschnittsdatenAnrechnungsstunden): Promise<void>;

	addAnrechnung(data: Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>): Promise<void>;

	patchAnrechnungen(data: List<Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>>): Promise<void>;

	removeAnrechnung(data: LehrerPersonalabschnittsdatenAnrechnungsstunden): Promise<void>;


	get lehrerUnterrichtsfaecher(): List<LehrerUnterrichtsfach>;

	addLehrerUnterrichtsfach(eintrag: Partial<LehrerUnterrichtsfach>): Promise<void>;

	patchLehrerUnterrichtsfach(eintrag: LehrerUnterrichtsfach, patch: Partial<LehrerUnterrichtsfach>): Promise<void>;

	removeLehrerUnterrichtsfach(eintrag: LehrerUnterrichtsfach): Promise<void>;

}

export const LehrerAuswahlStateKey: InjectionKey<LehrerAuswahlState> = Symbol('LehrerAuswahlState');

export function useLehrerAuswahlState(): LehrerAuswahlState {
	const state = AppContext.instance.inject(LehrerAuswahlStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des LehrerAuswahlState über provide in der main.ts eingebunden");
	}
	return state;
}
