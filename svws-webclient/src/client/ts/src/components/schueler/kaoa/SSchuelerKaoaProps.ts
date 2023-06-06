import type { SchuelerKAoADaten } from "@core";

export interface SchuelerKAoAProps {
	data: SchuelerKAoADaten;
	patch: (data : Partial<SchuelerKAoADaten>) => Promise<void>;
}