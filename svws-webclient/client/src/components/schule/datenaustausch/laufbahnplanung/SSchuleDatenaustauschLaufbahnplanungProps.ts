import { type SimpleOperationResponse } from "@core";

export interface SchuleDatenaustauschLaufbahnplanungProps {
	setGostLupoImportMDBFuerJahrgang: (formData: FormData) => Promise<SimpleOperationResponse>;
}