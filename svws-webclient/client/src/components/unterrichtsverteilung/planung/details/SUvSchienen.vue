<template>
	<div v-if="schiene !== undefined" class="flex flex-col gap-3 p-3">
		<svws-ui-notification v-if="fehler !== ''" type="error">{{ fehler }}</svws-ui-notification>
		<svws-ui-content-card class="uv-deaktivierbar col-span-full" :inert="bearbeitung !== null">
			<template #title>
				<div class="content-card--header"><h3 class="content-card--headline" title="Daten der Schiene">Daten der Schiene</h3>&nbsp;<svws-ui-badge type="light" title="ID" class="font-mono" size="big"> ID: {{ schiene.id }} </svws-ui-badge></div>
			</template>
			<svws-ui-input-wrapper :key="eingabeVersion" :grid="2">
				<svws-ui-input-number :readonly="!hatKompetenzAendern"
					:disabled="loading"
					placeholder="Nummer"
					:model-value="form.nummer"
					@change="onNummerChange"
					:min="1" />
				<svws-ui-text-input :readonly="!hatKompetenzAendern"
					:disabled="loading"
					placeholder="Bezeichnung"
					:model-value="form.bezeichnung"
					@change="bezeichnung => patch({ bezeichnung })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card class="uv-deaktivierbar col-span-full" :inert="bearbeitung !== null && bearbeitung.art !== 'schiene-jahrgang'"><s-uv-jahrgang-zuordnung titel="Jahrgänge" :jahrgaenge="selectedJahrgaenge" art="schiene-jahrgang" :id-ziel="schiene.id" :bearbeitung :readonly="!hatKompetenzAendern" :loading :bearbeiten="props.bearbeiten" :entfernen="removeJahrgang" /></svws-ui-content-card>
		<svws-ui-content-card title="Lerngruppen in dieser Schiene"
			class="uv-deaktivierbar col-span-full"
			:inert="bearbeitung !== null && bearbeitung.art !== 'schiene-lerngruppe'">
			<template #actions v-if="hatKompetenzAendern"><svws-ui-button v-if="bearbeitung === null" type="secondary" @click="props.bearbeiten('schiene-lerngruppe', schiene.id)">Bearbeiten</svws-ui-button></template>
			<div v-if="lerngruppenZeilen.length > 0" class="flex flex-col gap-2">
				<div v-for="row in lerngruppenZeilen"
					:key="row.zuordnung.idLerngruppe"
					class="flex min-w-0 items-center gap-2 rounded border border-ui-25 bg-ui p-2 hover:border-ui-brand">
					<button type="button"
						class="flex min-w-0 flex-1 items-center gap-2 text-left focus-visible:outline-2 focus-visible:outline-ui-brand"
						:disabled="bearbeitung !== null"
						@click="gotoLerngruppe(row.lerngruppe.id)">
						<s-uv-fach-badge :id-fach="presenter.lerngruppeFachId(row.lerngruppe)" />
						<span class="min-w-0 flex-1"><span class="block truncate text-sm font-semibold">{{ row.bezeichnung }}</span><span class="block text-xs text-ui-secondary">{{ row.typ }}</span></span>
						<span class="shrink-0 text-xs text-ui-secondary">{{ row.wochenstunden }} h</span>
					</button>
					<svws-ui-button v-if="bearbeitung?.art === 'schiene-lerngruppe'"
						type="danger"
						:disabled="loading"
						@click="removeLerngruppe(row.zuordnung)">
						Entfernen
					</svws-ui-button>
				</div>
			</div>
			<p v-else class="text-sm text-ui-secondary">Keine Lerngruppen in dieser Schiene.</p>
		</svws-ui-content-card>
	</div>
	<div v-else class="p-3">
		<span class="text-ui-disabled">Keine Schiene ausgewählt</span>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import type { UvLerngruppenSchiene } from '@core/core/data/uv/UvLerngruppenSchiene';
	import { UvSchiene } from "@core/core/data/uv/UvSchiene";
	import { useUvState } from "@ui/states/UvState";

	import SUvFachBadge from "../../SUvFachBadge.vue";
	import { createUvPresenter } from "../../UvPresenter";
	import SUvJahrgangZuordnung from '../SUvJahrgangZuordnung.vue';
	import type { UvZuordnungArt, UvZuordnungBearbeitung } from '../UvPlanung';

	const props = defineProps<{
		schiene: UvSchiene | undefined;
		gotoLerngruppe: (id: number) => void;
		bearbeitung: UvZuordnungBearbeitung | null;
		bearbeiten: (art: UvZuordnungArt, idZiel: number) => void;
	}>();

	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const form = computed(() => props.schiene ?? new UvSchiene());
	const fehler = ref('');
	const eingabeVersion = ref(0);

	watch(() => props.schiene?.id, () => {
		fehler.value = '';
	});

	async function speichern(id: number, aktion: () => Promise<void>, eingabenZuruecksetzen = false): Promise<void> {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		fehler.value = '';
		try {
			await aktion();
		} catch (error) {
			if (props.schiene?.id === id) {
				fehler.value = error instanceof Error ? error.message : 'Die Änderung wurde nicht gespeichert.';
				if (eingabenZuruecksetzen) {
					eingabeVersion.value++;
				}
			}
		} finally {
			loading.value = false;
		}
	}

	function patch(changes: Partial<UvSchiene>): void {
		const id = props.schiene?.id;
		if (id !== undefined && props.bearbeitung === null) {
			void speichern(id, () => state.planung.patchSchiene(id, changes), true);
		}
	}

	function onNummerChange(nummer: number | null | undefined): void {
		const schiene = props.schiene;
		if (schiene === undefined) {
			return;
		}
		if (nummer === null || nummer === undefined) {
			eingabeVersion.value++;
			return;
		}
		patch({ nummer });
	}


	const jahrgangItems = computed(() => [...state.uvManager.jahrgangsdatenGetMenge()]);

	const selectedJahrgaenge = computed(() => {
		if (props.schiene === undefined) {
			return [];
		}
		const idsErlaubt = new Set(form.value.idsJahrgaengeErlaubt);
		return jahrgangItems.value.filter(j => idsErlaubt.has(j.id));
	});

	async function removeJahrgang(id: number): Promise<void> {
		const idSchiene = props.schiene?.id;
		if (idSchiene === undefined || props.bearbeitung?.art !== 'schiene-jahrgang' || props.bearbeitung.idZiel !== idSchiene) {
			return;
		}
		await speichern(idSchiene, () => state.planung.aendereSchienenJahrgang(idSchiene, id, false));
	}

	const lerngruppenZeilen = computed(() => props.schiene === undefined ? [] : presenter.value.schienenLerngruppenZeilen(props.schiene));

	async function removeLerngruppe(zuordnung: UvLerngruppenSchiene): Promise<void> {
		const idSchiene = props.schiene?.id;
		if (idSchiene === undefined || props.bearbeitung?.art !== 'schiene-lerngruppe' || props.bearbeitung.idZiel !== idSchiene) {
			return;
		}
		await speichern(idSchiene, () => state.planung.delLerngruppenSchiene([zuordnung]));
	}

</script>
