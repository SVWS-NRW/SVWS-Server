import type { Aufsichtsbereich } from "@svws-nrw/svws-core";

export interface AufsichtsbereichDatenProps {
	patch: (data : Partial<Aufsichtsbereich>) => Promise<void>;
	data: Aufsichtsbereich;
}