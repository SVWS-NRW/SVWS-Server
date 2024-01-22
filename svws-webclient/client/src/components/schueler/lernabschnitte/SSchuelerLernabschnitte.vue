<template>
	<div class="page--content">
		<div class="flex-none w-28 h-full">
			<svws-ui-table :clicked="lernabschnitt" @update:clicked="gotoLernabschnitt" :columns="[{key: 'schuljahresabschnitt', label: 'Abschnitt'}]" :items="lernabschnitte" clickable type="navigation" class="-mt-1">
				<template #cell="{ rowData: row }">
					<span>
						{{ `${row.schuljahr}.${row.abschnitt} (${row.jahrgang})` }}
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
		</div>
		<Teleport to=".svws-sub-nav-target" v-if="isMounted">
			<nav class="svws-ui-secondary-tabs">
				<svws-ui-router-tab-bar-button v-for="(c, index) in children" :route="c" :selected="child" :hidden="false" @select="setChild(c)" :key="index" />
			</nav>
		</Teleport>
		<div class="grow h-full">
			<router-view :key="$route.hash" />
		</div>
	</div>
</template>


<script setup lang="ts">

	import type { SchuelerLernabschnitteProps } from "./SSchuelerLernabschnitteProps";
	import { ref, onMounted } from 'vue';

	const props = defineProps<SchuelerLernabschnitteProps>();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply flex gap-x-8;
	}

</style>

<style lang="postcss">

	.svws-select-lernabschnitt .text-input-component {
		@apply text-headline-md w-fit;
	}

</style>
