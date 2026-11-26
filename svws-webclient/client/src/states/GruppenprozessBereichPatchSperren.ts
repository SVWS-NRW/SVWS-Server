import type { ShallowRef } from "vue";
import { shallowRef } from "vue";

/**
 * Die Sperren des Speicherns (Patchen) eines Bereiches der Gruppenprozesse, z.B. wegen ungültiger Eingaben. Jede Sperre
 * hat einen Schlüssel (z.B. den Namen des betroffenen Feldes) und einen Hinweis für den Benutzer. Solange mindestens
 * eine Sperre besteht, dürfen die ausstehenden Änderungen des Bereiches nicht gespeichert werden.
 */
export class GruppenprozessBereichPatchSperren {

	/** Die Sperren: der Schlüssel der Sperre mit dem Hinweis für den Benutzer, wird bei Änderungen ersetzt */
	private readonly _sperren: ShallowRef<Map<string, string>> = shallowRef(new Map<string, string>());


	/**
	 * Gibt zurück, ob das Speichern erlaubt ist, d.h. ob keine Sperre vorliegt.
	 *
	 * @returns true, wenn das Speichern möglich ist
	 */
	public get isPatchAllowed(): boolean {
		return this._sperren.value.size === 0;
	}

	/**
	 * Gibt die Hinweise der Sperren zurück, um dem Benutzer zu zeigen, warum das Speichern nicht möglich ist.
	 *
	 * @returns die Hinweise, leer wenn das Speichern möglich ist
	 */
	public get hinweise(): string[] {
		return [...this._sperren.value.values()];
	}

	/**
	 * Sperrt das Speichern, z.B. weil eine Eingabe ungültig ist. Besteht unter dem Schlüssel bereits eine Sperre, so
	 * wird nur deren Hinweis aktualisiert.
	 *
	 * @param schluessel   der Schlüssel der Sperre, z.B. der Name des betroffenen Feldes
	 * @param hinweis      der Hinweis für den Benutzer
	 */
	public deny(schluessel: string, hinweis: string): void {
		this._sperren.value = new Map(this._sperren.value).set(schluessel, hinweis);
	}

	/**
	 * Hebt die Sperre zum Schlüssel auf. Ohne Sperre unter dem Schlüssel passiert nichts.
	 *
	 * @param schluessel   der Schlüssel der Sperre
	 */
	public allow(schluessel: string): void {
		if (!this._sperren.value.has(schluessel)) {
			return;
		}
		const sperren = new Map(this._sperren.value);
		sperren.delete(schluessel);
		this._sperren.value = sperren;
	}

	/**
	 * Hebt alle Sperren auf.
	 */
	public allowAll(): void {
		if (this._sperren.value.size > 0) {
			this._sperren.value = new Map<string, string>();
		}
	}

}
