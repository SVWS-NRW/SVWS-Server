import { Ref, computed } from 'vue'
import type { DataTableRow, DataTableItem } from '../types'

interface UseSelectableOptions {
	selectedItems: () => DataTableItem[] | undefined,
	emit: (v: DataTableItem[]) => void,
	sortedRows: Ref<DataTableRow[]>,
	isActive: () => boolean
}

export default function useSelectable({
	selectedItems: data, emit, sortedRows, isActive,
}: UseSelectableOptions) {
	// internally, we only work with raw values so we don't run into identity hazard when comparing them
	const selectedItemsRaw = computed(() => (data() ?? []).map(i => toRaw(i)))

	const noRowsSelected = computed(() => (
		!sortedRows.value.some(isRowSelected)
	))

	const allRowsSelected = computed(() => {
		if (sortedRows.value.length === 0) { return false }
		return sortedRows.value.every(isRowSelected)
	})

	const someNotAllRowsSelected = computed(() => {
		if (sortedRows.value.length === 0) { return false }
		return sortedRows.value.some(isRowSelected) && !allRowsSelected.value
	})

	function isRowSelected(row: DataTableRow) {
		return selectedItemsRaw.value.includes(row.source)
	}

	function selectAllRows() {
		emit([...sortedRows.value.map(row => row.source)]);
	}

	function unselectAllRows() {
		emit([]);
	}

	function selectRow(row: DataTableRow) {
		emit(selectedItemsRaw.value.concat(row.source))
	}

	function unselectRow(row: DataTableRow) {
		const index = selectedItemsRaw.value.indexOf(row.source)
		const newSelection = selectedItemsRaw.value.slice()
		newSelection.splice(index, 1)
		emit(newSelection)
	}

	function toggleRowSelection(row: DataTableRow) {
		if (!isActive()) {
			return
		}

		if (isRowSelected(row)) {
			unselectRow(row)
		} else {
			selectRow(row)
		}
	}

	function toggleBulkSelection() {
		if (allRowsSelected.value) {
			unselectAllRows()
		} else {
			selectAllRows()
		}
	}

	return {
		toggleRowSelection,
		toggleBulkSelection,
		isRowSelected,
		noRowsSelected,
		allRowsSelected,
		someNotAllRowsSelected,
	}
}
