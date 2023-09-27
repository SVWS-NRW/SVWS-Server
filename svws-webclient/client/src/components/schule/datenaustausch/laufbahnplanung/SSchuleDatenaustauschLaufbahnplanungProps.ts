import { type SimpleOperationResponse } from "@core";

export interface SchuleDatenaustauschLaufbahnplanungProps {
	setGostLupoImportMDBFuerJahrgang: (formData: FormData, mode: 'none' | 'schueler' | 'all') => Promise<SimpleOperationResponse>;
}