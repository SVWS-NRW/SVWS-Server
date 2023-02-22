import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";

export interface FoerderschwerpunktDatenProps {
	patch: (data : Partial<FoerderschwerpunktEintrag>) => Promise<void>;
	data: FoerderschwerpunktEintrag;
}