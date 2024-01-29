<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kürzel" :model-value="data().kuerzel" @change="kuerzel => patch({ kuerzel })" type="text" />
				<svws-ui-select title="Statistik-Fach" :model-value="ZulaessigesFach.getByKuerzelASD(data().kuerzelStatistik)"
					@update:model-value="value => patch({ kuerzelStatistik: (value === undefined) || (value === null) ? undefined : value.daten.kuerzelASD })"
					:items="ZulaessigesFach.values()" :item-text="(z: ZulaessigesFach) => z.daten.kuerzelASD + ' : ' + z.daten.bezeichnung" />
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="data().bezeichnung" @change="bezeichnung => patch({ bezeichnung })" type="text" />
				<svws-ui-text-input placeholder="Fachgruppe" :model-value="ZulaessigesFach.getByKuerzelASD(data().kuerzelStatistik)?.getFachgruppe()?.daten.bezeichnung ?? ''" type="text" disabled />
				<svws-ui-select title="Bilinguale Sachfachsprache" :model-value="BilingualeSprache.getByKuerzel(data().bilingualeSprache)"
					@update:model-value="value => patch({ bilingualeSprache: (value === undefined) || (value === null) ? null : value.daten.kuerzel })"
					:items="BilingualeSprache.values()" :item-text="(b: BilingualeSprache) => b.daten.kuerzel" />
				<svws-ui-select v-if="schulform === Schulform.BK || schulform === Schulform.SB"
					title="Aufgabenfeld" :model-value="data().aufgabenfeld" @update:model-value="f => patch({ aufgabenfeld: f ?? null })"
					:items="[ null, '1', '2', '3' ]" :item-text="getAufgabenfeld" />
				<svws-ui-input-number placeholder="Sortierung" :model-value="data().sortierung" @change="value => patch({ sortierung: value === null ? 32000 : value })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Zeugnis">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-checkbox :model-value="data().aufZeugnis" @update:model-value="value => patch({ aufZeugnis: value === true ? true : false })">
					Auf Zeugnis
				</svws-ui-checkbox>
				<svws-ui-text-input placeholder="Bezeichnung (Zeugnis)" :model-value="data().bezeichnungZeugnis" @change="value => patch({ bezeichnungZeugnis: value })" type="text" />
				<svws-ui-text-input placeholder="Bezeichnung (Überweisungszeugnis)" :model-value="data().bezeichnungUeberweisungszeugnis" @change="value => patch({ bezeichnungUeberweisungszeugnis: value })" type="text" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sonstiges">
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-checkbox :model-value="data().istSichtbar" @update:model-value="value => patch({ istSichtbar: value === true ? true : false })">
					Sichtbar
				</svws-ui-checkbox>
				<svws-ui-checkbox v-if="schulform.daten.hatGymOb" :model-value="data().istOberstufenFach" @update:model-value="value => patch({ istOberstufenFach: value === true ? true : false })">
					Fach der Oberstufe
				</svws-ui-checkbox>
				<svws-ui-checkbox v-if="schulform.daten.hatGymOb" :model-value="data().istPruefungsordnungsRelevant" @update:model-value="value => patch({ istPruefungsordnungsRelevant: value === true ? true : false })">
					Ist Prüfungsordnungs-Relevant (z.B. bei Belegprüfungen)
				</svws-ui-checkbox>
				<svws-ui-checkbox v-if="schulform !== Schulform.G" :model-value="data().istNachpruefungErlaubt" @update:model-value="value => patch({ istNachpruefungErlaubt: value === true ? true : false })">
					Nachprüfung erlaubt
				</svws-ui-checkbox>
				<svws-ui-checkbox v-if="schulform !== Schulform.G" :model-value="data().istSchriftlichZK" @update:model-value="value => patch({ istSchriftlichZK: value === true ? true : false })">
					Schriftliches Fach für ZK
				</svws-ui-checkbox>
				<svws-ui-checkbox v-if="schulform !== Schulform.G" :model-value="data().holeAusAltenLernabschnitten" @update:model-value="value => patch({ holeAusAltenLernabschnitten: value === true ? true : false })">
					Berücksichtigen beim Holen von abgeschlossenen Fächern
				</svws-ui-checkbox>
				<svws-ui-input-number placeholder="maximale Zeichenanzahl in Fachbemerkungen" :model-value="data().maxZeichenInFachbemerkungen === JavaInteger.MAX_VALUE ? null : data().maxZeichenInFachbemerkungen" @change="z => patch({ maxZeichenInFachbemerkungen: z ?? JavaInteger.MAX_VALUE })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { BilingualeSprache, ZulaessigesFach, Schulform, JavaInteger } from "@core";
	import type { FachDatenProps } from "./SFachDatenProps";

	const props = defineProps<FachDatenProps>();

	function getAufgabenfeld(aufgabenfeld: string | undefined | null) : string {
		switch(aufgabenfeld) {
			case '1': return 'Aufgabenfeld I';
			case '2': return 'Aufgabenfeld II';
			case '3': return 'Aufgabenfeld III';
			default: return '';
		}
	}

</script>
