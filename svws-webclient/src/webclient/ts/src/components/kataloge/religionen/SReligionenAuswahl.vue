<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="router.push({name: 'kataloge' })">Kataloge</a>
				<span>Religionen</span>
			</nav>
		</template>
		<template #abschnitt>
			<svws-ui-multi-select v-if="schule_abschnitte" v-model="akt_abschnitt" :items="schule_abschnitte" :item-sort="item_sort" :item-text="item_text"></svws-ui-multi-select>
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-table v-model="selected" :columns="cols" :data="rows" :footer="true">
					<template #footer>
						<button @click="modalAdd.openModal()" class="flex h-10 w-10 items-center justify-center">
							<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
						</button>
					</template>
				</svws-ui-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
	<svws-ui-modal ref="modalAdd" size="medium">
		<template #modalTitle>Religion Hinzufügen</template>
		<template #modalContent>
			<div class="input-wrapper">
				<svws-ui-multi-select v-model="reli_neu.kuerzel" title="Statistikkürzel" :items="inputKatalogReligionenStatistik"
					:item-text="(i: Religion) => i.daten.kuerzel.toString()" required />
				<svws-ui-text-input v-model="reli_neu.kuerzel" type="text" placeholder="Kürzel" />
				<svws-ui-text-input v-model="reli_neu.text" type="text" placeholder="Bezeichnung" />
				<svws-ui-text-input v-model="reli_neu.textZeugnis" type="text" placeholder="Zeugnisbezeichnung" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button v-if="reli_neu.kuerzel || reli_neu.textZeugnis || reli_neu.text" type="secondary" @click="deleteEntries()"> Felder Leeren </svws-ui-button>
			<svws-ui-button type="secondary" @click="modalAdd.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="saveEntries()"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { Religion, ReligionEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, reactive, ref, ShallowRef, WritableComputedRef } from "vue";
	import { App } from "~/apps/BaseApp";
	import { router } from "~/router";
	import { injectMainApp, Main } from "~/apps/Main";
	import { routeKatalogReligion } from "~/router/apps/RouteKatalogReligion";
	import { Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";
	import { Schule } from "~/apps/schule/Schule";
	import { DataTableColumn } from "@svws-nrw/svws-ui";

	const props = defineProps<{
		item: ShallowRef<ReligionEintrag | undefined>;
	}>();

	const selected = routeKatalogReligion.auswahl;

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "text", label: "Bezeichnung", sortable: true, span: 2 }
	];

	const main: Main = injectMainApp();
	const modalAdd = ref();

	const rows: ComputedRef<ReligionEintrag[]> = computed(() => routeKatalogReligion.liste.liste);

	/** Modalfenster: Neue Religion */
	const reli_neu: ReligionEintrag = reactive(new ReligionEintrag());

	const inputKatalogReligionenStatistik: ComputedRef<Religion[] | undefined> = computed(() => Religion.values());

	async function saveEntries() {
		//TODO  Den Attributswert von reli_neu.id von 0 auf null setzen.
		if (reli_neu.kuerzel) {
			await App.api.createReligion(reli_neu, App.schema);
			modalAdd.value.closeModal();
			routeKatalogReligion.liste.update_list();
		} else {
			alert("Kürzel darf nicht leer sein");
		}
		deleteEntries();
	}

	function deleteEntries() {
		reli_neu.kuerzel = null;
		reli_neu.text = null;
		reli_neu.textZeugnis = null;
	}

	const appSchule: ComputedRef<Schule> = computed(() => main.apps.schule);

	const schule_abschnitte: ComputedRef<Array<Schuljahresabschnitt> | undefined> = computed(() => {
		const liste = appSchule.value.schuleStammdaten.daten?.abschnitte;
		return liste?.toArray(new Array<Schuljahresabschnitt>()) || [];
	});

	const akt_abschnitt: WritableComputedRef<Schuljahresabschnitt> = computed({
		get: () => main.config.akt_abschnitt,
		set: (abschnitt) => main.config.akt_abschnitt = abschnitt
	});

	const item_sort = (a: Schuljahresabschnitt, b: Schuljahresabschnitt) => (b.schuljahr + b.abschnitt * 0.1 - (a.schuljahr + a.abschnitt * 0.1));

	const item_text = (item: Schuljahresabschnitt) => item.schuljahr ? `${item.schuljahr}, ${item.abschnitt}. HJ` : "Abschnitt";

</script>
