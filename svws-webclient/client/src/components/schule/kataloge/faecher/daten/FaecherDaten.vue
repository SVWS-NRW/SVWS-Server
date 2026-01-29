<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						:model-value="manager().daten().kuerzel"
						@change="patchKuerzel"
						:valid="kuerzelIsValid"
						:min-len="1" :max-len="20" required :readonly />
					<svws-ui-text-input placeholder="Bezeichnung"
						:model-value="manager().daten().bezeichnung"
						@change="patchBezeichnung"
						:valid="bezeichnungIsValid"
						:min-len="1" :max-len="255" required :readonly />
					<ui-select label="Fach ASD-Kürzel"
						v-model="selectedFach"
						:manager="fachKuerzelSelectManager"
						:readonly required :removable="false" statistics searchable />
					<ui-select label="Fach ASD-Text"
						v-model="selectedFach"
						:manager="fachTextSelectManager"
						:readonly required :removable="false" statistics searchable />
					<ui-select label="Bilinguale Sachfachsprache"
						v-model="selectedSachfachsprache"
						:manager="sachfachspracheManager"
						:readonly statistics searchable />
					<svws-ui-text-input placeholder="Fachgruppe"
						:model-value="fachgruppe"
						readonly />
					<ui-select label="Aufgabenfeld" v-if="istBerufskolleg"
						v-model="selectedAufgabenfeld"
						:manager="aufgabenfeldManager"
						:readonly searchable />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Zeugnis">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-checkbox v-model="aufZeugnis" :readonly focus-class-content>
						Auf Zeugnis
					</svws-ui-checkbox>
					<svws-ui-spacing />
					<svws-ui-text-input placeholder="Bezeichnung (Zeugnis)"
						:model-value="manager().daten().bezeichnungZeugnis"
						@change="patchBezeichnungZeugnis"
						:valid="v => optionalInputIsValid(v, 255)"
						:max-len="255" :readonly />
					<svws-ui-text-input placeholder="Bezeichnung (Überweisungszeugnis)"
						:model-value="manager().daten().bezeichnungUeberweisungszeugnis"
						@change="patchBezeichnungUeberweisungszeugnis"
						:valid="v => optionalInputIsValid(v, 255)"
						:max-len="255" :readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Sonstiges">
				<svws-ui-input-wrapper :grid="1">
					<template v-if="hatGymnasialeOberstufe">
						<svws-ui-checkbox v-model="istOberstufenFach" :readonly>
							Fach der Oberstufe
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="istPruefungsordnungsRelevant" :readonly>
							Ist Prüfungsordnungs-relevant (z.B. bei Belegprüfungen)
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="istMoeglichAlsNeueFremdspracheInSekII" :readonly>
							Ist in der Oberstufe eine neu einsetzende Fremdsprache
						</svws-ui-checkbox>
					</template>
					<svws-ui-checkbox v-model="istFremdsprache" :readonly>
						Ist eine Fremdsprache
					</svws-ui-checkbox>
					<template v-if="!istGrundschule">
						<svws-ui-checkbox v-model="istNachpruefungErlaubt" :readonly>
							Nachprüfung erlaubt
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="istSchriftlichZK" :readonly>
							Schriftliches Fach für ZK
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="holeAusAltenLernabschnitten" :readonly>
							Berücksichtigen beim Holen von abgeschlossenen Fächern
						</svws-ui-checkbox>
					</template>
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-input-number placeholder="maximale Zeichenanzahl in Fachbemerkungen"
							:model-value="manager().daten().maxZeichenInFachbemerkungen"
							@change="patchMaxZeichenInFachbemerkungen"
							:valid="v => maxZeichenInFachbemerkungenIsValid(v)"
							:min="0" :max="JavaInteger.MAX_VALUE" :readonly />
					</svws-ui-input-wrapper>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						:model-value="manager().daten().sortierung"
						@change="patchSortierung"
						:valid="sortierungIsValid" :min="0" :max="32000" :readonly :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="istSichtbar" :readonly>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { BilingualeSpracheKatalogEintrag, FachKatalogEintrag } from "@core";
	import { BilingualeSprache, Fach, Schulform, JavaInteger, BenutzerKompetenz } from "@core";
	import type { FaecherDatenProps } from "./FaecherDatenProps";
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid, optionalInputIsValid } from "~/util/validation/Validation";
	import { CoreTypeSelectManager, SelectManager } from "@ui";

	const props = defineProps<FaecherDatenProps>();
	const schuljahr = computed<number>(() => props.manager().getSchuljahr());
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);
	const istGrundschule = computed(() => props.manager().schulform() === Schulform.G);
	const istBerufskolleg = computed(() => props.manager().schulform() === Schulform.BK || props.manager().schulform() === Schulform.SB);
	const hatGymnasialeOberstufe = computed(() => props.manager().schulform().daten(schuljahr.value)?.hatGymOb ?? false);

	const fachgruppe = computed<string>(
		() => Fach.getBySchluesselOrDefault(props.manager().daten().kuerzelStatistik).getFachgruppe(props.schuljahr)?.daten(props.schuljahr)?.text ?? '—'
	);

	const selectedFach = computed<FachKatalogEintrag | null>({
		get: () => Fach.data().getEintragBySchuljahrUndSchluessel(props.schuljahr, props.manager().daten().kuerzelStatistik),
		set: (value: FachKatalogEintrag | null) => void patchKuerzelStatistik(value?.schluessel ?? null),
	});

	const selectedSachfachsprache = computed<BilingualeSpracheKatalogEintrag | null>({
		get: () => BilingualeSprache.data().getEintragBySchuljahrUndSchluessel(props.schuljahr, props.manager().daten().bilingualeSprache ?? ''),
		set: (value: BilingualeSpracheKatalogEintrag | null) => void props.patch({ bilingualeSprache: value?.schluessel }),
	});

	const selectedAufgabenfeld = computed<string | null>({
		get: () => props.manager().daten().aufgabenfeld,
		set: (value: string | null) => void props.patch({ aufgabenfeld: value }),
	});

	const aufZeugnis = computed<boolean>({
		get: () => props.manager().daten().aufZeugnis,
		set: (v: boolean) => void props.patch({ 'aufZeugnis': v }),
	});

	const istOberstufenFach = computed<boolean>({
		get: () => props.manager().daten().istOberstufenFach,
		set: (v: boolean) => void props.patch({ 'istOberstufenFach': v }),
	});

	const istPruefungsordnungsRelevant = computed<boolean>({
		get: () => props.manager().daten().istPruefungsordnungsRelevant,
		set: (v: boolean) => void props.patch({ 'istPruefungsordnungsRelevant': v }),
	});

	const istMoeglichAlsNeueFremdspracheInSekII = computed<boolean>({
		get: () => props.manager().daten().istMoeglichAlsNeueFremdspracheInSekII,
		set: (v: boolean) => void props.patch({ 'istMoeglichAlsNeueFremdspracheInSekII': v }),
	});

	const istFremdsprache = computed<boolean>({
		get: () => props.manager().daten().istFremdsprache,
		set: (v: boolean) => void props.patch({ 'istFremdsprache': v }),
	});

	const istNachpruefungErlaubt = computed<boolean>({
		get: () => props.manager().daten().istNachpruefungErlaubt,
		set: (v: boolean) => void props.patch({ 'istNachpruefungErlaubt': v }),
	});

	const istSchriftlichZK = computed<boolean>({
		get: () => props.manager().daten().istSchriftlichZK,
		set: (v: boolean) => void props.patch({ 'istSchriftlichZK': v }),
	});

	const holeAusAltenLernabschnitten = computed<boolean>({
		get: () => props.manager().daten().holeAusAltenLernabschnitten,
		set: (v: boolean) => void props.patch({ 'holeAusAltenLernabschnitten': v }),
	});

	const istSichtbar = computed<boolean>({
		get: () => props.manager().daten().istSichtbar,
		set: (v: boolean) => void props.patch({ 'istSichtbar': v }),
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

	// --- patch ---

	async function patchKuerzel(kuerzel: string | null) {
		if (kuerzelIsValid(kuerzel)) {
			await props.patch({ kuerzel: kuerzel });
		}
	}

	async function patchKuerzelStatistik(value: string | null) {
		if (mandatoryInputIsValid(value, 2)) {
			await props.patch({ kuerzelStatistik: value });
		}
	}

	async function patchBezeichnung(bezeichnung: string | null) {
		if (bezeichnungIsValid(bezeichnung)) {
			await props.patch({ bezeichnung: bezeichnung });
		}
	}

	async function patchBezeichnungZeugnis(value: string | null) {
		if (optionalInputIsValid(value, 255)) {
			await props.patch({ bezeichnungZeugnis: value });
		}
	}

	async function patchBezeichnungUeberweisungszeugnis(value: string | null) {
		if (optionalInputIsValid(value, 255)) {
			await props.patch({ bezeichnungUeberweisungszeugnis: value });
		}
	}

	async function patchMaxZeichenInFachbemerkungen(value: number | null) {
		if (maxZeichenInFachbemerkungenIsValid(value)) {
			await props.patch({ maxZeichenInFachbemerkungen: value });
		}
	}

	async function patchSortierung(sortierung: number | null) {
		if (sortierungIsValid(sortierung)) {
			await props.patch({ sortierung: sortierung });
		}
	}

	// --- validate ---

	function kuerzelIsValid(kuerzel: string | null): kuerzel is string {
		return mandatoryInputIsValid(kuerzel, 20)
			&& isUniqueInList(kuerzel, props.manager().liste.list(), "kuerzel", "id", props.manager().auswahlID() ?? undefined);
	}

	function bezeichnungIsValid(bezeichnung: string | null): bezeichnung is string {
		return mandatoryInputIsValid(bezeichnung, 255)
			&& isUniqueInList(bezeichnung, props.manager().liste.list(), "bezeichnung", "id", props.manager().auswahlID() ?? undefined);
	}

	function maxZeichenInFachbemerkungenIsValid(value: number | null): boolean {
		return !numberHasDecimals(value)
			&& numberIsValid(value, false, 0, JavaInteger.MAX_VALUE);
	}

	function sortierungIsValid(value: number | null): value is number {
		return !numberHasDecimals(value) && numberIsValid(value, true, 0, 32000);
	}

</script>
