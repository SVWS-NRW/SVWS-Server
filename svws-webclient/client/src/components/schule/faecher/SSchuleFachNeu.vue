<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kürzel" :required="true" :max-len="20" :valid="(v) => manager().validateKuerzel(v)" v-model="selectedFields.kuerzel" type="text" :disabled />
				<svws-ui-select title="Statistik-Fach" :required="true" :items="Fach.data().getListBySchuljahrAndSchulform(schuljahr, manager().schulform())"
					v-model="selectedStatistikFach" :item-text="getStatistikfachText" />
				<svws-ui-text-input placeholder="Bezeichnung" :required="true" :max-len="255" :valid="(v) => manager().validateBezeichnung(v)" v-model="selectedFields.bezeichnung" type="text" :disabled />
				<svws-ui-text-input placeholder="Fachgruppe" :model-value="fachgruppe" readonly type="text" :disabled />
				<svws-ui-select removable title="Bilinguale Sachfachsprache" :items="BilingualeSprache.values()" v-model="selectedBilingualeSprache"
					:item-text="b => b.daten(schuljahr)?.text ?? '—'" :disabled />
				<svws-ui-input-number placeholder="Sortierung" :valid="(v) => manager().validateSortierung(v)" v-model="selectedFields.sortierung" :disabled />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Zeugnis">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-checkbox v-model="selectedFields.aufZeugnis" :disabled>Auf Zeugnis</svws-ui-checkbox>
				<svws-ui-text-input placeholder="Bezeichnung (Zeugnis)" :required="true" v-model="selectedFields.bezeichnungZeugnis" type="text" :disabled />
				<svws-ui-text-input placeholder="Bezeichnung (Überweisungszeugnis)" :required="true"
					v-model="selectedFields.bezeichnungUeberweisungszeugnis" type="text" :disabled />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sonstiges">
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-checkbox v-model="selectedFields.istSichtbar" :disabled>Sichtbar</svws-ui-checkbox>
				<template v-if="manager().schulform().daten(schuljahr)?.hatGymOb ?? false">
					<svws-ui-checkbox v-model="selectedFields.istOberstufenFach" :disabled>Fach der Oberstufe</svws-ui-checkbox>
					<svws-ui-checkbox v-model="selectedFields.istPruefungsordnungsRelevant" :disabled>Ist Prüfungsordnungs-Relevant (z.B. bei Belegprüfungen)</svws-ui-checkbox>
				</template>
				<template v-if="manager().schulform() !== Schulform.G">
					<svws-ui-checkbox v-model="selectedFields.istNachpruefungErlaubt" :disabled>Nachprüfung erlaubt</svws-ui-checkbox>
					<svws-ui-checkbox v-model="selectedFields.istSchriftlichZK" :disabled>Schriftliches Fach für ZK</svws-ui-checkbox>
					<svws-ui-checkbox v-model="selectedFields.istSchriftlichBA" :disabled>Schriftliches Fach für BA</svws-ui-checkbox>
					<svws-ui-checkbox v-model="selectedFields.holeAusAltenLernabschnitten" :disabled>Berücksichtigen beim Holen von abgeschlossenen Fächern</svws-ui-checkbox>
				</template>
				<svws-ui-input-number placeholder="maximale Zeichenanzahl in Fachbemerkungen" v-model="selectedFields.maxZeichenInFachbemerkungen" :min="0"
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

	import { ref, computed } from "vue";
	import { BilingualeSprache, Fach, JavaInteger, Schulform, FachDaten, GostFachbereich } from "@core";
	import type { SchuleFachNeuProps } from "./SSchuleFachNeuProps";

	const props = defineProps<SchuleFachNeuProps>();
	const schuljahr = computed<number>(() => props.manager().getSchuljahr());

	const selectedFields = ref<FachDaten>(new FachDaten());

	const disabled = computed(() => selectedFields.value.kuerzelStatistik === "");

	const selectedStatistikFach = computed({
		get: () => Fach.data().getWertByKuerzel(selectedFields.value.kuerzelStatistik),
		set: (val: Fach) => {
			const eintrag = Fach.data().getEintragBySchuljahrUndWert(schuljahr.value, val);
			if (eintrag !== null) {
				selectedFields.value.kuerzelStatistik = eintrag.kuerzel;
				selectedFields.value.aufgabenfeld = eintrag.aufgabenfeld?.toString() ?? '';
				selectedFields.value.kuerzel = eintrag.kuerzel;
				selectedFields.value.bezeichnung = eintrag.text;
				selectedFields.value.istOberstufenFach = GostFachbereich.getAlleFaecherSortiert().contains(val);
				selectedFields.value.istPruefungsordnungsRelevant = GostFachbereich.getAlleFaecherSortiert().contains(val);
				selectedFields.value.istSichtbar = true;
				selectedFields.value.bezeichnungZeugnis = eintrag.text;
				selectedFields.value.bezeichnungUeberweisungszeugnis = eintrag.text;
				selectedFields.value.maxZeichenInFachbemerkungen = JavaInteger.MAX_VALUE;
			}
		},
	});

	const selectedBilingualeSprache = computed({
		get: () => selectedFields.value.bilingualeSprache !== null ? BilingualeSprache.data().getWertByKuerzel(selectedFields.value.bilingualeSprache) : null,
		set: (val: BilingualeSprache) => {
			const eintrag = BilingualeSprache.data().getEintragBySchuljahrUndWert(schuljahr.value, val);
			if (eintrag !== null)
				selectedFields.value.bilingualeSprache = eintrag.kuerzel;
		},
	});

	function getStatistikfachText(f: Fach) {
		const daten = f.daten(schuljahr.value);
		if (daten === null)
			return "unzulässiges Fach";
		return `${daten.schluessel} : ${daten.text}`;
	}

	const fachgruppe = computed(() => Fach.getBySchluesselOrDefault(selectedFields.value.kuerzelStatistik).getFachgruppe(schuljahr.value)?.daten(schuljahr.value)?.text ?? '—');

	async function cancel() {
		await props.gotoDefaultView(null);
	}

	async function addFachDaten() {
		const kopie: Partial<FachDaten> = selectedFields.value;
		delete kopie.id;
		await props.add(kopie);
		selectedFields.value = new FachDaten();
		await props.gotoDefaultView(null);
	}

	const isValid = computed(() => {
		if (selectedFields.value.kuerzelStatistik === "")
			return false;
		if (!props.manager().validateKuerzel(selectedFields.value.kuerzel))
			return false;
		if (!props.manager().validateBezeichnung(selectedFields.value.bezeichnung))
			return false;
		if (!props.manager().validateMaxZeichenInFachbemerkungen(selectedFields.value.maxZeichenInFachbemerkungen))
			return false;
		if (!props.manager().validateSortierung(selectedFields.value.sortierung))
			return false;
		return true;
	});

</script>
