<template>
	<template v-if="visible">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<svws-ui-tooltip :indicator="false">
							{{ auswahl?.kuerzel ? 'Klasse ' + auswahl.kuerzel : '—' }}
							<template #content>
								<span class="font-mono">
									ID:
									{{ auswahl?.id || '—' }}
								</span>
							</template>
						</svws-ui-tooltip>
					</h2>
					<span class="svws-subline">
						{{ lehrerkuerzel }}
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

	import type { KlassenAppProps } from "./SKlassenAppProps";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<KlassenAppProps>();

	const lehrerkuerzel = computed<string>(() => {
		let s = '';
		if (props.auswahl)
			for (const id of props.auswahl.klassenLehrer) {
				const lehrer = props.mapLehrer.get(id);
				if (lehrer) {
					if (s.length)
						s += `, ${lehrer.kuerzel}`;
					else s = lehrer.kuerzel;
				}
			}
		return s;
	});

	const visible: ComputedRef<boolean> = computed(() => props.auswahl !== undefined);

</script>
