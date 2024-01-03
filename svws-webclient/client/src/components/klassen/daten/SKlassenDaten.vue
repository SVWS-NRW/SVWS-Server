<template>
	<div class="page--content">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<template #actions>
					<svws-ui-checkbox :model-value="data.istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })"> Ist sichtbar </svws-ui-checkbox>
				</template>
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel => patch({ kuerzel })" type="text" />
					<svws-ui-text-input placeholder="Beschreibung" :model-value="data.beschreibung" @change="beschreibung => patch({ beschreibung })" type="text" />
					<svws-ui-spacing />
					<svws-ui-select title="Jahrgang" v-model="jahrgang" :items="klassenListeManager().jahrgaenge.list()" :item-text="item => item.kuerzel ?? ''" />
					<svws-ui-select title="Parallelität" :model-value="data.parallelitaet ?? '---'"
						@update:model-value="value => patch({ parallelitaet: value === '---' ? null : value })"
						:items="['---','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z']" :item-text="p => p" />
					<svws-ui-text-input placeholder="Teilstandort" :model-value="data.teilstandort" type="text" disabled /> <!-- TODO Select mit der Liste der Teilstandorte für diese Schule -->
					<svws-ui-input-number placeholder="Sortierung" :model-value="data.sortierung" @change="sortierung => (sortierung !== null) && patch({ sortierung })" />
					<svws-ui-spacing />
					<!-- TODO Falls IDs vorhanden hier Selects mit den entsprechenden Klassen-Katalogen einbauen -->
					<svws-ui-text-input placeholder="Vorgängerklasse" :model-value="data.kuerzelVorgaengerklasse === null ? '&nbsp;' : data.kuerzelVorgaengerklasse" type="text" disabled />
					<svws-ui-text-input placeholder="Folgeklasse" :model-value="data.kuerzelFolgeklasse === null ? '&nbsp;' : data.kuerzelFolgeklasse" type="text" disabled />
					<svws-ui-spacing />
					<svws-ui-select title="Schulgliederung" :model-value="Schulgliederung.getByID(data.idSchulgliederung)"
						@update:model-value="value => patch({ idSchulgliederung: value?.daten.id ?? -1 })"
						:items="schulgliederungen" :item-text="f => f.daten.kuerzel + ' - ' + f.daten.beschreibung" />
					<svws-ui-text-input placeholder="Prüfungsordnung" :model-value="data.pruefungsordnung" type="text" disabled />
					<svws-ui-select title="Klassenart" :model-value="Klassenart.getByID(data.idKlassenart)"
						@update:model-value="value => patch({ idKlassenart: value?.daten.id ?? -1 })"
						:items="Klassenart.get(schulform)" :item-text="f => f.daten.kuerzel + ' - ' + f.daten.bezeichnung" />
					<svws-ui-select v-if="data.idAllgemeinbildendOrganisationsform !== null" title="Organisationsform" :model-value="AllgemeinbildendOrganisationsformen.getByID(data.idAllgemeinbildendOrganisationsform)"
						@update:model-value="value => patch({ idAllgemeinbildendOrganisationsform: value?.daten.id ?? -1 })"
						:items="AllgemeinbildendOrganisationsformen.values()" :item-text="f => f.daten.kuerzel + ' - ' + f.daten.beschreibung" />
					<svws-ui-select v-if="data.idBerufsbildendOrganisationsform !== null" title="Organisationsform" :model-value="BerufskollegOrganisationsformen.getByID(data.idBerufsbildendOrganisationsform)"
						@update:model-value="value => patch({ idBerufsbildendOrganisationsform: value?.daten.id ?? -1 })"
						:items="BerufskollegOrganisationsformen.values()" :item-text="f => f.daten.kuerzel + ' - ' + f.daten.beschreibung" />
					<svws-ui-select v-if="data.idWeiterbildungOrganisationsform !== null" title="Organisationsform" :model-value="WeiterbildungskollegOrganisationsformen.getByID(data.idWeiterbildungOrganisationsform)"
						@update:model-value="value => patch({ idWeiterbildungOrganisationsform: value?.daten.id ?? -1 })"
						:items="WeiterbildungskollegOrganisationsformen.values()" :item-text="f => f.daten.kuerzel + ' - ' + f.daten.beschreibung" />
				</svws-ui-input-wrapper>
				<svws-ui-spacing :size="2" />
				<svws-ui-input-wrapper :grid="1">
					<svws-ui-checkbox :model-value="data.noteneingabeGesperrt" @update:model-value="noteneingabeGesperrt => patch({ noteneingabeGesperrt })"> Noteneingabe gesperrt </svws-ui-checkbox>
					<svws-ui-checkbox v-if="schulform === Schulform.G" :model-value="data.verwendungAnkreuzkompetenzen" @update:model-value="verwendungAnkreuzkompetenzen => patch({ verwendungAnkreuzkompetenzen })"> In dieser Klasse werden Ankreuzkompetenzen verwendet </svws-ui-checkbox>
					<svws-ui-checkbox v-if="schulform === Schulform.WB" :model-value="data.beginnSommersemester" @update:model-value="beginnSommersemester => patch({ beginnSommersemester })"> Beginn im Sommersemester </svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card title="Klassenleitung">
				<svws-ui-table :columns="colsKlassenleitungen" :items="listeKlassenleitungen" />
			</svws-ui-content-card>
		</div>
		<svws-ui-content-card title="Klassenliste">
			<svws-ui-table :columns="colsSchueler" :items="klassenListeManager().daten().schueler">
				<template #cell(status)="{ value } : { value: number}">
					<span :class="{'opacity-25': value === 2}">{{ SchuelerStatus.fromID(value)?.bezeichnung || "" }}</span>
				</template>
				<template #header(linkToSchueler)>
					<i-ri-group-line />
				</template>
				<template #cell(linkToSchueler)="{ rowData }">
					<button type="button" @click.stop="gotoSchueler(rowData)" class="button button--icon" title="Schüler ansehen">
						<i-ri-link />
					</button>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { type DataTableColumn } from "@ui";
	import { PersonalTyp, SchuelerStatus, type JahrgangsListeEintrag, type KlassenDaten, Schulform, Schulgliederung, Klassenart, AllgemeinbildendOrganisationsformen, BerufskollegOrganisationsformen,
		WeiterbildungskollegOrganisationsformen } from "@core";
	import type { KlassenDatenProps } from "./SKlassenDatenProps";

	const props = defineProps<KlassenDatenProps>();

	const data = computed<KlassenDaten>(() => props.klassenListeManager().daten());

	const jahrgang = computed<JahrgangsListeEintrag | null>({
		get: () => ((data.value === undefined) || (data.value.idJahrgang === null)) ? null : props.klassenListeManager().jahrgaenge.get(data.value.idJahrgang),
		set: (value) => void props.patch({ idJahrgang: value?.id ?? null })
	});


	type Lehrer = {
		kuerzel?: string;
		nachname?: string;
		vorname?: string;
		typ?: string;
	}

	const listeKlassenleitungen = computed<Lehrer[]>(() => {
		const a = [];
		for (const id of props.klassenListeManager().daten().klassenLeitungen) {
			const lehrer = props.klassenListeManager().lehrer.get(id);
			if (lehrer)
				a.push({
					kuerzel: lehrer.kuerzel,
					nachname: lehrer.nachname,
					vorname: lehrer.vorname,
					typ: PersonalTyp.fromBezeichnung(lehrer.personTyp)?.bezeichnung ?? undefined
				});
		}
		return a;
	});

	const colsKlassenleitungen: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", span: 1, sortable: false },
		{ key: "nachname", label: "Nachname", span: 2, sortable: false },
		{ key: "vorname", label: "Vorname", span: 2, sortable: false },
		{ key: "personTyp", label: "Typ", span: 1, sortable: false },
	];


	const colsSchueler: DataTableColumn[] = [
		{ key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center" },
		{ key: "nachname", label: "Nachname", span: 1, sortable: true },
		{ key: "vorname", label: "Vorname", span: 1, sortable: true },
		{ key: "status", label: "Status", sortable: true, span: 0.5 }
	];

</script>
