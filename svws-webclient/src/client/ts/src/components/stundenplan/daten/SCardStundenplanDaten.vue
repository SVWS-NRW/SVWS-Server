<template>
	<svws-ui-content-card title="Allgemein" class="lg:sticky lg:top-8 lg:col-start-1">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Bezeichnung" :model-value="stundenplanManager().getBezeichnungStundenplan()" @blur="bezeichnungStundenplan=>patch({ bezeichnungStundenplan })" type="text" />
			<svws-ui-text-input placeholder="Wochentypmodell" :model-value="stundenplanManager().getWochenTypModell()" @blur="patch({ wochenTypModell: Number($event) })" type="number" />
			<svws-ui-text-input placeholder="Gültig ab" :model-value="stundenplanManager().getGueltigAb()" @blur="gueltigAb=>patch({ gueltigAb })" type="date" />
			<svws-ui-text-input placeholder="Gültig bis" :model-value="stundenplanManager().getGueltigBis()" @blur="gueltigBis=>patch({ gueltigBis })" type="date" />
			<div class="col-span-full">
				<svws-ui-data-table :items="listJahrgaenge" :no-data="false" :columns="cols">
					<template #cell(id)="{value}">
						<svws-ui-checkbox circle :model-value="jahrgaenge.includes(value)" headless @update:model-value="updateJahrgaenge(value)" />
					</template>
				</svws-ui-data-table>
			</div>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { type JahrgangsListeEintrag, type List, type Stundenplan, type StundenplanManager } from "@core";
	import type { DataTableColumn } from "@ui";
	import { computed } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		patch: (data: Partial<Stundenplan>) => Promise<void>;
		listJahrgaenge: List<JahrgangsListeEintrag>;
		addJahrgang: (id: number) => Promise<void>;
		removeJahrgang: (id: number) => Promise<void>;
	}>();

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc", span: 0.25 },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
		{ key: "id", label: "Gültig", sortable: true, fixedWidth: 8, align: "center" }
	];

	const jahrgaenge = computed(()=> {
		const list = props.stundenplanManager().jahrgangGetMengeAsList();
		const a = [];
		for (const j of list)
			a.push(j.id);
		return a;
	})

	async function updateJahrgaenge(id: number) {
		if (jahrgaenge.value.includes(id))
			await props.removeJahrgang(id);
		else {
			await props.removeJahrgang(id);
		}
	}

</script>
