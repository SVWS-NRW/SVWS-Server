<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Lehrkräfte</span>
		</template>
		<template #abschnitt>
			<svws-ui-multi-select v-if="schule_abschnitte" v-model="akt_abschnitt" :items="schule_abschnitte" :item-sort="item_sort" :item-text="item_text"></svws-ui-multi-select>
		</template>
		<template #header>
			<div>
				<div class="mb-2">
					<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Namen oder Kürzel"><i-ri-search-line /></svws-ui-text-input>
				</div>
			</div>
		</template>
		<template #content>
			<div class="container">
				<svws-ui-table v-model="selected" :columns="cols" :data="rowsFiltered" :footer="false" is-multi-select>
					<template #cell-actions="{ row }">
          				<svws-ui-popover :hover="false" placement="left-end" :disable-click-away="false">
            				<template #trigger>
              					<button class="action-button">
                					<svws-ui-icon> <i-ri-more-2-fill /> </svws-ui-icon>
              					</button>
            				</template>
            				<template #content>
              					<div class="action-items">
                					<div v-for="action in actions" :key="action.action">
                  						<button class="action-item" @click="onAction(action.action, row)">{{ action.label }}</button>
                					</div>
              					</div>
            				</template>
          				</svws-ui-popover>
        			</template>
				</svws-ui-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import {computed, ComputedRef, Ref, ref, WritableComputedRef} from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { routeLehrer } from "~/router/apps/RouteLehrer";
	import {Schuljahresabschnitt} from "@svws-nrw/svws-core-ts";
	import {Schule} from "~/apps/schule/Schule";

	const cols = [
		{ key: "kuerzel", label: "Kürzel", width: "10%", sortable: true, defaultSort: "asc" },
		{ key: "nachname", label: "Nachname", width: "45%", sortable: true, span: 2 },
		{ key: "vorname", label: "Vorname", width: "45%", sortable: true, span: 2 }
	];

	const actions = [
		{ label: "Löschen", action: "delete" },
		{ label: "Kopieren", action: "copy" }
	];

	const search: Ref<string> = ref("");

	const main: Main = injectMainApp();
	const app = main.apps.lehrer;

	const props = defineProps<{ id?: number; item?: LehrerListeEintrag, routename: string }>();

	const rows: ComputedRef<LehrerListeEintrag[] | undefined> = computed(() => {
		return app.auswahl.liste;
	});

	const rowsFiltered: ComputedRef<LehrerListeEintrag[] | undefined> = computed(() => {
		if (rows.value === undefined)
			return undefined;
		if (search.value) {
			return rows.value.filter(
				(e: LehrerListeEintrag) =>
					e.nachname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()) ||
					e.vorname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
			);
		}
		return rows.value;
	});

	const selected = routeLehrer.auswahl;

	function onAction(action: string, item: LehrerListeEintrag) {
		console.log(action, item);
	}

	const schule_abschnitte: ComputedRef<
		Array<Schuljahresabschnitt> | undefined
	> = computed(() => {
		const liste = appSchule.value.schuleStammdaten.daten?.abschnitte;
		return liste?.toArray(new Array<Schuljahresabschnitt>()) || [];
	});

	const akt_abschnitt: WritableComputedRef<Schuljahresabschnitt> = computed({
		get(): Schuljahresabschnitt {
			return main.config.akt_abschnitt;
		},
		set(abschnitt: Schuljahresabschnitt) {
			main.config.akt_abschnitt = abschnitt;
		}
	});

	const appSchule: ComputedRef<Schule> = computed(() => {
		return main.apps.schule;
	});

	function item_sort(a: Schuljahresabschnitt, b: Schuljahresabschnitt) {
		return (
			b.schuljahr + b.abschnitt * 0.1 - (a.schuljahr + a.abschnitt * 0.1)
		);
	}

	function item_text(item: Schuljahresabschnitt) {
		return item.schuljahr
			? `${item.schuljahr}, ${item.abschnitt}. HJ`
			: "Abschnitt";
	}
</script>


<style>
	.action-button {
		@apply h-6 w-6;
	}

	.action-items {
		@apply bg-white;
		@apply flex flex-col;
		@apply px-2 py-1;
		@apply ring-1;
		@apply ring-black ring-opacity-5;
		@apply rounded-md;
		@apply shadow-lg;
		@apply w-48;
	}
</style>
