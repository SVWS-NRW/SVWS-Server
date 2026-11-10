import { OpenApiError } from "@core/api/OpenApiError";
import type { SchulwechselErfolgreicheBewerbung } from "@core/core/data/schule/SchulwechselErfolgreicheBewerbung";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { SchulwechselErfolgreicheBewerbungenState } from "@ui/states/schulwechsel/SchulwechselErfolgreicheBewerbungenState";
import { StateManager } from "@ui/ui/StateManager";

import { notificationStateImpl } from "../NotificationsStateImpl";
import { api } from "~/router/Api";

interface SchulwechselErfolgreicheBewerbungenReactiveState {
	erfolgreicheBewerbungen: List<SchulwechselErfolgreicheBewerbung>;
	newBewerbungenCount: number | null;
	isPreviewRunning: boolean;
}

/**
 * Die Implementierung des States für die Anzeige der erfolgreichen Bewerbungen (Schulwechsel).
 * Fehler beim Laden werden nicht abgefangen, sodass sie in der globalen Fehleranzeige erscheinen.
 * Die Abfrage nach neuen Bewerbungen und der Import kommunizieren mit einem Remote-Server. Ihre Fehler werden abgefangen
 * und mit der Meldung des SVWS-Servers als Notification gemeldet, da die Ansicht trotzdem nutzbar bleibt.
 */
export class SchulwechselErfolgreicheBewerbungenStateImpl extends StateManager<SchulwechselErfolgreicheBewerbungenReactiveState> implements SchulwechselErfolgreicheBewerbungenState {

	/** Die Nummer der zuletzt gestarteten Abfrage nach neuen Bewerbungen */
	private previewRequestId = 0;

	/** Die ID der Fehlermeldung der letzten Abfrage oder null, wenn keine angezeigt wird */
	private previewErrorNotificationId: number | null = null;

	public constructor() {
		super({
			erfolgreicheBewerbungen: new ArrayList<SchulwechselErfolgreicheBewerbung>(),
			newBewerbungenCount: null,
			isPreviewRunning: false,
		});
	}

	public get erfolgreicheBewerbungen(): List<SchulwechselErfolgreicheBewerbung> {
		return this.state.erfolgreicheBewerbungen;
	}

	public get newBewerbungenCount(): number | null {
		return this.state.newBewerbungenCount;
	}

	public get isPreviewRunning(): boolean {
		return this.state.isPreviewRunning;
	}

	public async updateErfolgreicheBewerbungen(): Promise<void> {
		const erfolgreicheBewerbungen = await api.server.getSchulwechselErfolgreicheBewerbungen(api.schema);
		this.setPatchedState({ erfolgreicheBewerbungen });
	}

	public async updateNewBewerbungenCount(): Promise<void> {
		// Die Abfrage läuft bei jedem Betreten der Ansicht und kann länger dauern. Beginnt währenddessen eine neue Abfrage, zählt nur
		// noch deren Ergebnis. Sonst würde die ältere Antwort den Zustand "läuft" vorzeitig beenden und einen veralteten Stand setzen.
		const requestId = ++this.previewRequestId;
		// Die Meldung einer früheren Abfrage wird entfernt, damit sich bei einem dauerhaften Fehler nicht für jedes Betreten
		// der Ansicht eine weitere Meldung ansammelt und keine veraltete Meldung stehen bleibt.
		if (this.previewErrorNotificationId !== null) {
			notificationStateImpl.remove(this.previewErrorNotificationId);
			this.previewErrorNotificationId = null;
		}
		// Der bisherige Wert wird verworfen, damit während der Abfrage und nach einem Fehler kein veralteter Stand angezeigt wird
		this.setPatchedState({ isPreviewRunning: true, newBewerbungenCount: null });
		try {
			const newBewerbungenCount = await api.server.getSchulwechselErfolgreicheBewerbungenSyncPreview(api.schema);
			if (requestId === this.previewRequestId) {
				this.setPatchedState({ newBewerbungenCount });
			}
		} catch (e) {
			if (!(e instanceof OpenApiError)) {
				throw e;
			}
			if (requestId !== this.previewRequestId) {
				return;
			}
			const message = await SchulwechselErfolgreicheBewerbungenStateImpl.getErrorMessage(e, "Die Abfrage nach neuen Daten");
			if (requestId === this.previewRequestId) {
				this.previewErrorNotificationId = notificationStateImpl.error("Abfrage fehlgeschlagen", message);
			}
		} finally {
			if (requestId === this.previewRequestId) {
				this.setPatchedState({ isPreviewRunning: false });
			}
		}
	}

