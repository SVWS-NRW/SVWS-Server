import type { FoerderschwerpunktEintrag } from "@core";

export interface FoerderschwerpunktDatenProps {
	patch: (data : Partial<FoerderschwerpunktEintrag>) => Promise<void>;
	data: FoerderschwerpunktEintrag;
}