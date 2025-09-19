<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Basisdaten">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Name" :model-value="daten.name1" @change="name1=>patch({name1: name1 ?? undefined})" :readonly />
				<svws-ui-text-input placeholder="Namensergänzung" :model-value="daten.name2" @change="name2=>patch({name2: name2 ?? undefined})" :readonly />
				<svws-ui-select title="Beschäftigungsart" :model-value="beschaeftigungsart" :items="mapBeschaeftigungsarten" :item-text="i => i.text ?? ''" :readonly />
				<svws-ui-text-input placeholder="Branche" :model-value="daten.branche" @change="branche=>patch({branche: branche ?? undefined})" :readonly />
			</svws-ui-input-wrapper>
			<svws-ui-spacing :size="2" />
			<svws-ui-input-wrapper :grid="2" class="input-wrapper--checkboxes">
				<svws-ui-checkbox :model-value="daten.ausbildungsbetrieb" @update:model-value="ausbildungsbetrieb => patch({ ausbildungsbetrieb } )" :readonly>Ausbildungsbetrieb</svws-ui-checkbox>
				<svws-ui-checkbox :model-value="daten.bietetPraktika" @update:model-value="bietetPraktika => patch({ bietetPraktika })" :readonly> Bietet Praktikumsplätze </svws-ui-checkbox>
				<svws-ui-checkbox :model-value="daten.Massnahmentraeger" @update:model-value="Massnahmentraeger => patch({ Massnahmentraeger })" :readonly> Maßnahmenträger </svws-ui-checkbox>
				<svws-ui-checkbox :model-value="daten.ErwFuehrungszeugnis" @update:model-value="ErwFuehrungszeugnis => patch({ ErwFuehrungszeugnis })" :readonly> Erweitertes Führungszeugnis notwendig </svws-ui-checkbox>
				<svws-ui-checkbox :model-value="daten.BelehrungISG" @update:model-value="BelehrungISG => patch({ BelehrungISG })" :readonly> Belehrung nach Infektionsschutzgesetz notwendig </svws-ui-checkbox>
			</svws-ui-input-wrapper>
			<svws-ui-spacing />
			<svws-ui-spacing />
			<svws-ui-textarea-input :model-value="daten.bemerkungen" @change="bemerkungen => patch({ bemerkungen: bemerkungen || '' })" placeholder="Bemerkungen" :readonly />
		</svws-ui-content-card>
		<svws-ui-content-card title="Adresse">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Straße / Hausnummer" :model-value="daten.strassenname" @change="strassenname=>patch({strassenname: strassenname ?? undefined})" :readonly />
				<svws-ui-text-input placeholder="Zusatz" :model-value="daten.hausnrzusatz" @change="hausnrzusatz=>patch({hausnrzusatz: hausnrzusatz ?? undefined})" :readonly />
				<svws-ui-select title="Wohnort" v-model="inputWohnortID" :items="mapOrte" :item-text="i => `${i.plz} ${i.ortsname}`" autocomplete :readonly
					class="col-span-full" statistics />
				<!-- <svws-ui-select title="Ortsteil" v-model="inputOrtsteilID" :items="mapOrtsteile" :item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''"
            :item-sort="ortsteilSort" :item-filter="ortsteilFilter" /> -->
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon" :model-value="daten.telefon1" @change="telefon1=>patch({telefon1: telefon1 ?? undefined})" :readonly :max-len="20" />
				<svws-ui-text-input placeholder="2. Telefon" :model-value="daten.telefon2" @change="telefon2=>patch({telefon2: telefon2 ?? undefined})" :readonly :max-len="20" />
				<svws-ui-text-input placeholder="E-Mail Adresse" :model-value="daten.email" @change="email=>patch({email: email ?? undefined})" type="email" verify-email :readonly />
				<svws-ui-text-input placeholder="Fax" :model-value="daten.fax" @change="fax=>patch({fax: fax ?? undefined})" :readonly :max-len="20" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Ansprechpartner">
			<svws-ui-table :columns :items="mapAnsprechpartner.values()" :selectable="hatKompetenzUpdate" :model-value="selected" @update:model-value="selected=$event" count>
				<template #cell(anrede)="{ rowData }">
					<svws-ui-text-input :model-value="rowData.anrede" @change="anrede => patchBetriebAnpsrechpartner({anrede}, rowData.id)" headless required />
				</template>
				<template #cell(name)="{ rowData }">
					<svws-ui-text-input :model-value="rowData.name" @change="name=>patchBetriebAnpsrechpartner({name}, rowData.id)" headless required />
				</template>
				<template #cell(vorname)="{ rowData }">
					<svws-ui-text-input :model-value="rowData.vorname" @change="vorname=>patchBetriebAnpsrechpartner({vorname}, rowData.id)" headless required />
				</template>
				<template #cell(telefon)="{ rowData }">
					<svws-ui-text-input :model-value="rowData.telefon" @change="telefon=>patchBetriebAnpsrechpartner({telefon}, rowData.id)" headless required :max-len="20" />
				</template>
				<template #cell(email)="{ rowData }">
					<svws-ui-text-input :model-value="rowData.email" @change="email=>patchBetriebAnpsrechpartner({email}, rowData.id)" headless required />
				</template>
				<template #actions>
					<svws-ui-button @click="remove" type="trash" :disabled="!selected.length || !hatKompetenzUpdate" />
					<s-card-betriebe-add-anprechpartner-modal v-slot="{ openModal }" :add-betrieb-ansprechpartner :benutzer-kompetenzen="props.benutzerKompetenzen">
						<svws-ui-button @click="openModal()" type="icon" title="Ansprechpartner hinzufügen" :disabled="!hatKompetenzUpdate"> <span class="icon i-ri-add-line" /> </svws-ui-button>
					</s-card-betriebe-add-anprechpartner-modal>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { ref, computed, watch } from 'vue';
	import type { BetriebeDatenProps } from './SBetriebeDatenProps';
	import type { BetriebAnsprechpartner, KatalogEintrag, OrtKatalogEintrag} from '@core';
	import { BenutzerKompetenz } from '@core';

	const props = defineProps<BetriebeDatenProps>()
	const selected = ref<BetriebAnsprechpartner[]>([]);
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const columns = [
		{key: 'anrede', label: 'Anrede', span: 1},
		{key: 'name', label: 'Name', span: 2},
		{key: 'vorname', label: 'Rufname', span: 2},
		{key: 'telefon', label: 'Telefon', span: 2},
		{key: 'email', label: 'Email', span: 3},
		{key: 'id', label: 'ID', span: 0.5},
	]

	const inputWohnortID = computed<OrtKatalogEintrag | null>({
		get: () => props.daten.ort_id !== null ? props.mapOrte.get(props.daten.ort_id) ?? null : null,
		set: (val) =>	void props.patch({ ort_id : val?.id }),
	});

	const beschaeftigungsart = computed<KatalogEintrag | null>({
		get: () => (props.daten.adressArt === null) ? null : props.mapBeschaeftigungsarten.get(props.daten.adressArt) ?? null,
		set: (value) => void props.patch({ adressArt: value?.id}),
	});

	watch(() => props.daten, () => selected.value = []);

	async function remove() {
		await props.removeBetriebAnsprechpartner(selected.value);
		selected.value = [];
	}
</script>
