import { DateUtils } from "@core/core/utils/DateUtils";

/**
 * Gibt das heutige lokale Datum zurück im Format YYYY-MM-DD.
 * (keine Zeitzone/UTC-Konvertierung)
 * @returns date (YYYY-MM-DD)
 */
export function dateToday(): string {
	return getDate(new Date())!;
}

/**
 * Gibt basierend ein auf dem heutigen lokalen Datum basierend berechnetes Datum zurück.
 * (Unterstützt positive und negative Tagesoffsets)
 *
 * @param data - Objekt mit optionalem `days`-Offset (default: 0). Negative Werte erlaubt.
 * @returns date (YYYY-MM-DD)
 *
 * @example
 * dateTodayPlus({ days: 7 })   // heute + 7 Tage
 * dateTodayPlus({ days: -3 })  // heute - 3 Tage
 * dateTodayPlus()              // heute
 */
export function dateTodayPlus({ days = 0 }: { days?: number } = {}): string {
	// kann erweitert werden um Logik für Monate/Jahre
	const today = new Date();
	today.setDate(today.getDate() + days);
	return getDate(today)!;
}

/**
 * Gibt das lokale Datum (YYYY-MM-DD) aus einem JS Date Objekt zurück.
 * (keine Zeitzone/UTC-Konvertierung)
 *
 * @param dateObject - JS Date Objekt
 * @returns date (YYYY-MM-DD) oder "Invalid-Date"
 */
export function getDate(dateObject: Date): string | undefined {
	if (!(dateObject instanceof Date)) {
		console.error("getDate: Ungültiges Date-Objekt übergeben", dateObject);
	}

	const y = dateObject.getFullYear();
	const m = String(dateObject.getMonth() + 1).padStart(2, "0");
	const d = String(dateObject.getDate()).padStart(2, "0");

	return `${y}-${m}-${d}`;
}

/**
 * Formatiert ein Datum in das lokale de-DE Datumsformat (DD.MM.YYYY).
 * Es findet keine UTC-Konvertierung statt.
 *
 * @param {string} dateString - im Format YYYY-MM-DD
 * @param fallback - Rückgabe, wenn Datum nicht gesetzt oder invalid ist (default: "")
 * @returns local date (DD.MM.YYYY) oder fallback
 */
export function formatToLocalDate(dateString: string | null, fallback: string = ""): string {
	if (dateString === null) {
		return fallback;
	}

	const date = new Date(dateString);

	if (Number.isNaN(date.getTime())) {
		return fallback;
	}

	return date.toLocaleDateString("de-DE", { day: '2-digit', month: '2-digit', year: 'numeric' });
}

/**
 * Extrahiert das Datum aus einem Custom-DateTime Format.
 *
 * @param dateTime - Custom DateTime (YYYY-MM-DD HH:mm:ss.sss)
 * @returns date (YYYY-MM-DD) oder "Invalid-Date"
 */
export function getDateFromDateTime(dateTime: string): string | undefined {

	const customDateTimeRegex = /^(\d{4}-\d{2}-\d{2})\s\d{2}:\d{2}:\d{2}\.\d{1,3}$/;
	const customMatch = customDateTimeRegex.exec(dateTime);
	if (customMatch?.[1] !== undefined) {
		const datePart = customMatch[1];
		if (DateUtils.isValidDate(datePart)) {
			return datePart;
		}
	}

	console.error("getDateFromDateTime: Ungültiges Eingabedatum:", dateTime);
}

/**
 * Formatiert ein lokales Datum (YYYY-MM-DD) in das Custom DateTime Format.
 *
 * Die Zeit wird immer auf Mitternacht gesetzt: 00:00:00.000
 * Das Custom Format nutzt ein Leerzeichen als Trenner (kein T) und enthält keine Zeitzone.
 * (kein ISO 8601 konformes DateTime-Format)
 *
 * @param date - lokales Datum ohne Zeitzone: YYYY-MM-DD
 * @returns custom dateTime (YYYY-MM-DD HH:mm:ss.sss) oder "Invalid-Date"
 *
 * @example
 * formatDateToDateTime("2026-04-23") // "2026-04-23 00:00:00.000"
 */
export function formatDateToDateTime(date: string): string | undefined {
	if (!DateUtils.isValidDate(date)) {
		console.error("formatDateToDateTime: Ungültiges Eingabedatum:", date);
	}

	return `${date} 00:00:00.0`;
}
