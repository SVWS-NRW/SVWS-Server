<template>
	<div v-if="$slots.search || $slots.filter">
		<div class="data-table__filter" :class="{'data-table__filter-open': $slots.filter && filterOpenRef}">
			<div class="flex w-full gap-2">
				<div class="flex-grow" v-if="$slots.search">
					<slot name="search" />
				</div>
				<div v-if="$slots.filterSimple">
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
						class="justify-center">
						<i-ri-filter-off-line />
						Zurücksetzen
					</svws-ui-button>
				</template>
			</div>
		</div>
	</div>
	<div role="table" aria-label="Tabelle" class="data-table"
		:class="{'data-table__selectable': selectable, 'data-table__sortable': sortBy, 'data-table__clickable': clickable, 'data-table__no-data': typeof noData !== 'undefined' ? noData : showNoDataHtml, 'data-table__has-row-actions': rowActions || manualRowActions, 'data-table__collapsible': collapsible, 'data-table--tab-bar': tabBarDesign}"
		v-bind="computedTableAttributes">
		<div role="rowgroup" aria-label="Tabellenkopf" class="data-table__thead">
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
						:class="`data-table__th__${column.key} data-table__th__align-${column.align}`"
						@click.exact="column.sortable && toggleSorting(column)">
						<div class="data-table__th-wrapper"
							:class="{'data-table__th-wrapper__sortable': column.sortable, 'data-table__th-wrapper__sortable-column': sortBy === column.name && sortingOrder}"
							:title="column.label">
							<span class="data-table__th-title">
								<slot :name="`header(${column.key})`"
									:column="column">
									{{ column.label }}
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
						<i-ri-settings3-line />
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
						<svws-ui-checkbox class="data-table__cell-checkbox"
							:model-value="isRowSelected(row)"
							@update:model-value="toggleRowSelection(row)"
							@click.stop />
					</div>
					<div role="cell" v-for="cell in row.cells"
						:key="`table-cell_${cell.column.key + cell.rowIndex}`"
						class="data-table__td"
						:class="`data-table__td__${cell.column.key} data-table__td__align-${cell.column.align}`">
						<slot v-if="`cell(${cell.column.key})` in $slots"
							:name="`cell(${cell.column.key})`"
							v-bind="cell" />
						<slot v-else name="cell" v-bind="cell">
							<svws-ui-text-input v-if="row.isEditing"
								v-model="cell.value"
								:headless="true"
								@update:value="(value: string) => cell.value = value"
								@click.stop="setClickedRow(row) " />
							<span v-else class="data-table__td-content" :title="cell.value">
								{{ cell.value }}
							</span>
						</slot>
					</div>
					<div v-if="rowActions" class="data-table__row-actions data-table__td">
						<svws-ui-popover :hover="false"
							:disable-click-away="false"
							:show-delay="0"
							placement="auto-start"
							:arrow="false"
							class="data-table__row-actions__popover"
							:context="true"
							@click.stop>
							<template #trigger>
								<svws-ui-button type="icon" size="small" class="cursor-context-menu">
									<svws-ui-icon>
										<i-ri-more2-fill />
									</svws-ui-icon>
								</svws-ui-button>
							</template>
							<template #content>
								<div v-for="action in rowActions"
									:key="action.action">
									<svws-ui-button class="action-item"
										type="transparent"
										size="small"
										@click="() => {rowExecute ? rowExecute(action.action, row) : null}">
										{{ action.label }}
									</svws-ui-button>
								</div>
							</template>
						</svws-ui-popover>
					</div>
				</div>
			</slot>
		</div>
		<div role="rowgroup" aria-label="Fußzeile" class="data-table__tfoot"
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
							class="font-bold opacity-50">{{ modelValue.length }}/{{
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
	import type { TableHTMLAttributes} from "vue";
	import { defineComponent, computed, ref, useAttrs } from "vue";

	import useColumns from "./hooks/useColumns";
	import useRows from "./hooks/useRows";
	import useSortable from "./hooks/useSortable";
	import useSelectable from "./hooks/useSelectable";
	import useClickable from "./hooks/useClickable";

	import type {
		DataTableColumnSource,
		DataTableItem,
		DataTableSortingOrder,
	} from "./types";

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
			collapsible?: boolean;
			noData?: boolean;
			scrollIntoView?: boolean;
			tabBarDesign?: boolean;
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
			noDataHtml: "Keine Einträge vorhanden",
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
			collapsible: false,
			noData: undefined,
			scrollIntoView: undefined,
			tabBarDesign: undefined,
		}
	);

	const emit = defineEmits<{
		(e: "update:modelValue", items: any[]): void;
		(e: "update:sortBy", sortBy: string): void;
		(e: "update:sortingOrder", sortingOrder: DataTableSortingOrder): void;
		(e: "update:clicked", items: any | null): void;
	}>();

	const attrs = useAttrs();

	const {columnsComputed, gridTemplateColumns} = useColumns(props);

	const gridTemplateColumnsComputed = computed(() => gridTemplateColumns.value || 'repeat(auto-fit, minmax(8rem, 1fr))');

	const {rowsComputed} = useRows(columnsComputed, props);

	const {
		// sortBy, sortingOrder,
		toggleSorting, sortedRows} = useSortable(
		columnsComputed,
		rowsComputed,
		props
	);

	const filterOpenRef = ref(props.filterOpen);

	const toggleFilterOpen = () => {
		filterOpenRef.value = !filterOpenRef.value;
	}

	const {
		toggleBulkSelection,
		isRowSelected,
		allRowsSelected,
		someNotAllRowsSelected,
		toggleRowSelection
	} = useSelectable({
		isActive: () => props.selectable,
		sortedRows,
		selectedItems: () => props.modelValue,
		emit: (v: DataTableItem[]) => emit('update:modelValue', v),
	});

	const {isRowClicked, toggleRowClick, setClickedRow } = useClickable({
		isActive: () => props.clickable,
		canReset: () => props.allowUnclick,
		clickedItem: () => props.clicked,
		emit: (v: DataTableItem | null) => emit('update:clicked', v),
		scrollIntoView: () => props.scrollIntoView,
	});

	const showNoDataHtml = computed(() => (sortedRows.value.length === 0));

	const computedTableAttributes = computed(() => ({
		...Object.fromEntries(Object.entries(attrs).filter(([key]) => !["class", "style"].includes(key))),
	} as TableHTMLAttributes));

