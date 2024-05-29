<template>
	<div class="page--content">
		<svws-ui-content-card title="Sprachenfolge" class="">
			<svws-ui-table :items="sprachbelegungen()" :columns="colsSprachenfolge" selectable v-model="auswahl">
				<template #cell(sprache)="{ value: kuerzel }">{{ ZulaessigesFach.getFremdspracheByKuerzelAtomar(kuerzel).daten.bezeichnung }} </template>
				<template #cell(reihenfolge)="{ rowData }">
					<svws-ui-input-number title="Reihenfolge" headless :model-value="rowData.reihenfolge" @update:model-value="reihenfolge=>reihenfolge && patchSprachbelegung({reihenfolge}, rowData.sprache)" :min="1" :max="9" />
				</template>
				<template #cell(belegungVonAbschnitt)="{ rowData }">
					<div class="flex items-center gap-0.5 border border-black/25 border-dashed hover:border-black/50 hover:border-solid hover:bg-white my-auto p-[0.1rem] rounded cursor-pointer" @click="patchSprachbelegung({belegungVonAbschnitt: rowData.belegungVonAbschnitt === 1 ? 2 : 1}, rowData.sprache)">
						<span :class="{ 'opacity-100 font-bold': rowData.belegungVonAbschnitt === 1, 'opacity-25 hover:opacity-100 font-medium': rowData.belegungVonAbschnitt === 2}">1</span>
						<span class="opacity-50">|</span>
						<span :class="{ 'opacity-100 font-bold': rowData.belegungVonAbschnitt === 2, 'opacity-25 hover:opacity-100 font-medium': rowData.belegungVonAbschnitt === 1}">2</span>
					</div>
				</template>
				<template #cell(belegungVonJahrgang)="{ rowData }">
					<svws-ui-select title="Von Jahrgang" headless :model-value="Jahrgaenge.getByKuerzel(rowData.belegungVonJahrgang)"
						@update:model-value="jahrgang => jahrgang?.daten.kuerzel && patchSprachbelegung({belegungVonJahrgang: jahrgang.daten.kuerzel, sprache: rowData.sprache}, rowData.sprache)"
						:items="sprachJahrgaengeVon" :item-text="i => i?.daten.kuerzel || ''" />
				</template>
				<template #cell(belegungBisAbschnitt)="{ rowData }">
					<div class="flex items-center gap-0.5 border border-black/25 border-dashed hover:border-black/50 hover:border-solid hover:bg-white my-auto p-[0.1rem] rounded cursor-pointer" @click="patchSprachbelegung({belegungBisAbschnitt: rowData.belegungVonAbschnitt === 1 ? 2 : 1}, rowData.sprache)">
						<span :class="{ 'opacity-100 font-bold': rowData.belegungBisAbschnitt === 1, 'opacity-25 hover:opacity-100 font-medium': rowData.belegungBisAbschnitt === 2}">1</span>
						<span class="opacity-50">|</span>
						<span :class="{ 'opacity-100 font-bold': rowData.belegungBisAbschnitt === 2, 'opacity-25 hover:opacity-100 font-medium': rowData.belegungBisAbschnitt === 1}">2</span>
					</div>
				</template>
				<template #cell(belegungBisJahrgang)="{ rowData }">
					<svws-ui-select title="Bis Jahrgang" headless :removable="true" :model-value="Jahrgaenge.getByKuerzel(rowData.belegungBisJahrgang)"
						@update:model-value="jahrgang => patchSprachbelegung({belegungBisJahrgang: jahrgang?.daten.kuerzel ?? null}, rowData.sprache)"
						:items="sprachJahrgaengeBis(rowData).value" :item-text="i=>i?.daten.kuerzel || ''" />
				</template>
				<template #cell(referenzniveau)="{ rowData }">
					<svws-ui-checkbox v-if="rowData.sprache === 'G'" v-model="hatGraecum" headless title="Graecum">Graecum</svws-ui-checkbox>
					<svws-ui-checkbox v-else-if="rowData.sprache === 'H'" v-model="hatHebraicum" headless title="Hebraicum">Hebraicum</svws-ui-checkbox>
					<template v-else-if="rowData.sprache === 'L'">
						<svws-ui-select headless :items="latein" :model-value="latinum" :item-text="i => i.text" @update:model-value="patchLatinum" :removable="true" />
					</template>
					<template v-else>
						<svws-ui-select title="Referenzniveau" headless :removable="true" :model-value="Sprachreferenzniveau.getByKuerzel(rowData.referenzniveau)" @update:model-value="referenzniveau => patchSprachbelegung({referenzniveau: referenzniveau?.daten.kuerzel ?? null}, rowData.sprache)" :items="Sprachreferenzniveau.values()" :item-text="i => i.daten.kuerzel" />
					</template>
				</template>
				<template #actions>
					<svws-ui-button @click="remove" type="trash" :disabled="auswahl.length === 0" />
					<svws-ui-button @click="suchen" type="icon" title="Diese Sprache in den Leistungsdaten suchen und Beginn und Ende aktualisieren" :disabled="auswahl.length === 0"> <span class="icon i-ri-search-line" /></svws-ui-button>
					<svws-ui-button @click="ermitteln" type="icon" title="Das GER/Latinum anhand aller Daten ermitteln" :disabled="auswahl.length === 0"><span class="icon i-ri-calculator-line" /></svws-ui-button>
					<div v-if="verfuegbareSprachen.length" class="w-1/4">
						<svws-ui-select title="Eine neue Sprache hinzufügen" removable headless :model-value="undefined" @update:model-value="sprache=> hinzufuegen(sprache)" :items="verfuegbareSprachen" :item-text="i=> `${i} - ${ZulaessigesFach.getFremdspracheByKuerzelAtomar(i).daten.bezeichnung}`" ref="selectSprachen" />
					</div>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sprachprüfungen" class="col-span-full">
			<svws-ui-table :items="sprachpruefungen()" :columns="colsSprachpruefungen" selectable v-model="auswahlPr">
				<template #cell(sprache)="{ value: kuerzel }">{{ ZulaessigesFach.getFremdspracheByKuerzelAtomar(kuerzel).daten.bezeichnung }} </template>
				<template #cell(typ)="{ rowData }">
					<svws-ui-select title="Typ" headless :items="typ" :item-text="i=> i.key"
						:model-value="rowData.istHSUPruefung ? typ[0] : rowData.istFeststellungspruefung ? typ[1] : undefined"
						@update:model-value="o => patchSprachpruefung({istHSUPruefung: o?.key === 'HSU', istFeststellungspruefung: o?.key === 'Festestellungsprüfung'}, rowData.sprache)" />
				</template>
				<template #cell(ersetzt)="{ rowData }">
					<svws-ui-select title="Ersetzt" headless :items="ersetzt" :item-text="i=> i.key"
						:model-value="rowData.kannErstePflichtfremdspracheErsetzen ? ersetzt[0] : rowData.kannZweitePflichtfremdspracheErsetzen ? ersetzt[1] : rowData.kannWahlpflichtfremdspracheErsetzen ? ersetzt[2] : undefined"
						@update:model-value="o => patchSprachpruefung({kannErstePflichtfremdspracheErsetzen: o?.key === '1. Pflichtfremdsprache', kannZweitePflichtfremdspracheErsetzen: o?.key === '2. Pflichtfremdsprache', kannWahlpflichtfremdspracheErsetzen: o?.key === 'Wahlpflichtfremdsprache'}, rowData.sprache)" />
				</template>
				<template #cell(kannBelegungAlsFortgefuehrteSpracheErlauben)="{ rowData }">
					<svws-ui-checkbox :model-value="rowData.kannBelegungAlsFortgefuehrteSpracheErlauben" @update:model-value="kannBelegungAlsFortgefuehrteSpracheErlauben => patchSprachpruefung({kannBelegungAlsFortgefuehrteSpracheErlauben}, rowData.sprache)" headless />
				</template>
				<template #cell(jahrgang)="{ rowData }">
					<svws-ui-select title="Jahrgang" headless :removable="true" :model-value="Jahrgaenge.getByKuerzel(rowData.jahrgang)" @update:model-value="jahrgang => patchSprachpruefung({jahrgang: jahrgang?.daten.kuerzel ?? null}, rowData.sprache)" :items="sprachJahrgaengeVon" :item-text="i=>i?.daten.kuerzel || ''" />
				</template>
				<template #cell(anspruchsniveauId)="{ rowData }">
					<svws-ui-select title="Sprachpruefungniveau" headless :removable="true" :model-value="Sprachpruefungniveau.getByID(rowData.anspruchsniveauId)" @update:model-value="anspruchsniveau => patchSprachpruefung({anspruchsniveauId: anspruchsniveau?.daten.id || null}, rowData.sprache)" :items="Sprachpruefungniveau.values()" :item-text="i => i.daten.kuerzel" />
				</template>
				<template #cell(note)="{ rowData }">
					<svws-ui-select :items="Note.getNotenOhneTendenz()" :item-text="i => i?.kuerzel" :model-value="Note.fromNoteSekI(rowData.note)" @update:model-value="note => patchSprachpruefung({ note: ((note === null) || (note === undefined)) ? null : note.getNoteSekI() }, rowData.sprache)" headless />
				</template>
				<template #cell(referenzniveau)="{ rowData }">
					<svws-ui-select title="Referenzniveau" headless :removable="true" :model-value="Sprachreferenzniveau.getByKuerzel(rowData.referenzniveau)" @update:model-value="referenzniveau => patchSprachpruefung({referenzniveau: referenzniveau?.daten.kuerzel ?? null}, rowData.sprache)" :items="Sprachreferenzniveau.values()" :item-text="i => i.daten.kuerzel" />
				</template>
				<template #cell(pruefungsdatum)="{ rowData }">
					<svws-ui-text-input placeholder="Prüfungsdatum" :model-value="rowData.pruefungsdatum" @change="pruefungsdatum => pruefungsdatum && patchSprachpruefung({pruefungsdatum}, rowData.sprache)" type="date" headless />
				</template>
				<template #cell(ersetzteSprache)="{ rowData }">
					<svws-ui-select title="An Stelle von" headless :model-value="rowData.ersetzteSprache" @update:model-value="ersetzteSprache => patchSprachpruefung({ersetzteSprache}, rowData.sprache)" :items="verfuegbareSprachen" :item-text="i=> `${i} - ${ZulaessigesFach.getFremdspracheByKuerzelAtomar(i).daten.bezeichnung}`" />
				</template>
				<template #actions>
					<svws-ui-button @click="removePruefungen" type="trash" :disabled="auswahlPr.length === 0" />
					<div v-if="verfuegbareSprachenPruefungen.length" class="w-1/4">
						<svws-ui-select title="Eine neue Sprachprüfung hinzufügen" removable headless :model-value="undefined" @update:model-value="sprache=> hinzufuegenPruefung(sprache)" :items="verfuegbareSprachenPruefungen" :item-text="i=> `${i} - ${ZulaessigesFach.getFremdspracheByKuerzelAtomar(i).daten.bezeichnung}`" ref="selectSprachenPruefung" />
					</div>
				</template>
				<!-- -->
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { ComponentExposed } from 'vue-component-type-helpers';
	import type { SchuelerSprachenProps } from './SchuelerSprachenProps';
	import type { DataTableColumn, SvwsUiSelect } from "@ui";
	import type { Sprachbelegung , Sprachpruefung} from '@core';
	import { Schulform, Sprachreferenzniveau, ZulaessigesFach, Jahrgaenge, Note, Schulgliederung, Sprachpruefungniveau } from '@core';

	const props = defineProps<SchuelerSprachenProps>();

	const auswahl = ref([]);
	const auswahlPr = ref([]);
	const selectSprachen = ref<ComponentExposed<typeof SvwsUiSelect<string[]>> | null>(null);
	const selectSprachenPruefung = ref<ComponentExposed<typeof SvwsUiSelect<string[]>> | null>(null);

	const colsSprachenfolge = computed<DataTableColumn[]>(() => {
		const schulgliederung = Schulgliederung.getByKuerzel(props.schuelerListeManager().auswahl().schulgliederung);
		return [{ key: "sprache", label: "Sprache", tooltip: "Kürzel der Sprache", span: 2 },
			{ key: "reihenfolge", label: "Reihenfolge", tooltip: "Reihenfolge", divider: true },
			...(([Schulform.BK, Schulform.SB].includes(props.schuelerListeManager().schulform()) && !(schulgliederung && ([Schulgliederung.D01, Schulgliederung.D02].includes(schulgliederung))))
				? []
				: [{ key: "belegungVonJahrgang", label: "ab Jg", tooltip: "belegt ab Jahrgang" },
					{ key: "belegungVonAbschnitt", label: "Halbjahr", tooltip: "belegt ab Abschnitt", divider: true },
					{ key: "belegungBisJahrgang", label: "bis Jg", tooltip: "belegt bis Jahrgang" },
					{ key: "belegungBisAbschnitt", label: "Halbjahr", tooltip: "belegt bis Abschnitt", divider: true }]),
			{ key: "referenzniveau", label: "Referenzniveau", tooltip: "das erreichte Referenzniveau nach dem gemeinsamen europäischen Referenznahmen", span: 2 },
		]}
	);

	const colsSprachpruefungen = computed<DataTableColumn[]>(() => {
		const schulgliederung = Schulgliederung.getByKuerzel(props.schuelerListeManager().auswahl().schulgliederung);
		return [{ key: "sprache", label: "Sprache", tooltip: "Kürzel der Sprache", minWidth: 4 },
			{ key: "typ", label: "Prüfungsart", tooltip: "Prüfung ist eine Prüfung im herkunftssprachlichen Unterricht oder eine Sprachfeststellungsprüfung", minWidth: 5 },
			{ key: "ersetzt", label: "Ersetzt", minWidth: 4 },
			{ key: "kannBelegungAlsFortgefuehrteSpracheErlauben", label: "Fortgef. Fs. GOSt", tooltip: "Durch die Prüfung kann die Sprache als fortgeführte Fremdsprache in der GOSt belegt werden", align: 'center', minWidth: 4 },
			...([Schulform.BK, Schulform.SB].includes(props.schuelerListeManager().schulform()) && !(schulgliederung && ([Schulgliederung.D01, Schulgliederung.D02].includes(schulgliederung)))
				? [] : [{ key: "jahrgang", label: "Jahrgang", tooltip: "Im Jahrgang", minWidth: 4 }]),
			{ key: "anspruchsniveauId", label: "Anspruchsniveau", tooltip: "Bezeichnung des am Schulabschluss orientierte Anspruchsniveau der Sprachprüfung", minWidth: 4 },
			{ key: "note", label: "Note", tooltip: "Prüfungsnote", minWidth: 2 },
			{ key: "referenzniveau", label: "Referenzniveau", tooltip: "Das Kürzel des Referenzniveau nach dem gemeinsamen europäischen Referenznahmen, welches durch die Prüfung erreicht wurde", minWidth: 3 },
			{ key: "pruefungsdatum", label: "Prüfungsdatum", tooltip: "Prüfungsdatum", minWidth: 3, },
			{ key: "ersetzteSprache", label: "An Stelle von", tooltip: "Die durch die Prüfung ersetzte Sprache", minWidth: 4 },
		]
	});

	const verfuegbareSprachen = computed(() => {
		const belegungen = new Set();
		const sprachen = [];
		for (const b of props.sprachbelegungen())
			belegungen.add(b.sprache);
		for (const k of ZulaessigesFach.getListFremdsprachenKuerzelAtomar()) {
			const sprache = ZulaessigesFach.getFremdspracheByKuerzelAtomar(k);
			if (!sprache.daten.istErsatzPflichtFS && !sprache.daten.istHKFS && !sprache.daten.istAusRegUFach && !belegungen.has(k))
				sprachen.push(k);
		}
		return sprachen;
	})

	const verfuegbareSprachenPruefungen = computed(() => {
		const belegungen = new Set();
		const sprachen = [];
		for (const p of props.sprachpruefungen())
			belegungen.add(p.sprache);
		for (const k of ZulaessigesFach.getListFremdsprachenKuerzelAtomar()) {
			const sprache = ZulaessigesFach.getFremdspracheByKuerzelAtomar(k);
			if (!sprache.daten.istErsatzPflichtFS && !sprache.daten.istHKFS && !sprache.daten.istAusRegUFach && !belegungen.has(k))
				sprachen.push(k);
		}
		return sprachen;
	})

	const sprachJahrgaengeVon = computed(() => {
		const schulform = props.schuelerListeManager().schulform();
		if ((schulform === Schulform.BK) || (schulform === Schulform.SB))
			return Jahrgaenge.get(Schulform.GE);
		return Jahrgaenge.get(schulform);
	});

	const sprachJahrgaengeBis = (sprachbelegung: Sprachbelegung) => computed(() => {
		const schulform = props.schuelerListeManager().schulform();
		const jahrgangVon = Jahrgaenge.getByKuerzel(sprachbelegung.belegungVonJahrgang);
		const jahrgaenge_list = ((schulform === Schulform.BK) || (schulform === Schulform.SB)) ? Jahrgaenge.get(Schulform.GE) : Jahrgaenge.get(schulform);
		const jahrgaenge = [];
		for (const jahrgang of jahrgaenge_list)
			if (jahrgangVon && (jahrgang.ordinal() > jahrgangVon.ordinal()))
				jahrgaenge.push(jahrgang)
		return jahrgaenge;
	});

	const latein = [{text: 'Kleines Latinum'}, {text: 'Latinum'}];
	const latinum = computed(()=> {
		if (hatKleinesLatinum.value)
			return latein[0];
		if (hatLatinum.value)
			return latein[1];
		return
	})

	async function patchLatinum(item:any) {
		console.log(item)
		if (item === undefined)
			await props.patchSprachbelegung({hatKleinesLatinum: false, hatLatinum: false}, 'L');
		if (item === latein[0])
			await props.patchSprachbelegung({hatKleinesLatinum: true, hatLatinum: false}, 'L');
		if (item === latein[1])
			await props.patchSprachbelegung({hatKleinesLatinum: false, hatLatinum: true}, 'L');
	}

	const hatKleinesLatinum = computed<boolean>(() => {
		for (const sprache of props.sprachbelegungen())
			if (sprache.sprache === 'L')
				return sprache.hatKleinesLatinum;
		return false;
	})

	const hatLatinum = computed<boolean>(() => {
		for (const sprache of props.sprachbelegungen())
			if (sprache.sprache === 'L')
				return sprache.hatLatinum;
		return false;
	});

	const hatGraecum = computed<boolean>({
		get: () => {
			for (const sprache of props.sprachbelegungen())
				if (sprache.sprache === 'G')
					return sprache.hatGraecum;
			return false;
		},
		set: (hatGraecum) => void props.patchSprachbelegung({hatGraecum}, 'G')
	});

	const hatHebraicum = computed<boolean>({
		get: () => {
			for (const sprache of props.sprachbelegungen())
				if (sprache.sprache === 'H')
					return sprache.hatHebraicum;
			return false;
		},
		set: (hatHebraicum) => void props.patchSprachbelegung({hatHebraicum}, 'H')
	});

	async function remove() {
		for (const sprache of auswahl.value)
			await props.removeSprachbelegung(sprache);
		auswahl.value = [];
	}
	async function suchen() {
		//suche Sprache
	}
	async function ermitteln() {
		//ermittel Sprache
	}

	async function hinzufuegen(sprache: undefined | null | string) {
		if (verfuegbareSprachen.value.length === 0 || selectSprachen.value === null || !sprache) {
			selectSprachen.value?.reset();
			return;
		}
		const data: Partial<Sprachbelegung> = {};
		data.sprache = sprache;
		data.reihenfolge = props.sprachbelegungen().size() + 1;
		data.belegungVonAbschnitt = 1;
		data.belegungBisAbschnitt = 2;
		const schulform = props.schuelerListeManager().schulform();
		if ((schulform !== Schulform.BK) && (schulform !== Schulform.SB))
			data.belegungVonJahrgang = props.schuelerListeManager().jahrgaenge.get(props.schuelerListeManager().auswahl().idJahrgang)?.kuerzelStatistik;
		await props.addSprachbelegung(data);
		selectSprachen.value.reset();
	}

	async function hinzufuegenPruefung(sprache: undefined | null | string) {
		if (verfuegbareSprachenPruefungen.value.length === 0 || selectSprachenPruefung.value === null || !sprache) {
			selectSprachenPruefung.value?.reset();
			return;
		}
		const data: Partial<Sprachpruefung> = {};
		data.sprache = sprache;
		const schulform = props.schuelerListeManager().schulform();
		if ((schulform !== Schulform.BK) && (schulform !== Schulform.SB))
			data.jahrgang = props.schuelerListeManager().jahrgaenge.get(props.schuelerListeManager().auswahl().idJahrgang)?.kuerzelStatistik;
		await props.addSprachpruefung(data);
		selectSprachenPruefung.value.reset();
	}

	async function removePruefungen() {
		for (const pruefung of auswahlPr.value)
			await props.removeSprachpruefung(pruefung);
	}

	const typ = [{key: 'HSU'}, {key: 'Festestellungsprüfung'}];
	const ersetzt = [{key: '1. Pflichtfremdsprache'}, {key: '2. Pflichtfremdsprache'}, {key: 'Wahlpflichtfremdsprache'}];

</script>
