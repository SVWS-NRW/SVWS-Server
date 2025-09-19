import { JavaString } from "@core";

export function emailIsValid(value: string | null, maxLength: number) {
	if ((value === null) || JavaString.isBlank(value))
		return true;

	if (value.length > maxLength)
		return false;

	return /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(value) ||
			/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value);
}

export function phoneNumberIsValid(input: string | null, maxLength: number) {
	if ((input === null) || (JavaString.isBlank(input)))
		return true;
	// folgende Formate sind erlaubt: 0151123456, 0151/123456, 0151-123456, +49/176-456456 -> Buchstaben sind nicht erlaubt
	return /^\+?\d+([-/]?\d+)*$/.test(input) && input.length <= maxLength;
}

export function plzIsValid(input: string | null, maxLength : number) {
	if (input === null || JavaString.isBlank(input))
		return true;

	if (input.length > maxLength)
		return false;

	return /^\d+$/.test(input)
}

export function mandatoryInputIsValid(value: string | null, maxLength: number) {
	if (value === null)
		return false;

	return (!JavaString.isBlank(value)) && (value.length <= maxLength);
}

export function optionalInputIsValid(input : string | null, maxLength: number) {
	if ((input === null) || (JavaString.isBlank(input)))
		return true;

	return input.length <= maxLength;
}

export function isUniqueInList<T>(value: string | null, list: Iterable<T>, key: keyof T): boolean {
	if ((value === null) || (value === ""))
		return true;

	for (const entry of list) {
		const fieldValue = entry[key];
		if (typeof fieldValue === "string" && fieldValue.trim().toLowerCase() === value.trim().toLowerCase())
			return false;
	}
	return true;
}
