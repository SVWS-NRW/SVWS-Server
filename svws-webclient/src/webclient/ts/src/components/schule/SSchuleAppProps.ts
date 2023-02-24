import { SchuleStammdaten, SimpleOperationResponse } from "@svws-nrw/svws-core-ts";

export interface SchuleAppProps {
	schuleStammdaten: SchuleStammdaten;
	setGostLupoImportMDBFuerJahrgang: (formData: FormData) => Promise<boolean>;
}