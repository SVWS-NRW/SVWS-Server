import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { useAbschnittState } from "@ui/states/AbschnittState";

import type { GenericAuswahlHost } from "../GenericAuswahlState";
import { GenericDatenStateImpl } from "../GenericDatenStateImpl";
import { api } from "~/router/Api";


/**
 * Der State für die Stammdaten der Klassen
 */
export class KlassenStammdatenState extends GenericDatenStateImpl<KlassenListeEintrag, KlassenDaten> {

	/**
	 * Erzeugt einen neuen State für die Stammdaten der Klassen.
	 *
	 * @param auswahl   der Auswahl-State, an den sich der State anhängt
	 */
	public constructor(auswahl: GenericAuswahlHost<KlassenListeEintrag>) {
		super(daten => daten.id, auswahl);
	}

	/**
	 * Gibt den Schuljahresabschnitt der Klasse der Einzelauswahl zurück. Dieser kann vom Schuljahresabschnitt der Auswahlliste abweichen.
	 *
	 * @returns der Schuljahresabschnitt
	 *
	 * @throws DeveloperNotificationException   wenn keine Daten der Einzelauswahl vorliegen oder der Schuljahresabschnitt fehlt
	 */
	public get schuljahresabschnitt(): Schuljahresabschnitt {
		return useAbschnittState().get(this.datenOrException().idSchuljahresabschnitt);
	}

	public async ladeDaten(id: number): Promise<KlassenDaten> {
		return await api.server.getKlasse(api.schema, id);
	}

	protected async doPatch(data: Partial<KlassenDaten>, id: number): Promise<boolean> {
		await api.server.patchKlasse(data, api.schema, id);
		return true;
	}

	protected override uebernehmeInEintrag(eintrag: KlassenListeEintrag, daten: KlassenDaten): void {
		if (eintrag.kuerzel !== daten.kuerzel) {
			eintrag.kuerzel = daten.kuerzel;
		}
	}


	/**
	 * Fügt der ausgewählten Klasse eine Klassenleitung hinzu.
	 *
	 * @param idLehrer   die ID des Lehrers, welcher die Klassenleitung übernimmt
	 * @param idKlasse   die ID der Klasse
	 */
	public async addKlassenleitung(idLehrer: number, idKlasse: number): Promise<void> {
		const daten = this.datenOrException();
		// Prüfe zunächst, ob die Lehrer-ID bereits in der Liste der Klassenleitungen vorkommt
		if (daten.klassenLeitungen.contains(idLehrer)) {
			throw new DeveloperNotificationException("Die Klassenleitung mit der Lehrer-ID " + idLehrer + " kommt bereits in der Klasse mit der ID " + idKlasse + "vor.");
		}

		// Erstelle die neue Klassenliste durch anhängen der neuen Lehrer-ID
		const listKlassenleitungenNeu = new ArrayList<number>(daten.klassenLeitungen);
		listKlassenleitungenNeu.add(idLehrer);

		// Führe den API-Aufruf durch
		const requestBody: Partial<KlassenDaten> = { klassenLeitungen: listKlassenleitungenNeu };
		await api.server.patchKlasse(requestBody, api.schema, idKlasse);

		// Aktualisiere die Liste der Klassenleitungen im Erfolgsfall
		daten.klassenLeitungen.add(idLehrer);
		this.commit();
	}

	/**
	 * Entfernt eine Klassenleitung bei der ausgewählten Klasse.
	 *
	 * @param idLehrer   die ID des Lehrers, welcher aus der Klassenleitung herausgenommen wird
	 */
	public async removeKlassenleitung(idLehrer: number): Promise<void> {
		const daten = this.datenOrException();

		// Bestimme die Position der Klassenleitung in der zugehörigen Liste
		const listKlassenleitungenNeu = new ArrayList<number>(daten.klassenLeitungen);
		const lehrerIndex: number = listKlassenleitungenNeu.indexOf(idLehrer);
		listKlassenleitungenNeu.removeElementAt(lehrerIndex);

		// Führe den API-Aufruf durch
		const requestBody: Partial<KlassenDaten> = { klassenLeitungen: listKlassenleitungenNeu };
		await api.server.patchKlasse(requestBody, api.schema, daten.id);

		// Aktualisiere die Liste der Klassenleitungen im Erfolgsfall
		daten.klassenLeitungen = listKlassenleitungenNeu;
		this.commit();
	}

