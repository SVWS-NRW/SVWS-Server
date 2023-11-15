<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Statistik-Fach" :model-value="ZulaessigesFach.getByKuerzelASD(data.kuerzelStatistik)"
					@update:model-value="value => patch({ kuerzelStatistik: (value === undefined) || (value === null) ? undefined : value.daten.kuerzelASD })"
					:items="ZulaessigesFach.values()" :item-text="(z: ZulaessigesFach) => z.daten.kuerzelASD + ' : ' + z.daten.bezeichnung" />
				<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel => patch({ kuerzel })" type="text" />
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="data.bezeichnung" @change="bezeichnung => patch({ bezeichnung })" type="text" />
				<svws-ui-text-input placeholder="Sortierung" :model-value="data.sortierung" @change="value => patch({ sortierung: value === undefined ? 32000 : Number(value) })" type="number" />
				<svws-ui-select title="Bilinguale Sachfachsprache" :model-value="BilingualeSprache.getByKuerzel(data.bilingualeSprache)"
					@update:model-value="value => patch({ bilingualeSprache: (value === undefined) || (value === null) ? null : value.daten.kuerzel })"
					:items="BilingualeSprache.values()" :item-text="(b: BilingualeSprache) => b.daten.kuerzel" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Zeugnis">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-checkbox :model-value="data.aufZeugnis" @update:model-value="value => patch({ aufZeugnis: value === true ? true : false })">
					Auf Zeugnis
				</svws-ui-checkbox>
				<svws-ui-text-input placeholder="Bezeichnung (Zeugnis)" :model-value="data.bezeichnungZeugnis" @change="value => patch({ bezeichnungZeugnis: value })" type="text" />
				<svws-ui-text-input placeholder="Bezeichnung (Überweisungszeugnis)" :model-value="data.bezeichnungUeberweisungszeugnis" @change="value => patch({ bezeichnungUeberweisungszeugnis: value })" type="text" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sonstiges">
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-checkbox :model-value="data.istSichtbar" @update:model-value="value => patch({ istSichtbar: value === true ? true : false })">
					Sichtbar
				</svws-ui-checkbox>
				<svws-ui-checkbox :model-value="data.istOberstufenFach" @update:model-value="value => patch({ istOberstufenFach: value === true ? true : false })">
					Fach der Oberstufe
				</svws-ui-checkbox>
				<svws-ui-checkbox :model-value="data.istNachpruefungErlaubt" @update:model-value="value => patch({ istNachpruefungErlaubt: value === true ? true : false })">
					Nachprüfung erlaubt
				</svws-ui-checkbox>
				<svws-ui-checkbox :model-value="data.istSchriftlichZK" @update:model-value="value => patch({ istSchriftlichZK: value === true ? true : false })">
					Schriftliches Fach für ZK
				</svws-ui-checkbox>
				<svws-ui-checkbox :model-value="data.holeAusAltenLernabschnitten" @update:model-value="value => patch({ holeAusAltenLernabschnitten: value === true ? true : false })">
					Berücksichtigen beim Holen von abgeschlossenen Fächern
				</svws-ui-checkbox>
				<svws-ui-text-input placeholder="maximale Zeichenanzahl in Fachbemerkungen" :model-value="data.maxZeichenInFachbemerkungen" @change="value => patch({ maxZeichenInFachbemerkungen: Number(value) })" type="number" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { BilingualeSprache, ZulaessigesFach } from "@core";
	import type { FachDatenProps } from "./SFachDatenProps";

	const props = defineProps<FachDatenProps>();

</script>
