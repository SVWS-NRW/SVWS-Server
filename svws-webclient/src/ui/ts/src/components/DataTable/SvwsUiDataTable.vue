<template>
	<div v-if="$slots.search || $slots.filter">
		<div class="data-table__filter" :class="{'data-table__filter-open': $slots.filter && filterOpenRef}">
			<div class="flex w-full gap-2">
				<div class="flex-grow" v-if="$slots.search">
					<slot name="search" />
				</div>
				<div v-if="$slots.filterSimple" class="data-table__filter-simple">
					<slot name="filterSimple" />
				</div>
				<div v-if="$slots.filter && filterHide" class="ml-auto flex flex-shrink-0">
					<div :class="{'opacity-40 hover:opacity-100': filterOpenRef}" class="toggle--filter" v-if="filterHide">
						<svws-ui-button type="transparent" @click="toggleFilterOpen" class="h-full">
							<i-ri-filter-line v-if="!filterOpenRef" />
							<i-ri-eye-off-line v-else />
							<span class="max-sm:hidden">Filter</span>
						</svws-ui-button>
					</div>
				</div>
			</div>
			<div v-if="$slots.filter && filterOpenRef" class="data-table__filter__fields" :class="{'-order-1': filterReverse}">
				<slot name="filter" />
				<template v-if="filtered && filterReset">
					<svws-ui-button type="transparent" @click="filterReset" title="Filter zurücksetzen"
						class="justify-center mr-auto">
						<i-ri-filter-off-line />
						<span>Zurücksetzen</span>
					</svws-ui-button>
				</template>
			</div>
		</div>
	</div>
	<div role="table" aria-label="Tabelle" class="data-table"
		:class="{
			'data-table__selectable': selectable,
			'data-table__sortable': sortBy,
			'data-table__clickable': clickable,
			'data-table__no-data': typeof noData !== 'undefined' ? noData : showNoDataHtml,
			'data-table__has-row-actions': rowActions || manualRowActions,
			'data-table__collapsible': collapsible,
			'data-table--tab-bar': tabBarDesign,
			'data-table--no-footer-scroll-bottom': !(!disableFooter && (selectable || $slots.footer || $slots.footerActions || count)),
			'data-table__contrast-border': contrastBorder,
			'data-table__panel-height': panelHeight,
			'overflow-x-hidden': overflowXHidden,
			'data-table__style-navigation': tableStyle === 'navigation',
		}"
		v-bind="computedTableAttributes">
		<div role="rowgroup" aria-label="Tabellenkopf" class="data-table__thead" v-if="!disableHeader" :class="{'shadow-lg-up': false}">
			<slot name="header"
				:all-rows-selected="allRowsSelected"
				:toggle-all-rows="toggleBulkSelection"
				:columns="columnsComputed">
				<div role="row" class="data-table__tr data-table__thead__tr">
					<div role="checkbox" aria-label="Alle auswählen" v-if="selectable"
						class="data-table__th data-table__thead__th data-table__cell-select">
						<svws-ui-checkbox class="data-table__cell-checkbox"
							:model-value="someNotAllRowsSelected ? 'indeterminate' : allRowsSelected"
							@update:model-value="toggleBulkSelection" />
					</div>
					<div role="columnheader"
						v-for="column in columnsComputed"
						:key="column.key"
						class="data-table__th data-table__thead__th"
						:class="`data-table__th__${column.key} data-table__th__align-${column.align}${column.disabled ? ' data-table__th__disabled' : ''}`"
						@click.exact="column.sortable && toggleSorting(column)">
						<div class="data-table__th-wrapper"
							:class="{'data-table__th-wrapper__sortable': column.sortable, 'data-table__th-wrapper__sortable-column': sortBy === column.name && sortingOrder}"
							:title="column.tooltip ? '' : column.label">
							<span class="data-table__th-title">
								<slot :name="`header(${column.key})`"
									:column="column">
									<svws-ui-tooltip v-if="column.tooltip">
										{{ column.label }}
										<template #content>
											{{ column.tooltip }}
										</template>
									</svws-ui-tooltip>
									<template v-else>
										{{ column.label }}
									</template>
								</slot>
							</span>
							<span v-if="column.sortable" :role=" column.sortable ? 'button' : undefined"
								class="data-table__th-sorting"
								:tabindex="column.sortable ? 0 : -1">
								<span class="sorting-arrows"
									:class="{'sorting-arrows__column': sortBy === column.name && sortingOrder}">
									<i-ri-arrow-up-down-line class="sorting-arrows__up"
										:class="{'sorting-arrows__active': sortBy === column.name && sortingOrder === 'asc'}" />
									<i-ri-arrow-up-down-line class="sorting-arrows__down"
										:class="{'sorting-arrows__active': sortBy === column.name && sortingOrder === 'desc'}" />
								</span>
							</span>
						</div>
					</div>
					<div v-if="rowActions"
						class="data-table__th data-table__thead__th text-black/25 data-table__th__row-actions"
						role="none" title="Aktionen">
						<i-ri-edit-2-line />
					</div>
				</div>
			</slot>
		</div>
		<div role="rowgroup" aria-label="Tabelleninhalt" class="data-table__tbody">
			<slot name="body" :rows="sortedRows">
				<div role="row" v-if="showNoDataHtml" key="showNoDataHtml">
					<div role="cell" class="no-data data-table__td data-table__tbody__td"
						:colspan="columnsComputed.length + (selectable ? 1 : 0)"
						v-html="noDataHtml" />
				</div>
				<div role="row" v-for="(row, index) in sortedRows"
					:key="`table-row_${row}_${index}`"
					class="data-table__tr data-table__tbody__tr"
					:class="[
						{ 'data-table__tr--selected': isRowSelected(row) },
						{ 'data-table__tr--clicked': isRowClicked(row) },
					]"
					@click="toggleRowClick(row)">
					<div role="cell" v-if="selectable"
						class="data-table__td data-table__cell-select"
						:key="`selectable__${row}_${index}`">
						<label role="none" class="checkbox" :class="{'checkbox--checked': isRowSelected(row)}" @click.stop>
							<input class="checkbox--control" type="checkbox" :checked="isRowSelected(row)" @input="toggleRowSelection(row)">
							<span role="checkbox" class="icon">
								<i-ri-checkbox-fill v-if="isRowSelected(row)" />
								<i-ri-checkbox-blank-line v-else />
							</span>
						</label>
					</div>
					<div role="cell" v-for="cell in row.cells"
						:key="`table-cell_${cell.column.key + cell.rowIndex}`"
						class="data-table__td"
						:class="`data-table__td__${cell.column.key} data-table__td__align-${cell.column.align}${cell.column.disabled ? ' data-table__td__disabled' : ''}`">
						<slot v-if="`cell(${cell.column.key})` in $slots"
							:name="`cell(${cell.column.key})`"
							v-bind="cell" />
						<slot v-else name="cell" v-bind="cell">
							<svws-ui-text-input v-if="row.isEditing"
								v-model="cell.value"
								:headless="true"
								:type="(cell.column.type)"
								@update:value="(value: string) => cell.value = value"
								@click.stop="setClickedRow(row) " />
							<span v-else class="data-table__td-content" :title="cell.value">
								{{ cell.column.type === 'date' ? (new Date(cell.value).toLocaleDateString('de', {day: '2-digit', month: '2-digit', year: 'numeric', timeZone: 'Europe/Berlin'})) : cell.value }}
							</span>
						</slot>
					</div>
				</div>
			</slot>
		</div>
		<div role="rowgroup" aria-label="Fußzeile" class="data-table__tfoot" :class="{'shadow-lg-up': false}"
			v-if="!disableFooter && (selectable || $slots.footer || $slots.footerActions || count)">
			<slot name="footer"
				:all-rows-selected="allRowsSelected"
				:toggle-all-rows="toggleBulkSelection"
				:rows="sortedRows">
				<div role="row" class="data-table__tr data-table__tfoot__tr">
					<div role="checkbox" v-if="selectable"
						class="data-table__th data-table__tfoot__th data-table__cell-select">
						<svws-ui-checkbox class="data-table__cell-checkbox"
							:model-value="someNotAllRowsSelected ? 'indeterminate' : allRowsSelected"
							@update:model-value="toggleBulkSelection" />
					</div>
					<div v-if="count && modelValue" role="cell"
						class="data-table__th data-table__tfoot__th data-table__tfoot-count text-sm">
						<span v-if="someNotAllRowsSelected || allRowsSelected"
							class="font-medium">{{ modelValue.length }}/{{
								sortedRows.length
							}} ausgewählt</span>
						<span v-else class="opacity-50">{{ sortedRows.length === 1 ? '1 Ergebnis': `${sortedRows.length} Ergebnisse` }}</span>
					</div>
					<div role="cell" class="data-table__th data-table__tfoot__th data-table__tfoot-actions"
						v-if="$slots.footerActions">
						<slot name="footerActions" />
					</div>
				</div>
			</slot>
		</div>
	</div>
