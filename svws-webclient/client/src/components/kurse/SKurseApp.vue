<template>
	<template v-if="(kurseAuswahlState.manager.hasDaten() && (kurseAuswahlState.activeViewType === ViewType.DEFAULT)) || (kurseAuswahlState.activeViewType !== ViewType.DEFAULT)">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<template v-if="kurseAuswahlState.activeViewType === ViewType.DEFAULT">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">
							<span>{{ kurseAuswahlState.manager.daten().kuerzel }}</span>
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ kurseAuswahlState.manager.daten().id }}
							</svws-ui-badge>
						</h2>
						<span class="svws-subline">
							{{ lehrerkuerzel }}
						</span>
					</div>
				</template>
				<template v-else-if="kurseAuswahlState.activeViewType === ViewType.HINZUFUEGEN">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">Anlegen eines neuen Kurses...</h2>
					</div>
				</template>
				<template v-else-if="kurseAuswahlState.activeViewType === ViewType.GRUPPENPROZESSE">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">Gruppenprozesse</h2>
						<span class="svws-subline">{{ kurseSubline }}</span>
					</div>
				</template>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-tab-bar :tab-manager="() => tabManager(kurseAuswahlState.activeViewType)" :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-presentation-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import type { TabManager } from "@ui/ui/nav/TabManager";
	import { ViewType } from "@ui/ui/nav/ViewType";

	import { useKurseAuswahlState } from "~/states/kurse/KurseAuswahlState";

	const props = defineProps<{
		tabManager: (viewType: ViewType) => TabManager;
		activeViewType: ViewType;
	}>();

	const kurseAuswahlState = useKurseAuswahlState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const lehrerkuerzel = computed<string>(() => {
		let s = '';
		if (kurseAuswahlState.manager.hasDaten()) {
			const idLehrer = kurseAuswahlState.manager.daten().lehrer;
			const lehrer = idLehrer === null ? null : kurseAuswahlState.manager.lehrer.get(idLehrer);
			s = (lehrer === null) ? " " : lehrer.kuerzel;
			// TODO Zusatzkräfte
			// for (const idZusatzkraft of kurseAuswahlState.manager.daten().zusatzkraefte) {
			// 	const zusatzkraft = kurseAuswahlState.manager.lehrer.get(idZusatzkraft);
			// 	if (zusatzkraft !== null) {
			// 		if (s.length)
			// 			s += `, ${lehrer.kuerzel}`;
			// 		else s = lehrer.kuerzel;
			// 	}
			// }
		}
		return s;
	});

	const kurseSubline = computed(() => {
		const auswahlKurseList = kurseAuswahlState.manager.liste.auswahlSorted();
		if (auswahlKurseList.size() > 5) {
			return `${auswahlKurseList.size()} Kurse ausgewählt`;
		}
		return [...auswahlKurseList].map(k => k.kuerzel).join(', ');
	});

</script>
