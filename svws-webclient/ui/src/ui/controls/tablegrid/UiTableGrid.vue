<template>
	<table class="ui-table-grid" :aria-label="name">
		<thead>
			<template v-for="i in headerCount" :key="i">
				<tr>
					<slot :i="i" name="header" />
				</tr>
			</template>
		</thead>
		<tbody>
			<template v-for="row in data" :key="getKey(row)">
				<tr>
					<slot :row="row" />
				</tr>
			</template>
		</tbody>
		<tfoot>
			<template v-for="i in footerCount" :key="i">
				<tr>
					<slot :i="i" name="footer" />
				</tr>
			</template>
		</tfoot>
	</table>
</template>

<script setup lang="ts" generic="T">

	import { computed } from 'vue';
	import type { Collection } from '../../../../../core/src/java/util/Collection';
	import type { List } from '../../../../../core/src/java/util/List';

	export interface CellFormat {
		widths: string[];
	}

	const props = withDefaults(defineProps<{
		data: Collection<T> | List<T>,
		cellFormat: CellFormat,
		headerCount?: number,
		footerCount?: number,
		name?: string | undefined,
		getKey: (row: T) => string,
	}>(), {
		headerCount: 1,
		footerCount: 1,
		name: undefined,
	});

	const gridTemplateColumnsComputed = computed<string>(() => props.cellFormat.widths.join(" "));

</script>

<style scoped>

	tr {
		grid-template-columns: v-bind(gridTemplateColumnsComputed);
	}

</style>
