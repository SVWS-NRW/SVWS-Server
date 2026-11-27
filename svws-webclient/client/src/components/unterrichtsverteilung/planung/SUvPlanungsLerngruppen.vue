<template>
	<div class="min-h-0 flex-1 overflow-auto p-3">
		<div class="mb-3 text-xs text-ui-secondary">{{ daten.besetzteAnzahl }} / {{ daten.anzahl }} Lerngruppen mit Lehrkraft · {{ formatStunden(daten.besetzteStunden) }} / {{ formatStunden(daten.gesamtStunden) }} h</div>
		<div v-for="gruppe in daten.gruppen" :key="gruppe.jahrgang" class="mb-4">
			<div class="mb-1 flex flex-wrap items-baseline gap-2 border-b border-ui-25 pb-1 text-xs">
				<h3 class="font-semibold">Jahrgang {{ gruppe.jahrgang }}</h3>
				<span class="text-ui-secondary">{{ gruppe.lerngruppen.length }} Lerngruppen · {{ formatStunden(gruppe.stunden) }} h · {{ gruppe.offen }} offen</span>
			</div>
			<div class="flex flex-col gap-1">
				<s-uv-planungs-lerngruppe-zeile v-for="lerngruppe in gruppe.lerngruppen"
					:key="lerngruppe.id"
					:lerngruppe
					:planungs-manager="auswahlManager"
					:auswahl-status="auswahlManager?.auswahlStatus(auswahl, 'lerngruppen', lerngruppe.id) ?? 'neutral'"
					:selectable
					:selected="selectedIds.includes(lerngruppe.id)"
					:auswaehlen="() => props.auswaehlen(lerngruppe.id)"
					:auswahl-umschalten="() => props.auswahlUmschalten(lerngruppe.id)"
					:details-anzeigen="() => props.detailsAnzeigen(lerngruppe.id)" />
			</div>
		</div>
		<p v-if="daten.gruppen.length === 0" class="p-2 text-ui-secondary">Keine Lerngruppen für diese Filter.</p>
	</div>
</template>

<script setup lang="ts">
	import { formatUvStunden as formatStunden } from '../UvPresenter';

	import SUvPlanungsLerngruppeZeile from './SUvPlanungsLerngruppeZeile.vue';
	import type { UvPlanungsAuswahl } from './UvPlanung';
	import type { UvLerngruppenAnsicht, UvPlanungsManager } from './UvPlanungsManager';

	const props = defineProps<{
		daten: UvLerngruppenAnsicht;
		auswahlManager: UvPlanungsManager | null;
		auswahl: UvPlanungsAuswahl | null;
		selectedIds: number[];
		selectable: boolean;
		auswaehlen: (id: number) => void;
		auswahlUmschalten: (id: number) => void;
		detailsAnzeigen: (id: number) => void;
	}>();
</script>
