<template>
	<div class="group relative flex min-w-0 items-center gap-2 rounded border border-ui-25 bg-ui px-2 py-1.5 hover:border-ui-brand" :class="uvAuswahlKlasse(auswahlStatus)">
		<input v-if="selectable"
			type="checkbox"
			class="shrink-0"
			:checked="selected"
			:aria-label="`${presenter.lerngruppeBezeichnung(lerngruppe)} für Sammelaktion auswählen`"
			@change="props.auswahlUmschalten?.()">
		<button type="button"
			class="flex min-w-0 flex-1 items-center gap-2 text-left focus-visible:outline-2 focus-visible:outline-ui-brand"
			:title="`${presenter.lerngruppeBezeichnung(lerngruppe)} öffnen`"
			@click="props.auswaehlen()">
			<span class="flex min-w-0 flex-1 items-center gap-2">
				<span class="min-w-0 truncate text-sm font-semibold">{{ presenter.lerngruppeBezeichnung(lerngruppe) }}</span>
				<s-uv-fach-badge :id-fach="presenter.lerngruppeFachId(lerngruppe)" />
			</span>
			<span v-if="kursart !== null" class="hidden shrink-0 rounded border border-ui-25 bg-ui-neutral px-1.5 py-0.5 text-xs font-semibold md:inline">{{ kursart }}</span>
			<span class="hidden max-w-24 truncate rounded border border-ui-25 px-1.5 py-0.5 text-xs sm:block" :class="besetzt ? 'text-ui-secondary' : 'border-ui-warning bg-ui-warning-weak text-ui-warning'">{{ lehrerText }}</span>
			<span class="shrink-0 text-xs text-ui-secondary" :title="`${schuelerzahl} Schüler`">{{ schuelerzahl }} SuS</span>
			<span class="w-9 shrink-0 text-right text-xs text-ui-secondary">{{ formatStunden(lerngruppe.wochenstunden) }} h</span>
		</button>
		<s-uv-details-oeffnen v-if="auswahlStatus === 'direkt'" :oeffnen="() => props.detailsAnzeigen?.()" />
	</div>
</template>

<script setup lang="ts">
	import { computed } from 'vue';

	import type { UvLerngruppe } from '@core/core/data/uv/UvLerngruppe';
	import { useUvState } from '@ui/states/UvState';

	import SUvFachBadge from '../SUvFachBadge.vue';
	import { createUvPresenter, formatUvStunden as formatStunden } from '../UvPresenter';

	import SUvDetailsOeffnen from './SUvDetailsOeffnen.vue';
	import { uvAuswahlKlasse } from './UvAuswahlDarstellung';
	import type { UvAuswahlStatus } from './UvPlanung';
	import type { UvPlanungsManager } from './UvPlanungsManager';

	const props = withDefaults(defineProps<{
		lerngruppe: UvLerngruppe;
		planungsManager: UvPlanungsManager | null;
		auswahlStatus?: UvAuswahlStatus;
		selectable?: boolean;
		selected?: boolean;
		auswaehlen: () => void;
		auswahlUmschalten?: () => void;
		detailsAnzeigen?: () => void;
	}>(), { auswahlStatus: 'neutral', selectable: false, selected: false, auswahlUmschalten: undefined, detailsAnzeigen: undefined });

	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));
	const kursart = computed(() => props.planungsManager?.lerngruppeKursart(props.lerngruppe) ?? null);
	const besetzt = computed(() => !(props.planungsManager?.lerngruppeIstOffen(props.lerngruppe.id) ?? true));
	const lehrerText = computed(() => {
		const text = props.planungsManager?.lerngruppeLehrerText(props.lerngruppe.id);
		return text === undefined || text === '' ? 'offen' : text;
	});
	const schuelerzahl = computed(() => props.planungsManager?.lerngruppeSchuelerzahl(props.lerngruppe) ?? 0);
</script>
