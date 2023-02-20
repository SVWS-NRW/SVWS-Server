import { SchuelerSchulbesuchsdaten } from "@svws-nrw/svws-core-ts";

export interface SchuelerSchulbesuchProps {
	data: SchuelerSchulbesuchsdaten;
	patch: (data : Partial<SchuelerSchulbesuchsdaten>) => Promise<void>;
}