	/**
	 * Ermittelt die Meldung für einen fehlgeschlagenen Aufruf. Ohne Antwort des Servers ist der SVWS-Server selbst
	 * nicht erreichbar, andernfalls liefert er die Meldung zum Fehler, z. B. bei der Kommunikation mit dem Remote-Server.
	 * Ist die Antwort leer oder HTML (z. B. die Fehlerseite eines Proxys), wird stattdessen eine Standardmeldung angezeigt.
	 * Sie ist wie die Meldungen des Servers aufgebaut: Beschreibung, Status-Code und die Ursache.
	 *
	 * @param e           der aufgetretene Fehler
	 * @param operation     die Beschreibung des fehlgeschlagenen Vorgangs für die Standardmeldungen
	 *
	 * @returns die Meldung für die Anzeige
	 */
	private static async getErrorMessage(e: OpenApiError, operation: string): Promise<string> {
		if (e.response === null) {
			return `Der SVWS-Server ist nicht erreichbar. ${operation} konnte nicht durchgeführt werden.`;
		}
		let text = "";
		try {
			text = await e.response.text();
		} catch {
			// Lässt sich der Inhalt nicht lesen (z. B. Abbruch der Verbindung), wird die Standardmeldung verwendet
		}
		if (SchulwechselErfolgreicheBewerbungenStateImpl.isDisplayable(e.response, text)) {
			return text;
		}
		return `${operation} ist fehlgeschlagen (Status-Code: ${e.response.status}): ${SchulwechselErfolgreicheBewerbungenStateImpl.getStatusText(e.response.status)}.`;
	}

	/**
	 * Liefert die Ursache für den Status-Code einer Fehlerantwort ohne anzeigbare Meldung.
	 * Die Texte entsprechen denen des Servers für den Remote-Server.
	 *
	 * @param status   der HTTP-Status-Code
	 *
	 * @returns die Ursache für die Anzeige
	 */
	private static getStatusText(status: number): string {
		switch (status) {
			case 401:
				return "Nicht authentifiziert";
			case 403:
				return "Fehlende Berechtigung";
			case 404:
				return "API-Ressource nicht gefunden";
			case 502:
			case 503:
			case 504:
				return "Der Server ist vorübergehend nicht erreichbar";
			default:
				return "Unerwarteter Fehler";
		}
	}

	/**
	 * Prüft, ob der Text einer Fehlerantwort als Meldung angezeigt werden kann. Meldungen des SVWS-Servers sind Klartext.
	 * Der Server sendet sie nicht zwingend als text/plain, deshalb werden nur leere und HTML-Antworten ausgeschlossen.
	 * Der Inhalt wird zusätzlich geprüft, da ein Proxy den Content-Type auch weglassen kann.
	 *
	 * @param response   die Fehlerantwort
	 * @param text       der Text der Fehlerantwort
	 *
	 * @returns true, wenn der Text angezeigt werden kann
	 */
	private static isDisplayable(response: Response, text: string): boolean {
		if (text.trim().length === 0) {
			return false;
		}
		const contentType = response.headers.get("Content-Type") ?? "";
		return !contentType.toLowerCase().includes("html") && !text.trimStart().startsWith("<");
	}

	public async sync(): Promise<number | null> {
		const sizeBefore = this.state.erfolgreicheBewerbungen.size();
		try {
			await api.server.syncSchulwechselErfolgreicheBewerbungen(api.schema);
		} catch (e) {
			if (!(e instanceof OpenApiError)) {
				throw e;
			}
			notificationStateImpl.error("Import fehlgeschlagen", await SchulwechselErfolgreicheBewerbungenStateImpl.getErrorMessage(e, "Der Import der neuen Datensätze"));
			return null;
		}
		// Der Import war erfolgreich. Schlägt nur das Nachladen fehl, wird dies gemeldet, ohne den Import als fehlgeschlagen auszugeben.
		// Dann ist die Anzahl der importierten Datensätze unbekannt. Die Abfrage nach neuen Daten meldet ihre Fehler selbst.
		try {
			await Promise.all([this.updateErfolgreicheBewerbungen(), this.updateNewBewerbungenCount()]);
		} catch (e) {
			if (!(e instanceof OpenApiError)) {
				throw e;
			}
			const message = await SchulwechselErfolgreicheBewerbungenStateImpl.getErrorMessage(e, "Das Laden der Datensätze");
			notificationStateImpl.error("Aktualisierung fehlgeschlagen", `Der Import war erfolgreich, die Anzeige konnte aber nicht aktualisiert werden. ${message}`);
			return null;
		}
		return Math.max(0, this.state.erfolgreicheBewerbungen.size() - sizeBefore);
	}

}

export const schulwechselErfolgreicheBewerbungenStateImpl = new SchulwechselErfolgreicheBewerbungenStateImpl();
