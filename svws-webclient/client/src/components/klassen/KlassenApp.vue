<template>
	<template v-if="(klassenState.manager.hasDaten() && (klassenState.activeViewType === ViewType.DEFAULT)) || (klassenState.activeViewType !== ViewType.DEFAULT)">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="klassenState.activeViewType === ViewType.DEFAULT">
						<h2 class="svws-headline">
							{{ klassenState.manager.daten().kuerzel ? 'Klasse ' + klassenState.manager.daten().kuerzel : '—' }}
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ klassenState.manager.daten().id }}
							</svws-ui-badge>
						</h2>
						<span class="svws-subline">
							{{ lehrerkuerzel }}
						</span>
					</template>
					<template v-else-if="klassenState.activeViewType === ViewType.HINZUFUEGEN">
						<h2 class="svws-headline">Anlegen einer neuen Klasse...</h2>
					</template>
					<template v-else-if="klassenState.activeViewType === ViewType.GRUPPENPROZESSE">
						<h2 class="svws-headline"> Gruppenprozesse </h2>
						<span class="svws-subline">{{ klassenSubline }}</span>
					</template>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>

		<svws-ui-tab-bar :tab-manager="() => tabManager(klassenState.activeViewType)" :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-team-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import type { TabManager } from "@ui/ui/nav/TabManager";
	import { ViewType } from "@ui/ui/nav/ViewType";

	import { useKlassenState } from "~/states/klassen/KlassenState";

	const props = defineProps<{
		tabManager: (viewType: ViewType) => TabManager;
		activeViewType: ViewType;
	}>();

	const klassenState = useKlassenState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const klassenSubline = computed(() => {
		const auswahlKlassenList = klassenState.manager.liste.auswahlSorted();
		if (auswahlKlassenList.size() > 5) {
			return `${auswahlKlassenList.size()} Klassen ausgewählt`;
		}
		return [...auswahlKlassenList].map(k => k.kuerzel).join(', ');
	});

	const lehrerkuerzel = computed<string>(() => {
		if (!klassenState.manager.hasDaten()) {
			return '';
		}
		let lehrerkuerzelStr = '';
		for (const lehrerId of klassenState.manager.daten().klassenLeitungen) {
			const lehrer = klassenState.manager.lehrer.get(lehrerId);
			if (lehrer === null) {
				continue;
			}
			lehrerkuerzelStr += (lehrerkuerzelStr.length > 0) ? `, ${lehrer.kuerzel}` : lehrer.kuerzel;
		}
		return lehrerkuerzelStr;
	});

</script>
