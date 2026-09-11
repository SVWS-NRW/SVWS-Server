<template>
	<header class="svws-ui-header">
		<div class="svws-ui-header--title">
			<div class="svws-headline-wrapper">
				<template v-if="activeViewType === ViewType.DEFAULT">
					<template v-if="manager().hasDaten() && manager().auswahl().id !== -1">
						<h2 class="svws-headline">
							{{ manager().auswahl().beschreibung }}
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small"> ID: {{ manager().auswahl().id }} </svws-ui-badge>
						</h2>
						<span class="svws-subline">{{ toYear(manager().auswahl().gueltigVon, manager().auswahl().gueltigBis) }} ({{ 'KW ' + toKW(manager().auswahl().gueltigVon, manager().auswahl().gueltigBis) }})</span>
					</template>
					<template v-else>
						<h2 class="svws-headline">
							Grunddaten
						</h2>
						<span class="svws-subline">für die Nutzung in UV-Planungsabschnitten</span>
					</template>
				</template>
				<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
					<h2 class="svws-headline">Anlegen eines neuen Planungsabschnitts...</h2>
				</template>
				<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
					<h2 class="svws-headline"> Gruppenprozesse </h2>
					<span class="svws-subline">{{ planungsabschnittSubline }}</span>
				</template>
			</div>
		</div>
		<div class="svws-ui-header--actions" />
	</header>

	<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
		<s-uv-auswahl-layout v-if="props.tabManager().tab.tabContentHatAuswahl">
			<template #auswahl><router-view name="auswahl" /></template>
			<router-view />
		</s-uv-auswahl-layout>
		<router-view v-else />
	</svws-ui-tab-bar>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import { DateUtils } from "@core/core/utils/DateUtils";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import type { UvPlanungsabschnitteListeManager } from "@ui/ui/manager/unterrichtsverteilung/UvPlanungsabschnitteListeManager";
	import type { TabManager } from "@ui/ui/nav/TabManager";
	import { ViewType } from "@ui/ui/nav/ViewType";

	import SUvAuswahlLayout from "./SUvAuswahlLayout.vue";

	const props = defineProps<{
		activeViewType: ViewType;
		tabManager: () => TabManager;
		manager: () => UvPlanungsabschnitteListeManager;
	}>();
	const manager = () => props.manager();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	function toYear(isoFrom: string, isoTo: string | null): string {
		if ((typeof isoFrom !== 'string') || (isoFrom.length < 4)) {
			return '—';
		}
		const dateFrom = DateUtils.extractFromDateISO8601(isoFrom);
		const yearFrom = dateFrom[0];
		if ((isoTo === null) || (typeof isoTo !== 'string') || (isoTo.length < 4)) {
			return yearFrom + "...";
		}
		const dateTo = DateUtils.extractFromDateISO8601(isoTo);
		const yearTo = dateTo[0];
		return "" + yearFrom + (yearFrom !== yearTo ? "/" + yearTo : "");
	}

	function toKW(isoFrom: string, isoTo: string | null): string {
		if ((typeof isoFrom !== 'string') || (isoFrom.length < 10)) {
			return '—';
		}
		const dateFrom = DateUtils.extractFromDateISO8601(isoFrom);
		const kwFrom = dateFrom[5];
		if ((isoTo === null) || (typeof isoTo !== 'string') || (isoTo.length < 10)) {
			return kwFrom + "...";
		}
		const dateTo = DateUtils.extractFromDateISO8601(isoTo);
		const kwTo = dateTo[5];
		return "" + kwFrom + (kwFrom !== kwTo ? "-" + kwTo : "");
	}

	const planungsabschnittSubline = computed(() => {
		const auswahlPlanungsabschnittList = manager().liste.auswahlSorted();
		if (auswahlPlanungsabschnittList.size() > 5) {
			return `${auswahlPlanungsabschnittList.size()} Planungsabschnitte ausgewählt`;
		}
		return [...auswahlPlanungsabschnittList].map(s => s.beschreibung).join(', ');
	});

</script>
