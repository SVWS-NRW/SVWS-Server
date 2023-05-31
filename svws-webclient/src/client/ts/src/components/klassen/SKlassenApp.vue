<template>
	<template v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ auswahl?.kuerzel ?? '' }}</span>
				<svws-ui-badge type="light" title="ID" class="font-mono">
					<i-ri-fingerprint-line />
					{{ auswahl?.id }}
				</svws-ui-badge>
			</div>
			<div>
				<div class="separate-items--custom">
					<span v-for="(l, i) in inputKlassenlehrer" :key="i" class="opacity-50"> {{ l.kuerzel }} </span>
				</div>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<i-ri-team-line />
	</div>
</template>

<script setup lang="ts">

	import type { LehrerListeEintrag } from "@svws-nrw/svws-core";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { KlassenAppProps } from "./SKlassenAppProps";

	const props = defineProps<KlassenAppProps>();

	const inputKlassenlehrer: ComputedRef<LehrerListeEintrag[]> = computed(() =>
		(props.auswahl?.klassenLehrer?.toArray() as number[] || []).map(id => props.mapLehrer.get(id) || undefined).filter(l => l !== undefined) as LehrerListeEintrag[]
	);

	const visible: ComputedRef<boolean> = computed(() => props.auswahl !== undefined);

</script>
