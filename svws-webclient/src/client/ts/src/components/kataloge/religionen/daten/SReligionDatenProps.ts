import type { ReligionEintrag } from "@svws-nrw/svws-core";

export interface ReligionDatenProps {
	patch: (data : Partial<ReligionEintrag>) => Promise<void>;
	auswahl: ReligionEintrag;
}