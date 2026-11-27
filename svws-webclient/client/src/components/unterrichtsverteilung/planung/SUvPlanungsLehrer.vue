<template>
	<div class="min-h-0 flex-1 overflow-auto p-3">
		<div class="grid grid-cols-[repeat(auto-fill,minmax(13rem,1fr))] content-start gap-3">
			<s-uv-planungs-lehrer-kachel v-for="eintrag in eintraege"
				:key="eintrag.id"
				:id-lehrer="eintrag.id"
				:modus="mitgliedschaftBearbeiten ? 'abschnitt-entfernen' : 'uebersicht'"
				:pending="mitgliedschaftPending"
				:entfernen-gesperrt="verwendeteLehrerIds.has(eintrag.id)"
				:kuerzel="eintrag.text"
				:name="eintrag.name"
				:faecher="eintrag.faecher"
				:verplant="eintrag.verplant"
				:anrechnung="eintrag.anrechnung"
				:soll="eintrag.soll"
				:rest="eintrag.reststunden"
				:auswahl-status="auswahlManager?.auswahlStatus(auswahl, 'lehrer', eintrag.id) ?? 'neutral'"
				:zugeordnet="!mitgliedschaftBearbeiten && eintrag.zugeordnet"
				:zuordnen-moeglich="!mitgliedschaftBearbeiten && hatKompetenzAendern && eintrag.zuweisbar"
				:auswaehlen="() => props.auswaehlen(eintrag.id)"
				:details-anzeigen="() => props.detailsAnzeigen(eintrag.id)"
				:zuordnen="() => props.lehrerZuordnen(eintrag.id)"
				:entfernen="() => props.mitgliedschaftEntfernen(eintrag.id)" />
		</div>
		<p v-if="eintraege.length === 0" class="p-2 text-ui-secondary">{{ gesamtAnzahl === 0 ? 'Diesem Planungsabschnitt sind noch keine Lehrkräfte zugewiesen.' : suche ? 'Keine Treffer.' : 'Keine Einträge vorhanden.' }}</p>
	</div>
</template>

<script setup lang="ts">
	import SUvPlanungsLehrerKachel from './SUvPlanungsLehrerKachel.vue';
	import type { UvPlanungsAuswahl } from './UvPlanung';
	import type { UvLehrerAnsichtEintrag, UvPlanungsManager } from './UvPlanungsManager';

	const props = defineProps<{
		eintraege: UvLehrerAnsichtEintrag[];
		gesamtAnzahl: number;
		suche: string;
		auswahl: UvPlanungsAuswahl | null;
		auswahlManager: UvPlanungsManager | null;
		hatKompetenzAendern: boolean;
		mitgliedschaftBearbeiten: boolean;
		mitgliedschaftPending: boolean;
		verwendeteLehrerIds: Set<number>;
		auswaehlen: (id: number) => void;
		detailsAnzeigen: (id: number) => void;
		lehrerZuordnen: (id: number) => void;
		mitgliedschaftEntfernen: (id: number) => void;
	}>();
</script>
