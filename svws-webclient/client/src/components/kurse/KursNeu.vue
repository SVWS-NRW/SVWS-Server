<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<template #actions>
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :readonly>Ist sichtbar</svws-ui-checkbox>
				</template>
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel"
						v-model="model.proxy.kuerzel"
						:validation="() => model.getFehler('kuerzel')"
						required :max-len="20" :readonly />
					<ui-select label="Fach"
						v-model="model.fach.value"
						:manager="fachSelectManager"
						:validation="() => model.getFehler('idFach')"
						:readonly required statistics :removable="false" />
					<ui-select label="Kursart"
						v-model="model.kursartAllg.value"
						:manager="kursartSelectManager"
						:validation="() => model.getFehler('kursartAllg')"
						:readonly :removable="false" required statistics />
					<svws-ui-input-number placeholder="Wochenstunden des Kurses"
						v-model="model.proxy.wochenstunden"
						:validation="() => model.getFehler('wochenstunden')"
						:readonly statistics :min="0" :max="40" required />
					<ui-select-multi label="Jahrgänge"
						v-model="model.jahrgaenge.value"
						:manager="jahrgaengeSelectManager"
						statistics :readonly />
					<svws-ui-text-input placeholder="Zeugnisbezeichnung"
						v-model="model.proxy.bezeichnungZeugnis"
						:validation="() => model.getFehler('bezeichnungZeugnis')"
						:max-len="130" :readonly />
					<ui-select label="Fortschreibungsart"
						v-model="model.fortschreibungsart.value"
						:manager="fortschreibungsartManager"
						:validation="() => model.getFehler('idKursFortschreibungsart')"
						:readonly required :removable="false" />
					<ui-select-multi label="Schienen"
						v-model="model.schienenArray.value"
						:manager="schienenSelectManager"
						:readonly />
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						:min="0" :readonly required />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card title="Lehrkraft">
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Lehrkraft"
						v-model="model.lehrer.value"
						:manager="lehrerSelectManager"
						:readonly statistics />
					<svws-ui-input-number placeholder="Wochenstunden der Lehrkraft"
						v-model="model.proxy.wochenstundenLehrer"
						:validation="() => model.getFehler('wochenstundenLehrer')"
						statistics :min="0" required :readonly
						:disabled="model.proxy.idLehrer === null" />
				</svws-ui-input-wrapper>
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
					<svws-ui-button @click="addKurs" :disabled="!isValid || !hatKompetenzAdd">Speichern</svws-ui-button>
				</div>
			</svws-ui-content-card>
		</div>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import { KursDaten } from "@core/asd/data/kurse/KursDaten";
	import type { FachDaten } from "@core/core/data/fach/FachDaten";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { KursFortschreibungsart } from "@core/core/types/KursFortschreibungsart";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	import type { KurseNeuProps } from "~/components/kurse/KurseNeuProps";
	import { KursModelProxy } from "~/components/kurse/modelproxy/KursModelProxy";
	import { useKurseAuswahlState } from "~/states/kurse/KurseAuswahlState";
	import { getKursarten } from "~/utils/helfer";

	const abschnittState = useAbschnittState();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();
	const kurseAuswahlState = useKurseAuswahlState();

	const props = defineProps<KurseNeuProps>();
	const initialData = ref<KursDaten>(Object.assign(new KursDaten(), { wochenstunden: 0, wochenstundenLehrer: 0, istSichtbar: true }));
	const model = new KursModelProxy(() => initialData.value);

	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzAdd.value);
	const isLoading = ref<boolean>(false);
	const isValid = computed<boolean>(() => !model.hatBlockierendeFehler());

	const kursarten = computed<Map<string, string>>(() => getKursarten(schuleState.schuljahr));

	// --- manager ---

	const kursartSelectManager = new SelectManager({
		options: computed(() => [...kursarten.value.keys()]),
		optionDisplayText: k => `${k} (${kursarten.value.get(k) ?? '???'})`,
		selectionDisplayText: k => `${k} (${kursarten.value.get(k) ?? '???'})`,
	});

	const lehrerSelectManager = new SelectManager<LehrerListeEintrag>({
		options: computed(() => {
			const result = new ArrayList<LehrerListeEintrag>();
			for (const l of kurseAuswahlState.lehrer.list()) {
				if (l.istAktiv && l.istSichtbar) {
					result.add(l);
				}
			}
			return result;
		}),
		optionDisplayText: l => `${l.kuerzel} - ${l.vorname} ${l.nachname}`,
		selectionDisplayText: l => `${l.kuerzel} - ${l.vorname} ${l.nachname}`,
	});

	const fachSelectManager = new SelectManager({
		options: computed(() => {
			const result = new ArrayList<FachDaten>();
			for (const f of kurseAuswahlState.faecher.list()) {
				if (f.istSichtbar) {
					result.add(f);
				}
			}
			return result;
		}),
		optionDisplayText: fach => `${fach.kuerzel} (${fach.bezeichnung})`,
		selectionDisplayText: fach => `${fach.kuerzel} (${fach.bezeichnung})`,
	});

	const jahrgaengeSelectManager = new SelectManager<JahrgangsDaten>({
		options: computed(() => kurseAuswahlState.jahrgaenge.list()),
		optionDisplayText: jg => jg.kuerzel ?? '',
		selectionDisplayText: jg => jg.kuerzel ?? '',
	});

	const fortschreibungsartManager = new SelectManager<KursFortschreibungsart>({
		options: computed(() => KursFortschreibungsart.values()),
		optionDisplayText: v => v.beschreibung,
		selectionDisplayText: v => v.beschreibung,
	});

	const schienenSelectManager = new SelectManager<number>({
		options: Array.from({ length: 40 }, (_, i) => i + 1),
		optionDisplayText: i => 'Schiene ' + i,
		selectionDisplayText: i => 'Schiene ' + i,
		sort: (a, b) => a - b,
	});

	// --- util ---

	async function addKurs() {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;
		model.proxy.idSchuljahresabschnitt = abschnittState.auswahl.id;
		// Die Wochenstunden der Lehrkraft werden nur übertragen, wenn eine Lehrkraft gesetzt ist
		const { id, weitereLehrer, wochenstundenLehrer, schueler, ...rest } = model.proxy;
		const partialData = (rest.idLehrer === null) ? rest : { ...rest, wochenstundenLehrer };
		await kurseAuswahlState.add(partialData);
		isLoading.value = false;
	}

	function cancel() {
		props.checkpoint.active = false;
		void kurseAuswahlState.navigator.select(null);
	}

	watch(() => model.proxy, async () => {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
