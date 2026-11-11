import { computed, type ComputedRef } from "vue";

import type { LehrerPersonalabschnittsdaten } from "@core/asd/data/lehrer/LehrerPersonalabschnittsdaten";
import type { LehrerPersonaldaten } from "@core/asd/data/lehrer/LehrerPersonaldaten";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { useBenutzerState } from "@ui/states/BenutzerState";
import { useFaecherState } from "@ui/states/kataloge/FaecherState";
import { useHerkunftschulenState } from "@ui/states/kataloge/HerkunftschulenState";

import type { GenericAuswahlHost } from "../GenericAuswahlState";
import { GenericDatenStateImpl } from "../GenericDatenStateImpl";
import { faecherStateImpl } from "../kataloge/FaecherStateImpl";
import { herkunftschulenStateImpl } from "../kataloge/HerkunftschulenStateImpl";
import { api } from "~/router/Api";

import { LehrerAnrechnungenOperationen, LehrerMehrleistungOperationen, LehrerMinderleistungOperationen } from "./LehrerAnrechnungenOperationen";
import { LehrerFunktionenOperationen } from "./LehrerFunktionenOperationen";
import { LehrerFachrichtungenOperationen, LehrerLehraemterOperationen, LehrerLehrbefaehigungenOperationen } from "./LehrerLehraemterOperationen";
import { LehrerUnterrichtsfaecherState } from "./LehrerUnterrichtsfaecherState";

/**
 * Der State für die Personaldaten des ausgewählten Lehrers. Die Personaldaten gehören parallel zu den Stammdaten zum
 * Lehrer, werden aber im Lazy-Modus betrieben: Sie werden nur geladen, solange die zugehörige Ansicht aktiv ist
 * (siehe {@link activate}). Dann werden sie beim Wechsel der Auswahl mit neu geladen.
 */
export class LehrerPersonaldatenState extends GenericDatenStateImpl<LehrerListeEintrag, LehrerPersonaldaten> {

	/** Die Funktion, welche die ID des aktuellen Schuljahresabschnittes liefert */
	private readonly _idSchuljahresabschnitt: () => number;

	/** Die Schulen mit Kürzel im Schulen-Katalog, zugeordnet zu deren Statistik-Schulnummer */
	private readonly _mapSchulen: ComputedRef<Map<string, SchulEintrag>>;

	/** Die Operationen für die Lehrämter */
	public readonly lehraemter = new LehrerLehraemterOperationen(this);

	/** Die Operationen für die Lehrbefähigungen */
	public readonly lehrbefaehigungen = new LehrerLehrbefaehigungenOperationen(this);

	/** Die Operationen für die Fachrichtungen */
	public readonly fachrichtungen = new LehrerFachrichtungenOperationen(this);

	/** Die Operationen für die Mehrleistung */
	public readonly mehrleistung = new LehrerMehrleistungOperationen(this);

	/** Die Operationen für die Minderleistung */
	public readonly minderleistung = new LehrerMinderleistungOperationen(this);

	/** Die Operationen für die allgemeinen Anrechnungen */
	public readonly anrechnungen = new LehrerAnrechnungenOperationen(this);

	/** Die Operationen für die schulinternen Funktionen */
	public readonly funktionen = new LehrerFunktionenOperationen(this);

	/** Der State für die Unterrichtsfächer des Lehrers, diese gehören zu den Personaldaten */
	public readonly unterrichtsfaecher = new LehrerUnterrichtsfaecherState(() => this.hasDaten, () => this.datenOrException().id);

	/**
	 * Erzeugt einen neuen State für die Personaldaten.
	 *
	 * @param auswahl                  der Auswahl-State, an den sich der State nach der Aktivierung anhängt
	 * @param idSchuljahresabschnitt   die Funktion, welche die ID des aktuellen Schuljahresabschnittes liefert
	 */
	public constructor(auswahl: GenericAuswahlHost<LehrerListeEintrag>, idSchuljahresabschnitt: () => number) {
		super(daten => daten.id, auswahl, true);
		this.registerChild(this.unterrichtsfaecher);
		this._idSchuljahresabschnitt = idSchuljahresabschnitt;
		this._mapSchulen = computed(() => {
			const map = new Map<string, SchulEintrag>();
			for (const schule of useHerkunftschulenState().herkunftschulen.list) {
				if ((schule.schulnummerStatistik !== null) && (schule.kuerzel !== null) && (schule.kuerzel.trim().length > 0)) {
					map.set(schule.schulnummerStatistik, schule);
				}
			}
			return map;
		});
	}

