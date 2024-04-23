<template>
	<div class="page--content">
		<svws-ui-todo v-if="serverMode !== ServerMode.STABLE" title="Abschlüsse und Berechtigungen an aktueller Schule">
			Hier werden zukünftig die Informationen zu den Abschlüssen und Berechtigungen an der aktuellen Schule angezeigt.
		</svws-ui-todo>
		<svws-ui-content-card title="Sprachenfolge" class="">
			<svws-ui-table :items="sprachbelegungen()" :columns="colsSprachenfolge" selectable v-model="auswahl">
				<template #cell(sprache)="{ value }">
					<svws-ui-select title="Sprache" headless :model-value="value" @update:model-value="sprache=> patchSprachbelegung({sprache}, value)" :items="sprachen(value).value" :item-text="i=> `${i} - ${ZulaessigesFach.getFremdspracheByKuerzelAtomar(i).daten.bezeichnung}`" />
				</template>
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
						:items="sprachJahrgaenge" :item-text="i => i?.daten.kuerzel || ''" />
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
						:items="sprachJahrgaenge" :item-text="i=>i?.daten.kuerzel || ''" />
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
					<svws-ui-button v-if="verfuegbareSprachen.length" @click="hinzufuegen" type="icon" title="Eine neue Sprache hinzufügen"><span class="icon i-ri-add-line" /></svws-ui-button>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sprachprüfungen" class="col-span-full">
			<svws-ui-table :items="sprachpruefungen()" :columns="colsSprachpruefungen" selectable v-model="auswahlPr">
				<template #cell(sprache)="{ value }">
					<svws-ui-select title="Sprache" headless :model-value="value" @update:model-value="sprache => patchSprachpruefung({sprache})" :items="sprachen(value).value" :item-text="i=> `${i} - ${ZulaessigesFach.getFremdspracheByKuerzelAtomar(i).daten.bezeichnung}`" />
				</template>
				<template #cell(istHSUPruefung)="{ value }">
					<svws-ui-checkbox :model-value="value" @update:model-value="istHSUPruefung => patchSprachpruefung({istHSUPruefung})" headless />
				</template>
				<template #cell(kannErstePflichtfremdspracheErsetzen)="{ value }">
					<svws-ui-checkbox :model-value="value" @update:model-value="kannErstePflichtfremdspracheErsetzen => patchSprachpruefung({kannErstePflichtfremdspracheErsetzen})" headless />
				</template>
				<template #cell(kannZweitePflichtfremdspracheErsetzen)="{ value }">
					<svws-ui-checkbox :model-value="value" @update:model-value="kannZweitePflichtfremdspracheErsetzen => patchSprachpruefung({kannZweitePflichtfremdspracheErsetzen})" headless />
				</template>
				<template #cell(kannBelegungAlsFortgefuehrteSpracheErlauben)="{ value }">
					<svws-ui-checkbox :model-value="value" @update:model-value="kannBelegungAlsFortgefuehrteSpracheErlauben => patchSprachpruefung({kannBelegungAlsFortgefuehrteSpracheErlauben})" headless />
				</template>
				<template #cell(kannWahlpflichtfremdspracheErsetzen)="{ value }">
					<svws-ui-checkbox :model-value="value" @update:model-value="kannWahlpflichtfremdspracheErsetzen => patchSprachpruefung({kannWahlpflichtfremdspracheErsetzen})" headless />
				</template>
				<template #cell(istFeststellungspruefung)="{ value }">
					<svws-ui-checkbox :model-value="value" @update:model-value="istFeststellungspruefung => patchSprachpruefung({istFeststellungspruefung})" headless />
				</template>
				<template #cell(jahrgang)="{ value }">
					<svws-ui-select title="Jahrgang" headless :removable="true" :model-value="Jahrgaenge.getByKuerzel(value)" @update:model-value="jahrgang => patchSprachpruefung({jahrgang: jahrgang?.daten.kuerzel ?? null})" :items="sprachJahrgaenge" :item-text="i=>i?.daten.kuerzel || ''" />
				</template>
				<template #cell(anspruchsniveauId)="{ value }">
					<svws-ui-select title="Referenzniveau" headless :removable="true" :model-value="Sprachreferenzniveau.getByID(value)" @update:model-value="anspruchsniveau => patchSprachpruefung({anspruchsniveauId: anspruchsniveau?.daten.id || null})" :items="Sprachreferenzniveau.values()" :item-text="i => i.daten.kuerzel" />
				</template>
				<template #cell(note)="{ value }">
					<svws-ui-select :items="Note.values()" :item-text="i => i?.kuerzel" :model-value="Note.fromNoteSekI(value)" @update:model-value="note => patchSprachpruefung({ note: ((note === null) || (note === undefined)) ? null : note.ordinal() })" headless />
				</template>
				<template #cell(referenzniveau)="{ value }">
					<svws-ui-select title="Referenzniveau" headless :removable="true" :model-value="Sprachreferenzniveau.getByKuerzel(value.referenzniveau)" @update:model-value="referenzniveau => patchSprachpruefung({referenzniveau: referenzniveau?.daten.kuerzel ?? null})" :items="Sprachreferenzniveau.values()" :item-text="i => i.daten.kuerzel" />
				</template>
				<template #cell(pruefungsdatum)="{ value }">
					<svws-ui-text-input placeholder="Prüfungsdatum" :model-value="value" @change="pruefungsdatum => pruefungsdatum && patchSprachpruefung({pruefungsdatum})" type="date" headless />
				</template>
				<template #cell(ersetzteSprache)="{ value }">
					<svws-ui-select title="Ersetzte Sprache" headless :model-value="value" @update:model-value="ersetzteSprache => patchSprachpruefung({ersetzteSprache})" :items="sprachen(value).value" :item-text="i=> `${i} - ${ZulaessigesFach.getFremdspracheByKuerzelAtomar(i).daten.bezeichnung}`" />
				</template>
				<template #actions>
					<svws-ui-button @click="removePruefungen" type="trash" :disabled="auswahlPr.length === 0" />
					<svws-ui-button @click="hinzufuegenPruefungen" type="icon" title="Eine neue Sprache hinzufügen"><span class="icon i-ri-add-line" /></svws-ui-button>
				</template>
				<!-- -->
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { SchuelerLaufbahninfoProps } from './SchuelerLaufbahninfoProps';
	import type { DataTableColumn } from "@ui";
	import type { Sprachbelegung , Sprachpruefung} from '@core';
	import { Schulform, Sprachreferenzniveau, ZulaessigesFach, Jahrgaenge, ServerMode, Note } from '@core';

	const props = defineProps<SchuelerLaufbahninfoProps>();

	const auswahl = ref([]);
	const auswahlPr = ref([]);

	const colsSprachenfolge: DataTableColumn[] = [
		{ key: "sprache", label: "Sprache", tooltip: "Kürzel der Sprache", span: 2 },
		{ key: "reihenfolge", label: "Reihenfolge", tooltip: "Reihenfolge", divider: true },
		{ key: "belegungVonJahrgang", label: "ab Jg", tooltip: "belegt ab Jahrgang" },
		{ key: "belegungVonAbschnitt", label: "Halbjahr", tooltip: "belegt ab Abschnitt", divider: true },
		{ key: "belegungBisJahrgang", label: "bis Jg", tooltip: "belegt bis Jahrgang" },
		{ key: "belegungBisAbschnitt", label: "Halbjahr", tooltip: "belegt bis Abschnitt", divider: true },
		{ key: "referenzniveau", label: "Qualifikation", tooltip: "die erreichte Qualifikation", span: 2 },
	];

	const colsSprachpruefungen: DataTableColumn[] = [
		{ key: "sprache", label: "Sprache", tooltip: "Kürzel der Sprache", minWidth: 4 },
		{ key: "istHSUPruefung", label: "HSU", tooltip: "Prüfung ist eine Prüfung im herkunftssprachlichen Unterricht", minWidth: 4 },
		{ key: "istFeststellungspruefung", label: "Festestellungsprüfung", tooltip: "Prüfung ist eine Sprachfeststellungsprüfung", minWidth: 4 },
		{ key: "kannErstePflichtfremdspracheErsetzen", label: "Ersetzt 1", tooltip: "Durch die Prüfung kann die erste Pflichtfremdsprache ersetzt werden", minWidth: 4 },
		{ key: "kannZweitePflichtfremdspracheErsetzen", label: "Ersetzt 2", tooltip: "Durch die Prüfung kann die zweite Pflichtfremdsprache ersetzt werden", minWidth: 4 },
		{ key: "kannBelegungAlsFortgefuehrteSpracheErlauben", label: "Fortgeführt", tooltip: "Durch die Prüfung kann die Sprache als fortgeführte Fremdsprache in der GOSt belegt werden", align: 'center', minWidth: 4, span: 0.5 },
		{ key: "kannWahlpflichtfremdspracheErsetzen", label: "Ersetzt WPF", tooltip: "Durch die Prüfung kann die Wahlpflichtfremdsprache ersetzt werden", align: 'center', minWidth: 4, span: 0.5 },
		{ key: "jahrgang", label: "Jahrgang", tooltip: "Im Jahrgang", minWidth: 4 },
		{ key: "anspruchsniveauId", label: "Anspruchsniveau", tooltip: "Bezeichnung des am Schulabschluss orientierte Anspruchsniveau der Sprachprüfung", minWidth: 4 },
		{ key: "note", label: "Note", tooltip: "Prüfungsnote", minWidth: 6 },
		{ key: "referenzniveau", label: "Niveau", tooltip: "Das Kürzel des GeR-Referenzniveaus, welches durch die Prüfung erreicht wurde", minWidth: 6 },
		{ key: "pruefungsdatum", label: "Prüfungsdatum", tooltip: "Prüfungsdatum", minWidth: 6, span: 1.5 },
		{ key: "ersetzteSprache", label: "An Stelle von", tooltip: "Die durch die Prüfung ersetzte Sprache", minWidth: 4 },
	];

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

	const sprachen = (s: string) => computed(() => [s, ...verfuegbareSprachen.value])

	const sprachJahrgaenge = computed(() => {
		const schulform = props.schuelerListeManager().schulform();
		if ((schulform === Schulform.BK) || (schulform === Schulform.SB))
			return Jahrgaenge.get(Schulform.GE);
		return Jahrgaenge.get(schulform);
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

	async function hinzufuegen() {
		if (verfuegbareSprachen.value.length === 0)
			return;
		const data: Partial<Sprachbelegung> = {};
		data.sprache = verfuegbareSprachen.value[0];
		data.reihenfolge = props.sprachbelegungen().size() + 1;
		data.belegungVonAbschnitt = 1;
		data.belegungBisAbschnitt = 2;
		data.belegungVonJahrgang = props.schuelerListeManager().jahrgaenge.get(props.schuelerListeManager().auswahl().idJahrgang)?.kuerzel;
		await props.addSprachbelegung(data);
	}

	async function hinzufuegenPruefungen() {
		if (verfuegbareSprachen.value.length === 0)
			return;
		const data: Partial<Sprachpruefung> = {};
		data.sprache = verfuegbareSprachen.value[0];
		data.jahrgang = props.schuelerListeManager().jahrgaenge.get(props.schuelerListeManager().auswahl().idJahrgang)?.kuerzel;
		await props.addSprachpruefung(data);
	}

	async function removePruefungen() {
		for (const pruefung of auswahlPr.value)
			await props.removeSprachpruefung(pruefung);
	}

</script>
