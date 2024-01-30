/** Die einzelne Nachrichten-Typen, die zwischen Worker und Worker-Manager ausgetausch werden. */
export type WorkerKursblockungMessageType = 'init' | 'next' | 'getErgebnisse';

/**
 * Das allgemeine Nachrichtenformat für den Austausch zwischen Worker und Worker-Manager
 */
export interface WorkerKursblockungMessage {
	cmd: WorkerKursblockungMessageType;
}

/**
 * Das Nachrichtenformat für die Initialisierung-Anfrage des Managers an
 * seinen Worker.
 */
export interface WorkerKursblockungRequestInit extends WorkerKursblockungMessage {
	cmd: 'init';
	faecher: string[];
	blockungsdaten: string;
}

/**
 * Das Nachrichtenformat für die Antwort des Workers auf due Initialisierung-Anfrage
 * des Managers.
 */
export interface WorkerKursblockungReplyInit extends WorkerKursblockungMessage {
	cmd: 'init';
	initialized: boolean;
}

/**
 * Das Nachrichtenformat für den Auftrag des Managers an den Worker zu Berechnung
 * von neuen Blockungsergebnissen und einer Rückmeldung nach der angegeben Zeit.
 */
export interface WorkerKursblockungRequestNext extends WorkerKursblockungMessage {
	cmd: 'next';
	interval: number;
}

/**
 * Das Nachrichtenformat für die Benachrichtung des Workers an den Mananger, das
 * die zuvor Zeit für die beauftragte Berechnung beendet ist mit der
 * Information, ob neue gute Blockungsergebnisse beim Worker vorliegen.
 */
export interface WorkerKursblockungReplyNext extends WorkerKursblockungMessage {
	cmd: 'next';
	hasUpdate: boolean;
}

/**
 * Das Nachrichtenformat für eine Anfrage des Managers an den Worker nach den aktuell
 * besten Blockungsergebnissen, die der Worker berechnet hat.
 */
export interface WorkerKursblockungRequestErgebnisse extends WorkerKursblockungMessage {
	cmd: 'getErgebnisse';
}

/**
 * Die Antwort des Workers an den Manager mit den aktuell besten Blockungsergebnissen
 * die der Worker berechnet hat.
 */
export interface WorkerKursblockungReplyErgebnisse extends WorkerKursblockungMessage {
	cmd: 'getErgebnisse';
	ergebnisse: string[];
}