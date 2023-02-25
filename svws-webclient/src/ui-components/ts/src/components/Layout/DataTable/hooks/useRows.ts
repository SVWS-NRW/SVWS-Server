import { Ref, computed } from 'vue'

import type {
	DataTableColumnInternal,
	DataTableItem,
	DataTableCell,
	DataTableRow,
} from '../types'

type UseRowsProps = {
	items: Iterable<DataTableItem>;
}

const buildTableCell = (
	rowIndex: number,
	rowData: DataTableItem,
	column: DataTableColumnInternal,
): DataTableCell => {
	return {
		rowIndex,
		rowData,
		column,
		value: rowData?.[column.key] as string ?? '',
	}
}

const buildTableRow = (
	source: DataTableItem,
	initialIndex: number,
	columns: DataTableColumnInternal[],
): DataTableRow => {
	return {
		initialIndex,
		source,
		cells: columns.map(column => buildTableCell(initialIndex, source, column)),
	}
}

export default function useRows (
	columns: Ref<DataTableColumnInternal[]>,
	props: UseRowsProps
) {
	const rowsComputed = computed(() => [...props.items]
		// we use toRaw() to avoid identity hazard with reactive proxies when comparing items
		.map((source, index) => buildTableRow(toRaw(source), index, columns.value)))

	return {
		rowsComputed,
	}
}
