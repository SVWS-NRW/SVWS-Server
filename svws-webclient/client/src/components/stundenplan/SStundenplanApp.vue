<template>
	<template v-if="(manager().hasDaten() && activeViewType === ViewType.DEFAULT) || activeViewType === ViewType.HINZUFUEGEN || activeViewType === ViewType.GRUPPENPROZESSE">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="activeViewType === ViewType.DEFAULT">
						<template v-if="manager().auswahl().id !== -1">
							<h2 class="svws-headline">
								{{ manager().auswahl().bezeichnung }}
								<svws-ui-badge type="light" title="ID" class="font-mono" size="small"> ID: {{ manager().auswahl().id }} </svws-ui-badge>
							</h2>
							<span class="svws-subline">{{ toYear(manager().auswahl().gueltigAb, manager().auswahl().gueltigBis) }} ({{ 'KW ' + toKW(manager().auswahl().gueltigAb) + '—' + toKW(manager().auswahl().gueltigBis) }})</span>
						</template>
						<template v-else>
							<h2 class="svws-headline">
								Allgemein / Vorlage
							</h2>
							<span class="svws-subline">Kataloge für die Nutzung in Stundenplänen</span>
						</template>
					</template>
					<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
						<h2 class="svws-headline">Anlegen eines neuen Stundenplans...</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
						<h2 class="svws-headline"> Gruppenprozesse </h2>
						<span class="svws-subline">Subtext</span>
					</template>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
			<div v-if="manager().auswahlIsVorlage()" class="page page-flex-row">
				<router-view name="eintraege" />
				<router-view />
			</div>
			<router-view v-else />
		</svws-ui-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-calendar-event-line" />
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanAppProps } from "./SStundenplanAppProps";
	import { DateUtils } from "@core";
	import { ViewType } from "@ui";
	import { useRegionSwitch } from "~/components/useRegionSwitch";

	const props = defineProps<StundenplanAppProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const wochentag = ['So.', 'Mo.', 'Di.', 'Mi.', 'Do.', 'Fr.', 'Sa.', 'So.' ];

	function toDateStr(iso: string) : string {
		if ((typeof iso !== 'string') || (iso.length < 10))
			return '—';
		const date = DateUtils.extractFromDateISO8601(iso);
		return wochentag[date[3] % 7] + " " + date[2] + "." + date[1] + "." + date[0];
	}

	function toYear(isoFrom: string, isoTo: string) : string {
		if ((typeof isoFrom !== 'string') || (isoFrom.length < 4))
			return '—';
		const dateFrom = DateUtils.extractFromDateISO8601(isoFrom);
		const dateTo = DateUtils.extractFromDateISO8601(isoTo);
		return "" + dateFrom[0] + (dateFrom[0] !== dateTo[0] ? "/" + dateTo[0] : "");
	}

	function toKW(iso: string) : string {
		if ((typeof iso !== 'string') || (iso.length < 10))
			return '—';
		const date = DateUtils.extractFromDateISO8601(iso);
		return "" + date[5];
	}

</script>
