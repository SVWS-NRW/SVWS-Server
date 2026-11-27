<template>
	<section class="flex min-h-0 flex-1 flex-col bg-ui">
		<div class="flex shrink-0 items-center justify-between gap-2 border-b border-ui-10 p-3">
			<h3 class="font-semibold">{{ titel }}</h3>
			<div class="flex items-center gap-2">
				<!-- Der Dialog bleibt nach dem Entfernen des Objekts bis zur deleted-Meldung eingebunden. -->
				<s-uv-auswahl-loeschen :sichtbar="loeschbar" :items="loeschAuswahl" :art="titel" :bezeichnung="loeschBezeichnung"
					:loeschen :disabled="bearbeitung !== null || !loeschbar" @deleted="geloescht" />
				<svws-ui-button type="secondary" :disabled="bearbeitung !== null" @click="props.schliessen()">Schließen</svws-ui-button>
			</div>
		</div>

		<div v-if="gueltigeAuswahl" class="min-h-0 flex-1 overflow-auto">
			<s-uv-lerngruppen v-if="art === 'lerngruppen'"
				:lerngruppe="manager.lerngruppeGetByIdOrNull(id) ?? undefined"
				:schuljahr
				:bearbeitung
				:zuordnung-pending
				:schueler-entfernen
				:bearbeiten="props.bearbeiten"
				:schuelergruppe-erstellen="props.schuelergruppeErstellen"
				:goto-klasse="idKlasse => eintragAuswaehlen('klassen', idKlasse)"
				:goto-lehrer="idLehrer => eintragAuswaehlen('lehrer', idLehrer)"
				:goto-schiene="idSchiene => eintragAuswaehlen('schienen', idSchiene)"
				:goto-schueler="idSchueler => eintragAuswaehlen('schueler', idSchueler)" />

			<s-uv-klassen v-else-if="art === 'klassen'" :klasse="manager.klasseGetByIdOrNull(id) ?? undefined"
				:schuljahr :bearbeitung :zuordnung-pending :schueler-entfernen :bearbeiten="props.bearbeiten"
				:schuelergruppe-erstellen="props.schuelergruppeErstellen"
				:goto-schueler="idSchueler => eintragAuswaehlen('schueler', idSchueler)" :goto-klasse="idKlasse => eintragAuswaehlen('klassen', idKlasse)"
				:goto-lehrer="idLehrer => eintragAuswaehlen('lehrer', idLehrer)" :goto-stundentafel="idTafel => grunddatenOeffnen('stundentafeln', idTafel)" />

			<s-uv-schueler v-else-if="art === 'schueler'"
				:schueler="plan === null ? undefined : manager.planungsabschnittSchuelerGetByIdOrNull(plan.id, id) ?? undefined"
				:entwuerfe="schuelerAenderungsentwuerfe" :goto-schueler="gotoSchildSchueler" :goto-klasse="idKlasse => eintragAuswaehlen('klassen', idKlasse)" />

			<s-uv-schienen v-else-if="art === 'schienen'" :schiene="manager.schieneGetByIdOrNull(id) ?? undefined"
				:goto-lerngruppe="idGruppe => eintragAuswaehlen('lerngruppen', idGruppe)" :bearbeitung :bearbeiten="props.bearbeiten" />

			<s-uv-lehrer v-else-if="art === 'lehrer'"
				:lehrer="manager.lehrerGetByIdOrNull(id)"
				:grunddaten-oeffnen="idLehrer => grunddatenOeffnen('lehrer', idLehrer)"
				:lerngruppe-auswaehlen="idGruppe => eintragAuswaehlen('lerngruppen', idGruppe)" />

			<s-uv-regel v-else-if="art === 'regeln'" :regel />
		</div>
		<p v-else class="p-4 text-sm text-ui-secondary">Dieses Objekt gehört nicht zum gewählten Planungsabschnitt oder ist nicht mehr vorhanden.</p>
	</section>
</template>

