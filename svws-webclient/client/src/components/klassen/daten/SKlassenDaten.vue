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
					<svws-ui-select title="Klassen-Jahrgang" v-model="jahrgang" :items="jahrgaenge" :item-text="textJahrgang" :empty-text="() => 'JU - Jahrgangsübergreifend'" removable />
					<svws-ui-select title="Parallelität" :model-value="data.parallelitaet ?? '---'"
						@update:model-value="value => patch({ parallelitaet: value === '---' ? null : value })"
						:items="['---','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z']" :item-text="p => p" />
					<svws-ui-text-input placeholder="Teilstandort" :model-value="data.teilstandort" type="text" disabled /> <!-- TODO Select mit der Liste der Teilstandorte für diese Schule -->
					<svws-ui-input-number placeholder="Sortierung" :model-value="data.sortierung" @change="sortierung => (sortierung !== null) && patch({ sortierung })" />
					<svws-ui-spacing />
					<svws-ui-select v-if="!listeVorgaengerklassen.isEmpty()" title="Vorgängerklasse" :model-value="data.idVorgaengerklasse === null ? null : mapKlassenVorigerAbschnitt().get(data.idVorgaengerklasse)"
						@update:model-value="value => patch({ idVorgaengerklasse: value?.id ?? null })"
						:items="listeVorgaengerklassen" :item-text="f => f.kuerzel ?? '---'" removable />
					<svws-ui-text-input v-else placeholder="Vorgängerklasse" :model-value="data.kuerzelVorgaengerklasse === null ? '&nbsp;' : data.kuerzelVorgaengerklasse" type="text" disabled />
					<svws-ui-select v-if="!listeFolgeklassen.isEmpty()" title="Folgeklasse" :model-value="data.idFolgeklasse === null ? null : mapKlassenFolgenderAbschnitt().get(data.idFolgeklasse)"
						@update:model-value="value => patch({ idFolgeklasse: value?.id ?? null })"
						:items="listeFolgeklassen" :item-text="f => f.kuerzel ?? '---'" removable />
					<svws-ui-text-input v-else placeholder="Folgeklasse" :model-value="data.kuerzelFolgeklasse === null ? '&nbsp;' : data.kuerzelFolgeklasse" type="text" disabled />
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
	import { SchuelerStatus, type JahrgangsListeEintrag, type KlassenDaten, Schulform, Schulgliederung, Klassenart, AllgemeinbildendOrganisationsformen, BerufskollegOrganisationsformen,
		WeiterbildungskollegOrganisationsformen, type KlassenListeEintrag, type List, ArrayList } from "@core";
	import type { KlassenDatenProps } from "./SKlassenDatenProps";

	const props = defineProps<KlassenDatenProps>();

	const data = computed<KlassenDaten>(() => props.klassenListeManager().daten());

	function textJahrgang(jg : JahrgangsListeEintrag) : string {
		if (jg.kuerzel === null)
			return 'JU - Jahrgangsübergreifend';
		if (jg.kuerzel === 'E1')
			return '1E' + ' - ' + jg.bezeichnung;
		if (jg.kuerzel === 'E2')
			return '2E' + ' - ' + jg.bezeichnung;
		return jg.kuerzel + ' - ' + jg.bezeichnung;
	}

	const jahrgang = computed<JahrgangsListeEintrag | null>({
		get: () => ((data.value === undefined) || (data.value.idJahrgang === null)) ? null : props.klassenListeManager().jahrgaenge.get(data.value.idJahrgang),
		set: (value) => void props.patch({ idJahrgang: value?.id ?? null })
	});

	const jahrgaenge = computed<List<JahrgangsListeEintrag>>(() => {
		const result = new ArrayList<JahrgangsListeEintrag>();
		for (const jg of props.klassenListeManager().jahrgaenge.list()) {
			if (jg.kuerzel !== "E3")  // Das dritte Jahr der Schuleingangsphase sollte nicht für einen Jahrgang einer Klasse verwendet werden, da es Schüler-spezifisch ist
				result.add(jg);
		}
		return result;
	});


	type Lehrer = {
		kuerzel?: string;
		nachname?: string;
		vorname?: string;
	}

	const listeFolgeklassen = computed<List<KlassenListeEintrag>>(() => {
		const result = new ArrayList<KlassenListeEintrag>();
		if (data.value.idJahrgang === null) {
			for (const kl of props.mapKlassenFolgenderAbschnitt().values())
				result.add(kl);
			return result;
		}
		const jg = props.klassenListeManager().jahrgaenge.get(data.value.idJahrgang);
		if (jg === null)
			return result;
		for (const kl of props.mapKlassenFolgenderAbschnitt().values()) {
			if (kl.idJahrgang === null) {
				result.add(kl);
			} else {
				const jgKl = props.klassenListeManager().jahrgaenge.get(kl.idJahrgang);
				if (jg.idFolgejahrgang === jgKl?.id)
					result.add(kl);
			}
		}
		return result;
	});

	const listeVorgaengerklassen = computed<List<KlassenListeEintrag>>(() => {
		const result = new ArrayList<KlassenListeEintrag>();
		if (data.value.idJahrgang === null) {
			for (const kl of props.mapKlassenVorigerAbschnitt().values())
				result.add(kl);
			return result;
		}
		const jg = props.klassenListeManager().jahrgaenge.get(data.value.idJahrgang);
		if (jg === null)
			return result;
		for (const kl of props.mapKlassenVorigerAbschnitt().values()) {
			if (kl.idJahrgang === null) {
				result.add(kl);
			} else {
				const jgKl = props.klassenListeManager().jahrgaenge.get(kl.idJahrgang);
				if (jg.id === jgKl?.idFolgejahrgang)
					result.add(kl);
			}
		}
		return result;
	});

	const listeKlassenleitungen = computed<Lehrer[]>(() => {
		const a = [];
		for (const id of props.klassenListeManager().daten().klassenLeitungen) {
			const lehrer = props.klassenListeManager().lehrer.get(id);
			if (lehrer)
				a.push({
					kuerzel: lehrer.kuerzel,
					nachname: lehrer.nachname,
					vorname: lehrer.vorname,
				});
		}
		return a;
	});

	const colsKlassenleitungen: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", span: 1, sortable: false },
		{ key: "nachname", label: "Nachname", span: 2, sortable: false },
		{ key: "vorname", label: "Vorname", span: 2, sortable: false },
	];


	const colsSchueler: DataTableColumn[] = [
		{ key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center" },
		{ key: "nachname", label: "Nachname", span: 1, sortable: true },
		{ key: "vorname", label: "Vorname", span: 1, sortable: true },
		{ key: "status", label: "Status", sortable: true, span: 0.5 }
	];

</script>
