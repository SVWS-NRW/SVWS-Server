import type { Aufsichtsbereich } from "@core";

export interface AufsichtsbereicheProps {
	patch: (data : Partial<Aufsichtsbereich>) => Promise<void>;
	auswahl: Aufsichtsbereich | undefined;
}