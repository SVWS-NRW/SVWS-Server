<template>
	<template v-if="kursListeManager().hasDaten()">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>{{ kursListeManager().daten().kuerzel }}</span>
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							ID:
							{{ kursListeManager().daten().id }}
						</svws-ui-badge>
					</h2>
					<span class="svws-subline">
						{{ lehrerkuerzel }}
					</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :tabs="tabs" :hidden="tabsHidden" :tab="tab" :set-tab="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-presentation-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { KurseAppProps } from "./SKurseAppProps";

	const props = defineProps<KurseAppProps>();

	const lehrerkuerzel = computed<string>(() => {
		let s = '';
		if (props.kursListeManager().hasDaten()) {
			const idLehrer = props.kursListeManager().daten().lehrer;
			const lehrer = idLehrer === null ? null : props.kursListeManager().lehrer.get(idLehrer);
			s = (lehrer === null) ? " " : lehrer.kuerzel;
			// TODO Zusatzkräfte
			// for (const idZusatzkraft of props.kursListeManager().daten().zusatzkraefte) {
			// 	const zusatzkraft = props.kursListeManager().lehrer.get(idZusatzkraft);
			// 	if (zusatzkraft !== null) {
			// 		if (s.length)
			// 			s += `, ${lehrer.kuerzel}`;
			// 		else s = lehrer.kuerzel;
			// 	}
			// }
		}
		return s;
	});

</script>
