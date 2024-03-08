<template>
	<div v-if="$slots.search || $slots.filter || $slots.filterAdvanced || toggleColumns" class="svws-ui-table-filter" :class="{'svws-open': $slots.filter && isFilterOpen}">
		<div class="flex w-full gap-0.5 overflow-hidden">
			<div class="flex-grow" v-if="$slots.search">
				<slot name="search" />
			</div>
			<div v-if="$slots.filter" class="flex flex-shrink-0 items-center gap-1" :class="{'mr-5': $slots.filterAdvanced || toggleColumns, 'ml-2': $slots.search, 'ml-auto': !$slots.search}">
				<slot name="filter" />
			</div>
			<div v-if="toggleColumns" :class="{'ml-auto': !$slots.filter}">
				<svws-ui-tooltip :indicator="false" :hover="false" :show-arrow="false" position="top" class="h-full">
					<svws-ui-button type="transparent" class="h-full">
						<i-ri-table-line />
						<template #badge v-if="hiddenColumns.size">
							<span />
						</template>
						<span>Daten</span>
					</svws-ui-button>
					<template #content>
						<ul class="min-w-[10rem] flex flex-col gap-0.5 pt-1">
							<li v-for="(column, index) in columns" :key="index">
								<template v-if="typeof column !== 'string'">
									<svws-ui-checkbox :model-value="!hiddenColumns.has(column.key)" :disabled="!column.toggle" @update:model-value="ok => updateHiddenColumns(column.key, ok)">
										{{ column.label }}
									</svws-ui-checkbox>
								</template>
							</li>
						</ul>
					</template>
				</svws-ui-tooltip>
			</div>
			<div v-if="$slots.filterAdvanced && filterHide" class="flex flex-shrink-0" :class="{'ml-auto': !$slots.filter && !toggleColumns}">
				<svws-ui-button type="transparent" @click="toggleFilterOpen" class="h-full" :class="{'opacity-50 hover:opacity-100 focus-visible:opacity-100': !filtered && isFilterOpen}">
					<template #badge v-if="filtered">
						<span />
					</template>
					<template v-if="!filterReset || !filtered">
						<i-ri-filter-line v-if="!isFilterOpen" />
						<i-ri-arrow-up-s-line v-else />
					</template>
					<span>Filter</span>
				</svws-ui-button>
				<svws-ui-button v-if="filterReset && filtered" type="icon" @click="filterReset" class="h-full" title="Filter zurücksetzen">
					<i-ri-filter-off-line />
				</svws-ui-button>
			</div>
		</div>
		<div v-if="$slots.filterAdvanced && isFilterOpen" class="svws-ui-table-filter--advanced">
			<slot name="filterAdvanced" />
		</div>
	</div>
	<div class="svws-ui-table" role="table" aria-label="Tabelle" v-bind="$attrs" style="scrollbar-gutter: stable;"
		:class="{
			'svws-clickable': clickable && (typeof noData !== 'undefined' ? !noData : !noDataCalculated),
			'svws-selectable': selectable,
			'svws-has-selection': selectable && selectedItemsRaw.length > 0,
			'svws-sortable': sortByAndOrder.key,
			'svws-no-data': typeof noData !== 'undefined' ? noData : noDataCalculated,
			'svws-type-navigation': type === 'navigation',
			'svws-type-grid': type === 'grid',
			'svws-has-background': hasBackground,
			'overflow-visible': !scroll,
			'overflow-auto': scroll,
		}">
		<div v-if="!disableHeader" class="svws-ui-thead" role="rowgroup" aria-label="Tabellenkopf">
			<slot name="header" :all-rows-selected="allRowsSelected" :toggle-all-rows="toggleBulkSelection" :columns="columnsComputed">
				<div role="row" class="svws-ui-tr">
					<div v-if="selectable" class="svws-ui-td svws-align-center" role="columnheader" aria-label="Alle auswählen">
						<svws-ui-checkbox :model-value="allRowsSelected" :indeterminate="someNotAllRowsSelected" @update:model-value="toggleBulkSelection" :disabled="typeof noData !== 'undefined' ? noData : noDataCalculated" />
					</div>
					<div v-for="column in columnsComputed" class="svws-ui-td" role="columnheader" :key="column.key" :title="column.tooltip ? '' : column.label" @click.exact="column.sortable && toggleSorting(column)" @keyup.enter="column.sortable && toggleSorting(column)"
						:class="[
							`svws-column-key--${column.key}`, `svws-align-${column.align}`,
							{
								'svws-disabled': column.disabled,
								'svws-sortable-column': column.sortable,
								'svws-active': column.sortable && (internalSortByAndOrder.key === column.name) && (typeof internalSortByAndOrder.order === 'boolean'),
								'svws-divider': column.divider,
							}]" :tabindex="column.sortable ? 0 : -1">
						<slot :name="`header(${column.key})`" :column="column">
							<svws-ui-tooltip v-if="column.tooltip">
								<span class="line-clamp-1 break-all leading-tight">{{ column.label }}</span>
								<template #content>
									{{ column.tooltip }}
								</template>
							</svws-ui-tooltip>
							<template v-else>
								<span class="line-clamp-1 break-all leading-tight">{{ column.label }}</span>
							</template>
						</slot>
						<span v-if="column.sortable" class="svws-sorting-icon" :class="{'-order-1': column.align === 'right'}">
							<i-ri-arrow-up-down-line class="svws-sorting-asc" :class="{'svws-active': ((internalSortByAndOrder.key === column.name) || (sortByMulti?.has(column.name))) && ((internalSortByAndOrder.order === true) || (sortByMulti?.get(column.name) === true))}" />
							<i-ri-arrow-up-down-line class="svws-sorting-desc" :class="{'svws-active': ((internalSortByAndOrder.key === column.name) || (sortByMulti?.has(column.name))) && ((internalSortByAndOrder.order === false) || (sortByMulti?.get(column.name) === false))}" />
						</span>
					</div>
				</div>
			</slot>
		</div>
		<div class="svws-ui-tbody" role="rowgroup" aria-label="Tabelleninhalt">
			<slot name="body" :rows="sortedRows">
				<div v-if="noDataCalculated && noData" class="svws-ui-tr" role="row">
					<div class="col-span-full svws-no-data-text svws-ui-td" role="cell">
						<span class="py-2 font-normal">
							<slot name="noData">{{ noDataText }}</slot>
						</span>
					</div>
				</div>
				<template v-for="(row, index) in sortedRows">
					<slot name="rowCustom" :row="row.source">
						<div class="svws-ui-tr" role="row" :key="`table-row_${row}_${index}`" @click.exact="toggleRowClick(row)" :ref="el => itemRefs.set(index, el)"
							:class="{ 'svws-selected': isRowSelected(row), 'svws-clicked': isRowClicked(row), }">
							<slot name="row" :row="row.source">
								<div v-if="selectable" class="svws-ui-td svws-align-center" role="cell" :key="`selectable__${row}_${index}`">
									<input type="checkbox" :checked="isRowSelected(row)" @input="toggleRowSelection(row)" @click.stop>
								</div>
								<slot name="rowSelectable" :row="row.source">
									<div class="svws-ui-td" role="cell" v-for="cell in row.cells" :key="`table-cell_${cell.column.key + cell.rowIndex}`"
										:class="[
											`svws-column-key--${cell.column.key}`, `svws-align-${cell.column.align}`,
											{
												'svws-disabled': cell.column.disabled,
												'svws-no-value': cell.value === null || cell.value === undefined || cell.value === '',
												'svws-divider': cell.column.divider,
											}]">
										<slot v-if="`cell(${cell.column.key})` in $slots" :name="`cell(${cell.column.key})`" v-bind="cell" />
										<slot v-else name="cell" v-bind="cell">
											<svws-ui-text-input v-if="row.isEditing" v-model="cell.value" :headless="true" :type="(cell.column.type)" @click.stop="setClickedRow(row) " />
											<template v-else-if="cell.value">
												{{ cell.column.type === 'date' ? (new Date(cell.value).toLocaleDateString('de', {day: '2-digit', month: '2-digit', year: 'numeric', timeZone: 'Europe/Berlin'})) : cell.value }}
											</template>
											<template v-else>
												<span class="text-black/25 dark:text-white/25">—</span>
											</template>
										</slot>
									</div>
								</slot>
							</slot>
						</div>
					</slot>
				</template>
			</slot>
		</div>
		<div v-if="$slots.dataFooter" class="svws-ui-tfoot--data" role="rowgroup">
			<slot name="dataFooter" />
		</div>
		<div v-if="!disableFooter && (selectable || $slots.footer || $slots.actions || count)" class="svws-ui-tfoot" role="rowgroup" aria-label="Fußzeile">
			<slot name="footer" :all-rows-selected="allRowsSelected" :toggle-all-rows="toggleBulkSelection" :rows="sortedRows">
				<div class="svws-ui-tr" role="row">
					<div v-if="selectable" class="svws-ui-td svws-align-center" role="columnheader" aria-label="Alle auswählen">
						<svws-ui-checkbox :model-value="allRowsSelected" :indeterminate="someNotAllRowsSelected" @update:model-value="toggleBulkSelection" :disabled="typeof noData !== 'undefined' ? noData : noDataCalculated" />
					</div>
					<div v-if="count" class="text-sm svws-ui-td" role="cell">
						<span v-if="allRowsSelected && modelValue" class="font-medium">
							Alle {{ modelValue.length }} ausgewählt
						</span>
						<span v-else-if="someNotAllRowsSelected && modelValue" class="font-medium">
							{{ modelValue.length }}<span class="opacity-50">/{{ sortedRows.length }}</span> ausgewählt
						</span>
						<span v-else class="opacity-50">
							{{ sortedRows.length === 1 ? '1 Eintrag': `${sortedRows.length} Einträge` }}
						</span>
					</div>
					<div v-if="$slots.actions" class="flex-grow justify-end svws-ui-td" role="cell">
						<slot name="actions" />
					</div>
				</div>
			</slot>
		</div>
	</div>
