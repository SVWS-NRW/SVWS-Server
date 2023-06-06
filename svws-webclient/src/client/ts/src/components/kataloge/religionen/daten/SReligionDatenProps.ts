import type { ReligionEintrag } from "@core";

export interface ReligionDatenProps {
	patch: (data : Partial<ReligionEintrag>) => Promise<void>;
	auswahl: ReligionEintrag;
}