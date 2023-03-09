export default function getKeys<K extends object>(items: Iterable<K>): string[] {
	const accumulatedObject = [...items].reduce((accumulator, value) => {
		return {...accumulator, ...value};
	}, {});
	return Object.keys(accumulatedObject);
}