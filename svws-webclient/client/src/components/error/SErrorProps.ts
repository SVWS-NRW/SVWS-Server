import type { BenutzerKompetenz } from "@core";
import type { api } from "../../router/Api";

export interface ErrorProps {
	code: number | undefined;
	error: Error | undefined;
	errortext: string | undefined;
	api: typeof api;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
