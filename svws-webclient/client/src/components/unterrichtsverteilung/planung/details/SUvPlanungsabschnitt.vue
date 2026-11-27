<template>
	<div v-if="manager().hasDaten()" class="flex flex-col gap-3 p-3">
		<div class="h-full overflow-y-auto w-full flex flex-col gap-8">
			<svws-ui-content-card :inert="bearbeitung !== null" class="uv-deaktivierbar">
				<template #title>
					<div class="content-card--header"><h3 class="content-card--headline" title="Allgemein">Allgemein</h3>&nbsp;<svws-ui-badge type="light" title="ID" class="font-mono" size="big"> ID: {{ manager().auswahl().id }} </svws-ui-badge></div>
				</template>
				<div class="flex flex-wrap items-center gap-2">
					<svws-ui-checkbox :readonly type="toggle" :disabled="loading || (!manager().auswahl().aktiv && (!zeitraumGueltig || ueberschneidung))"
						:model-value="manager().auswahl().aktiv" @update:model-value="handleChangeAktiv" />
					<span>Planungsabschnitt aktiv</span>
					<span v-if="ueberschneidung" class="text-ui-caution"><span class="icon icon-ui-caution i-ri-alert-line" /> Konflikt mit anderem aktiven Planungsabschnitt</span>
				</div>
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input class="contentFocusField"
						span="full"
						:readonly
						:disabled="loading"
						placeholder="Bezeichnung"
						:model-value="manager().auswahl().beschreibung"
						:valid="UvPlanungsabschnitteListeManager.validateBezeichnung"
						@change="handleChangeBezeichnung" />
					<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
						<svws-ui-text-input :readonly
							:disabled="loading"
							placeholder="Gültig ab"
							:model-value="gueltigData.gueltigVon"
							:valid="validateGueltigVon"
							@change="handleChangeGueltigVon"
							type="date" />
						<svws-ui-text-input :readonly
							:disabled="loading"
							placeholder="Gültig bis"
							:model-value="gueltigData.gueltigBis"
							:valid="validateGueltigBis"
							@change="handleChangeGueltigBis"
							type="date" />
					</div>
				</svws-ui-input-wrapper>
				<p v-if="!manager().auswahl().aktiv && !zeitraumGueltig" class="text-ui-caution">Bitte den Gültigkeitszeitraum prüfen.</p>
				<svws-ui-notification v-if="speicherfehler !== ''" type="error">{{ speicherfehler }}</svws-ui-notification>
				<svws-ui-button v-if="zeitraumGeaendert && zeitraumSpeicherbar && !readonly"
					type="secondary"
					:disabled="loading"
					@click="zeitraumSpeichern">
					Zeitraum speichern
				</svws-ui-button>
			</svws-ui-content-card>
			<svws-ui-content-card title="Zugeordnete Zeitraster">
				<template #actions v-if="!readonly"><svws-ui-button v-if="bearbeitung === null" type="secondary" @click="props.bearbeiten('abschnitt-zeitraster', manager().auswahl().id)">Bearbeiten</svws-ui-button></template>
				<p v-if="listZeitraster.length === 0" class="text-ui-secondary">Es gibt kein passendes Zeitraster. Unter Grunddaten → Zeitraster kann eines angelegt oder dessen Gültigkeitszeitraum angepasst werden.</p>
				<div v-if="zugeordneteZeitraster.length > 0" class="flex flex-col gap-2">
					<div v-for="zeitraster in zugeordneteZeitraster"
						:key="zeitraster.id"
						class="uv-deaktivierbar rounded-lg border border-ui-25 bg-ui p-3 hover:border-ui-brand"
						:inert="bearbeitung?.art === 'zeitraster-jahrgang' && bearbeitung.idZiel !== zeitraster.id">
						<div class="flex items-center justify-between gap-2">
							<button type="button"
								class="min-w-0 truncate text-left font-semibold text-ui-brand hover:underline"
								:disabled="bearbeitung !== null"
								@click="gotoZeitraster(zeitraster.id)">
								{{ zeitraster.bezeichnung }}
							</button>
							<svws-ui-button v-if="bearbeitung?.art === 'abschnitt-zeitraster'"
								type="danger"
								:disabled="loading"
								@click="removeZeitraster(zeitraster)">
								Entfernen
							</svws-ui-button>
						</div>
						<s-uv-jahrgang-zuordnung class="mt-2"
							titel="Jahrgänge"
							:jahrgaenge="jahrgaengeZeitraster(zeitraster)"
							art="zeitraster-jahrgang"
							:id-ziel="zeitraster.id"
							:bearbeitung
							:readonly
							:loading
							:bearbeiten="() => props.bearbeiten('zeitraster-jahrgang', zeitraster.id)"
							:entfernen="id => removeZeitrasterJahrgang(zeitraster.id, id)" />
					</div>
				</div>
				<p v-else-if="listZeitraster.length > 0" class="text-sm text-ui-secondary">Keine Zeitraster zugeordnet.</p>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { UvPlanungsabschnitt } from "@core/core/data/uv/UvPlanungsabschnitt";
	import type { UvZeitraster } from "@core/core/data/uv/UvZeitraster";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useUvState } from "@ui/states/UvState";
	import { UvPlanungsabschnitteListeManager } from "@ui/ui/manager/unterrichtsverteilung/UvPlanungsabschnitteListeManager";

	import SUvJahrgangZuordnung from '../SUvJahrgangZuordnung.vue';
	import type { UvZuordnungArt, UvZuordnungBearbeitung } from "../UvPlanung";

	const props = defineProps<{
		benutzerKompetenzen: Set<BenutzerKompetenz>;
		manager: () => UvPlanungsabschnitteListeManager;
		patch: (data: Partial<UvPlanungsabschnitt>) => Promise<boolean>;
		gotoZeitraster: (id: number) => Promise<void>;
		bearbeitung: UvZuordnungBearbeitung | null;
		bearbeiten: (art: UvZuordnungArt, idZiel: number) => void;
	}>();

	const state = useUvState();
	const manager = () => props.manager();

	const readonly = computed<boolean>(() => !(props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN)));

	const loading = ref(false);
	const speicherfehler = ref('');

	function canChange(): boolean {
		if (loading.value || readonly.value || state.planungsabschnitt === null) {
			return false;
		}
		return true;
	}

	async function savePatch(data: Partial<UvPlanungsabschnitt>): Promise<void> {
		if (!canChange() || props.bearbeitung !== null) {
			return;
		}
		const id = manager().auswahl().id;
		loading.value = true;
		speicherfehler.value = '';
		try {
			if (!await props.patch(data) && manager().auswahl().id === id) {
				speicherfehler.value = 'Die Änderung wurde nicht gespeichert.';
			}
		} catch (error) {
			if (manager().auswahl().id === id) {
				speicherfehler.value = error instanceof Error ? error.message : 'Die Änderung wurde nicht gespeichert.';
			}
		} finally {
			loading.value = false;
		}
	}

	const gueltigData = ref<{ gueltigVon: string; gueltigBis: string | null }>({ gueltigVon: '', gueltigBis: null });
	const zeitraumGueltig = computed(() => istZeitraumGueltig(gueltigData.value.gueltigVon, gueltigData.value.gueltigBis));
	const ueberschneidung = computed(() => zeitraumGueltig.value
		&& !manager().istKonfliktfreiZuAktivenPlanungsabschnitten(gueltigData.value.gueltigVon, gueltigData.value.gueltigBis));
	const zeitraumSpeicherbar = computed(() => zeitraumGueltig.value && (!manager().auswahl().aktiv || !ueberschneidung.value));
	const zeitraumGeaendert = computed(() => gueltigData.value.gueltigVon !== manager().auswahl().gueltigVon
		|| gueltigData.value.gueltigBis !== manager().auswahl().gueltigBis);

	const listZeitraster = computed<UvZeitraster[]>(() => [...state.uvManager.zeitrasterGetMengeGueltigByPlanungsabschnitt(manager().auswahl())]);
	const zugeordneteZeitraster = computed(() => listZeitraster.value.filter(zeitrasterIsZugeordnet));


	function zeitrasterIsZugeordnet(zeitraster: UvZeitraster): boolean {
		const planungsabschnitt = state.planungsabschnitt;
		if (planungsabschnitt === null) {
			return false;
		}
		return state.uvManager.zeitrasterIsInPlanungsabschnitt(zeitraster, planungsabschnitt);
	}

	watch([
		() => manager().auswahl().id,
		() => manager().auswahl().gueltigVon,
		() => manager().auswahl().gueltigBis,
	], ([, gueltigVon, gueltigBis]) => {
		gueltigData.value = { gueltigVon, gueltigBis };
		speicherfehler.value = '';
	}, { immediate: true });

	function istZeitraumGueltig(von: string | null, bis: string | null): boolean {
		return manager().validateGueltigVon(von, bis, false, false, false)
			&& (bis === null || manager().validateGueltigBis(von, bis, false, false, false));
	}

	function validateGueltigVon(von: string | null): boolean {
		return !manager().auswahl().aktiv || istZeitraumGueltig(von, gueltigData.value.gueltigBis);
	}

	function validateGueltigBis(bis: string | null): boolean {
		return !manager().auswahl().aktiv || istZeitraumGueltig(gueltigData.value.gueltigVon, bis === '' ? null : bis);
	}

	async function handleChangeAktiv(aktiv: boolean): Promise<void> {
		if (aktiv && (!zeitraumGueltig.value || ueberschneidung.value)) {
			return;
		}
		await savePatch(zeitraumGueltig.value ? { ...gueltigData.value, aktiv } : { aktiv });
	}

	async function handleChangeBezeichnung(beschreibung: string | null): Promise<void> {
		if (beschreibung !== null && beschreibung !== '') {
			await savePatch({ beschreibung });
		}
	}

	async function zeitraumSpeichern(): Promise<void> {
		if (zeitraumGeaendert.value && zeitraumSpeicherbar.value) {
			await savePatch({ ...gueltigData.value });
		}
	}

	function handleChangeGueltigVon(gueltigVon: string | null): void {
		gueltigData.value.gueltigVon = gueltigVon ?? '';
		void zeitraumSpeichern();
	}

	function handleChangeGueltigBis(gueltigBis: string | null): void {
		gueltigData.value.gueltigBis = gueltigBis === '' ? null : gueltigBis;
		void zeitraumSpeichern();
	}

	async function removeZeitraster(zeitraster: UvZeitraster): Promise<void> {
		if (!canChange() || props.bearbeitung?.art !== 'abschnitt-zeitraster') {
			return;
		}
		loading.value = true;
		try {
			await state.planung.removeZeitrasterZuordnung(zeitraster.id);
		} finally {
			loading.value = false;
		}
	}

	async function patchZeitrasterJahrgaenge(idZeitraster: number, jahrgaenge: JahrgangsDaten[]) {
		if (!canChange()) {
			return;
		}
		loading.value = true;
		try {
			const zuordnung = state.uvManager.planungsabschnittZeitrasterGetByIdOrException(manager().auswahl().id, idZeitraster);
			await state.planung.patchZeitrasterZuordnung(zuordnung, jahrgaenge);
		} finally {
			loading.value = false;
		}
	}

	function jahrgaengeZeitraster(zeitraster: UvZeitraster): JahrgangsDaten[] {
		return [...state.uvManager.jahrgangsdatenGetMengeByPlanungsabschnittAndZeitraster(manager().auswahl(), zeitraster)];
	}

	async function removeZeitrasterJahrgang(idZeitraster: number, idJahrgang: number): Promise<void> {
		const zeitraster = state.uvManager.zeitrasterGetByIdOrNull(idZeitraster);
		if (zeitraster !== null) {
			await patchZeitrasterJahrgaenge(idZeitraster, jahrgaengeZeitraster(zeitraster).filter(item => item.id !== idJahrgang));
		}
	}

</script>
