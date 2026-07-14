
/** Definiert die Sortierrichtung für ein einzelnes Feld */
export interface AuswahlManagerSortierOrdnung {
	/** Der Name des zu sortierenden Feldes */
	field: string;
	/** true für aufsteigende, false für absteigende Sortierung */
	ascending: boolean;
}
