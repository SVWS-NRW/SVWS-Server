<template>
	<div class="page--content h-full w-full">
		<svws-ui-table :clicked="lernabschnitt" @update:clicked="gotoLernabschnitt" :columns="[{key: 'schuljahresabschnitt', label: 'Abschnitt'}]" :items="lernabschnitte" clickable type="navigation" class="-mt-1">
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
		<Teleport to=".svws-sub-nav-target" v-if="isMounted">
			<nav class="svws-ui-secondary-tabs">
				<svws-ui-router-tab-bar-button v-for="(c, index) in children" :route="c" :selected="child"
					:hidden="false" @select="setChild(c)" :key="index" />
			</nav>
		</Teleport>
		<router-view :key="$route.hash" />
	</div>
</template>


<script setup lang="ts">

	import type { SchuelerLernabschnitteProps } from "./SSchuelerLernabschnitteProps";
	import { ref, onMounted } from 'vue';

	const props = defineProps<SchuelerLernabschnitteProps>();

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>


<style lang="postcss" scoped>

	.page--content {
		grid-template-columns: 7rem minmax(50rem, 1fr) minmax(50rem, 1fr);
	}

</style>

<style lang="postcss">
.svws-select-lernabschnitt .text-input-component {
	@apply text-headline-md w-fit;
}
</style>
