/**
 * Die Schnittstelle für einen State mit Detail-Daten zu einem Eintrag einer Auswahlliste (z.B. Telefoneinträge eines
 * Schülers). Die Daten werden geladen, sobald sich die Auswahl ändert.
 */
export interface GenericDetailState {

	/**
	 * Lädt die Detail-Daten zu der Auswahl, ohne sie zu übernehmen. Dies erlaubt es, die Daten parallel zu den
	 * Stammdaten zu laden und erst zu übernehmen, wenn alles erfolgreich geladen wurde.
	 *
	 * @param idAuswahl   die ID des ausgewählten Eintrags der Auswahlliste
	 *
	 * @returns eine Funktion, welche die geladenen Daten übernimmt
	 */
	loadForSelection(idAuswahl: number): Promise<() => void>;

	/**
	 * Verwirft die Detail-Daten.
	 */
	reset(): void;

}
