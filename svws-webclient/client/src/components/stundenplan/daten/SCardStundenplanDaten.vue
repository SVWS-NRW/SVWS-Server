<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Bezeichnung" :model-value="stundenplanManager().getBezeichnungStundenplan()" @change="bezeichnungStundenplan=>patch({ bezeichnungStundenplan })" type="text" />
			<svws-ui-input-number placeholder="Wochentypmodell" :model-value="stundenplanManager().getWochenTypModell() || 1" @change="wochenTypModell => doPatch(wochenTypModell)" type="number" min="1" />
			<svws-ui-text-input placeholder="Gültig ab" :model-value="stundenplanManager().getGueltigAb()" @change="gueltigAb=>patch({ gueltigAb })" type="date" />
			<svws-ui-text-input placeholder="Gültig bis" :model-value="stundenplanManager().getGueltigBis()" @change="gueltigBis=>patch({ gueltigBis })" type="date" />
			<svws-ui-spacing />
			<div class="col-span-full">
				<svws-ui-table :items="listJahrgaenge" :no-data="false" :columns="cols">
					<template #cell(id)="{value}">
						<svws-ui-checkbox type="toggle" :model-value="jahrgaenge.includes(value)" headless @update:model-value="updateJahrgaenge(value)" />
					</template>
				</svws-ui-table>
			</div>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { JahrgangsListeEintrag, List, Stundenplan, StundenplanManager } from "@core";
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
		{ key: "kuerzel", label: "Jahrgang", sortable: true, defaultSort: "asc", span: 0.25 },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
		{ key: "id", label: "Gültig", fixedWidth: 5, align: "center" },
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

	async function doPatch(wochenTypModell: number | null) {
		if (wochenTypModell === null || wochenTypModell < 1)
			return;
		// 1 ist nicht zulässig, wird zu 0 konvertiert, allgemein
		await props.patch({wochenTypModell: wochenTypModell === 1 ? 0 : wochenTypModell});
	}

</script>
