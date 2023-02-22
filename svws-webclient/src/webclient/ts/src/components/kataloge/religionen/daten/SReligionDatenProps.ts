import { ReligionEintrag } from "@svws-nrw/svws-core-ts";

export interface ReligionDatenProps {
	patch: (data : Partial<ReligionEintrag>) => Promise<void>;
	data: ReligionEintrag;
}