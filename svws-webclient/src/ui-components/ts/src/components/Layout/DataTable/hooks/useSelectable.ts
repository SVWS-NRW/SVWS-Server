import { Ref, computed, ref } from 'vue'

import type { DataTableRow, DataTableItem } from '../types'
import useSafeVModel from './useSafeVModel';

interface UseSelectableProps {
	modelValue: DataTableItem[] | undefined
	selectable: boolean
}

export default function useSelectable(
	sortedRows: Ref<DataTableRow[]>,
	props: UseSelectableProps,
) {
	const selectedItems = useSafeVModel(props, [] as DataTableItem[]);

	const noRowsSelected = computed(() => (
		!sortedRows.value.some(({ source }) => selectedItems.value.includes(source))
	))

	const allRowsSelected = computed(() => {
		if (sortedRows.value.length === 0) { return false }

		return sortedRows.value.every(({ source }) => selectedItems.value.includes(source))
	})

	function isRowSelected(row: DataTableRow) {
		return selectedItems.value.includes(row.source)
	}

	function selectAllRows() {
		selectedItems.value = [...new Set([
			...selectedItems.value,
			...sortedRows.value.map(row => row.source),
		])]
	}

	function unselectAllRows() {
		const sortedRowsKeys = sortedRows.value.map(row => row.source)

		selectedItems.value = selectedItems.value
			.filter((item) => !sortedRowsKeys.includes(item))
	}

	function selectRow(row: DataTableRow) {
		selectedItems.value.push(row.source);
	}

	function unselectRow(row: DataTableRow) {
		const index = selectedItems.value.findIndex(item => item === row.source)

		selectedItems.value = [
			...selectedItems.value.slice(0, index),
			...selectedItems.value.slice(index + 1),
		]
	}

	function toggleRowSelection(row: DataTableRow) {
		if (!props.selectable) {
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
	}
}
