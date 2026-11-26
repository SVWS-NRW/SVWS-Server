import type { ShallowRef } from "vue";
import { shallowRef } from "vue";

/** Die möglichen Antworten des Benutzers, wenn eine Navigation von einem Guard unterbrochen wird */
type NavigationGuardReply = "cancel" | "discard" | "save";

/** Die Angaben des Guards zu einer Abfrage, die das Modal anzeigt */
export interface NavigationGuardQueryInfo {

	/** Die Bezeichnungen der Bereiche, in denen ungespeicherte Änderungen vorliegen */
	bereiche: string[];

	/** Gibt an, ob die Änderungen gespeichert werden dürfen, d.h. ob "Speichern und fortfahren" möglich ist */
	isPatchAllowed: boolean;

	/** Die Hinweise, warum das Speichern nicht möglich ist, z.B. wegen ungültiger Eingaben */
	patchHinweise: string[];

}

/**
 * Die Abfrage, die ein Navigation-Guard stellt, wenn beim Verlassen einer Seite ungespeicherte Änderungen vorliegen.
 * Sie verbindet den Guard (der auf die Antwort wartet) mit dem Modal (das die Antwort vom Benutzer einholt).
 */
export class NavigationGuardQuery {

	/** Die Angaben, wenn der Guard keine macht: keine Bereiche, Speichern möglich, keine Hinweise */
	private static readonly DEFAULT_INFO: NavigationGuardQueryInfo = { bereiche: [], isPatchAllowed: true, patchHinweise: [] };

	/** Gibt an, ob die Abfrage gerade offen ist und auf eine Antwort wartet */
	private readonly _isOpen: ShallowRef<boolean> = shallowRef(false);

	/** Die Angaben des Guards zur offenen Abfrage */
	private readonly _info: ShallowRef<NavigationGuardQueryInfo> = shallowRef(NavigationGuardQuery.DEFAULT_INFO);

	/** Löst die Promise der offenen Abfrage auf */
	private _reply: ((reply: NavigationGuardReply) => void) | null = null;


	/**
	 * Gibt zurück, ob die Abfrage gerade offen ist.
	 *
	 * @returns true, wenn die Abfrage auf eine Antwort wartet
	 */
	public get isOpen(): boolean {
		return this._isOpen.value;
	}

	/**
	 * Gibt die Angaben des Guards zur Abfrage zurück. Ohne offene Abfrage sind es die Standardangaben.
	 *
	 * @returns die Angaben
	 */
	public get info(): NavigationGuardQueryInfo {
		return this._info.value;
	}

	/**
	 * Öffnet die Abfrage und wartet auf die Antwort des Benutzers. Eine bereits offene Abfrage wird mit "cancel"
	 * beantwortet, damit nie zwei Abfragen gleichzeitig auf eine Antwort warten.
	 *
	 * @param info   die Angaben des Guards, die das Modal anzeigt (Default: keine Bereiche, Speichern möglich, keine Hinweise)
	 *
	 * @returns die Antwort des Benutzers
	 */
	public query(info: NavigationGuardQueryInfo = NavigationGuardQuery.DEFAULT_INFO): Promise<NavigationGuardReply> {
		this.reply("cancel");
		return new Promise<NavigationGuardReply>(resolve => {
			this._reply = resolve;
			this._info.value = info;
			this._isOpen.value = true;
		});
	}

	/**
	 * Beantwortet die offene Abfrage und schließt sie. Ohne offene Abfrage passiert nichts.
	 *
	 * @param reply   die Antwort des Benutzers
	 */
	public reply(reply: NavigationGuardReply): void {
		const resolve = this._reply;
		this._reply = null;
		this._isOpen.value = false;
		this._info.value = NavigationGuardQuery.DEFAULT_INFO;
		resolve?.(reply);
	}

}
