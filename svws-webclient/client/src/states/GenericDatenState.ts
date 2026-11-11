/**
 * Die Schnittstelle von generischen States für Daten zu der Einzelauswahl einer Auswahlliste (z.B. die Stammdaten).
 * Der State hält die Daten der Einzelauswahl, lädt diese nach und führt Patches aus. Die Daten der Mehrfachauswahl
 * (Gruppenprozesse) liegen im {@link GenericGruppenprozessState}.
 *
 * @typeParam TDaten   der Typ der Daten
 */
export interface GenericDatenState<TDaten> {

	/**
	 * Gibt die Daten der Einzelauswahl zurück oder null, falls keine vorliegen.
	 */
	get daten(): TDaten | null;

	/**
	 * Gibt die Stammdaten zurück, die vor den aktuellen Stammdaten der Einzelauswahl gesetzt waren, oder null.
	 */
	get vorherige(): TDaten | null;

	/**
	 * Setzt die Stammdaten der Einzelauswahl und übernimmt Änderungen in den Eintrag der Auswahlliste.
	 *
	 * @param daten   die Stammdaten oder null
	 */
	setDaten(daten: TDaten | null): void;

	/**
	 * Gibt zurück, ob Stammdaten der Einzelauswahl vorliegen.
	 */
	get hasDaten(): boolean;

	/**
	 * Gibt die Stammdaten der Einzelauswahl zurück.
	 *
	 * @returns die Stammdaten
	 *
	 * @throws DeveloperNotificationException   wenn keine Stammdaten der Einzelauswahl vorliegen
	 */
	datenOrException(): TDaten;

	/**
	 * Gibt die ID der Stammdaten der Einzelauswahl zurück oder null, falls keine vorliegen.
	 */
	get id(): number | null;

	/**
	 * Lädt die Stammdaten zu der ID des Eintrags der Auswahlliste, ohne sie zu setzen.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @returns die geladenen Stammdaten
	 */
	ladeDaten(id: number): Promise<TDaten>;

	/**
	 * Lädt die Stammdaten zu der ID des Eintrags der Auswahlliste und setzt diese als Stammdaten der Einzelauswahl.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @returns die geladenen Stammdaten
	 */
	laden(id: number): Promise<TDaten>;

	/**
	 * Entfernt die Stammdaten der Einzelauswahl.
	 */
	clear(): void;

	/**
	 * Führt einen Patch auf den Stammdaten der Einzelauswahl mit den übergebenen Daten aus.
	 *
	 * @param data   die Daten für den Patch
	 *
	 * @returns eine Promise, die bei Erfolg true zurückgibt
	 */
	patch(data: Partial<TDaten>): Promise<boolean>;

	/**
	 * Setzt den State in den Default-State zurück.
	 */
	reset(): void;

}
