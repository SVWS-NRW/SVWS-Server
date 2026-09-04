<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						v-model="model.proxy.kuerzel"
						@change="model.patch"
						:validation="() => model.getFehler('kuerzel')"
						:max-len="20" required :disabled="!hatKompetenzAdd" />
					<svws-ui-text-input placeholder="Bezeichnung"
						v-model="model.proxy.bezeichnung"
						@change="model.patch"
						:validation="() => model.getFehler('bezeichnung')"
						:max-len="255" required :disabled="!hatKompetenzAdd" />
					<ui-select label="Fach ASD-Schlüssel"
						v-model="model.selectedFach.value"
						:manager="fachKuerzelSelectManager"
						:validation="() => model.getFehler('kuerzelStatistik')"
						required :removable="false" statistics :disabled />
					<ui-select label="Fach ASD-Text"
						v-model="model.selectedFach.value"
						:manager="fachTextSelectManager"
						required :removable="false" statistics :disabled />
					<ui-select label="Bilinguale Sachfachsprache"
						v-model="model.selectedSachfachsprache.value"
						:manager="sachfachspracheManager"
						statistics :disabled="!hatKompetenzAdd" />
					<svws-ui-text-input placeholder="Fachgruppe"
						:model-value="fachgruppe"
						readonly />
					<ui-select label="Aufgabenfeld" v-if="istBerufskolleg"
						v-model="model.selectedAufgabenfeld.value"
						:manager="aufgabenfeldManager"
						:disabled="!hatKompetenzAdd" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Zeugnis">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-checkbox v-model="model.proxy.aufZeugnis" :disabled="!hatKompetenzAdd" focus-class-content>
						Auf Zeugnis
					</svws-ui-checkbox>
					<svws-ui-spacing />
					<svws-ui-text-input placeholder="Bezeichnung (Zeugnis)"
						v-model="model.proxy.bezeichnungZeugnis"
						@change="model.patch"
						:validation="() => model.getFehler('bezeichnungZeugnis')"
						:max-len="255" :disabled="!hatKompetenzAdd" />
					<svws-ui-text-input placeholder="Bezeichnung (Überweisungszeugnis)"
						v-model="model.proxy.bezeichnungUeberweisungszeugnis"
						@change="model.patch"
						:validation="() => model.getFehler('bezeichnungUeberweisungszeugnis')"
						:max-len="255" :disabled="!hatKompetenzAdd" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Sonstiges">
				<svws-ui-input-wrapper :grid="1">
					<template v-if="hatGymnasialeOberstufe">
						<svws-ui-checkbox v-model="model.proxy.istOberstufenFach" :disabled="!hatKompetenzAdd">
							Fach der Oberstufe
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="model.proxy.istPruefungsordnungsRelevant" :disabled="!hatKompetenzAdd">
							Ist Prüfungsordnungs-relevant (z.B. bei Belegprüfungen)
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="model.proxy.istMoeglichAlsNeueFremdspracheInSekII" :disabled="!hatKompetenzAdd">
							Ist in der Oberstufe eine neu einsetzende Fremdsprache
						</svws-ui-checkbox>
					</template>
					<svws-ui-checkbox v-model="model.proxy.istFremdsprache" :disabled="!hatKompetenzAdd">
						Ist eine Fremdsprache
					</svws-ui-checkbox>
					<template v-if="!istGrundschule">
						<svws-ui-checkbox v-model="model.proxy.istNachpruefungErlaubt" :disabled="!hatKompetenzAdd">
							Nachprüfung erlaubt
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="model.proxy.istSchriftlichZK" :disabled="!hatKompetenzAdd">
							Schriftliches Fach für ZK
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="model.proxy.holeAusAltenLernabschnitten" :disabled="!hatKompetenzAdd">
							Berücksichtigen beim Holen von abgeschlossenen Fächern
						</svws-ui-checkbox>
					</template>
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-input-number placeholder="maximale Zeichenanzahl in Fachbemerkungen"
							v-model="model.proxy.maxZeichenInFachbemerkungen"
							@change="model.patch"
							:validation="() => model.getFehler('maxZeichenInFachbemerkungen')"
							:min="0" :max="JavaInteger.MAX_VALUE" :disabled="!hatKompetenzAdd" />
					</svws-ui-input-wrapper>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						@change="model.patch"
						:validation="() => model.getFehler('sortierung')"
						:min="0" :max="32000"
						:disabled
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :disabled="!hatKompetenzAdd">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addFach" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { BilingualeSprache } from "@core/asd/types/fach/BilingualeSprache";
	import { Fach } from "@core/asd/types/fach/Fach";
	import { Schulform } from "@core/asd/types/schule/Schulform";
	import { FachDaten } from "@core/core/data/fach/FachDaten";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { computed, ref, watch } from "vue";
	import { FachModelProxy } from "~/components/schule/kataloge/faecher/modelproxy/FachModelProxy";
	import type { FaecherNeuProps } from "./FaecherNeuProps";
	import { JavaInteger } from "@core/java/lang/JavaInteger";

	const props = defineProps<FaecherNeuProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const initialData = ref<FachDaten>(Object.assign(new FachDaten(), { sortierung: 32000, istSichtbar: true }));
	const model = new FachModelProxy(() => initialData.value, () => props.manager().liste.list(), schuleState.abschnitt.schuljahr);
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);
	const istGrundschule = computed(() => props.manager().schulform() === Schulform.G);
	const istBerufskolleg = computed(() => props.manager().schulform() === Schulform.BK || props.manager().schulform() === Schulform.SB);
	const hatGymnasialeOberstufe = computed(() => props.manager().schulform().daten(schuleState.abschnitt.schuljahr)?.hatGymOb ?? false);
	const fachgruppe = computed(() => Fach.getBySchluesselOrDefault(model.proxy.kuerzelStatistik).getFachgruppe(schuleState.abschnitt.schuljahr)?.daten(schuleState.abschnitt.schuljahr)?.text ?? '—');

	const formIsValid = computed(() => model.getAlleFehler().isEmpty());

	const fachKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Fach.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: v => v.schluessel,
		selectionDisplayText: v => v.schluessel,
	});

	const fachTextSelectManager = new CoreTypeSelectManager({
		clazz: Fach.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const sachfachspracheManager = new CoreTypeSelectManager({
		clazz: BilingualeSprache.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const aufgabenfeldManager = new SelectManager({
		options: ["1", "2", "3"],
		optionDisplayText: (v: string) => getTextAufgabenfeld(v),
		selectionDisplayText: (v: string) => getTextAufgabenfeld(v),
	});

	function getTextAufgabenfeld(aufgabenfeld: string | null): string {
		switch (aufgabenfeld) {
			case '1': return 'Aufgabenfeld I';
			case '2': return 'Aufgabenfeld II';
			case '3': return 'Aufgabenfeld III';
			default: return '';
		}
	}

	// --- util ---

	async function addFach() {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, referenziertInAnderenTabellen, ...partialData } = model.proxy;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	watch(() => model.proxy, async () => {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
