import type { Aufsichtsbereich } from "@core";

export interface AufsichtsbereichDatenProps {
	patch: (data : Partial<Aufsichtsbereich>) => Promise<void>;
	auswahl: Aufsichtsbereich | undefined;
}