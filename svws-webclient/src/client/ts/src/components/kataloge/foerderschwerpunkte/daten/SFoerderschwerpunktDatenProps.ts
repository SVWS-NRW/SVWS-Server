import type { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core";

export interface FoerderschwerpunktDatenProps {
	patch: (data : Partial<FoerderschwerpunktEintrag>) => Promise<void>;
	data: FoerderschwerpunktEintrag;
}