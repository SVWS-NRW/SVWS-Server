<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kürzel" :required="true" :max-len="20" :valid="validateKuerzel" v-model="selectedKuerzel" type="text" />
				<svws-ui-select title="Statistik-Fach" :required="true" :items="Fach.data().getWerte()" v-model="selectedStatistikFach"
					:item-text="(f: Fach) => f.daten(schuljahr)?.schluessel + ' : ' + f.daten(schuljahr)?.text" />
				<svws-ui-text-input placeholder="Bezeichnung" :required="true" :max-len="255" :valid="validateBezeichnung" v-model="selectedBezeichnung"
					type="text" />
				<svws-ui-text-input placeholder="Fachgruppe" :model-value="getFachgruppeTest()" readonly type="text" />
				<svws-ui-select removable title="Bilinguale Sachfachsprache" :items="BilingualeSprache.values()" v-model="selectedBilingualeSprache"
					:item-text="(b: BilingualeSprache) => b.daten(schuljahr)?.text ?? '—'" />
				<svws-ui-input-number placeholder="Sortierung" :valid="validateSortierung" v-model="selectedSortierung" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Zeugnis">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-checkbox v-model="selectedAufZeugnis">Auf Zeugnis</svws-ui-checkbox>
				<svws-ui-text-input v-if="selectedAufZeugnis" placeholder="Bezeichnung (Zeugnis)" :required="true" v-model="selectedBezeichnungZeugnis"
					type="text" />
				<svws-ui-text-input v-if="selectedAufZeugnis" placeholder="Bezeichnung (Überweisungszeugnis)" :required="true"
					v-model="selectedBezeichnungUeberweisungszeugnis" type="text" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sonstiges">
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-checkbox v-model="selectedIstSichtbar">Sichtbar</svws-ui-checkbox>
				<template v-if="fachListeManager().schulform().daten(schuljahr)?.hatGymOb ?? false">
					<svws-ui-checkbox v-model="selectedIstOberstufenFach">Fach der Oberstufe</svws-ui-checkbox>
					<svws-ui-checkbox v-model="selectedIstPruefungsordnungsRelevant">Ist Prüfungsordnungs-Relevant (z.B. bei Belegprüfungen)</svws-ui-checkbox>
				</template>
				<template v-if="fachListeManager().schulform() !== Schulform.G">
					<svws-ui-checkbox v-model="selectedIstNachpruefungErlaubt">Nachprüfung erlaubt</svws-ui-checkbox>
					<svws-ui-checkbox v-model="selectedIstSchriftlichZK">Schriftliches Fach für ZK</svws-ui-checkbox>
					<svws-ui-checkbox v-model="selectedIstSchriftlichBA">Schriftliches Fach für BA</svws-ui-checkbox>
					<svws-ui-checkbox v-model="selectedHoleAusAltenLernabschnitten">Berücksichtigen beim Holen von abgeschlossenen Fächern</svws-ui-checkbox>
				</template>
				<svws-ui-input-number placeholder="maximale Zeichenanzahl in Fachbemerkungen" v-model="selectedMaxZeichenInFachbemerkungen" :min="0"
					:max="JavaInteger.MAX_VALUE - 1" :valid="validateMaxZeichenInFachbemerkungen" />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addFachDaten()" :disabled="!isValid">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import type { SchuleFachNeuProps } from "./SSchuleFachNeuProps";
	import { BilingualeSprache, Fach, JavaInteger, Schulform } from "@core";
	import type { FachDaten } from "@core";
	import { ref, computed, watch } from "vue";

	const props = defineProps<SchuleFachNeuProps>();
	const isValid = ref<boolean>(false);
	const schuljahr = computed<number>(() => props.fachListeManager().getSchuljahr());

	interface SelectedFields {
		kuerzel: string;
		kuerzelStatistik: string;
		bezeichnung: string;
		sortierung: number;
		istOberstufenFach: boolean;
		istPruefungsordnungsRelevant: boolean;
		istSichtbar: boolean;
		aufgabenfeld: string | null;
		bilingualeSprache: string | null;
		istNachpruefungErlaubt: boolean;
		aufZeugnis: boolean;
		bezeichnungZeugnis: string;
		bezeichnungUeberweisungszeugnis: string;
		maxZeichenInFachbemerkungen: number;
		istSchriftlichZK: boolean;
		istSchriftlichBA: boolean;
		istFHRFach: boolean;
		holeAusAltenLernabschnitten: boolean;
	}
	const selectedFields = ref<SelectedFields>({
		kuerzel: "",
		kuerzelStatistik: "",
		bezeichnung: "",
		sortierung: -1,
		istOberstufenFach: false,
		istPruefungsordnungsRelevant: false,
		istSichtbar: false,
		aufgabenfeld: null,
		bilingualeSprache: null,
		istNachpruefungErlaubt: false,
		aufZeugnis: false,
		bezeichnungZeugnis: "",
		bezeichnungUeberweisungszeugnis: "",
		maxZeichenInFachbemerkungen: -1,
		istSchriftlichZK: false,
		istSchriftlichBA: false,
		istFHRFach: false,
		holeAusAltenLernabschnitten: false,
	});
	const selectedKuerzel = computed({
		get: () => selectedFields.value.kuerzel,
		set: (val: string) => selectedFields.value.kuerzel = val
	});
	const selectedStatistikFach = computed({
		get: () => Fach.data().getWertByKuerzel(selectedFields.value.kuerzelStatistik),
		set: (val: Fach) => {
			const eintrag = Fach.data().getEintragBySchuljahrUndWert(schuljahr.value, val);
			if (eintrag !== null)
				selectedFields.value.kuerzelStatistik = eintrag.kuerzel
		}
	})
	const selectedBezeichnung = computed({
		get: () => selectedFields.value.bezeichnung,
		set: (val: string) => selectedFields.value.bezeichnung = val
	})
	const selectedBilingualeSprache = computed({
		get: () => selectedFields.value.bilingualeSprache !== null ? BilingualeSprache.data().getWertByKuerzel(selectedFields.value.bilingualeSprache) : null,
		set: (val: BilingualeSprache) => {
			const eintrag = BilingualeSprache.data().getEintragBySchuljahrUndWert(schuljahr.value, val);
			if (eintrag !== null)
				selectedFields.value.bilingualeSprache = eintrag.kuerzel
		}
	})
	const selectedSortierung = computed({
		get: () => selectedFields.value.sortierung,
		set: (val: number) => selectedFields.value.sortierung = val
	});
	const selectedAufZeugnis = computed({
		get: () => selectedFields.value.aufZeugnis,
		set: (val: boolean) => selectedFields.value.aufZeugnis = val
	});
	const selectedBezeichnungZeugnis = computed({
		get: () => selectedFields.value.bezeichnungZeugnis,
		set: (val: string) => selectedFields.value.bezeichnungZeugnis = val
	});
	const selectedBezeichnungUeberweisungszeugnis = computed({
		get: () => selectedFields.value.bezeichnungUeberweisungszeugnis,
		set: (val: string) => selectedFields.value.bezeichnungUeberweisungszeugnis = val
	});
	const selectedIstSichtbar = computed({
		get: () => selectedFields.value.istSichtbar,
		set: (val: boolean) => selectedFields.value.istSichtbar = val
	});
	const selectedIstOberstufenFach = computed({
		get: () => selectedFields.value.istOberstufenFach,
		set: (val: boolean) => selectedFields.value.istOberstufenFach = val
	});
	const selectedIstPruefungsordnungsRelevant = computed({
		get: () => selectedFields.value.istPruefungsordnungsRelevant,
		set: (val: boolean) => selectedFields.value.istPruefungsordnungsRelevant = val
	});
	const selectedIstNachpruefungErlaubt = computed({
		get: () => selectedFields.value.istNachpruefungErlaubt,
		set: (val: boolean) => selectedFields.value.istNachpruefungErlaubt = val
	});
	const selectedIstSchriftlichZK = computed({
		get: () => selectedFields.value.istSchriftlichZK,
		set: (val: boolean) => selectedFields.value.istSchriftlichZK = val
	});
	const selectedIstSchriftlichBA = computed({
		get: () => selectedFields.value.istSchriftlichBA,
		set: (val: boolean) => selectedFields.value.istSchriftlichBA = val
	});
	const selectedHoleAusAltenLernabschnitten = computed({
		get: () => selectedFields.value.holeAusAltenLernabschnitten,
		set: (val: boolean) => selectedFields.value.holeAusAltenLernabschnitten = val
	});
	const selectedMaxZeichenInFachbemerkungen = computed({
		get: () => selectedFields.value.maxZeichenInFachbemerkungen,
		set: (val: number) => selectedFields.value.maxZeichenInFachbemerkungen = val
	});
	function getFachgruppeTest() {
		return Fach.getBySchluesselOrDefault(selectedFields.value.kuerzelStatistik).getFachgruppe(schuljahr.value)?.daten(schuljahr.value)?.text ?? '—'
	}
	function getAufgabenfeld() {
		const aufgabenfeld = Fach.getBySchluesselOrDefault(selectedFields.value.kuerzelStatistik).daten(schuljahr.value)?.aufgabenfeld
		if ((aufgabenfeld === null) || (aufgabenfeld === undefined))
			return null
		return aufgabenfeld.toString()
	}
	async function cancel() {
		await props.gotoDefaultView(null);
	}
	async function addFachDaten() {
		const data: Partial<FachDaten> = {
			kuerzel: selectedFields.value.kuerzel,
			kuerzelStatistik: selectedFields.value.kuerzelStatistik,
			bezeichnung: selectedFields.value.bezeichnung,
			sortierung: selectedFields.value.sortierung,
			istOberstufenFach: selectedFields.value.istOberstufenFach,
			istPruefungsordnungsRelevant: selectedFields.value.istPruefungsordnungsRelevant,
			istSichtbar: selectedFields.value.istSichtbar,
			aufgabenfeld: getAufgabenfeld(),
			bilingualeSprache: selectedFields.value.bilingualeSprache,
			istNachpruefungErlaubt: selectedFields.value.istNachpruefungErlaubt,
			aufZeugnis: selectedFields.value.aufZeugnis,
			bezeichnungZeugnis: selectedFields.value.bezeichnungZeugnis,
			bezeichnungUeberweisungszeugnis: selectedFields.value.bezeichnungUeberweisungszeugnis,
			maxZeichenInFachbemerkungen: selectedFields.value.maxZeichenInFachbemerkungen,
			istSchriftlichZK: selectedFields.value.istSchriftlichZK,
			istSchriftlichBA: selectedFields.value.istSchriftlichBA,
			istFHRFach: selectedFields.value.istFHRFach,
			holeAusAltenLernabschnitten: selectedFields.value.holeAusAltenLernabschnitten,
		}
		await props.add(data)
			.then(() => props.gotoDefaultView(null));
	}
	const validateKuerzel = (kuerzel: string | null): boolean => props.fachListeManager().validateKuerzel(kuerzel);
	const validateBezeichnung = (bezeichnung: string | null): boolean => props.fachListeManager().validateBezeichnung(bezeichnung);
	const validateMaxZeichenInFachbemerkungen =
		(maxZeichenInFachbemerkungen: number | null): boolean => props.fachListeManager().validateMaxZeichenInFachbemerkungen(maxZeichenInFachbemerkungen);
	const validateSortierung = (sortierung: number | null): boolean => props.fachListeManager().validateSortierung(sortierung);
	function validateAufZeugnis() {
		if (!selectedFields.value.aufZeugnis) {
			selectedFields.value.bezeichnungZeugnis = "";
			selectedFields.value.bezeichnungUeberweisungszeugnis = "";
			return true;
		}
		return (selectedFields.value.bezeichnungZeugnis !== "") && (selectedFields.value.bezeichnungUeberweisungszeugnis !== "");
	}
	function validateAll() {
		isValid.value = validateKuerzel(selectedFields.value.kuerzel) &&
			validateBezeichnung(selectedFields.value.bezeichnung) &&
			validateMaxZeichenInFachbemerkungen(selectedFields.value.maxZeichenInFachbemerkungen) &&
			validateSortierung(selectedFields.value.sortierung) &&
			selectedFields.value.kuerzelStatistik !== "" &&
			validateAufZeugnis();
	}
	watch(selectedFields.value, () => {
		validateAll()
	})
</script>
