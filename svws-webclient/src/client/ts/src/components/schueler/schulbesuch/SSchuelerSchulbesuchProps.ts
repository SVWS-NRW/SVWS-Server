import type { SchuelerSchulbesuchsdaten } from "@core";

export interface SchuelerSchulbesuchProps {
	data: SchuelerSchulbesuchsdaten;
	patch: (data : Partial<SchuelerSchulbesuchsdaten>) => Promise<void>;
}