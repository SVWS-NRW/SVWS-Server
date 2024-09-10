<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kürzel" :model-value="fachListeManager().daten().kuerzel" @change="kuerzel => patch({ kuerzel: kuerzel ?? undefined })" />
				<svws-ui-select title="Statistik-Fach" :model-value="Fach.data().getWertBySchluessel(fachListeManager().daten().kuerzelStatistik)"
					@update:model-value="value => patch({ kuerzelStatistik: (value === undefined) || (value === null) ? undefined : value.daten(schuljahr)?.schluessel })"
					:items="Fach.values()" :item-text="(z: Fach) => z.daten(schuljahr)?.schluessel + ' : ' + z.daten(schuljahr)?.text" />
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="fachListeManager().daten().bezeichnung" @change="bezeichnung => patch({ bezeichnung: bezeichnung ?? undefined })" />
				<svws-ui-text-input placeholder="Fachgruppe" :model-value="Fach.data().getWertBySchluessel(fachListeManager().daten().kuerzelStatistik)?.getFachgruppe(schuljahr)?.daten(schuljahr)?.text ?? '—'" disabled />
				<svws-ui-select title="Bilinguale Sachfachsprache"
					:model-value="(fachListeManager().daten().bilingualeSprache === null) ? null : BilingualeSprache.data().getWertByKuerzel(fachListeManager().daten().bilingualeSprache!)"
					@update:model-value="value => patch({ bilingualeSprache: value?.daten(schuljahr)?.kuerzel ?? null })"
					:items="BilingualeSprache.values()" :item-text="(b: BilingualeSprache) => b.daten(schuljahr)?.kuerzel ?? '—'" />
				<svws-ui-select v-if="fachListeManager().schulform() === Schulform.BK || fachListeManager().schulform() === Schulform.SB"
					title="Aufgabenfeld" :model-value="fachListeManager().daten().aufgabenfeld" @update:model-value="f => patch({ aufgabenfeld: f ?? null })"
					:items="[ null, '1', '2', '3' ]" :item-text="getAufgabenfeld" />
				<svws-ui-input-number placeholder="Sortierung" :model-value="fachListeManager().daten().sortierung" @change="value => patch({ sortierung: value === null ? 32000 : value })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Zeugnis">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-checkbox :model-value="fachListeManager().daten().aufZeugnis" @update:model-value="value => patch({ aufZeugnis: value === true ? true : false })">
					Auf Zeugnis
				</svws-ui-checkbox>
				<svws-ui-text-input placeholder="Bezeichnung (Zeugnis)" :model-value="fachListeManager().daten().bezeichnungZeugnis"
					@change="bezeichnungZeugnis => patch({ bezeichnungZeugnis: bezeichnungZeugnis ?? undefined })" />
				<svws-ui-text-input placeholder="Bezeichnung (Überweisungszeugnis)" :model-value="fachListeManager().daten().bezeichnungUeberweisungszeugnis"
					@change="bezeichnungUeberweisungszeugnis => patch({ bezeichnungUeberweisungszeugnis: bezeichnungUeberweisungszeugnis ?? undefined })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sonstiges">
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-checkbox :model-value="fachListeManager().daten().istSichtbar" @update:model-value="value => patch({ istSichtbar: value === true ? true : false })">
					Sichtbar
				</svws-ui-checkbox>
				<template v-if="fachListeManager().schulform().daten(schuljahr)?.hatGymOb ?? false">
					<svws-ui-checkbox :model-value="fachListeManager().daten().istOberstufenFach" @update:model-value="value => patch({ istOberstufenFach: value === true ? true : false })">
						Fach der Oberstufe
					</svws-ui-checkbox>
					<svws-ui-checkbox :model-value="fachListeManager().daten().istPruefungsordnungsRelevant" @update:model-value="value => patch({ istPruefungsordnungsRelevant: value === true ? true : false })">
						Ist Prüfungsordnungs-Relevant (z.B. bei Belegprüfungen)
					</svws-ui-checkbox>
				</template>
				<svws-ui-checkbox v-if="fachListeManager().schulform() !== Schulform.G" :model-value="fachListeManager().daten().istNachpruefungErlaubt" @update:model-value="value => patch({ istNachpruefungErlaubt: value === true ? true : false })">
					Nachprüfung erlaubt
				</svws-ui-checkbox>
				<svws-ui-checkbox v-if="fachListeManager().schulform() !== Schulform.G" :model-value="fachListeManager().daten().istSchriftlichZK" @update:model-value="value => patch({ istSchriftlichZK: value === true ? true : false })">
					Schriftliches Fach für ZK
				</svws-ui-checkbox>
				<svws-ui-checkbox v-if="fachListeManager().schulform() !== Schulform.G" :model-value="fachListeManager().daten().holeAusAltenLernabschnitten" @update:model-value="value => patch({ holeAusAltenLernabschnitten: value === true ? true : false })">
					Berücksichtigen beim Holen von abgeschlossenen Fächern
				</svws-ui-checkbox>
				<svws-ui-input-number placeholder="maximale Zeichenanzahl in Fachbemerkungen"
					:model-value="fachListeManager().daten().maxZeichenInFachbemerkungen === JavaInteger.MAX_VALUE ? null : fachListeManager().daten().maxZeichenInFachbemerkungen"
					@change="z => patch({ maxZeichenInFachbemerkungen: z ?? JavaInteger.MAX_VALUE })" :min="0" :max="JavaInteger.MAX_VALUE - 1"
					:valid="i => (i === null) || (i > 0) && (i < JavaInteger.MAX_VALUE)" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { BilingualeSprache, Fach, Schulform, JavaInteger } from "@core";
	import type { FachDatenProps } from "./SFachDatenProps";

	const props = defineProps<FachDatenProps>();

	const schuljahr = computed<number>(() => props.fachListeManager().getSchuljahr());

	function getAufgabenfeld(aufgabenfeld: string | undefined | null) : string {
		switch(aufgabenfeld) {
			case '1': return 'Aufgabenfeld I';
			case '2': return 'Aufgabenfeld II';
			case '3': return 'Aufgabenfeld III';
			default: return '';
		}
	}

</script>
