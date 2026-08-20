import { BasicValidator, ValidatorFehlerart } from "@core";
import type { ImageRestrictions, ImageInfo } from "../LogoUtils";


/**
 * Ein Validator, welcher prüft, ob das Bild einen validen Dateitypen hat
 */
export class ValidatorLogoImageFileType extends BasicValidator {

	/** Die Werte des Bildes */
	private readonly imageInfo: () => ImageInfo;
	/** Die Vorgaben, die das Bild erfüllen muss */
	private readonly imageRestrictions: ImageRestrictions;

	/**
	 * Erzeugt einen neuen Validator
	 *
	 * @param imageInfo           die Werte des Bildes
	 * @param imageRestrictions   die Vorgaben, die das Bild erfüllen muss
	 */
	constructor(imageInfo: () => ImageInfo, imageRestrictions: ImageRestrictions) {
		super(ValidatorFehlerart.MUSS);
		this.imageInfo = imageInfo;
		this.imageRestrictions = imageRestrictions;
	}

	/**
	 * Die Prüfroutine, die sicherstellt, dass der Dateityp des Bildes gültig ist.
	 *
	 * @returns true, wenn der Dateityp unterstützt wird
	 */
	protected pruefe(): boolean {
		const fileType = this.imageInfo().fileType;
		if (fileType === null) {
			return true;
		}

		if (!this.imageRestrictions.types.some(type => type.mimeType === fileType)) {
			this.addFehler(0,
				`Ungültiges Dateiformat: ${fileType}. Folgende Dateitypen sind erlaubt:
				${this.imageRestrictions.types.map(type => type.extensions.join(', ')).join(', ')}`);
			return false;
		}

		return true;
	}

}
