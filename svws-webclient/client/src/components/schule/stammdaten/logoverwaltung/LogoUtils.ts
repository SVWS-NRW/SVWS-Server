export const SUPPORTED_IMAGE_TYPES = [
	{ mimeType: 'image/png', extensions: ['.png'] },
	{ mimeType: 'image/jpeg', extensions: ['.jpg', '.jpeg', '.jpe'] },
	{ mimeType: 'image/gif', extensions: ['.gif'] },
	{ mimeType: 'image/svg+xml', extensions: ['.svg'] },
	{ mimeType: 'image/tiff', extensions: ['.tiff', '.tif'] },
] as const;

export type TableLogo = {
	id: number;
	kennung: string;
	bezeichnung: string;
	beschreibung: string;
	base64: string;
	hinzugefuegtAm: string;
};