</template>

<script lang="ts">
	export default defineComponent({
		inheritAttrs: false,
	});
</script>

<script lang="ts" setup>
	import type { DataTableCell, DataTableColumnInternal, DataTableColumnSource, DataTableItem, DataTableRow, DataTableSortingOrder, UseColumnProps } from "./types";
	import type { TableHTMLAttributes } from "vue";
	import { defineComponent, computed, useAttrs, toRef, toRaw, onMounted, onUpdated, getCurrentInstance, ref } from "vue";
	import { DataTableSortingOptions } from "./types";
	import { useDebounceFn } from "@vueuse/core";

	const props = withDefaults(
		defineProps<{
			columns?: DataTableColumnSource[];
			items?: Iterable<DataTableItem> | DataTableItem[];
			modelValue?: DataTableItem[];
			sortingOrder?: DataTableSortingOrder | undefined;
			sortBy?: string | undefined;
			selectable?: boolean;
			clicked?: DataTableItem | undefined;
			clickable?: boolean;
			allowUnclick?: boolean;
			noDataHtml?: string;
			uniqueKey?: string;
			count?: boolean;
			rowActions?: Array<{
				label: string;
				action: string;
			}>;
			rowExecute?: (action: string, row: DataTableItem) => void;
			manualRowActions?: boolean;
			filterOpen?: boolean;
			filterHide?: boolean;
			filtered?: boolean;
			filterReset?: () => void;
			filterReverse?: boolean;
			disableFooter?: boolean;
			disableHeader?: boolean;
			collapsible?: boolean;
			noData?: boolean;
			scrollIntoView?: boolean;
			tabBarDesign?: boolean;
			contrastBorder?: boolean;
			panelHeight?: boolean;
			overflowXHidden?: boolean;
			tableStyle?: 'default' | 'navigation';
		}>(),
		{
			columns: () => [],
			items: () => [],
			modelValue: undefined,
			sortingOrder: undefined,
			sortBy: undefined,
			clicked: undefined,
			clickable: false,
			allowUnclick: false,
			selectable: false,
			noDataHtml: "Keine Einträge vorhanden.",
			uniqueKey: undefined,
			count: false,
			rowActions: undefined,
			rowExecute: undefined,
			manualRowActions: false,
			filterOpen: false,
			filterHide: true,
			filtered: false,
			filterReset: () => {},
			filterReverse: false,
			disableFooter: false,
			disableHeader: false,
			collapsible: false,
			noData: undefined,
			scrollIntoView: undefined,
			tabBarDesign: undefined,
			contrastBorder: false,
			panelHeight: false,
			overflowXHidden: false,
			tableStyle: 'default',
		}
	);

	const emit = defineEmits<{
		(e: "update:modelValue", items: any[]): void;
		(e: "update:sortBy", sortBy: string): void;
		(e: "update:sortingOrder", sortingOrder: DataTableSortingOrder): void;
		(e: "update:clicked", items: any | null): void;
	}>();

	const attrs = useAttrs();
	function capitalizeFirstLetter(string: string) {
		return string.charAt(0).toUpperCase() + string.slice(1);
	}
	function getKeys<K extends object>(items: Iterable<K>): string[] {
		const accumulatedObject = [...items].reduce((accumulator, value) => {
			return {...accumulator, ...value};
		}, {});
		return Object.keys(accumulatedObject);
	}

	const buildTableColumn = (source: DataTableColumnSource, initialIndex: number): DataTableColumnInternal => {
		const input = typeof source === 'string' ? { key: source } : source;
		return {
			source,
			initialIndex,
			key: input.key,
			name: input.name || input.key,
			label: input.label || capitalizeFirstLetter(input.key),
			sortable: input.sortable || false,
			span: input.span || 1,
			fixedWidth: input.fixedWidth || 0,
			minWidth: input.minWidth || 0,
			align: input.align || 'left',
			tooltip: input.tooltip || '',
			disabled: input.disabled || false,
			type: input.type || 'text'
		}
	}

	const buildColumnsFromItems = (props: UseColumnProps) => {
		return getKeys(props.items).map((item, index) => buildTableColumn(item, index));
	}

	const buildNormalizedColumns = (props: UseColumnProps) => {
		return props.columns.map((item, index) => buildTableColumn(item, index));
	}

	const columnsComputed = computed(() =>
		(props.columns.length === 0) ? buildColumnsFromItems(props) : buildNormalizedColumns(props)
	)

	const gridTemplateColumns = computed(() => {
		return columnsComputed.value.map(column =>
			`minmax(${
				column.fixedWidth ? (column.fixedWidth + (typeof column.fixedWidth === "number" ? 'rem' : '')) : (column.minWidth ? (column.minWidth + (typeof column.minWidth === "number" ? 'rem' : '')) : '4rem')
			}, ${
				column.fixedWidth ? (column.fixedWidth + (typeof column.fixedWidth === "number" ? 'rem' : '')) : column.span + 'fr'
			})`
		).join(' ');
	})

	const gridTemplateColumnsComputed = computed(() => gridTemplateColumns.value || 'repeat(auto-fit, minmax(0, 1fr))');

	const rowsComputed = computed<DataTableRow[]>(() => [...props.items].map((source, index) => {
		return { initialIndex: index, source: toRaw(source), cells: columnsComputed.value.map(column => {
			return { rowIndex: index, rowData: toRaw(source), column, value: source?.[column.key] as string ?? '' } as DataTableCell;
		})}
	}));

	function useSafeVModel<P extends object, F, K extends keyof P>(props: P, fallbackValue: F, key?: K) {
		const instance = getCurrentInstance();
		const emit = instance?.emit;
		if (!key)
			key = 'modelValue' as K;
		const event = `update:${key.toString()}`;
		const fallback = ref<F>(fallbackValue)
		return computed<F>({
			get: () => {
				// @ts-expect-error fix later
				if (props[key] === undefined)
					return fallback.value;
				// @ts-expect-error fix later
				return props[key];
			},
			set: (value) => {
				// @ts-expect-error fix later
				if (props[key] === undefined) {
					// @ts-expect-error fix later
					fallback.value = value;
				}
				emit?.(event, value);
			},
		})
	}

	const sortBy = useSafeVModel(props, '', 'sortBy');
	const sortingOrder = useSafeVModel(props, null as DataTableSortingOrder, 'sortingOrder');

	const sortedRows = computed(() => {
		if (rowsComputed.value.length <= 1)
			return rowsComputed.value;
		const columnIndex = columnsComputed.value.findIndex( ({ key, sortable }) => sortBy.value === key && sortable, );
		const column = columnsComputed.value[columnIndex];
		if (!column)
			return rowsComputed.value;
		const sortingOrderRatio = sortingOrder.value === 'desc' ? -1 : 1
		return [...rowsComputed.value].sort((a, b) => {
			if (sortingOrder.value === null)
				return a.initialIndex - b.initialIndex;
			const firstValue = String(a.cells[columnIndex].value);
			const secondValue = String(b.cells[columnIndex].value);
			return sortingOrderRatio * (firstValue.localeCompare(secondValue));
		})
	})

	const cycleSorting = (value: DataTableSortingOrder) => {
		const index = DataTableSortingOptions.findIndex((sortingValue) => sortingValue === value)
		return (index !== -1)
			? DataTableSortingOptions[(index + 1) % DataTableSortingOptions.length]
			: DataTableSortingOptions[0];
	}

	function toggleSorting(column: DataTableColumnInternal) {
		if (column.key === sortBy.value) {
			sortingOrder.value = cycleSorting(sortingOrder.value);
		} else {
			sortBy.value = column.key;
			sortingOrder.value = DataTableSortingOptions[0];
		}
	}

	const filterOpenRef = toRef(props, 'filterOpen');

	const toggleFilterOpen = () => {
		filterOpenRef.value = !filterOpenRef.value;
	}

	const selectedItemsRaw = computed(() => (props.modelValue ?? []).map(i => toRaw(i)))
	const noRowsSelected = computed(() => (!sortedRows.value.some(isRowSelected)))
	const allRowsSelected = computed(() => (sortedRows.value.length === 0) ? false : sortedRows.value.every(isRowSelected));
	const someNotAllRowsSelected = computed(() => (sortedRows.value.length === 0) ? false : sortedRows.value.some(isRowSelected) && !allRowsSelected.value);

	function isRowSelected(row: DataTableRow) {
		return selectedItemsRaw.value.includes(row.source)
	}

	function selectAllRows() {
		emit('update:modelValue', [...sortedRows.value.map(row => row.source)]);
	}

	function unselectAllRows() {
		emit('update:modelValue', []);
	}

	function selectRow(row: DataTableRow) {
		emit('update:modelValue', selectedItemsRaw.value.concat(row.source))
	}

	function unselectRow(row: DataTableRow) {
		const index = selectedItemsRaw.value.indexOf(row.source)
		const newSelection = selectedItemsRaw.value.slice()
		newSelection.splice(index, 1)
		emit('update:modelValue', newSelection)
	}

	function toggleRowSelection(row: DataTableRow) {
		if (!props.selectable)
			return;
		if (isRowSelected(row))
			unselectRow(row)
		else
			selectRow(row)
	}

	function toggleBulkSelection() {
		if (allRowsSelected.value)
			unselectAllRows()
		else
			selectAllRows()
	}




	const clickedItemRaw = computed(() => (toRaw(props.clicked) ?? null));
	function isRowClicked(row: DataTableRow) {
		return row.source === clickedItemRaw.value;
	}
	function resetClickedRow() {
		if (props.allowUnclick)
			emit('update:clicked', null);
	}

	function toggleRowClick(row: DataTableRow) {
		if (!props.clickable)
			return;
		if (isRowClicked(row))
			resetClickedRow();
		else
			setClickedRow(row);
	}
	function setClickedRow(row: DataTableRow) {
		if (!props.clickable)
			return;
		emit('update:clicked', row.source);
	}

	onMounted(scrollToClickedElement);
	onUpdated(useDebounceFn(scrollToClickedElement, 250));

	function isInView(el: Element) {
		const box = el.getBoundingClientRect();
		return box.top < window.innerHeight && box.bottom >= 0;
	}

	function scrollToClickedElement() {
		if (!props.scrollIntoView)
			return;
		// TODO scrollIntoViewIfNeeded wird nicht von FF unterstützt as of 116
		const clickedElementHtml: any = document.querySelector('.data-table__tr--clicked');
		const scrollOptions: ScrollIntoViewOptions = { behavior: "smooth", block: "center" };
		if (clickedElementHtml) {
			if (typeof clickedElementHtml.scrollIntoViewIfNeeded === "function") clickedElementHtml.scrollIntoViewIfNeeded(scrollOptions)
			else if(!isInView(clickedElementHtml)) clickedElementHtml.scrollIntoView(scrollOptions);
		}
	}


	const showNoDataHtml = computed(() => (sortedRows.value.length === 0));

	const computedTableAttributes = computed(() => ({
		...Object.fromEntries(Object.entries(attrs).filter(([key]) => !["class", "style"].includes(key))),
	} as TableHTMLAttributes));

