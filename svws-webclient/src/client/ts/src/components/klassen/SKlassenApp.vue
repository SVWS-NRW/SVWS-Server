<template>
	<template v-if="visible">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						{{ auswahl?.kuerzel || '—' }}
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							ID:
							{{ auswahl?.id || '—' }}
						</svws-ui-badge>
					</h2>
					<span class="svws-subline separate-items--custom">
						<span v-for="(l, i) in inputKlassenlehrer" :key="i" class="opacity-50"> {{ l.kuerzel }} </span>
					</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<i-ri-team-line />
	</div>
</template>

<script setup lang="ts">

	import type { LehrerListeEintrag } from "@core";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { KlassenAppProps } from "./SKlassenAppProps";

	const props = defineProps<KlassenAppProps>();

	const inputKlassenlehrer: ComputedRef<LehrerListeEintrag[]> = computed(() =>
		(props.auswahl?.klassenLehrer?.toArray() as number[] || []).map(id => props.mapLehrer.get(id) || undefined).filter(l => l !== undefined) as LehrerListeEintrag[]
	);

	const visible: ComputedRef<boolean> = computed(() => props.auswahl !== undefined);

</script>
