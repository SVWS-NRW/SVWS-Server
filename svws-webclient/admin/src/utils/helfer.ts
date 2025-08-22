/**
 * Diese Funktion testet nach den offizellen Vorgaben zu gültigen Identifiern innerhalb von MariaDB:
 * https://mariadb.com/docs/server/reference/sql-structure/sql-language-structure/identifier-names
 * ASCII: [0-9,a-z,A-Z$_] (numerals 0-9, basic Latin letters, both lowercase and uppercase, dollar sign, underscore)
 *
 * Extended ist möglich, wird hier aber nicht angeboten
 *
 * @param str Die Eingabe aus einem TextInput
 * @returns true, wenn der name gütig ist
 */
export function validatorSchemaName(str: string | null): boolean {
	return (str !== null) && !(/[^0-9,a-z,A-Z$_]/.test(str));
}
