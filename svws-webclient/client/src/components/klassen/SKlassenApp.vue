<template>
	<template v-if="klassenListeManager().hasDaten()">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>{{ klassenListeManager().daten().kuerzel ? 'Klasse ' + klassenListeManager().daten().kuerzel : '—' }}</span>
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							ID:
							{{ klassenListeManager().daten().id }}
						</svws-ui-badge>
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
	import { computed } from "vue";

	const props = defineProps<KlassenAppProps>();

	const lehrerkuerzel = computed<string>(() => {
		let s = '';
		if (props.klassenListeManager().hasDaten())
			for (const id of props.klassenListeManager().daten().klassenLeitungen) {
				const lehrer = props.klassenListeManager().lehrer.get(id);
				if (lehrer) {
					if (s.length)
						s += `, ${lehrer.kuerzel}`;
					else s = lehrer.kuerzel;
				}
			}
		return s;
	});

</script>
