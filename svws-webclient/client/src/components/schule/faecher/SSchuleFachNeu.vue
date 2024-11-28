<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kürzel" :required="true" :max-len="20" :valid="(v) => manager().validateKuerzel(v)" v-model="data.kuerzel" type="text" :disabled />
				<svws-ui-select title="Statistik-Fach" :required="true" :items="Fach.data().getListBySchuljahrAndSchulform(schuljahr, manager().schulform())"
					v-model="selectedStatistikFach" :item-text="getStatistikfachText" />
				<svws-ui-text-input placeholder="Bezeichnung" :required="true" :max-len="255" :valid="(v) => manager().validateBezeichnung(v)" v-model="data.bezeichnung" type="text" :disabled />
				<svws-ui-text-input placeholder="Fachgruppe" :model-value="fachgruppe" type="text" disabled />
				<svws-ui-select removable title="Bilinguale Sachfachsprache" :items="BilingualeSprache.values()" v-model="selectedBilingualeSprache"
					:item-text="b => b.daten(schuljahr)?.text ?? '—'" :disabled />
				<svws-ui-input-number placeholder="Sortierung" :valid="(v) => manager().validateSortierung(v)" v-model="data.sortierung" :disabled />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Zeugnis">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-checkbox v-model="data.aufZeugnis" :disabled>Auf Zeugnis</svws-ui-checkbox>
				<svws-ui-text-input placeholder="Bezeichnung (Zeugnis)" :required="true" v-model="data.bezeichnungZeugnis" type="text" :disabled />
				<svws-ui-text-input placeholder="Bezeichnung (Überweisungszeugnis)" :required="true" v-model="data.bezeichnungUeberweisungszeugnis" type="text" :disabled />
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
				<svws-ui-button @click="addFachDaten()" :disabled="!isValid">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { ref, computed, watch } from "vue";
	import { BilingualeSprache, Fach, JavaInteger, Schulform, FachDaten, GostFachbereich } from "@core";
	import type { SchuleFachNeuProps } from "./SSchuleFachNeuProps";

	const props = defineProps<SchuleFachNeuProps>();

	const schuljahr = computed<number>(() => props.manager().getSchuljahr());
	const data = ref<FachDaten>(new FachDaten());

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

	const disabled = computed(() => data.value.kuerzelStatistik === "");

	const fachgruppe = computed(() => Fach.getBySchluesselOrDefault(data.value.kuerzelStatistik).getFachgruppe(schuljahr.value)?.daten(schuljahr.value)?.text ?? '—');

	const selectedStatistikFach = computed({
		get: () => Fach.data().getWertByKuerzel(data.value.kuerzelStatistik),
		set: (val: Fach) => {
			const eintrag = Fach.data().getEintragBySchuljahrUndWert(schuljahr.value, val);
			if (eintrag !== null) {
				data.value.kuerzelStatistik = eintrag.kuerzel;
				data.value.aufgabenfeld = eintrag.aufgabenfeld?.toString() ?? '';
				data.value.kuerzel = eintrag.kuerzel;
				data.value.bezeichnung = eintrag.text;
				data.value.istOberstufenFach = GostFachbereich.getAlleFaecherSortiert().contains(val);
				data.value.istPruefungsordnungsRelevant = GostFachbereich.getAlleFaecherSortiert().contains(val);
				data.value.istSichtbar = true;
				data.value.bezeichnungZeugnis = eintrag.text;
				data.value.bezeichnungUeberweisungszeugnis = eintrag.text;
				data.value.maxZeichenInFachbemerkungen = JavaInteger.MAX_VALUE;
			}
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

	function getStatistikfachText(fach: Fach) {
		const fachEintrag = fach.daten(schuljahr.value);
		if (fachEintrag === null)
			return "unzulässiges Fach";
		return `${fachEintrag.schluessel} : ${fachEintrag.text}`;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	async function addFachDaten() {
		if (isLoading.value)
			return;

		isLoading.value = true;
		props.checkpoint.active = false;
		const partialData: Partial<FachDaten> = data.value;
		delete partialData.id;
		await props.add(partialData);
		isLoading.value = false;
	}

</script>
