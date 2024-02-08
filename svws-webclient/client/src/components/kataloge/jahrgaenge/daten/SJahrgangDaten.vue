<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kürzel" :model-value="data().kuerzel" @change="patchKuerzel" type="text" />
				<svws-ui-text-input placeholder="Kürzel Schulgliederung" :model-value="data().kuerzelSchulgliederung" @change="kuerzelSchulgliederung=>patch({kuerzelSchulgliederung})" type="text" />
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="data().bezeichnung" @change="bezeichnung=>patch({bezeichnung})" type="text" />
				<svws-ui-select title="Folgejahrgang" v-model="inputIdFolgejahrgang" :items="inputJahrgaenge" :item-text="e => `${e?.kuerzel ? e.kuerzel + ' : ' : ''}${e?.bezeichnung || ''}`" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card>
			<template #title>
				<div class="content-card--header content-card--headline text-headline-sm">
					<i-ri-bar-chart-2-line class="mr-1 opacity-50" />
					<span>Statistik</span>
				</div>
			</template>
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Bezeichnung in Statistik" :model-value="data().kuerzelStatistik" @change="kuerzelStatistik=>patch({kuerzelStatistik})" type="text" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { type JahrgangsListeEintrag } from "@core";
	import type { JahrgangDatenProps } from "./SJahrgangDatenProps";

	const props = defineProps<JahrgangDatenProps>();

	const inputJahrgaenge = computed<JahrgangsListeEintrag[]>(() =>
		[...props.mapJahrgaenge.values()].filter(j => j.id !== props.data().id)
	);

	const inputIdFolgejahrgang = computed<JahrgangsListeEintrag | undefined>({
		get: () => {
			const idFolgejahrgang = props.data().idFolgejahrgang;
			return idFolgejahrgang === null ? undefined : props.mapJahrgaenge.get(idFolgejahrgang);
		},
		set: (value) => void props.patch({ idFolgejahrgang: value?.id })
	});

	async function patchKuerzel(kuerzel: string) {
		await props.patch({ kuerzel });
	}

</script>