</script>

<style lang="postcss">
:root {
	--checkbox-width: 1.75rem;

	@media (min-width: 2000px) {
		--checkbox-width: 2rem;
	}
}

.data-table {
	@apply flex flex-col;
	@apply w-full border border-black/25 bg-white border-b-0;
	@apply tabular-nums;
	@apply max-h-full overflow-y-visible overflow-x-auto;

	.app--sidebar-container & {
		@apply border-x-0;
	}

	&__th,
	&__td {
		@apply flex h-full items-center border-r border-b border-black/25 leading-none;
		padding: 0.1rem 0.5rem 0.1rem;
		line-height: 1;

		&__no-padding {
			@apply p-0;
		}

		&__padding-sm {
			@apply px-0;
		}

		&__separate {
			padding-right: 3px;
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
			}
		}

		&:last-child {
			@apply border-r-0;
		}

		&:first-child:not(.data-table__cell-select) {
			.app--sidebar-container & {
				@apply pl-3 2xl:pl-7 4xl:pl-8;
			}
		}

		&__id {
			@apply font-mono;
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
			@apply bg-primary bg-opacity-5 font-bold text-primary;
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
		@apply flex items-center justify-center p-0;
		@apply sticky left-0 z-10;

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
				@apply bg-dark-20 bg-opacity-25 rounded;
			}

			&-column,
			&-column:hover {
				@apply bg-primary bg-opacity-5;

				.data-table__th-title {
					@apply text-primary;
				}
			}
		}
	}

	&__th-sorting {
		@apply basis-1;
	}

	&__thead {
		@apply w-full bg-white;
		@apply font-bold text-button;
		@apply border-b border-black/25;
		padding-right: var(--scrollbar-width);

		.data-table__tr:last-child {
			.data-table__td,
			.data-table__th {
				@apply border-b-0;
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
			@apply overflow-hidden text-ellipsis;
			max-width: 100%;
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 1;
			word-break: break-all;
			min-width: 1rem;
			line-height: 1.25;
			width: auto;
		}

		&__tr__compact {
			.data-table__th,
			.data-table__td {
				@apply h-[1.9rem];
			}
		}
	}

	&__tbody {
		@apply h-full overflow-y-auto overflow-x-visible flex flex-col;
		background:
			linear-gradient(white 30%, rgba(255,255,255,0)),
			linear-gradient(rgba(255,255,255,0), white 70%) 0 100%,
			linear-gradient(to bottom, rgba(0,0,0,0.05), rgba(0,0,0,0)),
			linear-gradient(to top, rgba(0,0,0,0.1) 0, rgba(0,0,0,0.1) 1px, rgba(0,0,0,0)) 0 100%;
		background-repeat: no-repeat;
		background-color: #fff;
		background-size: 100% 4em, 100% 4em, 100% 1em, 100% 1em;
		background-attachment: local, local, scroll, scroll;

		&__tr {
			@apply min-h-[1.7rem];

			&:focus {
				@apply outline-none;
			}

			.data-table__tr__collapsed {
				@apply hidden;
			}

			.data-table__tr__expanded {
				@apply grid col-span-full;

				&:before {
					/*@apply absolute h-3 w-3 border-b border-l border-dark-20 top-1;
					left: 1.175rem;*/
					@apply absolute h-2/3 w-px bg-black/25;
					left: 1rem;
					top: 50%;
					transform: translateY(-50%) translateX(-50%);
					content: '';
				}

				.data-table__td:first-child {
					@apply pl-12;
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
			.button {
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

	&__tfoot {
		@apply w-full bg-white;
		@apply sticky bottom-0 left-0 z-20;
		@apply border-y border-black/25 -mt-px;

		&__tr {
			@apply w-full flex items-center;
			@apply h-[2.75rem];
		}

		.data-table__tr:last-child {
			.data-table__td,
			.data-table__th {
				@apply border-b-0;
			}
		}

		&__th,
		&__td {
			@apply border-0;
		}

		.data-table__cell-select {
			@apply border-r-0;
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
				@apply text-sm flex flex-col gap-1;
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
		@apply border-black/10 border-b;

		.data-table__thead {
			@apply text-black/25 pointer-events-none border-b-black/10;
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
			@apply opacity-100 text-primary;
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
					@apply text-primary;
				}

				&:focus-within input {
					@apply border-primary;
				}

				input {
					@apply bg-primary/5 text-primary border-transparent;
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
			grid-template-columns: repeat(auto-fit, minmax(10rem, 1fr));

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
			@apply bg-white text-primary;
		}

		.data-table__td,
		.data-table__th,
		.data-table__thead {
			@apply bg-transparent border-0 font-bold;
		}
	}
}
</style>
