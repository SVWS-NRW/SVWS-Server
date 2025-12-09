<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kürzel" required :max-len="20" :valid="(v) => manager().validateKuerzel(v)" v-model="data.kuerzel" :disabled />
				<svws-ui-select title="Statistik-Fach" required :items="statistikFachEintraege" :item-filter="coreTypeDataFilter"
					v-model="selectedStatistikFach" :item-text="getStatistikfachText" autocomplete />
				<svws-ui-text-input placeholder="Bezeichnung" required :max-len="255" :valid="(v) => manager().validateBezeichnung(v)" v-model="data.bezeichnung" :disabled />
				<svws-ui-text-input placeholder="Fachgruppe" :model-value="fachgruppe" disabled />
				<svws-ui-select removable title="Bilinguale Sachfachsprache" :items="BilingualeSprache.values()" v-model="selectedBilingualeSprache"
					:item-text="b => b.daten(schuljahr)?.text ?? '—'" :disabled />
				<svws-ui-input-number placeholder="Sortierung" :valid="(v) => manager().validateSortierung(v)" v-model="data.sortierung" :disabled />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Zeugnis">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-checkbox v-model="data.aufZeugnis" :disabled>Auf Zeugnis</svws-ui-checkbox>
				<div />
				<svws-ui-text-input placeholder="Bezeichnung (Zeugnis)" required v-model="data.bezeichnungZeugnis" :disabled />
				<svws-ui-text-input placeholder="Bezeichnung (Überweisungszeugnis)" required v-model="data.bezeichnungUeberweisungszeugnis" :disabled />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sonstiges">
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-checkbox v-model="data.istSichtbar" :disabled>Sichtbar</svws-ui-checkbox>
				<template v-if="manager().schulform().daten(schuljahr)?.hatGymOb ?? false">
					<svws-ui-checkbox v-model="data.istOberstufenFach" :disabled>Fach der Oberstufe</svws-ui-checkbox>
					<svws-ui-checkbox v-model="data.istPruefungsordnungsRelevant" :disabled>Ist Prüfungsordnungs-Relevant (z.B. bei Belegprüfungen)</svws-ui-checkbox>
				</template>
				<template v-if="manager().schulform() !== Schulform.G">
					<svws-ui-checkbox v-model="data.istNachpruefungErlaubt" :disabled>Nachprüfung erlaubt</svws-ui-checkbox>
					<svws-ui-checkbox v-model="data.istSchriftlichZK" :disabled>Schriftliches Fach für ZK</svws-ui-checkbox>
					<svws-ui-checkbox v-model="data.istSchriftlichBA" :disabled>Schriftliches Fach für BA</svws-ui-checkbox>
					<svws-ui-checkbox v-model="data.holeAusAltenLernabschnitten" :disabled>Berücksichtigen beim Holen von abgeschlossenen Fächern</svws-ui-checkbox>
				</template>
				<svws-ui-input-number placeholder="maximale Zeichenanzahl in Fachbemerkungen" v-model="data.maxZeichenInFachbemerkungen" :min="0"
					:max="JavaInteger.MAX_VALUE" :valid="(v) => manager().validateMaxZeichenInFachbemerkungen(v)" :disabled />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addFachDaten" :disabled="!isValid || !hatKompetenzUpdate">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import { BenutzerKompetenz, BilingualeSprache, Fach, FachDaten, GostFachbereich, JavaInteger, Schulform } from "@core";
	import type { FachKatalogEintrag, CoreTypeData } from "@core";
	import type { FaecherNeuProps } from "./SFaecherNeuProps";
	import { coreTypeDataFilter } from "~/utils/helfer";

	const props = defineProps<FaecherNeuProps>();

	const schuljahr = computed<number>(() => props.manager().getSchuljahr());
	const data = ref<FachDaten>(new FachDaten());
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isLoading = ref<boolean>(false);
	const isValid = ref<boolean>(false);

	watch(() => data.value, async () => {
		if (isLoading.value)
			return;

		props.checkpoint.active = true;
		validateAll();
	}, { immediate: false, deep: true });

	const validateAll = () => isValid.value = (data.value.kuerzelStatistik !== "") &&
		props.manager().validateKuerzel(data.value.kuerzel) &&
		props.manager().validateBezeichnung(data.value.bezeichnung) &&
		props.manager().validateMaxZeichenInFachbemerkungen(data.value.maxZeichenInFachbemerkungen) &&
		props.manager().validateSortierung(data.value.sortierung);

	const disabled = computed(() => (data.value.kuerzelStatistik === "") || !hatKompetenzUpdate.value);

	const fachgruppe = computed(() => Fach.getBySchluesselOrDefault(data.value.kuerzelStatistik).getFachgruppe(schuljahr.value)?.daten(schuljahr.value)?.text ?? '—');

	const statistikFachEintraege = computed(() => {
		const list = Fach.data().getListBySchuljahrAndSchulform(schuljahr.value, props.manager().schulform());
		return Array.from(list).map(item => Fach.data().getEintragBySchuljahrUndWert(schuljahr.value, item)).filter((eintrag) => eintrag !== null);
	});

	const selectedStatistikFach = computed({
		get: () => {
			const wert = Fach.data().getWertByKuerzel(data.value.kuerzelStatistik);
			if (wert === null)
				return null;
			return Fach.data().getEintragBySchuljahrUndWert(schuljahr.value, wert);
		},
		set: (eintrag: FachKatalogEintrag) => {
			data.value.kuerzelStatistik = eintrag.kuerzel;
			data.value.aufgabenfeld = eintrag.aufgabenfeld?.toString() ?? '';
			data.value.kuerzel = eintrag.kuerzel;
			data.value.bezeichnung = eintrag.text;
			data.value.istOberstufenFach = GostFachbereich.getAlleFaecherSortiert().contains(eintrag);
			data.value.istPruefungsordnungsRelevant = GostFachbereich.getAlleFaecherSortiert().contains(eintrag);
			data.value.istSichtbar = true;
			data.value.bezeichnungZeugnis = eintrag.text;
			data.value.bezeichnungUeberweisungszeugnis = eintrag.text;
			data.value.maxZeichenInFachbemerkungen = JavaInteger.MAX_VALUE;
		},
	});

	const selectedBilingualeSprache = computed({
		get: () => data.value.bilingualeSprache !== null ? BilingualeSprache.data().getWertByKuerzel(data.value.bilingualeSprache) : null,
		set: (val: BilingualeSprache) => {
			const eintrag = BilingualeSprache.data().getEintragBySchuljahrUndWert(schuljahr.value, val);
			if (eintrag !== null)
				data.value.bilingualeSprache = eintrag.kuerzel;
		},
	});

	function getStatistikfachText(fachEintrag: CoreTypeData) {
		return `${fachEintrag.schluessel} : ${fachEintrag.text}`;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	async function addFachDaten() {
		if (isLoading.value)
			return;

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, referenziertInAnderenTabellen, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

</script>
