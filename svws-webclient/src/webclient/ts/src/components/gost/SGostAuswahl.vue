<template>
	<svws-ui-secondary-menu>
		<template #headline>Abiturjahrgänge</template>
		<template #abschnitt>
			<svws-ui-multi-select v-if="schule_abschnitte" v-model="akt_abschnitt" :items="schule_abschnitte" :item-sort="item_sort" :item-text="item_text" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-table v-model="selected" :columns="cols" :data="rows" id="abiturjahr" :footer="true">
					<template #cell-abiturjahr="{ row }">
						{{ row.abiturjahr === -1 ? '' : row.abiturjahr }}
						<span v-if="(pending && row.abiturjahr === selected?.abiturjahr)" class="loading-spinner-dimensions">
							<img src="/loading_spinner.svg" alt="Ladeanzeige" class="loading-spinner-dimensions loading-rotation"></span>
					</template>
					<template #footer>
						<svws-ui-dropdown type="icon" class="">
							<template #dropdownButton>Abiturjahr hinzufügen</template>
							<template #dropdownItems>
								<svws-ui-dropdown-item v-for="jahrgang in jahrgaenge"
									:key="jahrgang.id"
									class="px-2"
									@click="abiturjahr_hinzufuegen(jahrgang)">
									{{ jahrgang.kuerzel }}
								</svws-ui-dropdown-item>
							</template>
						</svws-ui-dropdown>
					</template>
				</svws-ui-table>
				<router-view name="gost_kursplanung_auswahl" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { GostJahrgang, JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { routeGost } from "~/router/apps/RouteGost";
	import { Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";
	import { DataTableColumn } from "@svws-nrw/svws-ui";
	import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { routeGostKursplanungHalbjahr } from "~/router/apps/gost/kursplanung/RouteGostKursplanungHalbjahr";

	const props = defineProps<{
		item: ShallowRef<GostJahrgang | undefined>;
		schule: DataSchuleStammdaten;
		jahrgangsdaten: DataGostJahrgang;
		dataFaecher: DataGostFaecher;
	}>();

	const selected: WritableComputedRef<GostJahrgang | undefined> = routeGost.auswahl;
	const main: Main = injectMainApp();
	const appJahrgaenge = main.apps.jahrgaenge;

	const cols: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2 },
		{ key: "abiturjahr", label: "Abiturjahr", sortable: true },
		{ key: "jahrgang", label: "Stufe", sortable: true }];

	const rows: ComputedRef<GostJahrgang[]> =
		computed(() => {
			const list = [...routeGost.liste.liste];
			return list.sort((a, b) =>
				(a?.bezeichnung || "") < (b?.bezeichnung || "") ? 1 : -1)
		});

	const jahrgaenge: ComputedRef<JahrgangsListeEintrag[]> =
		computed( () => {
			const set = new Set(routeGost.liste.liste.map(r=>r.jahrgang))
			return appJahrgaenge.auswahl.liste.filter(j=>!set.has(j.kuerzel))
		});

	const pending: ComputedRef<boolean> = computed(() => routeGostKursplanungHalbjahr.data.dataKursblockung.pending);

	async function abiturjahr_hinzufuegen(jahrgang: JahrgangsListeEintrag) {
		try {
			const abiturjahr = await props.jahrgangsdaten.post_jahrgang(jahrgang.id);
			await routeGost.liste.update_list();
			const jahr = routeGost.liste.liste?.find(j => j.abiturjahr === abiturjahr);
			selected.value = jahr;
		} catch (e) {
			console.log("Fehler: ", e);
		}
	}

	const schule_abschnitte: ComputedRef<Array<Schuljahresabschnitt> | undefined> = computed(() =>
		props.schule.daten?.abschnitte?.toArray(new Array<Schuljahresabschnitt>()) || []
	);

	const akt_abschnitt: WritableComputedRef<Schuljahresabschnitt> = computed({
		get: () => main.config.akt_abschnitt,
		set: (value) => main.config.akt_abschnitt = value
	});

	const item_sort = (a: Schuljahresabschnitt, b: Schuljahresabschnitt) => b.schuljahr + b.abschnitt * 0.1 - (a.schuljahr + a.abschnitt * 0.1);

	const item_text = (item: Schuljahresabschnitt) => item.schuljahr ? `${item.schuljahr}, ${item.abschnitt}. HJ` : "Abschnitt";

</script>

<style>

	.loading-disclaimer {
		background-color: rgba(var(--svws-ui-color-dark-20), var(--tw-border-opacity));
		--tw-bg-opacity: 1;
		--tw-border-opacity: 1;
		border-width: 1px;
		padding: .25rem .75rem;
		line-height: 1.125;
	}

	.loading-spinner-dimensions {
		height: 1rem;
		width: 1rem;
	}

	.loading-display {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
	}

	.loading-rotation {
		display: block;
		position: relative;
		-webkit-animation: spin 6s steps(11, end) infinite;
		-moz-animation: spin 6s steps(11, end) infinite;
		animation: spin 6s steps(11, end) infinite;
	}

	.api-error-text {
		color: rgb(var(--svws-ui-color-error));
		font-weight: 700;
	}

	@-webkit-keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}

		100% {
			-webkit-transform: rotate(360deg);
		}
	}

	@-moz-keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}

		100% {
			-webkit-transform: rotate(360deg);
		}
	}

	@keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}

		100% {
			-webkit-transform: rotate(360deg);
		}
	}

</style>
