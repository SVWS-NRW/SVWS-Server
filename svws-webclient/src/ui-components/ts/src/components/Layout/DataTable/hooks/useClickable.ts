import { Ref, computed, watch, ref } from 'vue'

import type { DataTableRow, DataTableItem } from '../types'
import useSafeVModel from './useSafeVModel'

interface UseClickableProps {
	clicked: DataTableItem | undefined
	clickable: boolean
	uniqueKey: string | undefined
}

export default function useClickable(
	sortedRows: Ref<DataTableRow[]>,
	props: UseClickableProps,
) {
	const clickedItem = useSafeVModel(props, null as DataTableItem | null, 'clicked');

	function isRowClicked(row: DataTableRow) {
		if (props.uniqueKey) {
			if (clickedItem.value?.[props.uniqueKey]) {
				return clickedItem.value?.[props.uniqueKey] === row.source?.[props.uniqueKey]
			} else {
				return clickedItem.value === row.source
			}
		} else {
			return clickedItem.value === row.source
		}
	}

	function resetClickedRow() {
		clickedItem.value = null;
	}

	function toggleRowClick(row: DataTableRow) {
		if (!props.clickable) {
			return
		}

		if (isRowClicked(row)) {
			resetClickedRow();
		} else {
			setClickedRow(row);
		}
	}

	function setClickedRow(row: DataTableRow) {
		if (!props.clickable) {
			return
		}

		clickedItem.value = row.source;
	}

	return {
		isRowClicked,
		toggleRowClick,
	}
}
