<template>
	<template v-if="auswahl !== undefined">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						{{ auswahl.bezeichnung }}
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small"> ID: {{ auswahl.id }} </svws-ui-badge>
					</h2>
					<span class="svws-subline">{{ toYear(auswahl.gueltigAb, auswahl.gueltigBis) }} ({{ 'KW ' + toKW(auswahl.gueltigAb) + '—' + toKW(auswahl.gueltigBis) }})</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-tab-bar :tab-manager>
			<router-view />
		</svws-ui-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-calendar-event-line" />
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanAppProps } from "./SStundenplanAppProps";
	import { DateUtils } from "@core";

	defineProps<StundenplanAppProps>();

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

<style lang="postcss">

	.page--wrapper {
		@apply flex flex-col h-full;
	}

	.svws-ui-tab-content {
		@apply h-full overflow-hidden;
	}

</style>
