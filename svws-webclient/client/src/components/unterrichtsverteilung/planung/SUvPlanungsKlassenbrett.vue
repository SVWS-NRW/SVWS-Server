<template>
	<div class="min-h-0 flex-1 overflow-auto p-3">
		<div v-if="stufen.length === 0" class="p-3 text-sm text-ui-secondary">Keine Klassen gefunden.</div>
		<div v-for="stufe in stufen"
			:key="stufe.id"
			class="grid grid-cols-[5.5rem_minmax(0,1fr)] gap-x-3 gap-y-2 border-b border-ui-25 py-3 first:pt-0 last:border-b-0">
			<div class="flex flex-col justify-center gap-1 text-sm font-semibold">
				<span>{{ stufe.name }}</span>
				<span class="text-xs font-normal text-ui-secondary">{{ stufe.schueler }} Schüler</span>
				<span class="text-xs font-normal text-ui-secondary">{{ stufe.stunden }} h</span>
			</div>
			<div class="grid grid-cols-[repeat(auto-fill,minmax(9rem,1fr))] gap-2">
				<div v-for="klasse in stufe.klassen"
					:key="klasse.id"
					class="group relative flex min-w-0 items-center gap-2 rounded-lg border border-ui-25 bg-ui p-2 hover:border-ui-brand"
					:class="uvAuswahlKlasse(auswahlManager?.auswahlStatus(auswahl, 'klassen', klasse.id) ?? 'neutral')">
					<input v-if="hatKompetenzAendern && waehlbareIds.has(klasse.id)" type="checkbox" class="shrink-0" :checked="selectedIds.includes(klasse.id)"
						:aria-label="`${klasse.kuerzel} für Sammelaktion auswählen`" @change="props.klassenAuswahlUmschalten(klasse.id)">
					<button type="button"
						class="min-w-0 flex-1 text-left focus-visible:outline-2 focus-visible:outline-ui-brand"
						:title="`${klasse.kuerzel} · ${klasse.schueler} Schüler · ${klasse.stunden} h`"
						@click="props.klasseAuswaehlen(klasse.id)">
						<span class="block truncate font-semibold">{{ klasse.kuerzel }}</span>
						<span class="block truncate text-xs text-ui-secondary">{{ klasse.schueler }} Schüler · {{ klasse.stunden }} h</span>
					</button>
					<s-uv-details-oeffnen v-if="auswahlManager?.auswahlStatus(auswahl, 'klassen', klasse.id) === 'direkt'"
						variante="kompakt"
						:oeffnen="() => props.detailsAnzeigen('klassen', klasse.id)" />
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed } from 'vue';

	import { useUvState } from '@ui/states/UvState';

	import SUvDetailsOeffnen from './SUvDetailsOeffnen.vue';
	import { uvAuswahlKlasse } from './UvAuswahlDarstellung';
	import type { UvPlanungsAuswahl, UvPlanungsAuswahlArt } from './UvPlanung';
	import type { UvKlassenbrettStufe, UvPlanungsManager } from './UvPlanungsManager';

	const props = defineProps<{
		stufen: UvKlassenbrettStufe[];
		selectedIds: number[];
		waehlbareIds: Set<number>;
		auswahl: UvPlanungsAuswahl | null;
		auswahlManager: UvPlanungsManager | null;
		klasseAuswaehlen: (id: number) => void;
		detailsAnzeigen: (art: UvPlanungsAuswahlArt, id: number) => void;
		klassenAuswahlUmschalten: (id: number) => void;
	}>();

	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
</script>
