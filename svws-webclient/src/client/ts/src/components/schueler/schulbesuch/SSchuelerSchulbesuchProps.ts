import { SchuelerSchulbesuchsdaten } from "@svws-nrw/svws-core";

export interface SchuelerSchulbesuchProps {
	data: SchuelerSchulbesuchsdaten;
	patch: (data : Partial<SchuelerSchulbesuchsdaten>) => Promise<void>;
}