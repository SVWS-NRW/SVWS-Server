<template>
	<svws-ui-secondary-menu>
		<template #headline>Klassen</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #content>
			<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="mapKatalogeintraege.values()" :columns="cols" scroll>
				<template #cell(schueler)="{value}"> {{ value.size() }} </template>
				<template #cell(klassenLehrer)="{value}">
					{{ lehrerkuerzel(value) }}
				</template>
				<template #search>
					<svws-ui-text-input :model-value="klassenFilter.search" @update:model-value="setKlassenFilter({search: String($event), sichtbar: klassenFilter.sichtbar})" type="search" placeholder="Suche nach Klasse" />
				</template>
				<template #filter>
					<svws-ui-checkbox type="toggle" :model-value="klassenFilter.sichtbar" @update:model-value="setKlassenFilter({search: klassenFilter.search, sichtbar: Boolean($event)})">Sichtbar</svws-ui-checkbox>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { KlassenAuswahlProps } from "./SKlassenAuswahlProps";

	const props = defineProps<KlassenAuswahlProps>();

	const cols = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc", span: 0.5 },
		{ key: "klassenLehrer", label: "Klassenleitung" },
		{ key: "schueler", label: "Schüler", span: 0.5, sortable: true }
	];

	function lehrerkuerzel(list: number[]) {
		let s = '';
		if (props.auswahl)
			for (const id of list) {
				const lehrer = props.mapLehrer.get(id);
				if (lehrer) {
					if (s.length)
						s += `, ${lehrer.kuerzel}`;
					else s = lehrer.kuerzel;
				}
			}
		return s;
	}

</script>
