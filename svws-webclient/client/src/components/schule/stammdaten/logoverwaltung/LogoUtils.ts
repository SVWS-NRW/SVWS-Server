import { ReportingBildDefinition } from "@core/core/types/reporting/ReportingBildDefinition";
import type { LogoModelProxy } from "~/components/schule/stammdaten/logoverwaltung/modelProxy/LogoModelProxy";

export const SUPPORTED_IMAGE_TYPES = [
	{ mimeType: 'image/png', extensions: ['.png'] },
	{ mimeType: 'image/jpeg', extensions: ['.jpg', '.jpeg', '.jpe'] },
	{ mimeType: 'image/gif', extensions: ['.gif'] },
	{ mimeType: 'image/svg+xml', extensions: ['.svg'] },
	{ mimeType: 'image/tiff', extensions: ['.tiff', '.tif'] },
];

export type TableLogo = {
	id: number;
	kennung: string;
	bezeichnung: string;
	beschreibung: string;
	base64: string;
	hinzugefuegtAm: string;
};

export type ImageRestrictions = {
	hoeheInMM: number;
	breiteInMM: number;
	seitenverhaeltnis: string;
	aufloesungInDPI: number;
	maxGroesseInMB: number;
	types: { mimeType: string, extensions: string[] }[]
};

export type ImageInfo = {
	height: number;
	width: number;
	fileType: string | null;
	fileSize: number | null;
};

export function getImageRestrictions(kennung: string): ImageRestrictions {
	const definition = ReportingBildDefinition.getByKennung(kennung);

	let hoeheInMM = -1;
	let breiteInMM = -1;
	let seitenverhaeltnis = "/";

	if (definition !== null) {
		hoeheInMM = definition.getHoehe();
		breiteInMM = definition.getBreite();
		seitenverhaeltnis = getAspectRatio(hoeheInMM, breiteInMM);
	}

	return { hoeheInMM, breiteInMM, seitenverhaeltnis, aufloesungInDPI: 300, maxGroesseInMB: 2, types: SUPPORTED_IMAGE_TYPES };
}


export function getImageDimensions(src: string | null): Promise<{ width: number; height: number }> {
	return new Promise((resolve) => {
		if ((src === null) || (src === '')) {
			resolve({ width: 0, height: 0 });
			return;
		}

		const img = new Image();
		img.onload = () => resolve({ width: img.naturalWidth, height: img.naturalHeight });
		img.onerror = () => resolve({ width: 0, height: 0 });
		img.src = src;
	});
}

export async function setModelImageInfo(logoModel: LogoModelProxy | undefined, base64: string, fileType: string | null, fileSize: number | null) {
	if (logoModel === undefined) {
		return;
	}
	await getImageDimensions(base64).then(dimensions => {
		logoModel.imageInfo.width = dimensions.width;
		logoModel.imageInfo.height = dimensions.height;
		logoModel.imageInfo.fileSize = fileSize;
		logoModel.imageInfo.fileType = fileType;
	});
}

function getNormalizedAspectRatio(hoehe: number, breite: number): number {
	if ((hoehe <= 0) || (breite <= 0)) {
		return 1;
	}

	return Math.round((hoehe / breite) * 100) / 100;
}

export function getAspectRatio(hoehe: number, breite: number): string {
	const normalizedHeight = getNormalizedAspectRatio(hoehe, breite);
	return `1:${normalizedHeight}`;
}

export function getCssAspectRatio(kennung: string | null): string {
	if (kennung === null) {
		return "1 / 1";
	}

	const definition = ReportingBildDefinition.getByKennung(kennung);
	if (definition === null) {
		return "1 / 1";
	}

	const height = definition.getHoehe();
	const width = definition.getBreite();
	const normalizedHeight = getNormalizedAspectRatio(height, width);

	return `1 / ${normalizedHeight}`;
}

/**
 * Ermittelt den MimeType und den Daten-String aus einem Base64-String
 *
 * @param base64   vollständiger Base64-String mit DataURL
 *
 * @returns ein Objekt mit dem berechneten MimeType und dem Datenstring
 */
export function parseBase64(base64: string | undefined): { mimeType: string, data: string } | null {
	if (base64 === undefined) {
		return null;
	}
	const match = /^data:([^;]+);base64,(.+)$/.exec(base64);
	if (match === null) {
		return null;
	}
	return {
		mimeType: match[1],
		data: match[2],
	};
}

export function base64ToBlob(base64: string): Blob {
	const parsed = parseBase64(base64);
	if (parsed === null) {
		throw new Error("Ungültiger Base64 String. Es fehlt die DataUrl: data:[mimeType];base64;...");
	}
	const binary = atob(parsed.data);
	const bytes = Uint8Array.from(binary, c => c.charCodeAt(0));
	return new Blob([bytes], { type: parsed.mimeType });
}

/**
 * Liest ein File-Objekt und gibt dessen Inhalt als Base64-String zurück.
 * Der zurückgegebene String enthält den Data-URL-Prefix (z. B. "data:image/png;base64,...").
 */
export function readFileAsBase64(file: File): Promise<string> {
	return new Promise((resolve, reject) => {
		const reader = new FileReader();
		reader.onload = () => resolve(reader.result as string);
		reader.onerror = () => reject(new Error(reader.error?.message ?? "Fehler beim Lesen der Datei"));
		reader.readAsDataURL(file);
	});
}

export function getExtension(base64: string): string {
	const mimeType = parseBase64(base64)?.mimeType;
	return SUPPORTED_IMAGE_TYPES.find(t => t.mimeType === mimeType)?.extensions[0] ?? '.bin';
}
