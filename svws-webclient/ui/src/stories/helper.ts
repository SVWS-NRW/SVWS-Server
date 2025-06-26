/**
 * Hilfsfunktion, um Ereignisse zu loggen. Diese werden als console.log()
 * und über ein CustomEvent ausgegeben, das abgefangen und anderweitig verwendet werden kann.
 *
 * @param params beliebige Anzahl von Parametern
 */
export function logEvent(...params: any[]) {
	console.log(...params);
	document.dispatchEvent(new CustomEvent("log", { detail: params.join() }));
}
