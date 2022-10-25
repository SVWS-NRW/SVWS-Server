/**
 * Sammlung von FeedbackValues unter IDs
 */
 export type ApiStatus = {
	[key: number]: FeedbackValues
}

/**
 * Set von Werten, die den zu kommunizierenden Status von Api-Calls enthalten.
 */
export interface FeedbackValues {
	/** Zeigt an, ob der Api-Call gegenwärtig läuft. */
	idle: boolean,
	/** Zeigt an, ob der Api-Call fehlgeschlagen ist. */
	error: boolean,
}