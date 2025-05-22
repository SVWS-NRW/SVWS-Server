<template>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper :grid="1">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input class="contentFocusField" placeholder="Bezeichnung" :model-value="manager().daten().bezeichnung"
						@change="v => patch({ bezeichnung: v ?? undefined })" :max-len="50" :min-len="1"
						:readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Raum" :max-len="20" :model-value="manager().daten().raum"
						@change="raum => patch({ raum })" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Email" type="email" :max-len="100" :model-value="manager().daten().email"
						@change="email => patch({ email })" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Durchwahl" type="tel" :max-len="20" :model-value="manager().daten().durchwahl"
						@change="durchwahl => patch({ durchwahl })" :readonly="!hatKompetenzUpdate" />
					<svws-ui-spacing />
					<svws-ui-select title="Lehrer" :items="manager().getLehrer().values()" :item-text="v => v.vorname + ' ' + v.nachname"
						:model-value="manager().getLehrer().get(manager().daten().idAbteilungsleiter)"
						@update:model-value="v => patch({ idAbteilungsleiter: v?.id })" :readonly="!hatKompetenzUpdate" />
					<svws-ui-button :disabled="manager().daten().idAbteilungsleiter === null" type="transparent"
						@click="goToLehrer(manager().daten().idAbteilungsleiter ?? -1)">
						<span class="icon i-ri-link" /> Zum Lehrer
					</svws-ui-button>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing />
			<svws-ui-content-card title="Klassen">
				<svws-ui-table :items="manager().getKlassenByAuswahl()" :columns :selectable="hatKompetenzUpdate" v-model="selectedKlassen" clickable>
					<template #actions v-if="hatKompetenzUpdate">
						<svws-ui-button @click="deleteSelectedKlassen" type="trash" :disabled="selectedKlassen.length === 0" />
					</template>
				</svws-ui-table>
			</svws-ui-content-card>
		</svws-ui-input-wrapper>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { AbteilungenDatenProps } from "~/components/schule/kataloge/abteilungen/daten/SAbteilungenDatenProps";
	import type { DataTableColumn } from "@ui";
	import type { AbteilungKlassenzuordnung, KlassenDaten } from "@core";
	import { ArrayList, HashMap, BenutzerKompetenz } from "@core";

	const props = defineProps<AbteilungenDatenProps>();

	const selectedKlassen = ref<KlassenDaten[]>([]);

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));

	const columns: DataTableColumn[] = [ { key: "kuerzel", label: "Klasse"} ];

	const klassenzuordnungenByIdKlasse = computed(() => {
		const result = new HashMap<number, AbteilungKlassenzuordnung>();
		for (const k of props.manager().daten().klassenzuordnungen)
			result.put(k.idKlasse, k)
		return result;
	});

	async function deleteSelectedKlassen() {
		if (selectedKlassen.value.length === 0)
			return;
		const ids = new ArrayList<number>();
		for (const k of selectedKlassen.value) {
			const zuordnung = klassenzuordnungenByIdKlasse.value.get(k.id);
			if (zuordnung !== null)
				ids.add(zuordnung.id)
		}
		await props.deleteKlassenzuordnungen(ids);
		selectedKlassen.value = [];
	}

</script>