<script setup lang="ts">
	import { computed } from 'vue';

	import { useUvState } from '@ui/states/UvState';

	import SUvAuswahlLoeschen from '../SUvAuswahlLoeschen.vue';
	import { uvObjektTitel } from '../UvPresenter';

	import SUvKlassen from './details/SUvKlassen.vue';
	import SUvLehrer from './details/SUvLehrer.vue';
	import SUvLerngruppen from './details/SUvLerngruppen.vue';
	import SUvRegel from './details/SUvRegel.vue';
	import SUvSchienen from './details/SUvSchienen.vue';
	import SUvSchueler from './details/SUvSchueler.vue';
	import { type UvBereichArt, type UvPlanungsAuswahl, type UvPlanungsAuswahlArt, type UvSchuelerAenderungsentwurf, type UvZuordnungArt, type UvZuordnungBearbeitung } from './UvPlanung';
	import { useUvPlanungsManager } from './UvPlanungsKontext';

	const props = defineProps<{
		art: UvPlanungsAuswahlArt;
		schuelerAenderungsentwuerfe: Map<string, UvSchuelerAenderungsentwurf>;
		id: number;
		schuljahr: number;
		gotoGrunddaten: (art: 'lehrer' | 'stundentafeln' | 'raeume', id: number) => Promise<void>;
		gotoSchildSchueler: (id: number) => Promise<void>;
		addSchueler: (idSchuelergruppe: number, idSchueler: number) => Promise<void>;
		zuordnungPending: boolean;
		schuelerEntfernen: (idSchuelergruppe: number, idSchueler: number) => Promise<void>;
		bearbeitung: UvZuordnungBearbeitung | null;
		schliessen: () => void;
		auswaehlen: (art: UvBereichArt, id: number) => void;
		bearbeiten: (art: UvZuordnungArt, idZiel: number) => void;
		schuelergruppeErstellen: (art: 'klassen' | 'kurse', id: number, bezeichnung: string) => void;
	}>();
	const emit = defineEmits<{
		deleted: [art: UvPlanungsAuswahlArt, id: number];
	}>();

	const state = useUvState();
	const manager = computed(() => state.uvManager);
	const plan = computed(() => state.planungsabschnitt);
	const planungsManager = useUvPlanungsManager();

	// Fachliche Gültigkeit und Löschsperren stammen aus demselben Manager wie die Sammelauswahl.
	const titel = computed(() => uvObjektTitel(props.art));
	const gueltigeAuswahl = computed(() => planungsManager.value?.planungsObjektGueltig(props.art, props.id) ?? false);
	const loeschbar = computed(() => state.hatKompetenzAendern && (planungsManager.value?.planungsObjektLoeschbar(props.art, props.id) ?? false));
	const loeschAuswahl = computed<UvPlanungsAuswahl[]>(() => [{ art: props.art, id: props.id }]);
	const regel = computed(() => props.art === 'regeln' ? state.regelliste.find(item => item.id === props.id) : undefined);

	function loeschBezeichnung(item: UvPlanungsAuswahl): string {
		return `${uvObjektTitel(item.art)} (ID: ${item.id})`;
	}

	async function loeschen(items: UvPlanungsAuswahl[]): Promise<void> {
		if (items.length === 0) {
			return;
		}

		const item = items[0];
		await state.planung.deletePlanungsAuswahl(item.art, items.map(eintrag => eintrag.id));
	}

	function geloescht(items: UvPlanungsAuswahl[]): void {
		if (items.length > 0) {
			const item = items[0];
			emit('deleted', item.art, item.id);
		}
	}

	function eintragAuswaehlen(art: UvBereichArt, id: number): void {
		if (props.bearbeitung !== null) {
			return;
		}

		props.auswaehlen(art, id);
	}

	async function grunddatenOeffnen(art: 'lehrer' | 'stundentafeln' | 'raeume', id: number): Promise<void> {
		if (props.bearbeitung !== null) {
			return;
		}

		await props.gotoGrunddaten(art, id);
	}

</script>
