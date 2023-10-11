<template>
	<div class="page--content h-full w-full">
		<svws-ui-table :clicked="lernabschnitt" @update:clicked="gotoLernabschnitt" :columns="[{key: 'schuljahresabschnitt', label: 'Abschnitt'}]" :items="lernabschnitte" clickable type="navigation" disable-header>
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
		<div class="flex flex-col items-start">
			<nav class="svws-ui-title-tabs">
				<template v-for="c in children" :key="c.name">
					<button role="link" :class="[ 'svws-ui-title-tab', { 'svws-active': child === c } ]" @click="setChild(c)">
						<span class="absolute left-1/2 top-1/2 transform -translate-x-1/2 -translate-y-1/2">{{ c.text }}</span>
						<span class="font-bold opacity-0">{{ c.text }}</span>
					</button>
				</template>
			</nav>
			<router-view :key="$route.hash" />
		</div>
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
		grid-template-columns: 8.5rem minmax(50rem, 1fr);
	}

</style>

<style lang="postcss">
.svws-select-lernabschnitt .text-input-component {
	@apply text-headline-md w-fit;
}
</style>