</script>

<style lang="postcss">
:root {
	--checkbox-width: 1.75rem;
}

.data-table {
	@apply flex flex-col;
	@apply w-full border border-black/25 dark:border-white/25 bg-white dark:bg-black border-b-0;
	@apply tabular-nums;
	@apply max-h-full overflow-auto;

	&__contrast-border {
		@apply border-0;
	}

	.app--sidebar-container & {
		@apply border-x-0;
	}

	&__panel-height {
		max-height: calc(var(--panel-height) - 4rem);
	}

	&__th,
	&__td {
		@apply flex h-full items-center border-r border-b border-black/25 dark:border-white/25 leading-none;
		padding: 0.1rem 0.5rem 0.1rem;
		line-height: 1;

		.data-table__contrast-border & {
			@apply border-black dark:border-white;
		}

		&__disabled,
		.data-table__tr__disabled & {
			@apply cursor-not-allowed relative text-black/50 dark:text-white/50;

			.table--with-background & {
				@apply text-black dark:text-black;
			}

			&:before {
				@apply absolute inset-0 bg-black/5 dark:bg-white/10 pointer-events-none;
				content: '';

				.table--with-background & {
					@apply bg-black/40;
				}
			}
		}

		&__disabled-light,
		.data-table__tr__disabled-light & {
			@apply relative text-black font-normal;

			.table--with-background & {
				@apply text-black dark:text-black;
			}

			&:before {
				@apply absolute inset-0 bg-black/5 pointer-events-none;
				content: '';

				.table--with-background & {
					@apply bg-black/20;
					box-shadow: 0 0 0 1px rgba(0,0,0,0.05);
				}
			}
		}

		&__no-padding {
			@apply p-0;
		}

		&__padding-sm {
			@apply px-0;
		}

		&__separate {
			@apply border-r-2;
			/*padding-right: 3px;
			margin-right: 3px;
			position: relative;

			&:before {
				content: "";
				position: absolute;
				top: -1px;
				bottom: -1px;
				right: -4px;
				width: 3px;
				background-color: #ffffff;
			}

			+ .data-table__td,
			+ .data-table__th {
				@apply border-l;
			}*/
		}

		&:last-child {
			@apply border-r-0;
		}

		&:first-child:not(.data-table__cell-select) {
			.app--sidebar-container & {
				@apply pl-3 2xl:pl-7 4xl:pl-8;
			}
		}

		&__align-left {
			.data-table__th-wrapper__sortable {
				@apply -ml-1;
			}
		}

		&__align-right {
			@apply justify-end text-right;

			.data-table__th-wrapper {
				@apply justify-end;
			}

			.data-table__th-wrapper__sortable {
				@apply -mr-1;
			}

			.text-input--headless {
				@apply text-right;
			}
		}

		&__align-center,
		&__row-actions {
			@apply justify-center text-center;

			.data-table__th-wrapper {
				@apply justify-center text-center;
			}

			.text-input--headless {
				@apply text-center;
			}
		}
	}

	&__tr {
		@apply relative;
		@apply grid;
		grid-template-columns: v-bind(gridTemplateColumnsComputed);
		min-width: fit-content;

		&--selected {
			@apply font-medium;
		}

		&--clicked {
			@apply bg-svws/5 dark:bg-svws/5 font-bold text-svws;

			.page--statistik & {
				@apply bg-violet-500/5 dark:bg-violet-500/10 text-violet-500;
			}
		}
	}

	&__selectable {
		.data-table__tr {
			grid-template-columns: var(--checkbox-width) v-bind(gridTemplateColumnsComputed);
		}
	}

	&__has-row-actions {
		.data-table__tr {
			grid-template-columns: v-bind(gridTemplateColumnsComputed) var(--checkbox-width);
		}
	}

	&__has-row-actions.data-table__selectable {
		.data-table__tr {
			grid-template-columns: var(--checkbox-width) v-bind(gridTemplateColumnsComputed) var(--checkbox-width);
		}
	}

	&__cell-select {
		@apply flex items-center justify-center p-0 bg-white dark:bg-black;
		@apply sticky left-0 z-10;

		.data-table__tfoot & {
			@apply relative;
		}

		.checkbox:before {
			content: '';
			@apply absolute inset-0;
		}
	}

	&__cell-checkbox {
		@apply m-0;
	}

	&__th-wrapper {
		@apply inline-flex flex-row items-center;
		@apply gap-0.5;
		@apply font-bold max-md:text-sm-bold;

		&__sortable {
			@apply w-auto py-1 pr-0 pl-1 -ml-0.5;
			@apply cursor-pointer select-none;

			&:hover {
				@apply bg-light dark:bg-white/10 rounded;
			}

			&-column,
			&-column:hover {
				@apply bg-svws/5 dark:bg-svws/10;

				.page--statistik & {
					@apply bg-violet-500/5 dark:bg-violet-500/10;
				}

				.data-table__th-title {
					@apply text-svws;

					.page--statistik & {
						@apply text-violet-500 dark:text-violet-400;
					}
				}
			}
		}
	}

	&__th-sorting {
		@apply basis-1;
	}

	&__thead {
		@apply w-min min-w-full bg-white dark:bg-black dark:text-white;
		@apply font-bold text-button;
		@apply border-b border-black/25 dark:border-white/25;
		@apply sticky top-0 z-20;

		.data-table__contrast-border & {
			@apply border border-black dark:border-white;
		}

		.data-table__tr:last-child {
			.data-table__td,
			.data-table__th {
				@apply border-b-0;
			}
		}

		.data-table__th,
		.data-table__td {
			.data-table__contrast-border & {
				@apply border-black dark:border-white;
			}
		}

		&__th,
		&__td {
			@apply text-left;
			@apply h-[2.75rem];

			> div:not(.data-table__th-wrapper__sortable),
			> div > span {
				@apply w-full;
			}
		}

		.data-table__th-title {
			@apply line-clamp-1;
			word-break: break-all;
			min-width: 1rem;
			line-height: 1.25;
			width: auto;
		}

		.tooltip-trigger {
			@apply line-clamp-1;
		}

		&__tr__compact {
			.data-table__th,
			.data-table__td {
				@apply h-[1.9rem];
			}
		}

		.data-table__thead__tr__compact {
			&:last-child {
				@apply border-b border-black/25 dark:border-white/25;

				.data-table__contrast-border &,
				.table--with-background & {
					@apply border-b-0;
				}
			}
		}
	}

	&__tbody {
		@apply h-auto flex flex-col;

		.data-table__contrast-border & {
			@apply border-black dark:border-white border-x;
		}

		&__tr {
			@apply min-h-[1.7rem];

			&:focus {
				@apply outline-none;
			}

			&.data-table__tr__collapsed {
				@apply hidden;
			}

			&.data-table__tr__expanded {
				@apply border-b border-black/25 dark:border-white/25 bg-light dark:bg-white/10;

				.data-table__td {
					@apply border-b-0 bg-white dark:bg-black;

					.checkbox {
						@apply ml-5;
					}

					.button + .checkbox {
						@apply ml-0;
					}

					&:first-child {
						@apply border-l pl-3;

						.button--icon {
							@apply -ml-1.5;
						}
					}
				}

				&.data-table__tr__depth-1 {
					.data-table__td:first-child {
						@apply ml-4;
					}
				}

				&.data-table__tr__depth-2 {
					.data-table__td:first-child {
						@apply ml-8;
					}
				}

				&.data-table__tr__depth-3 {
					.data-table__td:first-child {
						@apply ml-12;
					}
				}

				&.data-table__tr__depth-4 {
					.data-table__td:first-child {
						@apply ml-16;
					}
				}

				&.data-table__tr__depth-5 {
					.data-table__td:first-child {
						@apply ml-20;
					}
				}

				&.data-table__tr__depth-6 {
					.data-table__td:first-child {
						@apply ml-24;
					}
				}
			}
		}

		.drag-el {
			&:last-child {
				.data-table__td {
					@apply border-b-0;
				}
			}
		}

		&__td {
			.button-secondary {
				@apply border;
				font-size: 0.833rem;
				padding: 0.1em 0.7em;
			}
		}

		.data-table__td-content {
			@apply overflow-hidden text-ellipsis;
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 1;
			word-break: break-all;
			line-height: 1.25;
		}

		.text-input-component input {
			@apply h-auto w-full p-0;
		}
	}

	/*&.data-table--no-footer-scroll-bottom {
		background:
			linear-gradient(white 30%, rgba(255,255,255,0)),
			linear-gradient(rgba(255,255,255,0), white 70%) 0 100%,
			linear-gradient(to bottom, rgba(0,0,0,0.05), rgba(0,0,0,0)),
			linear-gradient(to top, rgba(0,0,0,0.25) 0, rgba(0,0,0,0.25) 1px, rgba(0,0,0,0.1) 2px, rgba(0,0,0,0.05) 30%, rgba(0,0,0,0)) 0 100%;
		background-repeat: no-repeat;
		background-color: transparent;
		background-size: 100% 0, 100% 0, 100% 0, 100% 2em;
		background-attachment: local, local, scroll, scroll;
	}*/

	&__tfoot {
		@apply w-max min-w-full max-w-fit bg-white dark:bg-black dark:text-white;
		@apply sticky bottom-0 z-20;
		@apply border-y border-black/25 dark:border-white/25 -mt-px;

		.data-table__contrast-border & {
			@apply border-black dark:border-white border-x;
		}

		&__tr {
			@apply w-full flex items-center;
			@apply h-[2.75rem];

			> * {
				@apply flex-shrink-0;
			}
		}

		.data-table__tr:last-child {
			.data-table__td,
			.data-table__th {
				@apply border-b-0;
			}
		}

		&__th,
		&__td {
			@apply border-transparent dark:border-transparent;
		}

		.data-table__cell-select {
			width: var(--checkbox-width);
		}

		&-actions {
			@apply flex flex-row items-center justify-end gap-1 overflow-visible ml-auto;

			.dropdown--button {
				@apply border-0;
			}
		}
	}

	&__row-actions {
		@apply px-0 justify-center overflow-hidden;

		&__popover {
			@apply border-0 m-0 !important;

			.popper {
				@apply text-sm flex flex-col gap-4 px-1 py-3 shadow dark:shadow-white ring-1 ring-black/10 dark:ring-white/10;
				margin-right: -0.5rem !important;

				.button {
					@apply w-full;
				}
			}
		}
	}

	&__clickable {
		.data-table__row-actions__popover {
			@apply opacity-0;

			&:hover,
			&:focus-within {
				@apply opacity-100;
			}
		}

		.data-table__tbody {
			&__tr {
				&:hover {
					@apply cursor-pointer;
				}

				&:not(.data-table__tr--clicked):hover {
					@apply bg-light dark:bg-white/5;
				}
			}
		}

		.data-table__tr--clicked {
			.data-table__row-actions__popover {
				@apply opacity-100;
			}
		}

		.data-table__tbody .button {
			&:focus:not(:focus-visible) {
				@apply outline-none ring-0;
			}

			&:focus:not(:hover) {
				@apply bg-transparent;
			}
		}
	}

	&__no-data {
		@apply border-black/10;

		.data-table__thead {
			@apply border-b-black/10;
		}

		.data-table__thead,
		.data-table__tfoot .data-table__cell-select {
			@apply text-black/25 pointer-events-none;
		}

		.data-table__tbody {
			@apply border-b border-b-black/10;
		}

		.data-table__tfoot {
			@apply border-y-black/10;

			.data-table__td,
			.data-table__th {
				@apply border-x-transparent;
			}
		}

		.data-table__th,
		.data-table__td {
			@apply border-black/10;
		}

		.data-table__th-wrapper__sortable-column {
			@apply opacity-25;
		}

		/* .data-table__tfoot {
			@apply hidden;
		} */

		.no-data {
			@apply border-0 py-2;
		}
	}

	.sorting-arrows {
		@apply flex justify-center border-0 items-center;
		@apply relative w-6 h-6 -my-3 transform scale-90;

		&__column {
			@apply scale-100;
		}

		&:focus:not(:focus-visible) {
			@apply outline-none ring-0;
		}

		&:focus:not(:hover) {
			@apply bg-transparent;
		}

		&__up,
		&__down {
			@apply absolute top-1 left-0.5 w-4 h-4;
			@apply opacity-25;
		}

		&__active {
			@apply opacity-100 text-svws;

			.page--statistik & {
				@apply text-violet-500;
			}
		}

		&__up {
			clip-path: polygon(0 0, 50% 0, 50% 100%, 0 100%);
		}

		&__down {
			clip-path: polygon(50% 0, 50% 100%, 100% 100%, 100% 0);
		}
	}

	&__filter {
		@apply flex justify-between flex-wrap;
		@apply pb-2 gap-2 mb-1 -mb-px;
		transition: box-shadow 0.15s ease-out;

		.app--sidebar & {
			@apply px-7 4xl:px-8;
		}

		&-open {
			/*box-shadow: inset 0 4px 6px 2px theme("colors.light");*/
		}

		&-simple {
			@apply flex;
		}

		.text-input--search {
			@apply py-0;

			input {
				@apply border-transparent py-1;
			}

			&:focus-within input {
				@apply border-black/25;
			}

			&.text-input--filled {
				.text-input--search-icon {
					@apply text-svws;

					.page--statistik & {
						@apply text-violet-500;
					}
				}

				&:focus-within input {
					@apply border-svws;

					.page--statistik & {
						@apply border-violet-500;
					}
				}

				input {
					@apply bg-svws/5 text-svws border-transparent;

					.page--statistik & {
						@apply bg-violet-500/5 text-violet-500;
					}
				}
			}

			&:hover:not(:focus-within):not(.text-input-filled) input {
				@apply border-black/25;
			}
		}

		.toggle--filter {
			@apply h-full;
		}

		&__fields {
			@apply w-full grid gap-3;
			@apply pt-5 pb-2;
			grid-template-columns: repeat(auto-fit, minmax(11rem, 1fr));

			&.-order-1 {
				@apply pt-0;
			}
		}
	}

	.complex-filters &__filter {
		&__fields {
			@apply md:grid-cols-4;
		}
	}

	&--tab-bar {
		@apply border-0 bg-light rounded-lg p-1;

		.data-table__tbody {
			@apply bg-none bg-transparent overflow-visible space-y-1;
		}

		.data-table__tr {
			@apply rounded-md inline-flex px-1 py-1.5;
		}

		.data-table__tr--clicked {
			@apply bg-white text-svws;

			.page--statistik & {
				@apply text-violet-500;
			}
		}

		.data-table__td,
		.data-table__th,
		.data-table__thead {
			@apply bg-transparent border-0 font-bold;
		}
	}

	&__style-navigation {
		&,
		.data-table__thead,
		.data-table__td,
		.data-table__th {
			@apply border-none;
		}

		.data-table__tbody {
			@apply flex flex-col gap-0.5;

			.data-table__tr {
				@apply rounded w-fit;
			}
		}
	}
}

.table--with-background {
	@apply text-black;
}
</style>
