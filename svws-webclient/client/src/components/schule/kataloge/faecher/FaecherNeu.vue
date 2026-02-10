<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						v-model="data.kuerzel"
						:valid="() => fieldIsValid('kuerzel')"
						:min-len="1" :max-len="20" required :disabled />
					<svws-ui-text-input placeholder="Bezeichnung"
						v-model="data.bezeichnung"
						:valid="() => fieldIsValid('bezeichnung')"
						:min-len="1" :max-len="255" required :disabled />
					<ui-select label="Fach ASD-Kürzel"
						v-model="selectedFach"
						:manager="fachKuerzelSelectManager"
						required :removable="false" statistics :disabled="!hatKompetenzAdd" searchable />
					<ui-select label="Fach ASD-Text"
						v-model="selectedFach"
						:manager="fachTextSelectManager"
						required :removable="false" statistics :disabled="!hatKompetenzAdd" searchable />
					<ui-select label="Bilinguale Sachfachsprache"
						v-model="selectedSachfachsprache"
						:manager="sachfachspracheManager"
						statistics :disabled searchable />
					<svws-ui-text-input placeholder="Fachgruppe"
						:model-value="fachgruppe"
						readonly />
					<ui-select label="Aufgabenfeld" v-if="istBerufskolleg"
						v-model="data.aufgabenfeld"
						:manager="aufgabenfeldManager"
						:disabled searchable />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Zeugnis">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-checkbox v-model="data.aufZeugnis" :disabled focus-class-content>
						Auf Zeugnis
					</svws-ui-checkbox>
					<svws-ui-spacing />
					<svws-ui-text-input placeholder="Bezeichnung (Zeugnis)"
						v-model="data.bezeichnungZeugnis"
						:valid="() => fieldIsValid('bezeichnungZeugnis')"
						:max-len="255" :disabled />
					<svws-ui-text-input placeholder="Bezeichnung (Überweisungszeugnis)"
						v-model="data.bezeichnungUeberweisungszeugnis"
						:valid="() => fieldIsValid('bezeichnungUeberweisungszeugnis')"
						:max-len="255" :disabled />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Sonstiges">
				<svws-ui-input-wrapper :grid="1">
					<template v-if="hatGymnasialeOberstufe">
						<svws-ui-checkbox v-model="data.istOberstufenFach" :disabled>
							Fach der Oberstufe
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="data.istPruefungsordnungsRelevant" :disabled>
							Ist Prüfungsordnungs-relevant (z.B. bei Belegprüfungen)
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="data.istMoeglichAlsNeueFremdspracheInSekII" :disabled>
							Ist in der Oberstufe eine neu einsetzende Fremdsprache
						</svws-ui-checkbox>
					</template>
					<svws-ui-checkbox v-model="data.istFremdsprache" :disabled>
						Ist eine Fremdsprache
					</svws-ui-checkbox>
					<template v-if="!istGrundschule">
						<svws-ui-checkbox v-model="data.istNachpruefungErlaubt" :disabled>
							Nachprüfung erlaubt
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="data.istSchriftlichZK" :disabled>
							Schriftliches Fach für ZK
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="data.holeAusAltenLernabschnitten" :disabled>
							Berücksichtigen beim Holen von abgeschlossenen Fächern
						</svws-ui-checkbox>
					</template>
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-input-number placeholder="maximale Zeichenanzahl in Fachbemerkungen"
							v-model="data.maxZeichenInFachbemerkungen"
							:valid="() => fieldIsValid('maxZeichenInFachbemerkungen')"
							:min="0" :max="JavaInteger.MAX_VALUE" :disabled />
					</svws-ui-input-wrapper>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.sortierung"
						:valid="() => fieldIsValid('sortierung')" :min="0" :max="32000" :disabled :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="data.istSichtbar" :disabled>
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

	import { computed, ref, watch } from "vue";
	import type { BilingualeSpracheKatalogEintrag, FachKatalogEintrag } from "@core";
	import { BenutzerKompetenz, BilingualeSprache, Fach, FachDaten, JavaInteger, Schulform } from "@core";
	import type { FaecherNeuProps } from "./FaecherNeuProps";
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid, optionalInputIsValid } from "~/util/validation/Validation";
	import { CoreTypeSelectManager, SelectManager } from "@ui";

	const props = defineProps<FaecherNeuProps>();
	const data = ref<FachDaten>(Object.assign(new FachDaten(), { sortierung: 32000, istSichtbar: true }));
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => (data.value.kuerzelStatistik === "") || !hatKompetenzAdd.value);
	const istGrundschule = computed(() => props.manager().schulform() === Schulform.G);
	const istBerufskolleg = computed(() => props.manager().schulform() === Schulform.BK || props.manager().schulform() === Schulform.SB);
	const hatGymnasialeOberstufe = computed(() => props.manager().schulform().daten(schuljahr.value)?.hatGymOb ?? false);
	const schuljahr = computed<number>(() => props.manager().getSchuljahr());
	const fachgruppe = computed(() => Fach.getBySchluesselOrDefault(data.value.kuerzelStatistik).getFachgruppe(schuljahr.value)?.daten(schuljahr.value)?.text ?? '—');

	const selectedFach = computed<FachKatalogEintrag | null>({
		get: () => Fach.data().getEintragBySchuljahrUndSchluessel(props.schuljahr, data.value.kuerzelStatistik),
		set: (eintrag: FachKatalogEintrag | null) => data.value.kuerzelStatistik = eintrag?.schluessel ?? '',
	});

	const selectedSachfachsprache = computed<BilingualeSpracheKatalogEintrag | null>({
		get: () => BilingualeSprache.data().getEintragBySchuljahrUndSchluessel(props.schuljahr, data.value.bilingualeSprache ?? ''),
		set: (value: BilingualeSpracheKatalogEintrag | null) => data.value.bilingualeSprache = value?.schluessel ?? null,
	});

	const fachKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Fach.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const fachTextSelectManager = new CoreTypeSelectManager({
		clazz: Fach.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const sachfachspracheManager = new CoreTypeSelectManager({
		clazz: BilingualeSprache.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
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

	// --- validate ---

	function kuerzelIsValid(kuerzel: string | null): boolean {
		return mandatoryInputIsValid(kuerzel, 20)
			&& isUniqueInList(kuerzel, props.manager().liste.list(), "kuerzel");
	}

	function bezeichnungIsValid(bezeichnung: string | null): boolean {
		return mandatoryInputIsValid(bezeichnung, 255)
			&& isUniqueInList(bezeichnung, props.manager().liste.list(), "bezeichnung");
	}

	function sortierungIsValid(sortierung: number): boolean {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

	function maxZeichenInFachbemerkungenIsValid(value: number | null): boolean {
		return !numberHasDecimals(value)
			&& numberIsValid(value, false, 0, JavaInteger.MAX_VALUE);
	}

	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof FachDaten));
	});

	const fieldIsValid = (field: keyof FachDaten): boolean => {
		switch (field) {
			case "kuerzel":
				return kuerzelIsValid(data.value.kuerzel);
			case "bezeichnung":
				return bezeichnungIsValid(data.value.bezeichnung);
			case 'kuerzelStatistik':
				return mandatoryInputIsValid(data.value.kuerzelStatistik, 2);
			case 'bezeichnungZeugnis':
				return optionalInputIsValid(data.value.bezeichnungZeugnis, 255);
			case 'bezeichnungUeberweisungszeugnis':
				return optionalInputIsValid(data.value.bezeichnungUeberweisungszeugnis, 255);
			case 'maxZeichenInFachbemerkungen':
				return maxZeichenInFachbemerkungenIsValid(data.value.maxZeichenInFachbemerkungen);
			case 'sortierung':
				return sortierungIsValid(data.value.sortierung);
			default:
				return true;
		}
	};

	// --- util ---

	async function addFach() {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, referenziertInAnderenTabellen, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