	/**
	 * Gibt die Schulen mit Kürzel aus dem Schulen-Katalog zurück, zugeordnet zu deren Statistik-Schulnummer.
	 *
	 * @returns die Map mit den Schulen
	 */
	public get mapSchulen(): Map<string, SchulEintrag> {
		return this._mapSchulen.value;
	}

	/**
	 * Gibt die Fächer aus dem Fächer-Katalog zurück, zugeordnet zu deren ID.
	 *
	 * @returns die Map mit den Fächern
	 */
	public get mapFaecher(): Map<number, FachDaten> {
		return useFaecherState().faecher.byId;
	}

	/**
	 * Gibt die Personalabschnittsdaten mit der ID zurück.
	 *
	 * @param id   die ID der Personalabschnittsdaten
	 *
	 * @returns die Personalabschnittsdaten oder null, falls keine mit der ID vorhanden sind
	 */
	public abschnittById(id: number): LehrerPersonalabschnittsdaten | null {
		for (const abschnitt of this._state.value.daten?.abschnittsdaten ?? []) {
			if (abschnitt.id === id) {
				return abschnitt;
			}
		}
		return null;
	}

	/**
	 * Gibt die Personalabschnittsdaten zu dem Schuljahresabschnitt zurück.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @returns die Personalabschnittsdaten oder null, falls keine zu dem Schuljahresabschnitt vorhanden sind
	 */
	public abschnittBySchuljahresabschnitt(idSchuljahresabschnitt: number): LehrerPersonalabschnittsdaten | null {
		for (const abschnitt of this._state.value.daten?.abschnittsdaten ?? []) {
			if (abschnitt.idSchuljahresabschnitt === idSchuljahresabschnitt) {
				return abschnitt;
			}
		}
		return null;
	}

	/**
	 * Gibt die Personalabschnittsdaten des aktuellen Schuljahresabschnittes zurück.
	 *
	 * @returns die Personalabschnittsdaten
	 *
	 * @throws DeveloperNotificationException   wenn keine Personalabschnittsdaten vorhanden sind
	 */
	public abschnittAktuellOrException(): LehrerPersonalabschnittsdaten {
		const idSchuljahresabschnitt = this._idSchuljahresabschnitt();
		const abschnitt = this.abschnittBySchuljahresabschnitt(idSchuljahresabschnitt);
		if (abschnitt === null) {
			throw new DeveloperNotificationException("Es konnten keine gültigen Lehrerabschnittsdaten für den Schuljahresabschnitt mit der ID " + idSchuljahresabschnitt.toString() + " geladen werden.");
		}
		return abschnitt;
	}

	/**
	 * Lädt die Personaldaten des Lehrers sowie die benötigten Kataloge. Ist die Liste der
	 * Abschnittsdaten leer, so werden Abschnittsdaten für den aktuellen Schuljahresabschnitt angelegt.
	 *
	 * @param id   die ID des Lehrers
	 *
	 * @returns die Personaldaten
	 */
	public async ladeDaten(id: number): Promise<LehrerPersonaldaten> {
		const [daten] = await Promise.all([
			api.server.getLehrerPersonaldaten(api.schema, id),
			herkunftschulenStateImpl.init(),
			faecherStateImpl.init(),
		]);
		if (daten.abschnittsdaten.isEmpty()) {
			const idSchuljahresabschnitt = this._idSchuljahresabschnitt();
			daten.abschnittsdaten.add(await api.server.createLehrerPersonalabschnittsdaten({ idLehrer: daten.id, idSchuljahresabschnitt }, api.schema));
		}
		return daten;
	}

	protected override hasPermission(): boolean {
		return useBenutzerState().benutzerHatKompetenz(BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
	}

	protected async doPatch(data: Partial<LehrerPersonaldaten>, id: number): Promise<boolean> {
		await api.server.patchLehrerPersonaldaten(data, api.schema, id);
		return true;
	}

	/**
	 * Löst die Reaktivität nach einer Änderung an den Personaldaten aus.
	 */
	public commit(): void {
		super.commit();
	}

	/**
	 * Passt die Personalabschnittsdaten mit der ID an.
	 *
	 * @param data   die Änderungen
	 * @param id     die ID der Personalabschnittsdaten
	 */
	public async patchAbschnitt(data: Partial<LehrerPersonalabschnittsdaten>, id: number): Promise<void> {
		this.datenOrException();
		const abschnitt = this.abschnittById(id);
		if (abschnitt === null) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten mit der ID " + id.toString() + " geladen.");
		}
		await api.server.patchLehrerPersonalabschnittsdaten(data, api.schema, abschnitt.id);
		Object.assign(abschnitt, data);
		this.commit();
	}

}
