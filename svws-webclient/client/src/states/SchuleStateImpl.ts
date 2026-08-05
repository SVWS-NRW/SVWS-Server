import type { List, SchuleStammdaten, Schuljahresabschnitt } from "@core";
import { DeveloperNotificationException, Schulform, Schulgliederung, ValidatorKontext } from "@core";
import { api } from "~/router/Api";
import { abschnittStateImpl } from "./AbschnittStateImpl";
import type { SchuleState } from "@ui";
import { StateManager } from "@ui";

interface SchuleReactiveState {
	// Die Stammdaten der Schule, sofern ein Login stattgefunden hat
	stammdaten: SchuleStammdaten | null;
	kontext: ValidatorKontext | null;
	abschnitt: Schuljahresabschnitt | null;
}

/**
 * Die Schnittstelle für die Daten der Schule und des aktuellen Abschnitts der Schule
 */
export class SchuleStateImpl extends StateManager<SchuleReactiveState> implements SchuleState {

	public constructor() {
		super({
			stammdaten: null,
			kontext: null,
			abschnitt: null,
		});
	}

	/**
	 * Initialisiert die Daten, die beim Login geladen werden sollen
	 *
	 * @returns {Promise<boolean>} true beim erfolgreichen Laden der Daten und ansonsten false
	 */
	public async init(): Promise<void> {
		try {
			const stammdaten = await api.server.getSchuleStammdaten(api.schema);
			const schulform = Schulform.data().getWertByKuerzelOrException(stammdaten.schulform);
			const kontext = new ValidatorKontext(stammdaten.schulNr, schulform, stammdaten.abschnitte, stammdaten.idSchuljahresabschnitt, false);
			let abschnitt = null;
			for (const a of stammdaten.abschnitte) {
				if (stammdaten.idSchuljahresabschnitt === a.id) {
					abschnitt = a;
					break;
				}
			}
			this.setPatchedDefaultState({ stammdaten, kontext, abschnitt });
			abschnittStateImpl.init(stammdaten);
		} catch {
			this.reset();
			abschnittStateImpl.reset();
			throw new DeveloperNotificationException("Es konnte kein State für die Stammdaten der Schule erzeugt werden");
		}
	}

	public async patchStammdaten(patch: Partial<SchuleStammdaten>): Promise<void> {
		await api.server.patchSchuleStammdaten(patch, api.schema);
		Object.assign(this.stammdaten, patch);
		this.commit();
	}

	/**
	 * Gibt die Stammdaten der Schule zurück, sofern bereits ein Login stattgefunden hat.
	 *
	 * @returns die Stammdaten
	 */
	public get stammdaten(): SchuleStammdaten {
		if (this.state.stammdaten === null) {
			throw new DeveloperNotificationException("Der Benutzer muss angemeldet sein und die Stammdaten der Schule müssen erfolgreich geladen sein.");
		}
		return this.state.stammdaten;
	}

	/**
	 * Gibt den Validator-Kontext für die Validierung von Statistik-relevanten Daten zurück.
	 *
	 * @returns der Validator-Kontext
	 */
	public get validatorKontext(): ValidatorKontext {
		if (this.state.kontext === null) {
			throw new DeveloperNotificationException("Der Benutzer muss angemeldet sein und der Validator-Kontext muss erfolgreich erstellt sein.");
		}
		return this.state.kontext;
	}

	/**
	 * Gibt die Schulform der Schule zurück, wo der Benutzer angemeldet ist.
	 *
	 * @returns die Schulform
	 */
	public get schulform(): Schulform {
		const schulform = Schulform.data().getWertByKuerzel(this.stammdaten.schulform);
		if (schulform === null) {
			throw new DeveloperNotificationException("In den Schul-Stammdaten ist eine ungültige Schulform eingetragen.");
		}
		return schulform;
	}

	/**
	 * Gibt die zulässigen Schulgliederungen für die Schule zurück, wo der
	 * Benutzer angemeldet ist.
	 *
	 * @returns eine Liste mit den Schulgliederungen
	 */
	public get schulgliederungen(): List<Schulgliederung> {
		return Schulgliederung.getBySchuljahrAndSchulform(abschnittStateImpl.auswahl.schuljahr, this.schulform);
	}

	/**
	 * Gibt das Schuljahr des Abschnitts zurück
	 *
	 * @returns das Schuljahr
	 */
	public get schuljahr(): number {
		return this.abschnitt.schuljahr;

	}

	/**
	 * Gibt den aktuellen Schulabschnitt zurück, in dem sich die Schule befindet.
	 *
	 * @returns der aktuelle Schulabschnitt
	 */
	get abschnitt(): Schuljahresabschnitt {
		if (this.state.abschnitt === null) {
			throw new DeveloperNotificationException("In den Schul-Stammdaten ist kein gültiger Schulabschnitt eingetragen.");
		}
		return this.state.abschnitt;
	}

}

export const schuleStateImpl = new SchuleStateImpl();
