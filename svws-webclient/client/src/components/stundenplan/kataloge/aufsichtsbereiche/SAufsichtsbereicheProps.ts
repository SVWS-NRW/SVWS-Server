import type { Aufsichtsbereich } from "@core/core/data/schule/Aufsichtsbereich";

export interface AufsichtsbereicheProps {
	patch: (data: Partial<Aufsichtsbereich>) => Promise<void>;
	auswahl: Aufsichtsbereich | undefined;
}