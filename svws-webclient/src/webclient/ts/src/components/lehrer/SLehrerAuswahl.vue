<template>
	<svws-ui-secondary-menu>
		<template #headline> Lehrerauswahl </template>
		<template #header>
			<div class="px-6 pt-3">
				<div class="mt-4">
					<svws-ui-text-input
						v-model="search"
						type="search"
						placeholder="Suche nach Namen oder Kürzel"
						><i-ri-search-line
					/></svws-ui-text-input>
				</div>
			</div>
		</template>
		<template #content>
			<div class="container">
				<svws-ui-new-table
					v-model="selected"
					:columns="cols"
					:data="rowsFiltered"
					:footer="false"
					is-multi-select
				>
					<template #cell-actions="{ row }">
          				<svws-ui-popover :hover="false" placement="left-end" :disable-click-away="false">
            				<template #trigger>
              					<Button class="action-button">
                					<Icon>
                  						<i-ri-more-2-fill />
                					</Icon>
              					</Button>
            				</template>
            				<template #content>
              					<div class="action-items">
                					<div v-for="action in actions" :key="action.action">
                  						<Button class="action-item" type="transparent" @click="onAction(action.action, row)">{{ action.label
                  						}}</Button>
                					</div>
              					</div>
            				</template>
          				</svws-ui-popover>
        			</template>
				</svws-ui-new-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">
	import type { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { useAuswahlViaRoute } from '~/router/auswahlViaRoute';

	const cols = [
	{
			key: "kuerzel",
			label: "Kuerzel",
			width: "10%",
			sortable: true,
			defaultSort: "asc"
		},
		{
			key: "nachname",
			label: "Nachname",
			width: "45%",
			sortable: true
		},
		{
			key: "vorname",
			label: "Vorname",
			width: "45%",
			sortable: true
		},
  		{
    		key: 'actions',
    		label: ''
  		}
	];

	const actions = [
		{ label: "Löschen", action: "delete" },
		{ label: "Kopieren", action: "copy" }
	];

	const search: Ref<string> = ref("");

	const main: Main = injectMainApp();
	const app = main.apps.lehrer;

	const rows: ComputedRef<LehrerListeEintrag[] | undefined> = computed(() => {
		return app.auswahl.liste;
	});

	const rowsFiltered: ComputedRef<LehrerListeEintrag[] | undefined> =
		computed(() => {
			if (rows.value) {
				const rowsValue: LehrerListeEintrag[] = rows.value;
				if (search.value) {
					return rowsValue.filter(
						(e: LehrerListeEintrag) =>
							e.nachname
								.toLocaleLowerCase()
								.includes(search.value.toLocaleLowerCase()) ||
							e.vorname
								.toLocaleLowerCase()
								.includes(search.value.toLocaleLowerCase())
					);
				}
				return rowsValue;
			}
			return undefined;
		});

	const selected = useAuswahlViaRoute('lehrer');

	function onAction(action: string, item: LehrerListeEintrag) {
		console.log(action, item);
	}
</script>
