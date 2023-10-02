<template>
	<div class="page--content">
		<svws-ui-table :clicked="lernabschnitt" @update:clicked="gotoLernabschnitt" :columns="[{key: 'schuljahresabschnitt', label: 'Abschnitt'}]" :items="lernabschnitte" clickable type="navigation">
			<template #cell="{ rowData: row }">
				<span>
					{{ row.schuljahr + "." + row.abschnitt }}
				</span>
				<svws-ui-tooltip v-if="row.wechselNr !== 0">
					<span class="opacity-50 inline-block cursor-pointer top-0.5 relative">
						(alt)
					</span>
					<template #content>
						Daten vor dem {{ row.wechselNr }}. Wechsel
					</template>
				</svws-ui-tooltip>
			</template>
		</svws-ui-table>
		<router-view :key="$route.hash" />
	</div>
</template>


<script setup lang="ts">

	import type { SchuelerLeistungenProps } from "./SSchuelerLeistungenProps";

	const props = defineProps<SchuelerLeistungenProps>();

</script>


<style lang="postcss" scoped>

	.page--content {
		grid-template-columns: 8.5rem minmax(50rem, 1fr);

		@media (min-width: 1440px) {
			grid-template-columns: 8.5rem minmax(62rem, 1fr) minmax(0, 1fr);
		}
	}

</style>