</template>

<script lang="ts" setup generic="DataTableItem extends Record<string, any>">

	import type { DataTableColumn, InputType, SortByAndOrder } from "../types";
	import { computed, useAttrs, toRef, toRaw, ref, watch, nextTick } from "vue";

	type DataTableColumnSource = DataTableColumn | string

	type DataTableColumnInternal = {
		[key: string]: unknown
		source: DataTableColumnSource
		initialIndex: number
		key: string
		name: string
		label: string
		sortable: boolean
		span: number
		fixedWidth: string | number
		minWidth: string | number
		align: 'left' | 'center' | 'right'
		tooltip: string
		disabled: boolean
		type: InputType
		divider: boolean
		toggle: boolean
	}

	type DataTableCell = {
		rowIndex: number
		rowData: DataTableItem
		column: DataTableColumnInternal
		value: any
	}

	type DataTableRow = {
		initialIndex: number
		source: DataTableItem
		cells: DataTableCell[]
		isEditing?: boolean
	}

	type UseColumnProps = {
		columns: DataTableColumnSource[];
		items: Iterable<DataTableItem>;
	}

	defineOptions({ inheritAttrs: false });

	const props = withDefaults(
		defineProps<{
			columns?: DataTableColumnSource[];
			items?: Iterable<DataTableItem> | DataTableItem[];
			modelValue?: DataTableItem[];
			uniqueKey?: string;
			clickable?: boolean;
			allowUnclick?: boolean;
			clicked?: DataTableItem | undefined | null;
			selectable?: boolean;
			disableHeader?: boolean;
			disableFooter?: boolean;
			count?: boolean;
			noData?: boolean;
			noDataText?: string;
			scrollIntoView?: boolean;
			type?: 'table' | 'navigation' | 'grid';
			hasBackground?: boolean;
			filterOpen?: boolean;
			filterHide?: boolean;
			filtered?: boolean;
			filterReset?: () => any;
			sortByAndOrder?: SortByAndOrder;
			sortByMulti?: Map<string, (boolean | null)>;
			toggleColumns?: boolean;
			scroll?: boolean;
		}>(),
		{
			columns: () => [],
			items: () => [],
			modelValue: undefined,
			uniqueKey: undefined,
			disableHeader: false,
			clickable: false,
			allowUnclick: false,
			clicked: undefined,
			selectable: false,
			disableFooter: false,
			count: false,
			noData: undefined,
			noDataText: "Keine Einträge gefunden.",
			scrollIntoView: undefined,
			type: 'table',
			hasBackground: false,
			filterOpen: false,
			filterHide: true,
			filtered: false,
			filterReset: undefined,
			sortByAndOrder: () => ({key: null, order: null}),
			sortByMulti: undefined,
			toggleColumns: false,
			scroll: false,
		}
	);

	const emit = defineEmits<{
		"update:modelValue": [items: any[]];
		"update:sortByAndOrder": [obj: SortByAndOrder];
		"update:clicked": [items: any | null];
		"update:filterOpen": [open: boolean];
	}>();

	const attrs = useAttrs();
	const itemRefs = ref(new Map());

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
			type: input.type || 'text',
			divider: input.divider || false,
			toggle: input.toggle || false
		}
	}

	const buildColumnsFromItems = (props: UseColumnProps) => {
		return getKeys(props.items).map((item, index) => buildTableColumn(item, index));
	}

	const buildNormalizedColumns = (props: UseColumnProps) => {
		return props.columns.map((column, index) => buildTableColumn(column, index)).filter(column => !hiddenColumns.value.has(column.key));
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
			return { rowIndex: index, rowData: toRaw(source), column, value: source[column.key] ?? '' };
		})}
	}));

	const sortedRows = computed(() => {
		if (rowsComputed.value.length < 0 || props.sortByMulti !== undefined)
			return rowsComputed.value;
		const columnIndex = columnsComputed.value.findIndex( ({ key, sortable }) => internalSortByAndOrder.value.key === key && sortable, );
		const column = columnsComputed.value[columnIndex];
		if (!column)
			return rowsComputed.value;
		const sortingOrderRatio = internalSortByAndOrder.value.order === false ? -1 : 1
		return [...rowsComputed.value].sort((a, b) => {
			if (internalSortByAndOrder.value.order === null)
				return a.initialIndex - b.initialIndex;
			const firstValue = String(a.cells[columnIndex].value);
			const secondValue = String(b.cells[columnIndex].value);
			return sortingOrderRatio * (firstValue.localeCompare(secondValue));
		})
	})

	const cycleSorting = (value: boolean | null | undefined) => {
		if (value === null || value === undefined)
			return true;
		if (value === true)
			return false;
		return null;
	}

	// eslint-disable-next-line vue/no-setup-props-destructure
	const internalSortByAndOrder = ref<SortByAndOrder>(props.sortByAndOrder)

	function toggleSorting(column: DataTableColumnInternal) {
		const neu: SortByAndOrder = {key: column.key, order: true};
		if (column.key === internalSortByAndOrder.value.key)
			neu.order = cycleSorting(internalSortByAndOrder.value.order);
		else if (props.sortByMulti !== undefined){
			const alt = props.sortByMulti.get(column.key);
			neu.order = cycleSorting(alt);
		}
		internalSortByAndOrder.value = neu;
		emit('update:sortByAndOrder', neu);
	}

	const filterOpenProp = toRef(props, 'filterOpen');
	const isFilterOpen = ref(filterOpenProp.value);

	const toggleFilterOpen = () => {
		isFilterOpen.value = !isFilterOpen.value;
		emit('update:filterOpen', isFilterOpen.value);
	}

	const selectedItemsRaw = computed(() => (props.modelValue ?? []).map(i => toRaw(i)))
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
	const clickedItemIndex = ref<number|undefined>();

	function isRowClicked(row: DataTableRow) {
		const is = row.source === clickedItemRaw.value;
		if (!is)
			return false;
		clickedItemIndex.value = sortedRows.value.indexOf(row);
		return true;
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

	watch(() => props.items, () => nextTick(scrollToClickedElement));

	function isInView(el: Element) {
		const box = el.getBoundingClientRect();
		return box.top < window.innerHeight && box.bottom >= 0;
	}

	function scrollToClickedElement() {
		if (!props.scrollIntoView || clickedItemIndex.value === undefined)
			return;
		// TODO scrollIntoViewIfNeeded wird nicht von FF unterstützt as of 116
		const clickedElementHtml = itemRefs.value.get(clickedItemIndex.value);
		const scrollOptions: ScrollIntoViewOptions = { behavior: "auto", block: "center" };
		if (clickedElementHtml) {
			if (typeof clickedElementHtml.scrollIntoViewIfNeeded === "function")
				clickedElementHtml.scrollIntoViewIfNeeded(scrollOptions);
			else if(!isInView(clickedElementHtml))
				clickedElementHtml.scrollIntoView(scrollOptions);
		}
	}

	const noDataCalculated = computed(() => (sortedRows.value.length === 0));

	const hiddenColumns = ref<Set<string>>(new Set());

	function updateHiddenColumns(columnKey: string, ok: any) {
		ok ? hiddenColumns.value.delete(columnKey) : hiddenColumns.value.add(columnKey);
	}

</script>

<style lang="postcss">
.svws-ui-table {
	@apply flex max-h-full w-full flex-col bg-white tabular-nums dark:bg-black;
  @apply border-black/25 dark:border-white/25;
	--checkbox-width: 1.75rem;
	--background-color: rgb(var(--color-white));

  & + & {
    @apply mt-10;
  }

	&.svws-has-background {
	  @apply text-black dark:text-black;
	  /*@apply rounded-lg border border-black/50 border-b-0 ring-4 ring-light dark:ring-white/5;*/
	}

	&.svws-no-data,
	&.svws-no-data .svws-ui-thead,
	&.svws-no-data .svws-ui-tbody,
	&.svws-no-data .svws-ui-tfoot,
	&.svws-no-data .svws-ui-td {
		@apply border-black/10 dark:border-white/10;
	}

	.app--sidebar-container & {
		@apply border-x-0;
	}

  .content-card &:not(.svws-no-mx),
  .svws-table-offset & {
    @apply -mx-3 w-auto;

    .svws-ui-td:first-child {
      @apply pl-3;
    }
  }

  .content-card &:not(.svws-no-mx):not(.svws-has-background),
  .svws-table-offset &:not(.svws-has-background) {
    .svws-ui-td:last-child {
      @apply pr-3;
    }
  }

  .app-layout--aside-container & {
    @apply -mx-6 w-auto;

    .svws-ui-td:first-child {
      @apply pl-6;
    }

    .svws-ui-td:last-child {
      @apply pr-6;
    }
  }

  .svws-ui-badge {
  	&.svws-has-background & {
      @apply border-transparent;
    }
  }
}

.svws-ui-thead,
.svws-ui-tbody,
.svws-ui-tfoot {
	@apply min-w-fit;
}

.svws-ui-thead,
.svws-ui-tfoot,
.svws-ui-tfoot--data {
	@apply sticky bg-white dark:bg-black dark:text-white;
	@apply border-black/25 dark:border-white/25;

	.svws-ui-table.svws-has-background & {
		@apply border-black/50 dark:border-white/50;
	}

	.svws-ui-tr:last-child .svws-ui-td {
		@apply border-b-0;
	}
}

.svws-ui-thead {
	@apply top-0 z-20;
	@apply border-b;

  .svws-ui-tr:not(:last-child) .svws-ui-td {
    @apply border-t border-t-transparent;
  }

  .svws-ui-badge {
    @apply text-button font-bold;
  }

  .svws-no-data & {
    @apply pointer-events-none text-black/25 dark:text-white/25;
  }

	.svws-ui-td {
		@apply items-center pt-0 text-button;

		&.svws-sortable-column {
			@apply cursor-pointer select-none focus:outline-none;

			.tooltip-trigger {
				@apply cursor-pointer;
			}

			&:not(.svws-active):hover {
				.svws-sorting-icon {
					@apply bg-light dark:bg-white/10;

					.svws-sorting-asc,
					.svws-sorting-desc {
							@apply opacity-100;
					}
				}
			}

			&:focus-visible {
				.svws-sorting-icon {
					@apply ring-2 ring-black/25 ring-offset-1;
				}
			}

			&.svws-active,
			&.svws-active:hover {
				@apply text-svws;

				.svws-sorting-icon {
					@apply bg-svws/5 dark:bg-svws/10;

					.page--statistik & {
						@apply bg-violet-500/5 text-violet-500 dark:bg-violet-500/10;
					}
				}
			}
		}

		.svws-sorting-icon {
			@apply relative -my-2 inline-flex h-5 w-5 flex-shrink-0 items-center justify-center rounded-md border-0;

			.svws-sorting-asc,
			.svws-sorting-desc {
				@apply absolute h-4 w-4 opacity-25 top-0.5 left-0.5;

				&.svws-active {
					@apply opacity-100 text-svws;

					.page--statistik & {
						@apply text-violet-500;
					}
				}
			}

			.svws-sorting-asc {
				clip-path: polygon(0 0, 50% 0, 50% 100%, 0 100%);
			}

			.svws-sorting-desc {
				clip-path: polygon(50% 0, 50% 100%, 100% 100%, 100% 0);
			}
		}

		.tooltip-trigger {
			@apply inline-flex h-full items-center line-clamp-1;
		}
	}
}

.svws-ui-tbody {
	@apply flex h-auto flex-col;

	.svws-clickable & {
		.svws-ui-tr {
			@apply cursor-pointer;

			&:not(.svws-clicked):hover {
				@apply bg-black/5 dark:bg-white/5;
			}
		}
	}

  .button {
    @apply -my-1 h-[1.4rem];
  }

  .tooltip-trigger {
    margin-top: -0.1rem;
    margin-bottom: -0.1rem;

    .button {
      @apply -my-0.5;
    }
  }

  .svws-ui-checkbox {
    @apply my-0;
  }

  .svws-ui-tr {
	  .svws-ui-table.svws-has-background & {
      background-color: var(--background-color);
    }
  }

  .svws-ui-td {
    .text-input-component {
      @apply w-full h-auto -my-0.5;
    }

    .text-input--control {
      @apply border-none;
    }
  }

	.svws-has-selection & {
		.svws-ui-tr:not(.svws-selected):not(.svws-clicked) {
			@apply text-black/50 dark:text-white/50;

      .svws-ui-badge {
        @apply text-black opacity-50 dark:text-white;
      }
		}
	}
}

.svws-ui-tfoot,
.svws-ui-tfoot--data {
	@apply sticky z-10;
	@apply -mt-px border-y;
  	@apply -bottom-px;

	.svws-ui-table.overflow-auto & {
		@apply bottom-0;
	}
}

.svws-ui-tfoot {
	.svws-ui-tr {
		@apply flex w-full items-center;

		.svws-ui-td {
			@apply items-center border-transparent dark:border-transparent;
		}
	}

	.svws-selectable & {
		.svws-ui-td:first-child {
			width: var(--checkbox-width);
		}
	}
}

.svws-ui-tr {
	@apply relative grid text-base min-h-[1.7rem] focus:outline-none;
	grid-template-columns: v-bind(gridTemplateColumnsComputed);
	min-width: fit-content;

	&.svws-selected {
		@apply font-medium;
	}

	&.svws-clicked {
		@apply font-bold bg-svws/5 text-svws dark:bg-svws/5;

		.svws-ui-badge {
		  @apply border-black/25 dark:border-white/25 font-bold;
		}

		.page--statistik & {
			@apply bg-violet-500/5 text-violet-500 dark:bg-violet-500/10;
		}

    .svws-ui-tr:not(.svws-clicked) {
      @apply bg-white font-medium text-black dark:text-white;
    }
	}

	.svws-selectable & {
		grid-template-columns: var(--checkbox-width) v-bind(gridTemplateColumnsComputed);
	}

	.svws-ui-tfoot & {
		@apply min-h-[2.25rem] h-auto;
	}

	.svws-ui-thead & {
		@apply min-h-[1.9rem] h-auto;

		.svws-ui-td {
			@apply py-2;
		}

	}
}

.svws-ui-td {
	@apply flex items-start gap-1 overflow-hidden border-b border-black/25 leading-none dark:border-white/25;
	padding: 0.3rem 0.25rem;

	&.svws-no-padding {
		@apply p-0;
	}

	.svws-no-padding {
		margin: -0.1rem -0.25rem;
	}

	&.svws-divider {
		@apply border-r;

    + .svws-ui-td:not(.svws-align-center) {
      @apply pl-2;

      .svws-ui-badge {
        @apply -ml-1.5;
      }
    }
	}

	.svws-ui-table.svws-has-background & {
		@apply border-black/50 dark:border-white/50;
	}

	&.svws-align-right {
		@apply justify-end text-right;
	}

	&.svws-align-center {
		@apply justify-center text-center;
	}

	&.svws-disabled,
	.svws-disabled &,
	&.svws-disabled-soft,
	.svws-disabled-soft & {
		@apply relative cursor-default text-black/50 dark:text-white/50;

		.svws-ui-table.svws-has-background & {
			@apply text-black/75 dark:text-black/75;
		}

		&:before {
			@apply pointer-events-none absolute inset-0 bg-black/10 dark:bg-white/10 border border-black/5;
			content: '';

			.svws-ui-table.svws-has-background & {
				@apply bg-black/40;
			}
		}
	}

	&.svws-disabled-soft,
	.svws-disabled-soft & {
		@apply text-black dark:text-white;

		.svws-has-background & {
			@apply text-black/80 dark:text-black/80;

			.button {
				@apply text-black dark:text-black;
			}
		}

		&:before {
			.svws-ui-table.svws-has-background & {
				@apply bg-black/20;
				box-shadow: 0 0 0 1px rgba(0,0,0,0.05);
			}
		}
	}

	.svws-ui-tr.svws-disabled-soft & {
		&:before {
			@apply border-transparent;
		}
	}

	.app--sidebar-container .svws-ui-table:not(.svws-selectable) &:first-child,
	.svws-ui-table.svws-selectable &.svws-no-data-text {
		@apply pl-3 2xl:pl-7 4xl:pl-8;
	}

  .app--sidebar-container &.svws-align-right:last-child {
    @apply pr-3;
  }

	.drag-el:last-child & {
		@apply border-b-0;
	}

	.button {
		font-size: 0.833rem;
		padding: 0.1em 0.7em;
	}

	.text-input-component input {
		@apply h-auto w-full py-0;
	}

  input[type="date"] {
    @apply px-0;
  }
}

.svws-ui-table {
  /*&.svws-type-collapsible*/

  .svws-ui-tr {
    .svws-ui-tr {
      @apply col-span-full last:pb-4;

      > .svws-ui-td {
        @apply border-dashed border-transparent;

		  &.svws-divider {
			  @apply border-solid border-r-black/25 dark:border-r-white/25;
		  }
      }
    }

    > .svws-ui-tr:not(:last-child) {
      @apply border-b border-dashed border-black/50 dark:border-white/50;
    }
  }

  .svws-toggle-collapse {
    @apply inline-flex h-5 w-5 items-center justify-center rounded -my-0.5 mx-0.5;

    &:hover,
    &:focus-visible {
      @apply bg-black/10 dark:bg-white/10;
    }
  }
}

.svws-ui-table.svws-type-navigation {
  @apply mx-0;

	&,
	.svws-ui-thead,
	.svws-ui-tbody,
	.svws-ui-tfoot,
	.svws-ui-td {
		@apply border-none;
	}

	.svws-ui-tbody {
		@apply flex flex-col gap-[0.1rem];

		.svws-ui-tr {
			@apply w-fit rounded;
		}
	}
}

.svws-ui-table.svws-type-grid {
  @apply border-x border-t border-black/25 dark:border-white/25;

  &.svws-has-background {
    @apply border-black/50 dark:border-black/50;
  }

  .svws-ui-thead,
  .svws-ui-tbody {
    .svws-ui-td:not(:last-child) {
      @apply border-r border-black/25 dark:border-white/25;
    }

    .svws-ui-td:not(.svws-align-center) {
      @apply pl-1.5;
    }

    .svws-ui-td:not(.svws-align-center):not(:last-child) {
      @apply pr-1.5;
    }

    .svws-ui-td.svws-divider {
      @apply border-r-2;
    }
  }

  &.svws-has-background {
    .svws-ui-thead,
    .svws-ui-tbody {
      .svws-ui-td:not(:last-child) {
        @apply border-black/50 dark:border-black/50;
      }
    }
  }

  .content-card &:not(.svws-no-mx) .svws-ui-td:first-child,
  .svws-table-offset & .svws-ui-td:first-child {
    @apply pl-3;
  }

  .content-card &.svws-selectable:not(.svws-no-mx) .svws-ui-td:first-child,
  .svws-table-offset &.svws-selectable .svws-ui-td:first-child {
    @apply pl-1;
  }
}

.svws-ui-table-filter {
  @apply mb-5;

  .svws-ui-checkbox {
    @apply font-normal text-button;

    .svws-ui-checkbox--label {
      @apply pt-0.5;
    }
  }

  .button--badge {
    @apply left-auto mt-0 h-2 w-2 p-0 bg-svws right-0.5 top-1.5;
  }
}

.svws-ui-table-filter--advanced {
  @apply grid grid-cols-1 gap-2 pt-5 sm:grid-cols-2;

  .svws-ui-toggle {
    @apply mt-2;
  }
}

.svws-fachwahlen--has-selection .svws-ui-tbody .svws-ui-td:not(.svws-selected) {
	@apply bg-white/50 text-black/50 dark:text-black/50 font-normal;
}

.svws-fachwahlen--has-selection .svws-ui-td.svws-selected {
	@apply font-bold;
}
</style>
