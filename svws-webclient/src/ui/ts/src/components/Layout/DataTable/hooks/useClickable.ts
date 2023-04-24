/* eslint-disable @typescript-eslint/ban-ts-comment */
import { computed, onUpdated, toRaw, onMounted } from 'vue'
import { useDebounceFn } from '@vueuse/core';

import type { DataTableRow, DataTableItem } from '../types'

interface UseClickableOptions {
	clickedItem: () => DataTableItem | undefined,
	emit: (v: DataTableItem | null) => void,
	isActive: () => boolean,
	canReset: () => boolean,
}

export default function useClickable({ clickedItem: data, emit, isActive, canReset }: UseClickableOptions) {
	// internally, we only work with raw values so we don't run into identity hazard when comparing them
	const clickedItemRaw = computed(() => (toRaw(data()) ?? null));

	function isRowClicked(row: DataTableRow) {
		return row.source === clickedItemRaw.value;
	}

	function resetClickedRow() {
		if (canReset())
			emit(null);
	}

	function toggleRowClick(row: DataTableRow) {
		if (!isActive)
			return;
		if (isRowClicked(row))
			resetClickedRow();
		else
			setClickedRow(row);
	}

	function setClickedRow(row: DataTableRow) {
		if (!isActive)
			return;
		emit(row.source);
	}

	onMounted(scrollToClickedElement);
	onUpdated(useDebounceFn(scrollToClickedElement, 250));

	function isInView(el: Element) {
		const box = el.getBoundingClientRect();
		return box.top < window.innerHeight && box.bottom >= 0;
	}

	function scrollToClickedElement() {
		const clickedElementHtml = document.querySelector('.data-table__tr--clicked');
		const scrollOptions: ScrollIntoViewOptions = { behavior: "smooth", block: "center" };
		if (clickedElementHtml) {
			// @ts-ignore
			if (typeof clickedElementHtml.scrollIntoViewIfNeeded === "function") clickedElementHtml.scrollIntoViewIfNeeded(scrollOptions)
		 	else if(!isInView(clickedElementHtml)) clickedElementHtml.scrollIntoView(scrollOptions);
		}
	}

	return { isRowClicked, toggleRowClick, setClickedRow };
}
