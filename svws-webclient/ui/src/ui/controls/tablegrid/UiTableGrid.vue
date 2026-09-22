<template>
	<table ref="tableRef" class="ui-table-grid" :aria-label="name">
		<thead>
			<template v-for="i in headerCount" :key="i">
				<tr>
					<slot :i name="header" />
				</tr>
			</template>
		</thead>
		<tbody ref="tbodyRef">
			<template v-for="(row, index) in manager().daten" :key="manager().getRowKey(row)">
				<tr :class="[{ 'hover:bg-ui-hover': showRowHighlight,'bg-ui-selected': !hideSelection && (manager().focusRow === index) }, 'group']"
					@click="rowClicked(row, index)">
					<slot :row :index />
				</tr>
			</template>
		</tbody>
		<tfoot>
			<template v-for="i in footerCount" :key="i">
				<tr>
					<slot :i name="footer" />
				</tr>
			</template>
		</tfoot>
	</table>
</template>

<script setup lang="ts" generic="T,U extends PropertyKey">

	import { computed, onMounted, onUnmounted, useTemplateRef } from 'vue';

	import type { Collection } from '@core/java/util/Collection';

	import type { GridManager } from './GridManager';

	export interface CellFormat {
		widths: string[];
	}

	function rowClicked(row: T, index: number) {
		props.manager().doFocusRowIfNotFocussed(index);
	}

	const props = withDefaults(defineProps<{
		headerCount?: number,
		footerCount?: number,
		name?: string | undefined,
		manager: () => GridManager<U, T, Collection<T> | T[]>,
		hideSelection?: boolean,
		showRowHighlight?: boolean,
	}>(), {
		headerCount: 1,
		footerCount: 1,
		name: undefined,
		hideSelection: false,
		showRowHighlight: true,
	});

	const gridTemplateColumnsComputed = computed<string>(() => props.manager().getGridTemplateColumns());

	//# region ----------------------- Scrollbar-Fix ------------------------

	const tableRef = useTemplateRef<HTMLTableElement>('tableRef');
	const tbodyRef = useTemplateRef<HTMLTableSectionElement>('tbodyRef');

	let resizeObserver: ResizeObserver | null = null;

	/** Setzt die CSS-Variable, um die Scrollbarbreite bei Overflow von tbody, in thead und tfoot kompensieren. */
	function updateScrollbarOffset(): void {
		const tbody = tbodyRef.value;
		const table = tableRef.value;
		if ((tbody === null) || (table === null)) {
			return;
		}
		const scrollbarWidth = tbody.offsetWidth - tbody.clientWidth;
		table.style.setProperty('--scrollbar-width', `${scrollbarWidth}px`);
	}

	onMounted(() => {
		updateScrollbarOffset();
		resizeObserver = new ResizeObserver(updateScrollbarOffset);
		if (tbodyRef.value !== null) {
			resizeObserver.observe(tbodyRef.value);
		}
	});

	onUnmounted(() => {
		resizeObserver?.disconnect();
		resizeObserver = null;
	});
	//# endregion

</script>

<style scoped>

	tr {
		grid-template-columns: v-bind(gridTemplateColumnsComputed);
	}

</style>
