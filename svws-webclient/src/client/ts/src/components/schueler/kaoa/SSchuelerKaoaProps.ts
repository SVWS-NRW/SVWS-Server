import type { SchuelerKAoADaten } from "@svws-nrw/svws-core";

export interface SchuelerKAoAProps {
	data: SchuelerKAoADaten;
	patch: (data : Partial<SchuelerKAoADaten>) => Promise<void>;
}