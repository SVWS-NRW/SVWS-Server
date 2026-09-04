import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { ImageInfo, ImageRestrictions } from "../LogoUtils";


/**
 * Ein Validator, welcher prüft, ob ein Bild das eine valide Dateigröße besitzt
 */
export class ValidatorLogoImageFileSize extends BasicValidator {

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
	 * Die Prüfroutine, welche prüft, ob das Bild eine valide Dateigröße besitzt.
	 *
	 * @returns true, wenn das Bild kleiner gleich der maximalen Dateigröße ist
	 */
	protected pruefe(): boolean {
		const fileSize = this.imageInfo().fileSize;
		if (fileSize === null) {
			return true;
		}

		const imageRestrictions = this.imageRestrictions;
		if (fileSize > imageRestrictions.maxGroesseInMB * 1024 * 1024) {
			this.addFehler(0,
				`Ungültige Dateigröße: ${(fileSize / 1024 / 1024).toFixed(2)} MB. Das Bild darf nicht größer als ${imageRestrictions.maxGroesseInMB} MB sein`);
			return false;
		}

		return true;
	}

}