	/**
	 * Erhöht, bzw. senkt die Position der Klassenleitung mit der angegebenen Lehrer-ID auf der lokalen Klassenleitungs-Liste.
	 * Dabei wird der Reihenfolgen-Wert zwischen dem nächstgrößeren (bzw. nächskleineren) Eintrag
	 * und dem angegebenen Eintrag getauscht.
	 *
	 * @param klassenleitungen   die Liste der Klassenleitungen
	 * @param lehrerId           Lehrer-ID der zu höher- bzw. tieferstellenden Klassenleitung
	 * @param erhoehe            true, falls die Klassenleitung eine höhere Position auf der Klassenleitungs-Liste haben soll,
	 *                           false, wenn sie eine tiefere Position auf der Klassenleitungs-Liste haben soll.
	 *
	 * @return true, falls Änderungen durchgeführt wurden und ansonsten false
	 *
	 * @throws DeveloperNotificationException wenn die Klassen-Daten oder die übergebene Lehrer-ID ungültig sind
	 */
	public static updateReihenfolge(klassenleitungen: List<number>, lehrerId: number, erhoehe: boolean): boolean {
		if (klassenleitungen.size() === 1) {
			return false;
		}
		const posLehrer: number = klassenleitungen.indexOf(lehrerId);
		if (posLehrer < 0) {
			throw new DeveloperNotificationException("Es wurde keine Klassenleitung mit der angegebenen Klassen- und Lehrer-ID gefunden.");
		}
		if (erhoehe) {
			if (posLehrer === 0) {
				return false;
			}
			const lehrerIdVorgaenger: number = klassenleitungen.get(posLehrer - 1).valueOf();
			klassenleitungen.set(posLehrer, lehrerIdVorgaenger);
			klassenleitungen.set(posLehrer - 1, lehrerId);
			return true;
		}
		if ((posLehrer + 1) >= klassenleitungen.size()) {
			return false;
		}
		const lehrerIdNachfolger: number = klassenleitungen.get(posLehrer + 1).valueOf();
		klassenleitungen.set(posLehrer, lehrerIdNachfolger);
		klassenleitungen.set(posLehrer + 1, lehrerId);
		return true;
	}

	/**
	 * Verschiebt eine Klassenleitung bei der ausgewählten Klasse um eine Position nach oben oder unten.
	 *
	 * @param idLehrer   die ID des Lehrers, dessen Position angepasst werden soll
	 * @param erhoehe    true, wenn die Position erhöht (nach oben) werden soll, sonst false
	 */
	public async updateReihenfolgeKlassenleitung(idLehrer: number, erhoehe: boolean): Promise<void> {
		const daten = this.datenOrException();

		// Erstelle eine Kopie der Liste der Klassenleitungen und führe an dieser die Änderungen durch
		const listKlassenleitungenNeu = new ArrayList<number>(daten.klassenLeitungen);
		if (!KlassenStammdatenState.updateReihenfolge(listKlassenleitungenNeu, idLehrer, erhoehe)) {
			return;
		}

		// Führe den API-Aufruf durch
		const requestBody: Partial<KlassenDaten> = { klassenLeitungen: listKlassenleitungenNeu };
		await api.server.patchKlasse(requestBody, api.schema, daten.id);

		// Aktualisiere die Liste der Klassenleitungen im Erfolgsfall
		daten.klassenLeitungen = listKlassenleitungenNeu;
		this.commit();
	}
}
